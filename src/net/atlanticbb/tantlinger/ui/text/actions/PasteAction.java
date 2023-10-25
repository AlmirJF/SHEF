/*
 * Created on Jun 19, 2005
 *
 */
package net.atlanticbb.tantlinger.ui.text.actions;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import net.atlanticbb.tantlinger.ui.UIUtils;
import net.atlanticbb.tantlinger.ui.text.CompoundUndoManager;
import net.atlanticbb.tantlinger.ui.text.HTMLUtils;

import org.bushe.swing.action.ActionManager;
import org.bushe.swing.action.ShouldBeEnabledDelegate;


public class PasteAction extends HTMLTextEditAction
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
        
    public PasteAction()
    {
        super(i18n.str("paste"));
        putValue(MNEMONIC_KEY, new Integer(i18n.mnem("paste")));
        putValue(SMALL_ICON, UIUtils.getIcon(UIUtils.X16, "paste.png"));
        putValue(ActionManager.LARGE_ICON, UIUtils.getIcon(UIUtils.X24, "paste.png"));
		putValue(ACCELERATOR_KEY,
			KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        addShouldBeEnabledDelegate(new ShouldBeEnabledDelegate()
        {
            public boolean shouldBeEnabled(Action a)
            {                          
                //return getCurrentEditor() != null &&
                //    Toolkit.getDefaultToolkit().getSystemClipboard().getContents(PasteAction.this) != null;
                return true;
            }
        });
        
        putValue(Action.SHORT_DESCRIPTION, getValue(Action.NAME));
    }
    
    protected void updateWysiwygContextState(JEditorPane wysEditor)
    {
        this.updateEnabledState();
    }
    
    protected void updateSourceContextState(JEditorPane srcEditor)
    {
        this.updateEnabledState();
    }

    /* (non-Javadoc)
     * @see net.atlanticbb.tantlinger.ui.text.actions.HTMLTextEditAction#sourceEditPerformed(java.awt.event.ActionEvent, javax.swing.JEditorPane)
     */
    protected void sourceEditPerformed(ActionEvent e, JEditorPane editor)
    {
        editor.paste();        
    }

    
    private void pasteText(JEditorPane editor) throws UnsupportedFlavorException, IOException, BadLocationException {
        
        Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        HTMLEditorKit ekit = (HTMLEditorKit)editor.getEditorKit();
        HTMLDocument document = (HTMLDocument)editor.getDocument(); 
        
        String txt = (String)systemClipboard.getData(DataFlavor.stringFlavor);
        document.replace(editor.getSelectionStart(),
            editor.getSelectionEnd() - editor.getSelectionStart(),
            txt, ekit.getInputAttributes());
        
  
    }
    
    private void pasteImage(JEditorPane editor) throws IOException, UnsupportedFlavorException {
        
        Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
 
        Image i = (Image)systemClipboard.getContents(null).getTransferData(DataFlavor.imageFlavor);
                        
        boolean alpha = true;
        if (i instanceof BufferedImage) {
            if (!((BufferedImage)i).getColorModel().hasAlpha())
                alpha = false;
        }
            
        int type = alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;

        
        BufferedImage bi = new BufferedImage(i.getWidth(null), i.getHeight(null), type);
        Graphics2D g = bi.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(i, 0, 0, i.getWidth(null), i.getHeight(null), null);
        g.dispose();
        
        File tempFile = File.createTempFile("pastedImage", "png");
        ImageIO.write(bi, "png", tempFile);
        
                    
        String tagText =  "<img src=\"file:" + tempFile.getAbsolutePath() + "\">";
        
        if(editor.getCaretPosition() == editor.getDocument().getLength())
            tagText += "&nbsp;"; //$NON-NLS-1$

        editor.replaceSelection(""); //$NON-NLS-1$
        HTML.Tag tag = HTML.Tag.IMG;
        if(tagText.startsWith("<a")) //$NON-NLS-1$
            tag = HTML.Tag.A;

        HTMLUtils.insertHTML(tagText, tag, editor);       
                
    }
        
        
    
    
    /* (non-Javadoc)
     * @see net.atlanticbb.tantlinger.ui.text.actions.HTMLTextEditAction#wysiwygEditPerformed(java.awt.event.ActionEvent, javax.swing.JEditorPane)
     */
    protected void wysiwygEditPerformed(ActionEvent e, JEditorPane editor)
    {        
        HTMLDocument document = (HTMLDocument)editor.getDocument();        
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        
        try 
        {
            CompoundUndoManager.beginCompoundEdit(document);
            
            if (clip.isDataFlavorAvailable(DataFlavor.stringFlavor))
                pasteText(editor);
            
            else if (clip.isDataFlavorAvailable(DataFlavor.imageFlavor))
                pasteImage(editor);
            
            
        } 
        catch(Exception ex) 
        {
            System.err.println(ex);
        }
        finally
        {
            CompoundUndoManager.endCompoundEdit(document);
        }
    }    
}
