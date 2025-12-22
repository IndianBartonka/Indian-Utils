package pl.indianbartonka.util.swing;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.Document;
import pl.indianbartonka.util.annotation.Since;

@Since("0.0.9.3")
public class JOptionPaneTextArea extends JScrollPane {

    private final JTextArea jTextArea;

    public JOptionPaneTextArea(final String text) {
        this.jTextArea = new JTextArea(text);
        this.setUneditable();
        this.setPreferredSize(this.calculateDynamicSize(text));
    }

    public JOptionPaneTextArea(final int rows, final int columns) {
        this.jTextArea = new JTextArea(rows, columns);
        this.setUneditable();
        this.setPreferredSize(this.calculateDynamicSize(""));
    }

    public JOptionPaneTextArea(final String text, final int rows, final int columns) {
        this.jTextArea = new JTextArea(text, rows, columns);
        this.setUneditable();
        this.setPreferredSize(this.calculateDynamicSize(text));
    }

    public JOptionPaneTextArea(final Document doc) {
        this.jTextArea = new JTextArea(doc);
        this.setUneditable();
        this.setPreferredSize(this.calculateDynamicSize(""));
    }

    public JOptionPaneTextArea(final Document doc, final String text, final int rows, final int columns) {
        this.jTextArea = new JTextArea(doc, text, rows, columns);
        this.setUneditable();
        this.setPreferredSize(this.calculateDynamicSize(text));
    }

    private void setUneditable() {
        this.jTextArea.setWrapStyleWord(true);
        this.jTextArea.setLineWrap(true);
        this.jTextArea.setEditable(false);
        this.setViewportView(this.jTextArea);
    }

    private Dimension calculateDynamicSize(final String text) {
        final FontMetrics metrics = this.jTextArea.getFontMetrics(this.jTextArea.getFont());
        final int textWidth = metrics.stringWidth(text);
        final int textHeight = metrics.getHeight() * (text.split("\n").length);

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        return new Dimension(Math.min(textWidth + 20, (int) (screenSize.width * 0.4)), Math.min(textHeight + 20, (int) (screenSize.height * 0.7)));
    }

    public void setText(final String text) {
        this.jTextArea.setText(text);
        this.setPreferredSize(this.calculateDynamicSize(text));
    }
}