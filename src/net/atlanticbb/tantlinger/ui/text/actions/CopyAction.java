/*
 * Created on Nov 2, 2007
 */
package net.atlanticbb.tantlinger.ui.text.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.KeyStroke;
import net.atlanticbb.tantlinger.ui.UIUtils;

import org.bushe.swing.action.ActionManager;

/**
 * @author Bob Tantlinger
 *
 */
public class CopyAction extends BasicEditAction {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CopyAction() {
        super("");
        putValue(Action.NAME, i18n.str("copy"));
        putValue(Action.SMALL_ICON, UIUtils.getIcon(UIUtils.X16, "copy.png"));
        putValue(ActionManager.LARGE_ICON, UIUtils.getIcon(UIUtils.X24, "copy.png"));
        putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        putValue(MNEMONIC_KEY, Integer.valueOf(i18n.mnem("copy")));
        addShouldBeEnabledDelegate((Action a) -> {
            JEditorPane ed = getCurrentEditor();
            return ed != null && ed.getSelectionStart() != ed.getSelectionEnd();
            //return true;
        });
        putValue(Action.SHORT_DESCRIPTION, getValue(Action.NAME));
    }

    /* (non-Javadoc)
     * @see net.atlanticbb.tantlinger.ui.text.actions.BasicEditAction#doEdit(java.awt.event.ActionEvent, javax.swing.JEditorPane)
     */
    @Override
    protected void doEdit(ActionEvent e, JEditorPane editor) {
        editor.copy();
    }

    @Override
    protected void contextChanged() {
        super.contextChanged();
        this.updateEnabledState();
    }

}
