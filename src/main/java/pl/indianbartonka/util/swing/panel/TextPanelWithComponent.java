package pl.indianbartonka.util.swing.panel;

import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pl.indianbartonka.util.annotation.Since;

@Since("0.0.9.3")
public class TextPanelWithComponent extends JPanel {

    public TextPanelWithComponent(final String labelText, final Component component) {
        this(labelText, component, false, null);
    }

    public TextPanelWithComponent(final JLabel jLabel, final Component component) {
        this(jLabel, component, false, null);
    }

    public TextPanelWithComponent(final String labelText, final Component component, final String toolTip) {
        this(labelText, component, false, toolTip);
    }

    public TextPanelWithComponent(final JLabel jLabel, final Component component, final String toolTip) {
        this(jLabel, component, false, toolTip);
    }

    public TextPanelWithComponent(final String labelText, final Component component, final boolean reversed) {
        this(labelText, component, reversed, null);
    }

    public TextPanelWithComponent(final JLabel jLabel, final Component component, final boolean reversed) {
        this(jLabel, component, reversed, null);
    }

    public TextPanelWithComponent(final JLabel jLabel, final Component component, final boolean reversed, final String toolTip) {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        if (reversed) {
            this.add(component);
            this.add(jLabel);
        } else {
            this.add(jLabel);
            this.add(component);
        }

        if (toolTip != null) {
            this.setToolTipText(toolTip);
        }
    }

    public TextPanelWithComponent(final String labelText, final Component component, final boolean reversed, final String toolTip) {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        if (reversed) {
            this.add(component);
            this.add(new JLabel(labelText));
        } else {
            this.add(new JLabel(labelText));
            this.add(component);
        }

        if (toolTip != null) {
            this.setToolTipText(toolTip);
        }
    }
}
