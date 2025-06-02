package pl.indianbartonka.util.swing.panel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import pl.indianbartonka.util.annotation.Since;

@Since("0.0.9.3")
public class ProgressPanel extends JPanel {

    private final JProgressBar progressBar;
    private boolean canRemove;

    public ProgressPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.progressBar = new JProgressBar(0, 100);
        this.progressBar.setStringPainted(true);

        this.add(this.progressBar);

        this.canRemove = false;
    }

    @Deprecated
    @Override
    public void setName(final String name) {
        super.setName(name);
    }

    public void setText(final String text) {
        this.progressBar.setString(text);

        this.revalidate();
        this.repaint();
    }

    public void setValue(final int progress) {
        this.progressBar.setValue(progress);
    }

    public JProgressBar getProgressBar() {
        return this.progressBar;
    }

    public boolean canRemove() {
        return this.canRemove;
    }

    public void removePanel() {
        this.canRemove = true;
        //TODO: Spróbuj brać rodzica i usuwać się od niego 
    }
}
