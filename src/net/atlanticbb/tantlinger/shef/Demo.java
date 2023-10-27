package net.atlanticbb.tantlinger.shef;

import java.awt.event.ComponentAdapter;
import java.awt.event.ContainerAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.atlanticbb.tantlinger.io.IOUtils;

/**
 *
 * @author Bob Tantlinger
 */
public class Demo {

    private final JFrame frame;
    private final HTMLEditorPane editor;

    public Demo() {

        editor = new HTMLEditorPane(true);
        InputStream in = Demo.class.getResourceAsStream("/net/atlanticbb/tantlinger/shef/htmlsnip.txt");
        try {
            editor.setText(IOUtils.read(in));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            IOUtils.close(in);
        }

        editor.setText(String.valueOf(Locale.getDefault()));

        frame = new JFrame();
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(editor.getEditMenu());
        menuBar.add(editor.getFormatMenu());
        menuBar.add(editor.getInsertMenu());
        frame.setJMenuBar(menuBar);

        frame.setTitle("HTML Editor Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.getContentPane().add(editor);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printHtml();
            }
        });
    }

    private void printHtml() {
        System.out.println(editor.getText());
    }

    public static void main(String args[]) throws InterruptedException, InvocationTargetException {

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException
                | InstantiationException | UnsupportedLookAndFeelException ex) {
        }

        SwingUtilities.invokeAndWait(() -> {
            Demo demo = new Demo();
        });
    }
}
