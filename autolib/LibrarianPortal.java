package autolib;

import autolib.panels.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Main librarian portal frame.
 * Contains: dark sidebar, top bar, and swappable content panel.
 */
public class LibrarianPortal extends JFrame {

    // Content panels (lazy-initialized)
    private DashboardPanel dashboardPanel;
    private BookCatalogPanel bookCatalogPanel;
    private StudentsPanel studentsPanel;
    private TransactionsPanel transactionsPanel;
    private ReportsPanel reportsPanel;
    private SettingsPanel settingsPanel;

    private JPanel contentArea;
    private JLabel pageTitleLabel;
    private JLabel dateLabel;
    private Timer clockTimer;

    // Nav button references for highlight toggling
    private NavItem activeItem;

    public LibrarianPortal() {
        setTitle("AutoLib — Librarian Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 660);
        setMinimumSize(new Dimension(900, 580));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMainArea(), BorderLayout.CENTER);
        setContentPane(root);

        clockTimer = new Timer(60_000, e -> refreshDate());
        clockTimer.start();

        // Show dashboard by default
        showPanel("Dashboard");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Sidebar
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sb = new JPanel();
        sb.setBackground(UIConstants.SIDEBAR_BG);
        sb.setPreferredSize(new Dimension(UIConstants.SIDEBAR_WIDTH, 0));
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));

        // Logo
        sb.add(buildSidebarLogo());
        sb.add(Box.createVerticalStrut(8));

        // Main section
        sb.add(sectionLabel("MAIN"));
        activeItem = new NavItem("Dashboard", "\u2302", true);
        NavItem bookItem = new NavItem("Book Catalog", "\u229E", false);
        NavItem stuItem = new NavItem("Students", "\u263A", false);

        sb.add(activeItem);
        sb.add(bookItem);
        sb.add(stuItem);

        // Reports section
        sb.add(sectionLabel("REPORTS"));
        NavItem txItem = new NavItem("Transactions", "\u21C4", false);
        NavItem repItem = new NavItem("Reports", "\u2261", false);
        sb.add(txItem);
        sb.add(repItem);

        // System section
        sb.add(sectionLabel("SYSTEM"));
        NavItem kioskItem = new NavItem("Student Kiosk \u2197", "\u2609", false);
        NavItem settingsItem = new NavItem("Settings", "\u2699", false);
        sb.add(kioskItem);
        sb.add(settingsItem);

        sb.add(Box.createVerticalGlue());
        sb.add(buildSidebarUser());

        // Actions
        ActionListener nav = e -> {
            NavItem src = (NavItem) e.getSource();
            activateNav(src);
            showPanel(src.getPageName());
        };
        for (NavItem item : new NavItem[] { activeItem, bookItem, stuItem, txItem, repItem, kioskItem, settingsItem }) {
            item.addActionListener(nav);
        }

        kioskItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Student Kiosk opens in a separate window (NFC tap required).",
                    "Student Kiosk", JOptionPane.INFORMATION_MESSAGE);
        });

        return sb;
    }

    private JPanel buildSidebarLogo() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 14));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JPanel icon = UIHelpers.makeLogoIcon(32, 8);

        JPanel txt = new JPanel();
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        txt.setOpaque(false);

        JLabel name = new JLabel("AutoLib");
        name.setFont(new Font("Segoe UI", Font.BOLD, 16));
        name.setForeground(Color.WHITE);

        JLabel role = new JLabel("LIBRARIAN PORTAL");
        role.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        role.setForeground(new Color(90, 100, 128));

        txt.add(name);
        txt.add(role);
        p.add(icon);
        p.add(txt);
        return p;
    }

    private JPanel buildSidebarUser() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(14, 18, 30));
        p.setBorder(new EmptyBorder(12, 14, 12, 14));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));

        // Avatar
        JLabel avatar = new JLabel("LR", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIConstants.ACCENT_RED);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        avatar.setForeground(Color.WHITE);
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(32, 32));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(0, 10, 0, 0));

        JLabel nameL = new JLabel("Librarian Reyes");
        nameL.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameL.setForeground(Color.WHITE);

        JLabel roleL = new JLabel("Head Librarian");
        roleL.setFont(UIConstants.F_TINY);
        roleL.setForeground(new Color(100, 112, 140));

        info.add(nameL);
        info.add(roleL);

        // Logout icon
        JButton logout = new JButton("→");
        logout.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        logout.setForeground(new Color(100, 112, 140));
        logout.setBorderPainted(false);
        logout.setContentAreaFilled(false);
        logout.setFocusPainted(false);
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logout.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(this,
                    "Log out of the librarian portal?", "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                clockTimer.stop();
                dispose();
                SwingUtilities.invokeLater(() -> new PortalScreen().setVisible(true));
            }
        });

        p.add(avatar, BorderLayout.WEST);
        p.add(info, BorderLayout.CENTER);
        p.add(logout, BorderLayout.EAST);
        return p;
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 9));
        l.setForeground(UIConstants.SIDEBAR_SECTION);
        l.setBorder(new EmptyBorder(14, 16, 4, 16));
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Main area (top bar + content)
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildMainArea() {
        JPanel main = new JPanel(new BorderLayout(0, 0));
        main.setBackground(UIConstants.CONTENT_BG);
        main.add(buildTopBar(), BorderLayout.NORTH);
        main.add(buildContentArea(), BorderLayout.CENTER);
        return main;
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(UIConstants.TOPBAR_BG);
        bar.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, UIConstants.TOPBAR_BORDER),
                new EmptyBorder(0, 20, 0, 20)));
        bar.setPreferredSize(new Dimension(0, UIConstants.TOPBAR_HEIGHT));

        // Left: back + page title
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);

        JButton backBtn = new JButton("‹ Portal");
        backBtn.setFont(UIConstants.F_SMALL);
        backBtn.setForeground(UIConstants.TEXT_SECONDARY);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(this,
                    "Return to portal selection?", "Back to Portal",
                    JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                clockTimer.stop();
                dispose();
                SwingUtilities.invokeLater(() -> new PortalScreen().setVisible(true));
            }
        });

        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setForeground(UIConstants.TOPBAR_BORDER);
        sep.setPreferredSize(new Dimension(1, 22));

        pageTitleLabel = new JLabel("Dashboard");
        pageTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pageTitleLabel.setForeground(UIConstants.TEXT_PRIMARY);

        dateLabel = new JLabel();
        dateLabel.setFont(UIConstants.F_SMALL);
        dateLabel.setForeground(UIConstants.TEXT_MUTED);
        dateLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
        refreshDate();

        left.add(backBtn);
        left.add(Box.createHorizontalStrut(10));
        left.add(sep);
        left.add(Box.createHorizontalStrut(14));
        left.add(pageTitleLabel);
        left.add(dateLabel);

        // Right: status + bell
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        addStatusDot(right, "NFC Reader", new Color(34, 197, 94));
        addStatusDot(right, "Barcode Scanner", new Color(34, 197, 94));

        JLabel bell = new JLabel("\uD83D\uDD14");
        bell.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        bell.setForeground(UIConstants.TEXT_SECONDARY);
        bell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Notification badge
        JPanel bellWrap = new JPanel(null);
        bellWrap.setOpaque(false);
        bellWrap.setPreferredSize(new Dimension(36, 36));
        bell.setBounds(4, 6, 26, 24);
        JLabel badge = new JLabel("3");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 9));
        badge.setForeground(Color.WHITE);
        badge.setBackground(UIConstants.ACCENT_RED);
        badge.setOpaque(true);
        badge.setHorizontalAlignment(SwingConstants.CENTER);
        badge.setBounds(18, 2, 16, 14);
        badge.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        bellWrap.add(bell);
        bellWrap.add(badge);
        right.add(bellWrap);

        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private void addStatusDot(JPanel p, String text, Color color) {
        p.add(UIHelpers.makeStatusDot(text, color));
    }

    private void refreshDate() {
        if (dateLabel != null)
            dateLabel.setText(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a")));
    }

    private JPanel buildContentArea() {
        contentArea = new JPanel(new CardLayout());
        contentArea.setBackground(UIConstants.CONTENT_BG);

        dashboardPanel = new DashboardPanel(this);
        bookCatalogPanel = new BookCatalogPanel(this);
        studentsPanel = new StudentsPanel(this);
        transactionsPanel = new TransactionsPanel(this);
        reportsPanel = new ReportsPanel(this);
        settingsPanel = new SettingsPanel(this);

        contentArea.add(dashboardPanel, "Dashboard");
        contentArea.add(bookCatalogPanel, "Book Catalog");
        contentArea.add(studentsPanel, "Students");
        contentArea.add(transactionsPanel, "Transactions");
        contentArea.add(reportsPanel, "Reports");
        contentArea.add(settingsPanel, "Settings");

        return contentArea;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Navigation helpers
    // ─────────────────────────────────────────────────────────────────────────
    public void showPanel(String name) {
        String cardName = name.replace(" \u2197", ""); // strip kiosk arrow
        pageTitleLabel.setText(cardName);
        CardLayout cl = (CardLayout) contentArea.getLayout();
        cl.show(contentArea, cardName);

        if (cardName.equals("Book Catalog"))
            bookCatalogPanel.refresh();
        if (cardName.equals("Students"))
            studentsPanel.refresh();
        if (cardName.equals("Transactions"))
            transactionsPanel.refresh();
        if (cardName.equals("Dashboard"))
            dashboardPanel.refresh();
    }

    private void activateNav(NavItem item) {
        if (activeItem != null)
            activeItem.setActive(false);
        item.setActive(true);
        activeItem = item;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Inner: Nav item button
    // ─────────────────────────────────────────────────────────────────────────
    public static class NavItem extends JButton {
        private boolean active;
        private String pageName;
        private boolean hover = false;

        public NavItem(String label, String icon, boolean active) {
            this.pageName = label;
            this.active = active;

            setText(icon + "   " + label);
            setFont(active ? UIConstants.F_NAV_ACTIVE : UIConstants.F_NAV);
            setForeground(active ? UIConstants.SIDEBAR_ACTIVE_TXT : UIConstants.SIDEBAR_TEXT);
            setHorizontalAlignment(LEFT);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(0, 16, 0, 16));
            setPreferredSize(new Dimension(UIConstants.SIDEBAR_WIDTH, 38));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        public void setActive(boolean a) {
            active = a;
            setFont(a ? UIConstants.F_NAV_ACTIVE : UIConstants.F_NAV);
            setForeground(a ? UIConstants.SIDEBAR_ACTIVE_TXT : UIConstants.SIDEBAR_TEXT);
            repaint();
        }

        public String getPageName() {
            return pageName;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (active) {
                g2.setColor(UIConstants.SIDEBAR_ACTIVE_BG);
                g2.fillRoundRect(6, 2, getWidth() - 12, getHeight() - 4, 8, 8);
            } else if (hover) {
                g2.setColor(UIConstants.SIDEBAR_HOVER_BG);
                g2.fillRoundRect(6, 2, getWidth() - 12, getHeight() - 4, 8, 8);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
