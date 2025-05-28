package pl.indianbartonka.util.swing;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.text.Document;
import pl.indianbartonka.util.annotation.Since;

@Since("0.0.9.3")
public class AdvancedTextField extends JTextField {

    private String cut = "Wytnij";
    private String paste = "Wklej";
    private String copy = "Kopiuj";

    public AdvancedTextField() {
        this.addPopupMenu();
    }

    public AdvancedTextField(final String text) {
        super(text);
        this.addPopupMenu();
    }

    public AdvancedTextField(final int columns) {
        super(columns);
        this.addPopupMenu();
    }

    public AdvancedTextField(final String text, final int columns) {
        super(text, columns);
        this.addPopupMenu();
    }

    public AdvancedTextField(final Document doc, final String text, final int columns) {
        super(doc, text, columns);
        this.addPopupMenu();
    }

    public AdvancedTextField(final String text, final int columns, final String toolTip) {
        super(text, columns);
        this.setToolTipText(toolTip);
        this.addPopupMenu();
    }

    private void addPopupMenu() {
        final JPopupMenu popupMenu = new JPopupMenu();

        final JMenuItem cutItem = new JMenuItem(this.cut);
        cutItem.addActionListener(e -> this.cut());

        final JMenuItem copyItem = new JMenuItem(this.copy);
        copyItem.addActionListener(e -> this.copy());

        final JMenuItem pasteItem = new JMenuItem(this.paste);
        pasteItem.addActionListener(e -> this.paste());

        popupMenu.add(cutItem);
        popupMenu.add(copyItem);
        popupMenu.add(pasteItem);

        this.setComponentPopupMenu(popupMenu);
    }

    public void setCutText(final String cut) {
        this.cut = cut;
    }

    public void setPasteText(final String paste) {
        this.paste = paste;
    }

    public void setCopyText(final String copy) {
        this.copy = copy;
    }
}
