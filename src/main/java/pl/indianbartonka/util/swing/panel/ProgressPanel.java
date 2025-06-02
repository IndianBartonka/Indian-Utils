package pl.indianbartonka.util.swing.panel;

import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import pl.indianbartonka.util.annotation.Since;

@Since("0.0.9.3")
public class ProgressPanel extends JPanel {

    private final JProgressBar progressBar;

    public ProgressPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.progressBar = new JProgressBar(0, 100);
        this.progressBar.setStringPainted(true);

        this.add(this.progressBar);
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
    
    public void removePanel() {
       final Container parent = this.getParent();
        parent.remove(this);
        parent.revalidate();
        parent.repaint();
    }
}
