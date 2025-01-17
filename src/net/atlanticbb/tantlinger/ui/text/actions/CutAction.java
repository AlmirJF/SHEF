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
public class CutAction extends BasicEditAction {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CutAction() {
        super("");
        putValue(Action.NAME, i18n.str("cut"));
        putValue(Action.SMALL_ICON, UIUtils.getIcon(UIUtils.X16, "cut.png"));
        putValue(ActionManager.LARGE_ICON, UIUtils.getIcon(UIUtils.X24, "cut.png"));
        putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        putValue(MNEMONIC_KEY, Integer.valueOf(i18n.mnem("cut")));
        addShouldBeEnabledDelegate((Action a) -> {
            JEditorPane ed = getCurrentEditor();
            return ed != null
                    && ed.getSelectionStart() != ed.getSelectionEnd();
            //return true;
        });
        putValue(Action.SHORT_DESCRIPTION, getValue(Action.NAME));
    }

    /* (non-Javadoc)
     * @see net.atlanticbb.tantlinger.ui.text.actions.BasicEditAction#doEdit(java.awt.event.ActionEvent, javax.swing.JEditorPane)
     */
    @Override
    protected void doEdit(ActionEvent e, JEditorPane editor) {
        editor.cut();
    }

    @Override
    protected void contextChanged() {
        super.contextChanged();
        this.updateEnabledState();
    }
}
