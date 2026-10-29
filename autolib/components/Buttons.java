package autolib.components;

import autolib.UIConstants;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Button component library for AutoLib.
 *
 * Variants:
 *  - {@link PrimaryButton}   — solid red; main call-to-action
 *  - {@link SecondaryButton} — outline ghost; cancel / secondary action
 *  - {@link DangerButton}    — dark-red fill; destructive actions
 *  - {@link SuccessButton}   — green fill; confirm / save actions
 *  - {@link TextButton}      — flat text link; inline navigation
 *  - {@link IconButton}      — small square icon; table row actions
 *  - {@link IconLabelButton} — icon + text label; toolbar actions
 *  - {@link ToggleButton}    — opaque Week/Month style state toggle
 *
 * All buttons are AA-rendered, have hand cursors, hover feedback, and
 * customisable padding via their constructors.
 *
 * Usage:
 * <pre>
 *   Buttons.PrimaryButton   save  = new Buttons.PrimaryButton("+ Add Book");
 *   Buttons.SecondaryButton cancel = new Buttons.SecondaryButton("Cancel");
 *   Buttons.DangerButton    del   = new Buttons.DangerButton("Delete");
 *   Buttons.SuccessButton   ok    = new Buttons.SuccessButton("\u2713 Save Changes");
 *   Buttons.TextButton      link  = new Buttons.TextButton("View all \u2192", UIConstants.ACCENT_RED);
 *   Buttons.IconButton      eye   = new Buttons.IconButton("\uD83D\uDC41");
 *   Buttons.IconLabelButton exp   = new Buttons.IconLabelButton("\u2193", "Export");
 *   Buttons.ToggleButton    wk    = new Buttons.ToggleButton("Week", true);
 * </pre>
 */
public final class Buttons {

    private Buttons() {}

    // ── Internal base ──────────────────────────────────────────────────────────
    /**
     * Base class: AA-rounded fill, hover tracking, hand cursor,
     * no border/focus/content-area painting.
     */
    private abstract static class BaseButton extends JButton {
        protected boolean hovered = false;

        protected BaseButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
            });
        }

        /** Fills a rounded-rectangle and optionally strokes a border. */
        protected void fillRounded(Graphics g, Color fill, Color stroke) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            if (stroke != null) {
                g2.setColor(stroke);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            }
            g2.dispose();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PrimaryButton — solid red fill
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Main call-to-action button (solid red).
     * <pre>
     *   new Buttons.PrimaryButton("+ Add Book")
     * </pre>
     */
    public static class PrimaryButton extends BaseButton {
        public PrimaryButton(String text) {
            super(text);
            setFont(UIConstants.F_BTN);
            setForeground(Color.WHITE);
            setBorder(new EmptyBorder(8, 20, 8, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            fillRounded(g, hovered ? UIConstants.ACCENT_RED_DARK : UIConstants.ACCENT_RED, null);
            super.paintComponent(g);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SecondaryButton — outline / ghost
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Secondary / cancel action button (outline style).
     * <pre>
     *   new Buttons.SecondaryButton("Cancel")
     * </pre>
     */
    public static class SecondaryButton extends BaseButton {
        private static final Color HOVER_BG = new Color(240, 242, 246);

        public SecondaryButton(String text) {
            super(text);
            setFont(UIConstants.F_BTN);
            setForeground(UIConstants.TEXT_SECONDARY);
            setBorder(new EmptyBorder(8, 18, 8, 18));
        }

        @Override
        protected void paintComponent(Graphics g) {
            fillRounded(g, hovered ? HOVER_BG : new Color(0, 0, 0, 0), UIConstants.INPUT_BORDER);
            super.paintComponent(g);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DangerButton — dark red fill for destructive actions
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Destructive-action button (dark red — slightly darker than primary).
     * <pre>
     *   new Buttons.DangerButton("Delete Record")
     * </pre>
     */
    public static class DangerButton extends BaseButton {
        private static final Color BASE  = new Color(180, 28, 40);
        private static final Color HOVER = new Color(140, 18, 28);

        public DangerButton(String text) {
            super(text);
            setFont(UIConstants.F_BTN);
            setForeground(Color.WHITE);
            setBorder(new EmptyBorder(8, 20, 8, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            fillRounded(g, hovered ? HOVER : BASE, null);
            super.paintComponent(g);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SuccessButton — green fill for confirm / save
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Confirm / save button (green).
     * <pre>
     *   new Buttons.SuccessButton("\u2713 Save Changes")
     * </pre>
     */
    public static class SuccessButton extends BaseButton {
        private static final Color BASE  = new Color(22, 163, 74);
        private static final Color HOVER = new Color(15, 130, 55);

        public SuccessButton(String text) {
            super(text);
            setFont(UIConstants.F_BTN);
            setForeground(Color.WHITE);
            setBorder(new EmptyBorder(8, 20, 8, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            fillRounded(g, hovered ? HOVER : BASE, null);
            super.paintComponent(g);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TextButton — flat text / link style
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Flat text-link button (no background, coloured text, darkens on hover).
     * <pre>
     *   new Buttons.TextButton("View all \u2192", UIConstants.ACCENT_RED)
     * </pre>
     */
    public static class TextButton extends BaseButton {
        private final Color normalFg;
        private final Color hoverFg;

        public TextButton(String text, Color fg) {
            super(text);
            this.normalFg = fg;
            this.hoverFg  = fg.darker();
            setFont(UIConstants.F_SMALL);
            setForeground(fg);
            setBorder(new EmptyBorder(2, 0, 2, 0));
        }

        @Override
        protected void paintComponent(Graphics g) {
            setForeground(hovered ? hoverFg : normalFg);
            super.paintComponent(g);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // IconButton — small square icon button (table row actions)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Small square (28 × 28 px) icon button for table row actions.
     * <pre>
     *   new Buttons.IconButton("\uD83D\uDC41")   // eye / view
     *   new Buttons.IconButton("\u2212")          // minus / delete
     *   new Buttons.IconButton("\u270E")          // pencil / edit
     * </pre>
     */
    public static class IconButton extends BaseButton {
        private static final Color HOVER_BG = new Color(230, 232, 240);

        public IconButton(String icon) {
            super(icon);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(UIConstants.TEXT_SECONDARY);
            setPreferredSize(new Dimension(28, 28));
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (hovered) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(HOVER_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // IconLabelButton — emoji icon + text label (toolbar buttons)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Toolbar button that combines an emoji icon with a text label.
     * Uses the same red primary style as PrimaryButton.
     * <pre>
     *   new Buttons.IconLabelButton("\u2193", "Export")
     *   new Buttons.IconLabelButton("\u2295", "Add Book")
     * </pre>
     */
    public static class IconLabelButton extends BaseButton {
        public IconLabelButton(String icon, String label) {
            super(icon + "  " + label);
            setFont(UIConstants.F_BTN);
            setForeground(Color.WHITE);
            setBorder(new EmptyBorder(8, 16, 8, 16));
        }

        @Override
        protected void paintComponent(Graphics g) {
            fillRounded(g, hovered ? UIConstants.ACCENT_RED_DARK : UIConstants.ACCENT_RED, null);
            super.paintComponent(g);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ToggleButton — opaque pill-style state toggle (e.g. Week / Month)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Two-state opaque toggle button (active = solid red, inactive = light grey).
     * Call {@link #setActive(boolean)} to switch state programmatically.
     * <pre>
     *   Buttons.ToggleButton wk = new Buttons.ToggleButton("Week",  true);
     *   Buttons.ToggleButton mo = new Buttons.ToggleButton("Month", false);
     *   wk.addActionListener(e -&gt; { wk.setActive(true); mo.setActive(false); });
     * </pre>
     */
    public static class ToggleButton extends JButton {
        private static final Color INACTIVE_BG = new Color(240, 242, 248);
        private boolean active;

        public ToggleButton(String text, boolean active) {
            super(text);
            this.active = active;
            setFont(UIConstants.F_SMALL);
            setOpaque(true);
            setContentAreaFilled(true);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(4, 12, 4, 12));
            applyStyle();
        }

        /** Updates the visual active/inactive state. */
        public void setActive(boolean active) {
            this.active = active;
            applyStyle();
        }

        public boolean isActive() { return active; }

        private void applyStyle() {
            setBackground(active ? UIConstants.ACCENT_RED : INACTIVE_BG);
            setForeground(active ? Color.WHITE : UIConstants.TEXT_SECONDARY);
            repaint();
        }
    }
}
