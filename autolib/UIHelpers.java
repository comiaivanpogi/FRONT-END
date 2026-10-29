package autolib;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Factory helpers that eliminate duplicated UI-construction boilerplate
 * scattered across panels and dialogs.
 *
 * Every method here is purely functional (no shared mutable state) so it is
 * safe to call from any panel.
 */
public final class UIHelpers {

    private UIHelpers() {}

    // ─────────────────────────────────────────────────────────────────────────
    // Search bar
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Creates a rounded search bar with a magnifying-glass icon and
     * placeholder-text behaviour.
     *
     * @param placeholder  text shown when the field is empty / unfocused
     * @param onChange     called on every keystroke (pass {@code null} to skip)
     * @return             an array: [0] = outer JPanel wrapper, [1] = the JTextField
     */
    public static Object[] makeSearchBar(String placeholder, Runnable onChange) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(Color.WHITE);
        wrap.setBorder(new CompoundBorder(
                new LineBorder(UIConstants.INPUT_BORDER, 1, true),
                new EmptyBorder(0, 10, 0, 10)));

        JLabel ico = new JLabel("\uD83D\uDD0D ");
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        ico.setForeground(UIConstants.TEXT_MUTED);

        JTextField field = new JTextField();
        field.setFont(UIConstants.F_BODY);
        field.setBackground(Color.WHITE);
        field.setBorder(null);
        field.setOpaque(true);
        field.setText(placeholder);
        field.setForeground(UIConstants.INPUT_PLACEHOLDER);

        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(UIConstants.TEXT_PRIMARY);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(UIConstants.INPUT_PLACEHOLDER);
                }
            }
        });

        if (onChange != null) {
            DocumentListener dl = new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e)  { onChange.run(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e)  { onChange.run(); }
                public void changedUpdate(javax.swing.event.DocumentEvent e) { onChange.run(); }
            };
            field.getDocument().addDocumentListener(dl);
        }

        wrap.add(ico,   BorderLayout.WEST);
        wrap.add(field, BorderLayout.CENTER);
        return new Object[]{wrap, field};
    }

    /** Returns the real query (empty string when placeholder is still shown). */
    public static String getSearchQuery(JTextField field, String placeholder) {
        String t = field.getText();
        return t.equals(placeholder) ? "" : t;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Styled JTable
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Applies the standard AutoLib table styling to the given table.
     *
     * @param table     the table to style
     * @param rowHeight row height in pixels (typical: 38 or 40)
     */
    public static void styleTable(JTable table, int rowHeight) {
        table.setFont(UIConstants.F_TABLE_CELL);
        table.setForeground(UIConstants.TEXT_PRIMARY);
        table.setBackground(UIConstants.CARD_BG);
        table.setGridColor(UIConstants.TABLE_GRID);
        table.setRowHeight(rowHeight);
        table.setShowGrid(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(253, 242, 243));
        table.setSelectionForeground(UIConstants.TEXT_PRIMARY);

        JTableHeader hdr = table.getTableHeader();
        hdr.setFont(UIConstants.F_TABLE_HDR);
        hdr.setBackground(UIConstants.TABLE_HEADER_BG);
        hdr.setForeground(UIConstants.TABLE_HEADER_TEXT);
        hdr.setBorder(new MatteBorder(0, 0, 1, 0, UIConstants.TABLE_GRID));
        hdr.setReorderingAllowed(false);
        hdr.setPreferredSize(new Dimension(0, 36));
    }

    /** Wraps a table in a styled, borderless scroll pane. */
    public static JScrollPane makeTableScroll(JTable table) {
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(null);
        sp.setBackground(UIConstants.CARD_BG);
        sp.getViewport().setBackground(UIConstants.CARD_BG);
        sp.getVerticalScrollBar().setUnitIncrement(10);
        return sp;
    }

    /** Default alternating-row renderer (plain text, left-padded 8 px). */
    public static TableCellRenderer alternatingRenderer() {
        return (tbl, val, sel, foc, row, col) -> {
            JLabel l = new JLabel(String.valueOf(val));
            l.setFont(UIConstants.F_TABLE_CELL);
            l.setForeground(UIConstants.TEXT_PRIMARY);
            l.setBorder(new EmptyBorder(0, 8, 0, 0));
            l.setBackground(row % 2 == 0 ? UIConstants.TABLE_ROW_EVEN : UIConstants.TABLE_ROW_ODD);
            l.setOpaque(true);
            return l;
        };
    }

    /** Monospaced ID / barcode renderer (Consolas, muted colour). */
    public static TableCellRenderer monoIdRenderer() {
        return (tbl, val, sel, foc, row, col) -> {
            JLabel l = new JLabel(String.valueOf(val));
            l.setFont(new Font("Consolas", Font.PLAIN, 12));
            l.setForeground(UIConstants.TEXT_MUTED);
            l.setBorder(new EmptyBorder(0, 12, 0, 0));
            l.setBackground(row % 2 == 0 ? UIConstants.TABLE_ROW_EVEN : UIConstants.TABLE_ROW_ODD);
            l.setOpaque(true);
            return l;
        };
    }

    /** Clickable-style link renderer. */
    public static TableCellRenderer linkRenderer(Color fg) {
        return (tbl, val, sel, foc, row, col) -> {
            JLabel l = new JLabel(String.valueOf(val));
            l.setFont(UIConstants.F_TABLE_CELL);
            l.setForeground(fg);
            l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            l.setBorder(new EmptyBorder(0, 4, 0, 0));
            l.setBackground(row % 2 == 0 ? UIConstants.TABLE_ROW_EVEN : UIConstants.TABLE_ROW_ODD);
            l.setOpaque(true);
            return l;
        };
    }

    /** Badge renderer – wraps a StatusBadge in a flow panel. */
    public static TableCellRenderer badgeRenderer(java.util.function.Function<String, UIComponents.StatusBadge> factory) {
        return (tbl, val, sel, foc, row, col) -> {
            JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
            wrap.setBackground(row % 2 == 0 ? UIConstants.TABLE_ROW_EVEN : UIConstants.TABLE_ROW_ODD);
            wrap.add(factory.apply(String.valueOf(val)));
            return wrap;
        };
    }

    /** Two icon-button action renderer. */
    public static TableCellRenderer actionsRenderer(String viewIcon, String delIcon) {
        return (tbl, val, sel, foc, row, col) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
            p.setBackground(row % 2 == 0 ? UIConstants.TABLE_ROW_EVEN : UIConstants.TABLE_ROW_ODD);
            p.add(new UIComponents.IconButton(viewIcon));
            p.add(new UIComponents.IconButton(delIcon));
            return p;
        };
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Pager bar
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Builds a standard pager bar: count label on the left, page buttons on
     * the right.
     *
     * @param pageLabels  e.g. {"&#8249; Prev", "1", "2", "3", "Next &#8250;"}
     * @param activePage  label of the currently-active page button (e.g. "1")
     * @param countRef    single-element array; countRef[0] is set to the JLabel
     *                    so callers can update it after data loads
     */
    public static JPanel makePager(String[] pageLabels, String activePage, JLabel[] countRef) {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);

        JLabel countLabel = new JLabel();
        countLabel.setFont(UIConstants.F_SMALL);
        countLabel.setForeground(UIConstants.TEXT_MUTED);
        if (countRef != null && countRef.length > 0)
            countRef[0] = countLabel;

        JPanel pages = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        pages.setOpaque(false);
        for (String lbl : pageLabels) {
            boolean active = lbl.equals(activePage);
            JButton btn = new JButton(lbl);
            btn.setFont(UIConstants.F_SMALL);
            btn.setBackground(active ? UIConstants.ACCENT_RED : Color.WHITE);
            btn.setForeground(active ? Color.WHITE : UIConstants.TEXT_SECONDARY);
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);
            btn.setBorder(new CompoundBorder(
                    new LineBorder(UIConstants.CARD_BORDER, 1, true),
                    new EmptyBorder(4, 10, 4, 10)));
            btn.setFocusPainted(false);
            pages.add(btn);
        }

        bar.add(countLabel, BorderLayout.WEST);
        bar.add(pages,      BorderLayout.EAST);
        return bar;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Status dot (portal/top-bar status indicator)
    // ─────────────────────────────────────────────────────────────────────────

    /** Builds a small coloured-dot + label row. */
    public static JPanel makeStatusDot(String text, Color dotColor) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row.setOpaque(false);

        JLabel dot = new JLabel("\u25CF");
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        dot.setForeground(dotColor);

        JLabel lbl = new JLabel(text);
        lbl.setFont(UIConstants.F_TINY);
        lbl.setForeground(UIConstants.TEXT_SECONDARY);

        row.add(dot);
        row.add(lbl);
        return row;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Card header
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Builds a card header row: bold title on the left, optional right widget.
     *
     * @param title title text
     * @param right optional right component (may be {@code null})
     */
    public static JPanel makeCardHeader(String title, JComponent right) {
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(UIConstants.F_SUBHEAD);
        t.setForeground(UIConstants.TEXT_PRIMARY);
        hdr.add(t, BorderLayout.WEST);

        if (right != null)
            hdr.add(right, BorderLayout.EAST);
        return hdr;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Logo icon
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Returns a square panel that paints the AutoLib logo icon (red rounded
     * square with three white horizontal lines representing book pages).
     *
     * @param size   pixel size of the square
     * @param radius corner radius
     */
    public static JPanel makeLogoIcon(int size, int radius) {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(185, 28, 44));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cx = getWidth() / 2, cy = getHeight() / 2;
                int hw = (int) (getWidth()  * 0.40);
                int gap = (int) (getHeight() * 0.18);
                g2.drawLine(cx - hw, cy - gap, cx + hw,              cy - gap);
                g2.drawLine(cx - hw, cy,       cx + hw,              cy);
                g2.drawLine(cx - hw, cy + gap, cx + (int)(hw * 0.55), cy + gap);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        Dimension d = new Dimension(size, size);
        p.setPreferredSize(d);
        p.setMinimumSize(d);
        p.setMaximumSize(d);
        return p;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Dialog utilities
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Makes a dialog's header panel act as a drag handle so the undecorated
     * dialog can be repositioned by the user.
     */
    public static void makeDraggable(JPanel header, JDialog dialog) {
        MouseAdapter drag = new MouseAdapter() {
            Point start;
            @Override public void mousePressed(MouseEvent e)  { start = e.getPoint(); }
            @Override public void mouseDragged(MouseEvent e) {
                Point l = dialog.getLocation();
                dialog.setLocation(l.x + e.getX() - start.x,
                                   l.y + e.getY() - start.y);
            }
        };
        header.addMouseListener(drag);
        header.addMouseMotionListener(drag);
    }

    /**
     * Builds a standard undecorated-dialog header bar: dark background,
     * title on the left, close (✕) on the right, draggable.
     */
    public static JPanel makeDialogHeader(String titleText, JDialog dialog) {
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(UIConstants.SIDEBAR_BG);
        hdr.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        hdr.add(title, BorderLayout.WEST);

        JButton closeBtn = new JButton("\u2715");
        closeBtn.setFont(UIConstants.F_BODY);
        closeBtn.setForeground(new Color(140, 150, 170));
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dialog.dispose());
        hdr.add(closeBtn, BorderLayout.EAST);

        makeDraggable(hdr, dialog);
        return hdr;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Toggle button (Week / Month style)
    // ─────────────────────────────────────────────────────────────────────────

    /** Builds a small opaque toggle button. */
    public static JButton makeToggleBtn(String text, boolean active) {
        JButton b = new JButton(text);
        b.setFont(UIConstants.F_SMALL);
        b.setBackground(active ? UIConstants.ACCENT_RED : new Color(240, 242, 248));
        b.setForeground(active ? Color.WHITE : UIConstants.TEXT_SECONDARY);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(4, 12, 4, 12));
        return b;
    }

    /** Marks {@code btn} as active and {@code other} as inactive (visual only). */
    public static void activateToggle(JButton btn, JButton other) {
        btn.setBackground(UIConstants.ACCENT_RED);
        btn.setForeground(Color.WHITE);
        other.setBackground(new Color(240, 242, 248));
        other.setForeground(UIConstants.TEXT_SECONDARY);
    }
}
