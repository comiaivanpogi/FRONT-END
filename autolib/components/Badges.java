package autolib.components;

import autolib.UIConstants;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Badge component library for AutoLib.
 *
 * Components:
 *  - {@link StatusBadge}     — pill-shaped coloured label (Available, Borrowed, Overdue…)
 *  - {@link CountBadge}      — numeric notification bubble (e.g. "3" on a bell icon)
 *  - {@link TagBadge}        — flat rectangular tag (section, category)
 *  - Factory methods         — {@code forBookStatus()}, {@code forTxType()}
 *
 * Usage:
 * <pre>
 *   Badges.StatusBadge available = Badges.forBookStatus("Available");
 *   Badges.StatusBadge borrowed  = Badges.forTxType("Borrowed");
 *   Badges.CountBadge  bell      = new Badges.CountBadge(3);
 *   Badges.TagBadge    section   = new Badges.TagBadge("Engineering", UIConstants.ACCENT_BLUE);
 * </pre>
 */
public final class Badges {

    private Badges() {}

    // ─────────────────────────────────────────────────────────────────────────
    // StatusBadge — rounded pill with colour-coded text/background
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Pill-shaped status badge with anti-aliased rounded background.
     *
     * <pre>
     *   new Badges.StatusBadge("Available", UIConstants.ACCENT_GREEN, UIConstants.ACCENT_GREEN_BG)
     * </pre>
     */
    public static class StatusBadge extends JLabel {
        private final Color bgColor;

        public StatusBadge(String text, Color fg, Color bg) {
            super(text);
            this.bgColor = bg;
            setFont(UIConstants.F_TINY);
            setForeground(fg);
            setOpaque(false);
            setHorizontalAlignment(CENTER);
            setBorder(new EmptyBorder(3, 10, 3, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                    UIConstants.BADGE_RADIUS, UIConstants.BADGE_RADIUS);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CountBadge — numeric notification bubble
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Small circular number badge typically overlaid on a bell or icon.
     *
     * <pre>
     *   Badges.CountBadge badge = new Badges.CountBadge(3);
     *   badge.setCount(5);   // update dynamically
     * </pre>
     */
    public static class CountBadge extends JLabel {

        public CountBadge(int count) {
            super(String.valueOf(count), SwingConstants.CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 9));
            setForeground(Color.WHITE);
            setBackground(UIConstants.ACCENT_RED);
            setOpaque(true);
            setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        }

        /** Updates the displayed count. */
        public void setCount(int count) {
            setText(String.valueOf(count));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TagBadge — flat rectangular category / section tag
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Flat rectangular tag badge for categories, sections, or departments.
     * Background is a 15% opacity tint of the given colour.
     *
     * <pre>
     *   new Badges.TagBadge("Engineering", UIConstants.ACCENT_BLUE)
     *   new Badges.TagBadge("Science &amp; Tech", UIConstants.ACCENT_GREEN)
     * </pre>
     */
    public static class TagBadge extends JLabel {
        private final Color fgColor;
        private final Color bgColor;

        public TagBadge(String text, Color accentColor) {
            super(text);
            this.fgColor = accentColor;
            // Light tint: take the accent colour at ~18% opacity on white
            this.bgColor = new Color(
                    (int)(accentColor.getRed()   * 0.18 + 255 * 0.82),
                    (int)(accentColor.getGreen() * 0.18 + 255 * 0.82),
                    (int)(accentColor.getBlue()  * 0.18 + 255 * 0.82));
            setFont(UIConstants.F_TINY);
            setForeground(accentColor);
            setOpaque(false);
            setHorizontalAlignment(CENTER);
            setBorder(new EmptyBorder(2, 8, 2, 8));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
            // subtle border
            g2.setColor(new Color(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue(), 60));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 4, 4);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Factory methods — semantic badge creation
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Creates a {@link StatusBadge} appropriate for the given book status string.
     *
     * <pre>
     *   Badges.forBookStatus("Available")  // green
     *   Badges.forBookStatus("Borrowed")   // red
     *   Badges.forBookStatus("All Out")    // amber
     * </pre>
     */
    public static StatusBadge forBookStatus(String status) {
        switch (status) {
            case "All Out":
                return new StatusBadge("All Out",
                        UIConstants.ACCENT_AMBER, UIConstants.ACCENT_AMBER_BG);
            case "Borrowed":
                return new StatusBadge("Borrowed",
                        UIConstants.ACCENT_RED, UIConstants.ACCENT_RED_LIGHT);
            default:
                return new StatusBadge("Available",
                        UIConstants.ACCENT_GREEN, UIConstants.ACCENT_GREEN_BG);
        }
    }

    /**
     * Creates a {@link StatusBadge} appropriate for the given transaction type string.
     *
     * <pre>
     *   Badges.forTxType("Borrowed")  // red
     *   Badges.forTxType("Overdue")   // orange
     *   Badges.forTxType("Returned")  // green
     * </pre>
     */
    public static StatusBadge forTxType(String type) {
        switch (type) {
            case "Borrowed":
                return new StatusBadge("Borrowed",
                        UIConstants.ACCENT_RED, UIConstants.ACCENT_RED_LIGHT);
            case "Overdue":
                return new StatusBadge("Overdue",
                        UIConstants.ACCENT_ORANGE, UIConstants.ACCENT_ORANGE_BG);
            default:
                return new StatusBadge("Returned",
                        UIConstants.ACCENT_GREEN, UIConstants.ACCENT_GREEN_BG);
        }
    }

    /**
     * Creates a {@link StatusBadge} for NFC registration status.
     *
     * <pre>
     *   Badges.forNfcStatus(true)   // green "✓ Registered"
     *   Badges.forNfcStatus(false)  // amber "⚠ Pending"
     * </pre>
     */
    public static StatusBadge forNfcStatus(boolean registered) {
        if (registered)
            return new StatusBadge("\u2713 Registered",
                    UIConstants.ACCENT_GREEN, UIConstants.ACCENT_GREEN_BG);
        return new StatusBadge("\u26A0 Pending",
                UIConstants.ACCENT_AMBER, UIConstants.ACCENT_AMBER_BG);
    }
}
