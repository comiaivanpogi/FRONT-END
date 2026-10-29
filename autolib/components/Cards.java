package autolib.components;

import autolib.UIConstants;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Card / container component library for AutoLib.
 *
 * Components:
 *  - {@link RoundedCard}  — base rounded panel with optional border
 *  - {@link StatCard}     — dashboard KPI card (icon + number + label + sub-badge)
 *  - {@link ReportCard}   — report-selection card (icon + title + description + button)
 *  - {@link SectionCard}  — content card with a header bar and body area
 *
 * Usage:
 * <pre>
 *   // Base card
 *   Cards.RoundedCard card = new Cards.RoundedCard();
 *   card.add(new JLabel("Content"));
 *
 *   // Stat card
 *   Cards.StatCard stat = new Cards.StatCard(
 *       "\uD83D\uDCDA", "1,248", "Total Books", "+12 this month", UIConstants.ACCENT_GREEN);
 *
 *   // Report card
 *   Cards.ReportCard report = new Cards.ReportCard(
 *       "\uD83D\uDCCA", "Borrowing Summary", "Daily / weekly / monthly overview",
 *       "Generate", () -> generateReport());
 *
 *   // Section card with a header
 *   Cards.SectionCard section = new Cards.SectionCard("Recent Transactions", viewAllBtn);
 *   section.getBody().add(myTable);
 * </pre>
 */
public final class Cards {

    private Cards() {}

    // ─────────────────────────────────────────────────────────────────────────
    // RoundedCard — base rounded container panel
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * White card with rounded corners and a subtle border.
     * Acts as a drop-in replacement for a plain JPanel in any layout.
     *
     * <pre>
     *   Cards.RoundedCard card = new Cards.RoundedCard();
     *   card.setLayout(new BorderLayout());
     *   card.add(content, BorderLayout.CENTER);
     * </pre>
     */
    public static class RoundedCard extends JPanel {
        private final int   radius;
        private final Color bg;
        private final Color border;

        /** Standard white card with the project's CARD_RADIUS. */
        public RoundedCard() {
            this(UIConstants.CARD_RADIUS, UIConstants.CARD_BG, UIConstants.CARD_BORDER);
        }

        /**
         * Custom card.
         * @param radius corner radius in pixels
         * @param bg     fill colour
         * @param border stroke colour (pass {@code null} for no border)
         */
        public RoundedCard(int radius, Color bg, Color border) {
            this.radius = radius;
            this.bg     = bg;
            this.border = border;
            setOpaque(false);
            setBorder(new EmptyBorder(16, 18, 16, 18));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            if (border != null) {
                g2.setColor(border);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // StatCard — dashboard KPI card
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Dashboard KPI card: emoji icon → large number → label → small badge.
     *
     * <pre>
     *   Cards.StatCard s = new Cards.StatCard(
     *       "\uD83D\uDCDA",         // icon
     *       "1,248",                // big number
     *       "Total Books",          // label
     *       "+12 this month",       // badge text
     *       UIConstants.ACCENT_GREEN);
     * </pre>
     */
    public static class StatCard extends RoundedCard {
        private final JLabel numberLabel;

        public StatCard(String icon, String number, String label,
                        String badgeText, Color badgeColor) {
            setLayout(new BorderLayout());

            JLabel iconLbl = new JLabel(icon);
            iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
            iconLbl.setBorder(new EmptyBorder(0, 0, 8, 0));

            numberLabel = new JLabel(number);
            numberLabel.setFont(UIConstants.F_STAT_NUM);
            numberLabel.setForeground(UIConstants.TEXT_PRIMARY);

            JLabel labelLbl = new JLabel(label);
            labelLbl.setFont(UIConstants.F_STAT_LBL);
            labelLbl.setForeground(UIConstants.TEXT_SECONDARY);

            // Sub-badge colour logic: pick a matching background
            Color badgeBg = badgeColor.equals(UIConstants.ACCENT_GREEN) ? UIConstants.ACCENT_GREEN_BG
                          : badgeColor.equals(UIConstants.ACCENT_RED)   ? UIConstants.ACCENT_RED_LIGHT
                          : new Color(240, 242, 248);
            Badges.StatusBadge badge = new Badges.StatusBadge(badgeText, badgeColor, badgeBg);
            badge.setHorizontalAlignment(SwingConstants.LEFT);

            JPanel inner = new JPanel();
            inner.setOpaque(false);
            inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
            inner.add(iconLbl);
            inner.add(numberLabel);
            inner.add(labelLbl);
            inner.add(Box.createVerticalStrut(8));
            inner.add(badge);

            add(inner, BorderLayout.CENTER);
        }

        /** Update the displayed number at runtime. */
        public void setNumber(String number) {
            numberLabel.setText(number);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ReportCard — report selection card
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Report-selection card shown in the Reports panel grid.
     * Large emoji icon → title → description → primary action button.
     *
     * <pre>
     *   Cards.ReportCard r = new Cards.ReportCard(
     *       "\uD83D\uDCCA", "Borrowing Summary",
     *       "Daily / weekly / monthly overview",
     *       "Generate", () -&gt; generateReport("Borrowing Summary"));
     * </pre>
     */
    public static class ReportCard extends RoundedCard {

        public ReportCard(String icon, String title, String description,
                          String buttonLabel, Runnable onGenerate) {
            setLayout(new BorderLayout());

            JPanel body = new JPanel();
            body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
            body.setOpaque(false);

            JLabel ico = new JLabel(icon);
            ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
            ico.setBorder(new EmptyBorder(0, 0, 10, 0));

            JLabel t = new JLabel(title);
            t.setFont(UIConstants.F_SUBHEAD);
            t.setForeground(UIConstants.TEXT_PRIMARY);

            JLabel d = new JLabel("<html>" + description + "</html>");
            d.setFont(UIConstants.F_SMALL);
            d.setForeground(UIConstants.TEXT_MUTED);

            Buttons.PrimaryButton btn = new Buttons.PrimaryButton(buttonLabel);
            btn.setBorder(new EmptyBorder(7, 14, 7, 14));
            btn.addActionListener(e -> onGenerate.run());

            body.add(ico);
            body.add(t);
            body.add(d);
            body.add(Box.createVerticalStrut(10));
            body.add(btn);

            add(body, BorderLayout.CENTER);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SectionCard — card with a built-in title header + body area
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Card that has a built-in header row (title on the left, optional
     * component on the right) and a separate body area below.
     *
     * <pre>
     *   Cards.SectionCard card = new Cards.SectionCard("Recent Transactions", viewAllLink);
     *   card.getBody().add(transactionList);
     * </pre>
     */
    public static class SectionCard extends RoundedCard {
        private final JPanel body;

        /**
         * @param title       card heading text
         * @param rightWidget optional widget shown in the top-right corner
         *                    (e.g. a "View all →" link); pass {@code null} to omit
         */
        public SectionCard(String title, JComponent rightWidget) {
            setLayout(new BorderLayout(0, 10));
            setBorder(new EmptyBorder(16, 18, 12, 18));

            // Header
            JPanel hdr = new JPanel(new BorderLayout());
            hdr.setOpaque(false);
            JLabel titleLbl = new JLabel(title);
            titleLbl.setFont(UIConstants.F_SUBHEAD);
            titleLbl.setForeground(UIConstants.TEXT_PRIMARY);
            hdr.add(titleLbl, BorderLayout.WEST);
            if (rightWidget != null)
                hdr.add(rightWidget, BorderLayout.EAST);

            // Body
            body = new JPanel();
            body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
            body.setOpaque(false);

            add(hdr,  BorderLayout.NORTH);
            add(body, BorderLayout.CENTER);
        }

        /** Convenience — no right widget. */
        public SectionCard(String title) {
            this(title, null);
        }

        /**
         * Returns the body panel where content should be added.
         * Use any layout manager on it as needed.
         */
        public JPanel getBody() { return body; }
    }
}
