package autolib.components;

import autolib.UIConstants;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Text / label component library for AutoLib.
 *
 * Components:
 *  - {@link HeadingLabel}   — large bold page heading (H1 equivalent)
 *  - {@link SubheadLabel}   — medium bold section heading
 *  - {@link BodyLabel}      — standard body text
 *  - {@link MutedLabel}     — secondary / helper text (grey)
 *  - {@link SectionLabel}   — small ALL-CAPS sidebar category header
 *  - {@link LinkLabel}      — red clickable hyperlink-style label
 *  - {@link AvatarLabel}    — circular avatar with initials
 *  - {@link StatusDotLabel} — coloured dot + text (device status indicator)
 *
 * Usage:
 * <pre>
 *   Labels.HeadingLabel  h1  = new Labels.HeadingLabel("AutoLib");
 *   Labels.SubheadLabel  sh  = new Labels.SubheadLabel("Book Catalog");
 *   Labels.BodyLabel     bl  = new Labels.BodyLabel("Select a portal to continue.");
 *   Labels.MutedLabel    ml  = new Labels.MutedLabel("Showing 1–25 of 1,248 books");
 *   Labels.SectionLabel  sl  = new Labels.SectionLabel("MAIN");
 *   Labels.LinkLabel     ll  = new Labels.LinkLabel("View all \u2192", () -&gt; portal.showPanel("Transactions"));
 *   Labels.AvatarLabel   av  = new Labels.AvatarLabel("LR", UIConstants.ACCENT_RED);
 *   Labels.StatusDotLabel sd = new Labels.StatusDotLabel("NFC Reader Online", UIConstants.ACCENT_GREEN);
 * </pre>
 */
public final class Labels {

    private Labels() {}

    // ─────────────────────────────────────────────────────────────────────────
    // HeadingLabel — large bold page heading
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Large bold heading (28 pt) — use once per page as the primary title.
     *
     * <pre>
     *   Labels.HeadingLabel h = new Labels.HeadingLabel("Dashboard");
     * </pre>
     */
    public static class HeadingLabel extends JLabel {
        public HeadingLabel(String text) {
            super(text);
            setFont(UIConstants.F_TITLE);
            setForeground(UIConstants.TEXT_PRIMARY);
        }

        /** Variant with custom colour (e.g. white for dark backgrounds). */
        public HeadingLabel(String text, Color color) {
            this(text);
            setForeground(color);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SubheadLabel — medium bold section heading
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Medium bold heading (16 pt) — for card titles and section heads.
     *
     * <pre>
     *   Labels.SubheadLabel sh = new Labels.SubheadLabel("Recent Transactions");
     * </pre>
     */
    public static class SubheadLabel extends JLabel {
        public SubheadLabel(String text) {
            super(text);
            setFont(UIConstants.F_HEADING);
            setForeground(UIConstants.TEXT_PRIMARY);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // BodyLabel — standard body text
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Standard 13 pt body text label.
     *
     * <pre>
     *   Labels.BodyLabel bl = new Labels.BodyLabel("NFC \u0026 Barcode-Integrated System");
     * </pre>
     */
    public static class BodyLabel extends JLabel {
        public BodyLabel(String text) {
            super(text);
            setFont(UIConstants.F_BODY);
            setForeground(UIConstants.TEXT_PRIMARY);
        }

        /** HTML-wrapped multi-line variant. */
        public BodyLabel(String text, boolean multiline) {
            this(multiline ? "<html>" + text + "</html>" : text);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MutedLabel — secondary / helper text
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Muted grey helper text (11 pt) — used for counts, hints, and captions.
     *
     * <pre>
     *   Labels.MutedLabel ml = new Labels.MutedLabel("Showing 1–25 of 1,248 books");
     * </pre>
     */
    public static class MutedLabel extends JLabel {
        public MutedLabel(String text) {
            super(text);
            setFont(UIConstants.F_SMALL);
            setForeground(UIConstants.TEXT_MUTED);
        }

        /** Tiny 10 pt variant (same muted colour). */
        public MutedLabel(String text, boolean tiny) {
            this(text);
            if (tiny) setFont(UIConstants.F_TINY);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SectionLabel — sidebar ALL-CAPS category header
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Small ALL-CAPS sidebar section label (e.g. "MAIN", "REPORTS", "SYSTEM").
     *
     * <pre>
     *   Labels.SectionLabel sl = new Labels.SectionLabel("MAIN");
     *   sidebar.add(sl);
     * </pre>
     */
    public static class SectionLabel extends JLabel {
        public SectionLabel(String text) {
            super(text.toUpperCase());
            setFont(new Font("Segoe UI", Font.BOLD, 9));
            setForeground(UIConstants.SIDEBAR_SECTION);
            setBorder(new EmptyBorder(14, 16, 4, 16));
            setAlignmentX(LEFT_ALIGNMENT);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LinkLabel — red clickable hyperlink-style label
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Inline clickable label that looks and behaves like a hyperlink.
     * Colour darkens on hover; the cursor changes to a hand.
     *
     * <pre>
     *   Labels.LinkLabel ll = new Labels.LinkLabel("View all \u2192", () -&gt; portal.showPanel("Transactions"));
     * </pre>
     */
    public static class LinkLabel extends JLabel {
        private boolean hovered = false;

        public LinkLabel(String text, Runnable onClick) {
            super(text);
            setFont(UIConstants.F_SMALL);
            setForeground(UIConstants.ACCENT_RED);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                @Override public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
                @Override public void mouseClicked(MouseEvent e) { if (onClick != null) onClick.run(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            setForeground(hovered ? UIConstants.ACCENT_RED_DARK : UIConstants.ACCENT_RED);
            super.paintComponent(g);
            // Underline on hover
            if (hovered) {
                FontMetrics fm = g.getFontMetrics();
                int textW = fm.stringWidth(getText());
                Insets ins = getInsets();
                int x = ins.left;
                int y = ins.top + fm.getAscent() + 1;
                g.setColor(UIConstants.ACCENT_RED_DARK);
                g.drawLine(x, y, x + textW, y);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // AvatarLabel — circular avatar with initials
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Circular avatar label displaying 1–2 initials on a coloured background.
     * Size defaults to 30 × 30 px; use {@code setPreferredSize} to change.
     *
     * <pre>
     *   Labels.AvatarLabel av = new Labels.AvatarLabel("LR", UIConstants.ACCENT_RED);
     *   Labels.AvatarLabel av = new Labels.AvatarLabel("LR", UIConstants.ACCENT_RED, 40);
     * </pre>
     */
    public static class AvatarLabel extends JLabel {
        private final Color bgColor;

        public AvatarLabel(String initials, Color bg) {
            this(initials, bg, 30);
        }

        public AvatarLabel(String initials, Color bg, int size) {
            super(initials);
            this.bgColor = bg;
            setFont(new Font("Segoe UI", Font.BOLD, (int)(size * 0.42)));
            setForeground(Color.WHITE);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
            setOpaque(false);
            Dimension d = new Dimension(size, size);
            setPreferredSize(d);
            setMinimumSize(d);
            setMaximumSize(d);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillOval(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // StatusDotLabel — coloured dot + text (device / system status)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Inline coloured-dot + text status indicator — used in the top-bar and
     * portal status bar.
     *
     * <pre>
     *   Labels.StatusDotLabel nfc     = new Labels.StatusDotLabel("NFC Reader Online",   UIConstants.ACCENT_GREEN);
     *   Labels.StatusDotLabel printer = new Labels.StatusDotLabel("Printer Offline",      new Color(200, 50, 50));
     * </pre>
     */
    public static class StatusDotLabel extends JPanel {
        private final JLabel dotLabel;
        private final JLabel textLabel;

        public StatusDotLabel(String text, Color dotColor) {
            setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
            setOpaque(false);

            dotLabel = new JLabel("\u25CF");
            dotLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            dotLabel.setForeground(dotColor);

            textLabel = new JLabel(text);
            textLabel.setFont(UIConstants.F_TINY);
            textLabel.setForeground(UIConstants.TEXT_SECONDARY);

            add(dotLabel);
            add(textLabel);
        }

        /** Updates the status text at runtime. */
        public void setText(String text) { textLabel.setText(text); }

        /** Updates the dot colour at runtime (e.g. to reflect a status change). */
        public void setDotColor(Color color) { dotLabel.setForeground(color); }
    }
}
