package autolib.components;

import autolib.UIConstants;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Placeholder / empty-state component library for AutoLib.
 *
 * Components:
 *  - {@link EmptyState}     — no-data panel with icon, title, message, optional action button
 *  - {@link ComingSoon}     — "feature not yet implemented" placeholder panel
 *  - {@link ErrorState}     — error/failure panel with retry action
 *  - {@link SkeletonRow}    — animated shimmer loading row
 *  - {@link SkeletonTable}  — stacked SkeletonRows for table loading states
 *
 * Usage:
 * <pre>
 *   // No data found after search
 *   panel.add(new Placeholders.EmptyState(
 *       "\uD83D\uDCDA",
 *       "No Books Found",
 *       "Try adjusting your search filters.",
 *       "Clear Filters",
 *       this::clearFilters));
 *
 *   // Coming-soon section
 *   panel.add(new Placeholders.ComingSoon("Analytics", "Borrow trends and forecasts"));
 *
 *   // Loading rows while data fetches
 *   panel.add(new Placeholders.SkeletonTable(5));
 * </pre>
 */
public final class Placeholders {

    private Placeholders() {}

    // ─────────────────────────────────────────────────────────────────────────
    // EmptyState — generic no-data panel
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Vertically centred empty-state panel: large emoji icon, bold title,
     * muted description, and an optional action button.
     */
    public static class EmptyState extends JPanel {

        /**
         * @param icon        emoji or text icon shown large at the top
         * @param title       bold heading (e.g. "No Books Found")
         * @param description muted helper text below the title
         * @param actionLabel label for the optional button — pass {@code null} to hide
         * @param action      runnable called when the button is clicked
         */
        public EmptyState(String icon, String title, String description,
                          String actionLabel, Runnable action) {
            setLayout(new GridBagLayout());
            setOpaque(false);

            JPanel inner = new JPanel();
            inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
            inner.setOpaque(false);

            // Icon
            JLabel iconLbl = new JLabel(icon, SwingConstants.CENTER);
            iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
            iconLbl.setAlignmentX(CENTER_ALIGNMENT);
            iconLbl.setBorder(new EmptyBorder(0, 0, 12, 0));

            // Title
            JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
            titleLbl.setFont(UIConstants.F_HEADING);
            titleLbl.setForeground(UIConstants.TEXT_PRIMARY);
            titleLbl.setAlignmentX(CENTER_ALIGNMENT);

            // Description
            JLabel descLbl = new JLabel(
                    "<html><center>" + description + "</center></html>",
                    SwingConstants.CENTER);
            descLbl.setFont(UIConstants.F_BODY);
            descLbl.setForeground(UIConstants.TEXT_MUTED);
            descLbl.setAlignmentX(CENTER_ALIGNMENT);
            descLbl.setBorder(new EmptyBorder(6, 0, 20, 0));

            inner.add(iconLbl);
            inner.add(titleLbl);
            inner.add(descLbl);

            // Optional action button
            if (actionLabel != null && action != null) {
                Buttons.PrimaryButton btn = new Buttons.PrimaryButton(actionLabel);
                btn.setAlignmentX(CENTER_ALIGNMENT);
                btn.addActionListener(e -> action.run());
                inner.add(btn);
            }

            add(inner);
        }

        /** Convenience constructor — no action button. */
        public EmptyState(String icon, String title, String description) {
            this(icon, title, description, null, null);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ComingSoon — feature-not-yet-implemented panel
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * "Coming soon" placeholder shown in panels that are not yet implemented.
     * Displays a rocket icon, the feature name, and an optional description.
     *
     * <pre>
     *   panel.add(new Placeholders.ComingSoon("Analytics", "Borrow trends \u0026 forecasts"));
     * </pre>
     */
    public static class ComingSoon extends JPanel {

        public ComingSoon(String featureName, String description) {
            setLayout(new GridBagLayout());
            setOpaque(false);

            JPanel inner = new JPanel();
            inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
            inner.setOpaque(false);
            inner.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));

            // Rocket icon in red circle
            JLabel rocket = new JLabel("\uD83D\uDE80", SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(UIConstants.ACCENT_RED_LIGHT);
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            rocket.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
            rocket.setOpaque(false);
            rocket.setPreferredSize(new Dimension(80, 80));
            rocket.setMinimumSize(new Dimension(80, 80));
            rocket.setMaximumSize(new Dimension(80, 80));
            rocket.setAlignmentX(CENTER_ALIGNMENT);
            rocket.setBorder(new EmptyBorder(0, 0, 16, 0));

            // "Coming Soon" badge
            JLabel badge = new JLabel("COMING SOON", SwingConstants.CENTER);
            badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
            badge.setForeground(UIConstants.ACCENT_RED);
            badge.setBackground(UIConstants.ACCENT_RED_LIGHT);
            badge.setOpaque(true);
            badge.setBorder(new EmptyBorder(4, 14, 4, 14));
            badge.setAlignmentX(CENTER_ALIGNMENT);

            // Feature name
            JLabel nameLbl = new JLabel(featureName, SwingConstants.CENTER);
            nameLbl.setFont(UIConstants.F_HEADING);
            nameLbl.setForeground(UIConstants.TEXT_PRIMARY);
            nameLbl.setAlignmentX(CENTER_ALIGNMENT);
            nameLbl.setBorder(new EmptyBorder(10, 0, 6, 0));

            // Description
            JLabel descLbl = new JLabel(
                    "<html><center>" + description + "</center></html>",
                    SwingConstants.CENTER);
            descLbl.setFont(UIConstants.F_BODY);
            descLbl.setForeground(UIConstants.TEXT_MUTED);
            descLbl.setAlignmentX(CENTER_ALIGNMENT);

            inner.add(rocket);
            inner.add(badge);
            inner.add(nameLbl);
            inner.add(descLbl);

            add(inner);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ErrorState — error / failure panel with retry
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Error state shown when a data-load fails. Displays a warning icon,
     * a message, and a "Try Again" button that triggers the retry action.
     *
     * <pre>
     *   panel.add(new Placeholders.ErrorState(
     *       "Could not load books",
     *       "Check your connection and try again.",
     *       this::loadData));
     * </pre>
     */
    public static class ErrorState extends JPanel {

        public ErrorState(String title, String message, Runnable retryAction) {
            setLayout(new GridBagLayout());
            setOpaque(false);

            JPanel inner = new JPanel();
            inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
            inner.setOpaque(false);

            JLabel icon = new JLabel("\u26A0\uFE0F", SwingConstants.CENTER);
            icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
            icon.setAlignmentX(CENTER_ALIGNMENT);
            icon.setBorder(new EmptyBorder(0, 0, 10, 0));

            JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
            titleLbl.setFont(UIConstants.F_SUBHEAD);
            titleLbl.setForeground(UIConstants.TEXT_PRIMARY);
            titleLbl.setAlignmentX(CENTER_ALIGNMENT);

            JLabel msgLbl = new JLabel(
                    "<html><center>" + message + "</center></html>",
                    SwingConstants.CENTER);
            msgLbl.setFont(UIConstants.F_SMALL);
            msgLbl.setForeground(UIConstants.TEXT_MUTED);
            msgLbl.setAlignmentX(CENTER_ALIGNMENT);
            msgLbl.setBorder(new EmptyBorder(4, 0, 16, 0));

            Buttons.SecondaryButton retryBtn = new Buttons.SecondaryButton("\u21BA Try Again");
            retryBtn.setAlignmentX(CENTER_ALIGNMENT);
            retryBtn.addActionListener(e -> retryAction.run());

            inner.add(icon);
            inner.add(titleLbl);
            inner.add(msgLbl);
            inner.add(retryBtn);

            add(inner);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SkeletonRow — single animated shimmer loading row
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * A single skeleton loading row — a rounded grey bar that pulses to
     * indicate content is being loaded. Use multiple rows inside a
     * {@link SkeletonTable} for a full loading state.
     *
     * Width fills the parent; height is fixed at {@code rowHeight}.
     */
    public static class SkeletonRow extends JPanel {
        private float alpha = 0.3f;
        private boolean rising = true;
        private final Timer anim;
        private final int rowHeight;
        private final int[] colWidths; // relative widths in percent (must sum ≤ 100)

        /**
         * @param rowHeight  height of the row in pixels
         * @param colWidths  percentage widths for each "cell" bar (e.g. {15, 40, 20, 15, 10})
         */
        public SkeletonRow(int rowHeight, int[] colWidths) {
            this.rowHeight = rowHeight;
            this.colWidths = colWidths;
            setOpaque(false);
            setPreferredSize(new Dimension(0, rowHeight + 6));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, rowHeight + 6));

            // Pulse animation — updates alpha 20 fps
            anim = new Timer(50, e -> {
                alpha += rising ? 0.04f : -0.04f;
                if (alpha >= 0.55f) { alpha = 0.55f; rising = false; }
                if (alpha <= 0.15f) { alpha = 0.15f; rising = true;  }
                repaint();
            });
            anim.start();
        }

        /** Convenience — single full-width bar. */
        public SkeletonRow(int rowHeight) {
            this(rowHeight, new int[]{100});
        }

        /** Stop the animation (call when the panel is removed). */
        public void stopAnimation() { anim.stop(); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color bar = new Color(200, 206, 220, (int)(alpha * 255));
            g2.setColor(bar);

            int totalW = getWidth() - 24; // inner padding
            int barH   = rowHeight - 10;
            int y      = (getHeight() - barH) / 2;
            int x      = 12;
            int gap    = 8;

            for (int w : colWidths) {
                int barW = (int)(totalW * w / 100.0) - gap;
                if (barW > 0)
                    g2.fillRoundRect(x, y, barW, barH, 6, 6);
                x += barW + gap;
            }
            g2.dispose();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SkeletonTable — stacked skeleton rows for table loading state
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * A stack of {@link SkeletonRow}s used to fill the space of a table while
     * its data is loading.
     *
     * <pre>
     *   // Show while loading:
     *   contentArea.add(new Placeholders.SkeletonTable(6, 38,
     *       new int[]{10, 30, 20, 15, 10, 15}));
     *
     *   // Remove when data arrives:
     *   contentArea.remove(skeletonTable);
     *   skeletonTable.stopAll();
     * </pre>
     */
    public static class SkeletonTable extends JPanel {
        private final SkeletonRow[] rows;

        /**
         * @param rowCount  number of loading rows to display
         * @param rowHeight height of each row in pixels
         * @param colWidths percentage widths for each "cell" bar
         */
        public SkeletonTable(int rowCount, int rowHeight, int[] colWidths) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);
            rows = new SkeletonRow[rowCount];
            for (int i = 0; i < rowCount; i++) {
                rows[i] = new SkeletonRow(rowHeight, colWidths);
                // Offset each row's animation phase slightly
                final SkeletonRow row = rows[i];
                final int delay = i * 120;
                Timer offset = new Timer(delay, e -> row.anim.start());
                offset.setRepeats(false);
                row.anim.stop();
                offset.start();
                add(row);
                if (i < rowCount - 1)
                    add(Box.createVerticalStrut(1));
            }
        }


        /** Convenience — 5 rows, standard 38px height, 5 equal-ish columns. */
        public SkeletonTable(int rowCount) {
            this(rowCount, 38, new int[]{12, 35, 18, 20, 15});
        }

        /** Stops all row animations (call before removing from panel). */
        public void stopAll() {
            for (SkeletonRow r : rows) r.stopAnimation();
        }
    }
}
