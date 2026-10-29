package autolib.panels;

import autolib.*;
import autolib.models.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Dashboard panel — stat cards, borrowing chart, recent transactions,
 * most-borrowed table, overdue alerts.
 */
public class DashboardPanel extends JPanel {

    private final LibrarianPortal portal;
    private final DataStore db = DataStore.get();

    // Week / Month toggle
    private UIComponents.BarChart chartPanel;

    public DashboardPanel(LibrarianPortal portal) {
        this.portal = portal;
        setBackground(UIConstants.CONTENT_BG);
        setLayout(new BorderLayout(0, 0));
        build();
    }

    public void refresh() { /* data is live */ }

    private void build() {
        JPanel scroll = new JPanel();
        scroll.setBackground(UIConstants.CONTENT_BG);
        scroll.setLayout(new BoxLayout(scroll, BoxLayout.Y_AXIS));
        scroll.setBorder(new EmptyBorder(20, 20, 20, 20));

        // ── Stat cards ─────────────────────────────────────────────────────
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 12, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        statsRow.add(statCard("\uD83D\uDCDA", String.valueOf(db.totalBooks()),
                "Total Books", "+12 this month", UIConstants.ACCENT_GREEN));
        statsRow.add(statCard("\uD83D\uDCE6", String.valueOf(db.currentlyBorrowed()),
                "Currently Borrowed", "→ Active loans", UIConstants.TEXT_MUTED));
        statsRow.add(statCard("\u23F0", String.valueOf(db.overdueBooks()),
                "Overdue Books", "+3 new overdue", UIConstants.ACCENT_RED));
        statsRow.add(statCard("\uD83D\uDC64", String.valueOf(db.registeredStudents()),
                "Registered Students", "+ 8 new", UIConstants.ACCENT_GREEN));

        // ── Middle row: chart + recent transactions ─────────────────────────
        JPanel middleRow = new JPanel(new GridLayout(1, 2, 12, 0));
        middleRow.setOpaque(false);
        middleRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        middleRow.add(buildChartCard());
        middleRow.add(buildRecentTxCard());

        // ── Bottom row: most borrowed + overdue alerts ──────────────────────
        JPanel bottomRow = new JPanel(new GridLayout(1, 2, 12, 0));
        bottomRow.setOpaque(false);
        bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        bottomRow.add(buildMostBorrowedCard());
        bottomRow.add(buildOverdueCard());

        scroll.add(statsRow);
        scroll.add(Box.createVerticalStrut(16));
        scroll.add(middleRow);
        scroll.add(Box.createVerticalStrut(16));
        scroll.add(bottomRow);

        JScrollPane sp = new JScrollPane(scroll);
        sp.setBorder(null);
        sp.setBackground(UIConstants.CONTENT_BG);
        sp.getViewport().setBackground(UIConstants.CONTENT_BG);
        sp.getVerticalScrollBar().setUnitIncrement(12);
        add(sp, BorderLayout.CENTER);
    }

    // ── Stat card ─────────────────────────────────────────────────────────────
    private JPanel statCard(String icon, String number, String label, String sub, Color subColor) {
        UIComponents.RoundedPanel card = new UIComponents.RoundedPanel(
                UIConstants.CARD_RADIUS, UIConstants.CARD_BG, UIConstants.CARD_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        iconLbl.setBorder(new EmptyBorder(0, 0, 8, 0));

        JLabel numLbl = new JLabel(number);
        numLbl.setFont(UIConstants.F_STAT_NUM);
        numLbl.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.F_STAT_LBL);
        lbl.setForeground(UIConstants.TEXT_SECONDARY);

        UIComponents.StatusBadge subBadge = new UIComponents.StatusBadge(sub, subColor,
                subColor == UIConstants.ACCENT_GREEN ? UIConstants.ACCENT_GREEN_BG
                        : subColor == UIConstants.ACCENT_RED ? UIConstants.ACCENT_RED_LIGHT
                                : new Color(240, 242, 248));
        subBadge.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(iconLbl);
        center.add(numLbl);
        center.add(lbl);
        center.add(Box.createVerticalStrut(8));
        center.add(subBadge);

        card.add(center, BorderLayout.CENTER);
        return card;
    }

    // ── Borrowing Activity chart card ─────────────────────────────────────────
    private JPanel buildChartCard() {
        UIComponents.RoundedPanel card = new UIComponents.RoundedPanel(
                UIConstants.CARD_RADIUS, UIConstants.CARD_BG, UIConstants.CARD_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JButton wkBtn = UIHelpers.makeToggleBtn("Week",  true);
        JButton moBtn = UIHelpers.makeToggleBtn("Month", false);

        JPanel toggle = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
        toggle.setOpaque(false);
        toggle.add(wkBtn);
        toggle.add(moBtn);

        JPanel hdr = UIHelpers.makeCardHeader("Borrowing Activity", toggle);

        chartPanel = new UIComponents.BarChart(
                db.weeklyBorrowData(),
                new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"});

        wkBtn.addActionListener(e -> {
            chartPanel.setData(db.weeklyBorrowData(),
                    new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"});
            UIHelpers.activateToggle(wkBtn, moBtn);
        });
        moBtn.addActionListener(e -> {
            chartPanel.setData(db.monthlyBorrowData(),
                    new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul"});
            UIHelpers.activateToggle(moBtn, wkBtn);
        });

        card.add(hdr, BorderLayout.NORTH);
        card.add(Box.createVerticalStrut(8), BorderLayout.CENTER);
        card.add(chartPanel, BorderLayout.SOUTH);
        return card;
    }

    // ── Recent Transactions card ──────────────────────────────────────────────
    private JPanel buildRecentTxCard() {
        UIComponents.RoundedPanel card = new UIComponents.RoundedPanel(
                UIConstants.CARD_RADIUS, UIConstants.CARD_BG, UIConstants.CARD_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 12, 18));

        JLabel viewAll = new JLabel("View all \u2192");
        viewAll.setFont(UIConstants.F_SMALL);
        viewAll.setForeground(UIConstants.ACCENT_RED);
        viewAll.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        viewAll.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { portal.showPanel("Transactions"); }
        });

        JPanel hdr = UIHelpers.makeCardHeader("Recent Transactions", viewAll);

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(10, 0, 0, 0));

        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("h:mm a");
        Color[] avatarColors = {
                new Color(59, 130, 246), new Color(34, 197, 94),
                new Color(234, 88, 12),  new Color(168, 85, 247),
                new Color(20, 184, 166)
        };
        int ci = 0;
        for (Transaction tx : db.getTransactions()) {
            list.add(txRow(tx, avatarColors[ci % avatarColors.length], timeFmt));
            list.add(Box.createVerticalStrut(4));
            ci++;
        }

        card.add(hdr,  BorderLayout.NORTH);
        card.add(list, BorderLayout.CENTER);
        return card;
    }

    private JPanel txRow(Transaction tx, Color avatarColor, DateTimeFormatter fmt) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(4, 0, 4, 0));

        UIComponents.AvatarLabel av = new UIComponents.AvatarLabel(tx.getStudentInitial(), avatarColor);
        av.setPreferredSize(new Dimension(30, 30));

        JPanel mid = new JPanel();
        mid.setLayout(new BoxLayout(mid, BoxLayout.Y_AXIS));
        mid.setOpaque(false);

        JLabel name = new JLabel(tx.getStudentName());
        name.setFont(UIConstants.F_SUBHEAD);
        name.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel book = new JLabel(tx.getBookTitle());
        book.setFont(UIConstants.F_TINY);
        book.setForeground(UIConstants.TEXT_MUTED);
        mid.add(name);
        mid.add(book);

        String timeStr = tx.getTimestamp().toLocalDate().equals(java.time.LocalDate.now())
                ? tx.getTimestamp().format(fmt) : "Yesterday";
        JLabel timeLbl = new JLabel(timeStr);
        timeLbl.setFont(UIConstants.F_TINY);
        timeLbl.setForeground(UIConstants.TEXT_MUTED);
        timeLbl.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel right = new JPanel(new BorderLayout(0, 4));
        right.setOpaque(false);
        right.add(timeLbl, BorderLayout.NORTH);
        right.add(UIComponents.txBadge(tx.getTypeText()), BorderLayout.SOUTH);

        row.add(av,    BorderLayout.WEST);
        row.add(mid,   BorderLayout.CENTER);
        row.add(right, BorderLayout.EAST);
        return row;
    }

    // ── Most Borrowed card ────────────────────────────────────────────────────
    private JPanel buildMostBorrowedCard() {
        UIComponents.RoundedPanel card = new UIComponents.RoundedPanel(
                UIConstants.CARD_RADIUS, UIConstants.CARD_BG, UIConstants.CARD_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel title = new JLabel("Most Borrowed Books");
        title.setFont(UIConstants.F_SUBHEAD);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));

        String[] cols = {"#", "TITLE", "AUTHOR", "BORROWED", "AVAILABLE"};
        List<Book> mb = db.mostBorrowed();
        Object[][] data = new Object[mb.size()][5];
        for (int i = 0; i < mb.size(); i++) {
            Book b = mb.get(i);
            data[i] = new Object[]{i + 1, b.getTitle(), b.getAuthor(),
                    b.getBorrowedCount() + "\u00d7", b.getAvailableCopies()};
        }

        JTable tbl = new JTable(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        UIHelpers.styleTable(tbl, 28);
        tbl.setSelectionBackground(UIConstants.ACCENT_RED_LIGHT);

        card.add(title, BorderLayout.NORTH);
        card.add(UIHelpers.makeTableScroll(tbl), BorderLayout.CENTER);
        return card;
    }

    // ── Overdue Alerts card ───────────────────────────────────────────────────
    private JPanel buildOverdueCard() {
        UIComponents.RoundedPanel card = new UIComponents.RoundedPanel(
                UIConstants.CARD_RADIUS, UIConstants.CARD_BG, UIConstants.CARD_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        UIComponents.StatusBadge cnt = new UIComponents.StatusBadge(
                String.valueOf(db.overdueBooks()), Color.WHITE, UIConstants.ACCENT_RED);
        JPanel hdr = UIHelpers.makeCardHeader("Overdue Alerts", cnt);

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(10, 0, 0, 0));

        String[][] rows = {
                {"Reyes, Ana C.",      "Chemistry for Engineers",           "+5 days"},
                {"Torres, Ben M.",     "Physics for Engineers",             "+3 days"},
                {"Villanueva, K.R.",   "Organic Chemistry",                 "+1 day"}
        };
        for (String[] r : rows)
            list.add(overdueRow(r[0], r[1], r[2]));

        card.add(hdr,  BorderLayout.NORTH);
        card.add(list, BorderLayout.CENTER);
        return card;
    }

    private JPanel overdueRow(String student, String book, String days) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new CompoundBorder(
                new MatteBorder(0, 3, 0, 0, UIConstants.ACCENT_RED),
                new EmptyBorder(6, 10, 6, 0)));

        JPanel txt = new JPanel();
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        txt.setOpaque(false);

        JLabel sL = new JLabel(student);
        sL.setFont(UIConstants.F_SUBHEAD);
        sL.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel bL = new JLabel(book);
        bL.setFont(UIConstants.F_TINY);
        bL.setForeground(UIConstants.TEXT_MUTED);
        txt.add(sL);
        txt.add(bL);

        JLabel dL = new JLabel(days);
        dL.setFont(UIConstants.F_SMALL);
        dL.setForeground(UIConstants.ACCENT_RED);

        row.add(txt, BorderLayout.CENTER);
        row.add(dL,  BorderLayout.EAST);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        return row;
    }
}
