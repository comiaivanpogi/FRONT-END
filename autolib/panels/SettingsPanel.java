package autolib.panels;

import autolib.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/** Settings panel — basic system configuration UI. */
public class SettingsPanel extends JPanel {
    public SettingsPanel(LibrarianPortal portal) {
        setBackground(UIConstants.CONTENT_BG);
        setLayout(new BorderLayout());
        build();
    }

    private void build() {
        JPanel wrap = new JPanel(new BorderLayout(0, 16));
        wrap.setBackground(UIConstants.CONTENT_BG);
        wrap.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel grid = new JPanel(new GridLayout(1, 2, 14, 0));
        grid.setOpaque(false);

        // Left: Library info
        UIComponents.RoundedPanel left = new UIComponents.RoundedPanel(
                UIConstants.CARD_RADIUS, UIConstants.CARD_BG, UIConstants.CARD_BORDER);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(new EmptyBorder(20, 22, 20, 22));

        JLabel t1 = new JLabel("Library Information");
        t1.setFont(UIConstants.F_HEADING);
        t1.setForeground(UIConstants.TEXT_PRIMARY);
        t1.setBorder(new EmptyBorder(0, 0, 16, 0));
        left.add(t1);
        left.add(settingRow("Library Name", "Batangas State University Library"));
        left.add(settingRow("System Version", "AutoLib v1.0.0"));
        left.add(settingRow("Admin Email", "library@batstate-u.edu.ph"));
        left.add(settingRow("Loan Duration", "7 days"));
        left.add(settingRow("Max Active Loans", "3 books per student"));
        left.add(Box.createVerticalStrut(20));
        UIComponents.PrimaryButton saveBtn = new UIComponents.PrimaryButton("Save Changes");
        saveBtn.setAlignmentX(LEFT_ALIGNMENT);
        saveBtn.addActionListener(
                e -> JOptionPane.showMessageDialog(this, "Settings saved.", "Saved", JOptionPane.INFORMATION_MESSAGE));
        left.add(saveBtn);

        // Right: Device status
        UIComponents.RoundedPanel right = new UIComponents.RoundedPanel(
                UIConstants.CARD_RADIUS, UIConstants.CARD_BG, UIConstants.CARD_BORDER);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(new EmptyBorder(20, 22, 20, 22));

        JLabel t2 = new JLabel("Device Status");
        t2.setFont(UIConstants.F_HEADING);
        t2.setForeground(UIConstants.TEXT_PRIMARY);
        t2.setBorder(new EmptyBorder(0, 0, 16, 0));
        right.add(t2);
        right.add(deviceRow("NFC Reader", "Online", UIConstants.ACCENT_GREEN));
        right.add(deviceRow("Barcode Scanner", "Online", UIConstants.ACCENT_GREEN));
        right.add(deviceRow("Receipt Printer", "Offline", UIConstants.ACCENT_RED));
        right.add(deviceRow("Database", "Connected", UIConstants.ACCENT_GREEN));

        grid.add(left);
        grid.add(right);
        wrap.add(grid, BorderLayout.NORTH);
        add(wrap, BorderLayout.CENTER);
    }

    private JPanel settingRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        row.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.F_SMALL);
        lbl.setForeground(UIConstants.TEXT_MUTED);

        JTextField val = UIComponents.styledField(value);
        val.setText(value);
        val.setPreferredSize(new Dimension(0, 34));

        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);
        col.add(lbl);
        col.add(Box.createVerticalStrut(4));
        col.add(val);
        row.add(col, BorderLayout.CENTER);
        return row;
    }

    private JPanel deviceRow(String device, String status, Color color) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        row.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel dev = new JLabel(device);
        dev.setFont(UIConstants.F_BODY);
        dev.setForeground(UIConstants.TEXT_PRIMARY);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.setOpaque(false);
        JLabel dot = new JLabel("\u25CF");
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        dot.setForeground(color);
        JLabel st = new JLabel(status);
        st.setFont(UIConstants.F_SMALL);
        st.setForeground(color);
        right.add(dot);
        right.add(st);

        row.add(dev, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);
        return row;
    }
}
