package autolib.panels;

import autolib.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/** Placeholder Reports panel. */
public class ReportsPanel extends JPanel {
    public ReportsPanel(LibrarianPortal portal) {
        setBackground(UIConstants.CONTENT_BG);
        setLayout(new BorderLayout());
        build();
    }

    private void build() {
        JPanel wrap = new JPanel(new BorderLayout(0, 12));
        wrap.setBackground(UIConstants.CONTENT_BG);
        wrap.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Summary cards
        JPanel cards = new JPanel(new GridLayout(2, 3, 14, 14));
        cards.setOpaque(false);
        String[][] reports = {
                { "\uD83D\uDCCA", "Borrowing Summary", "Daily / weekly / monthly overview" },
                { "\u23F0", "Overdue Report", "Students with past-due books" },
                { "\uD83D\uDCDA", "Inventory Report", "Stock levels by section" },
                { "\uD83D\uDC64", "Student Activity", "Borrow/return history per student" },
                { "\u2B50", "Popular Books", "Most borrowed titles this month" },
                { "\uD83D\uDCBE", "Export Data", "Download CSV or PDF reports" }
        };
        for (String[] r : reports)
            cards.add(reportCard(r[0], r[1], r[2]));

        wrap.add(cards, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        JLabel hint = new JLabel("Select a report type above to generate it.");
        hint.setFont(UIConstants.F_BODY);
        hint.setForeground(UIConstants.TEXT_MUTED);
        center.add(hint);
        wrap.add(center, BorderLayout.CENTER);

        add(wrap, BorderLayout.CENTER);
    }

    private JPanel reportCard(String icon, String title, String sub) {
        UIComponents.RoundedPanel c = new UIComponents.RoundedPanel(
                UIConstants.CARD_RADIUS, UIConstants.CARD_BG, UIConstants.CARD_BORDER);
        c.setLayout(new BorderLayout());
        c.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        ico.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel t = new JLabel(title);
        t.setFont(UIConstants.F_SUBHEAD);
        t.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel s = new JLabel("<html>" + sub + "</html>");
        s.setFont(UIConstants.F_SMALL);
        s.setForeground(UIConstants.TEXT_MUTED);

        UIComponents.PrimaryButton gen = new UIComponents.PrimaryButton("Generate");
        gen.addActionListener(e -> JOptionPane.showMessageDialog(this,
                title + " will be generated.", "Report", JOptionPane.INFORMATION_MESSAGE));
        gen.setBorder(new EmptyBorder(7, 14, 7, 14));

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.add(ico);
        body.add(t);
        body.add(s);
        body.add(Box.createVerticalStrut(10));
        body.add(gen);

        c.add(body, BorderLayout.CENTER);
        return c;
    }
}
