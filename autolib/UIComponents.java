package autolib;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * Shared UI helper components used throughout AutoLib.
 */
public class UIComponents {

    // ── Rounded panel ─────────────────────────────────────────────────────────
    public static class RoundedPanel extends JPanel {
        private int radius;
        private Color bg;
        private Color border;

        public RoundedPanel(int radius, Color bg) {
            this(radius, bg, null);
        }

        public RoundedPanel(int radius, Color bg, Color border) {
            this.radius = radius;
            this.bg = bg;
            this.border = border;
            setOpaque(false);
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

    // ── Status badge label ────────────────────────────────────────────────────
    public static class StatusBadge extends JLabel {
        public StatusBadge(String text, Color fg, Color bg) {
            super(text);
            setFont(UIConstants.F_TINY);
            setForeground(fg);
            setOpaque(false);
            setHorizontalAlignment(CENTER);
            setBorder(new EmptyBorder(3, 10, 3, 10));
            setBackground(bg);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), UIConstants.BADGE_RADIUS, UIConstants.BADGE_RADIUS);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Build a StatusBadge from a book status string. */
    public static StatusBadge bookStatusBadge(String status) {
        switch (status) {
            case "All Out":
                return new StatusBadge("All Out", UIConstants.ACCENT_AMBER, UIConstants.ACCENT_AMBER_BG);
            case "Borrowed":
                return new StatusBadge("Borrowed", UIConstants.ACCENT_RED, UIConstants.ACCENT_RED_LIGHT);
            default:
                return new StatusBadge("Available", UIConstants.ACCENT_GREEN, UIConstants.ACCENT_GREEN_BG);
        }
    }

    /** Build a StatusBadge from a transaction type string. */
    public static StatusBadge txBadge(String type) {
        switch (type) {
            case "Borrowed":
                return new StatusBadge("Borrowed", UIConstants.ACCENT_RED, UIConstants.ACCENT_RED_LIGHT);
            case "Overdue":
                return new StatusBadge("Overdue", UIConstants.ACCENT_ORANGE, UIConstants.ACCENT_ORANGE_BG);
            default:
                return new StatusBadge("Returned", UIConstants.ACCENT_GREEN, UIConstants.ACCENT_GREEN_BG);
        }
    }

    // ── Primary (red) button ──────────────────────────────────────────────────
    public static class PrimaryButton extends JButton {
        private boolean hovered = false;

        public PrimaryButton(String text) {
            super(text);
            setFont(UIConstants.F_BTN);
            setForeground(Color.WHITE);
            setBackground(UIConstants.ACCENT_RED);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(8, 20, 8, 20));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hovered ? UIConstants.ACCENT_RED_DARK : UIConstants.ACCENT_RED);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ── Ghost (outline) button ────────────────────────────────────────────────
    public static class GhostButton extends JButton {
        private boolean hovered = false;

        public GhostButton(String text) {
            super(text);
            setFont(UIConstants.F_BTN);
            setForeground(UIConstants.TEXT_SECONDARY);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(8, 18, 8, 18));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (hovered) {
                g2.setColor(new Color(240, 242, 246));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            }
            g2.setColor(UIConstants.INPUT_BORDER);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ── Icon button (small square, for table actions) ─────────────────────────
    public static class IconButton extends JButton {
        private boolean hovered = false;

        public IconButton(String text) {
            super(text);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(UIConstants.TEXT_SECONDARY);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(28, 28));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (hovered) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(230, 232, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }

    // ── Styled text field ─────────────────────────────────────────────────────
    public static JTextField styledField(String placeholder) {
        JTextField f = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setFont(getFont());
                    g2.setColor(UIConstants.INPUT_PLACEHOLDER);
                    Insets ins = getInsets();
                    g2.drawString(placeholder, ins.left + 2, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 1);
                    g2.dispose();
                }
            }
        };
        f.setFont(UIConstants.F_BODY);
        f.setForeground(UIConstants.TEXT_PRIMARY);
        f.setBackground(UIConstants.INPUT_BG);
        f.setBorder(new CompoundBorder(
                new LineBorder(UIConstants.INPUT_BORDER, 1, true),
                new EmptyBorder(7, 10, 7, 10)));
        return f;
    }

    // ── Styled combo box ──────────────────────────────────────────────────────
    public static JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(UIConstants.F_BODY);
        c.setBackground(UIConstants.INPUT_BG);
        c.setForeground(UIConstants.TEXT_PRIMARY);
        c.setBorder(new LineBorder(UIConstants.INPUT_BORDER, 1, true));
        return c;
    }

    // ── Section label (sidebar category) ─────────────────────────────────────
    public static JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(UIConstants.SIDEBAR_SECTION);
        l.setBorder(new EmptyBorder(14, 16, 4, 16));
        return l;
    }

    // ── Avatar circle label ───────────────────────────────────────────────────
    public static class AvatarLabel extends JLabel {
        private Color bg;

        public AvatarLabel(String initials, Color bg) {
            super(initials);
            this.bg = bg;
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setForeground(Color.WHITE);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
            setOpaque(false);
            setPreferredSize(new Dimension(30, 30));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillOval(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ── Divider ───────────────────────────────────────────────────────────────
    public static JSeparator divider() {
        JSeparator s = new JSeparator();
        s.setForeground(UIConstants.CARD_BORDER);
        return s;
    }

    // ── Bar chart ─────────────────────────────────────────────────────────────
    /**
     * Simple bar-chart panel used by the Dashboard (and potentially other
     * panels).  Call {@link #setData} to update the bars and trigger a repaint.
     */
    public static class BarChart extends JPanel {
        private int[]    data;
        private String[] labels;

        public BarChart(int[] data, String[] labels) {
            this.data   = data;
            this.labels = labels;
            setOpaque(false);
            setPreferredSize(new Dimension(0, 180));
        }

        public void setData(int[] data, String[] labels) {
            this.data   = data;
            this.labels = labels;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data == null || data.length == 0) return;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int W = getWidth(), H = getHeight();
            int pad = 10, labelH = 22, topPad = 10;
            int chartH = H - labelH - topPad;
            int n = data.length;
            int max = 1;
            for (int v : data) if (v > max) max = v;

            int barW = (W - pad * 2) / n;
            int gap  = (int) (barW * 0.25);
            int bw   = barW - gap;

            // Grid lines
            g2.setColor(new Color(232, 236, 244));
            g2.setStroke(new BasicStroke(0.8f));
            for (int i = 0; i <= 3; i++) {
                int y = topPad + (int) (chartH * i / 3.0);
                g2.drawLine(pad, y, W - pad, y);
            }

            // Bars
            for (int i = 0; i < n; i++) {
                int barH = (int) ((double) data[i] / max * chartH);
                int x    = pad + i * barW + gap / 2;
                int y    = topPad + chartH - barH;
                boolean isMax = (data[i] == max);
                g2.setColor(isMax ? UIConstants.ACCENT_RED : new Color(210, 214, 226));
                g2.fillRoundRect(x, y, bw, barH, 5, 5);
            }

            // Labels
            g2.setFont(UIConstants.F_TINY);
            g2.setColor(UIConstants.TEXT_MUTED);
            FontMetrics fm = g2.getFontMetrics();
            for (int i = 0; i < n && i < labels.length; i++) {
                int x  = pad + i * barW + gap / 2 + bw / 2;
                int lw = fm.stringWidth(labels[i]);
                g2.drawString(labels[i], x - lw / 2, H - 4);
            }
            g2.dispose();
        }
    }
}
