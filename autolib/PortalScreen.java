package autolib;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * First screen shown when the application starts.
 * Matches reference image 1: dark portal selection card.
 */
public class PortalScreen extends JFrame {

    private JLabel clockLabel;
    private Timer clockTimer;

    public PortalScreen() {
        setTitle("AutoLib — Portal Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 600);
        setMinimumSize(new Dimension(800, 520));
        setLocationRelativeTo(null);

        JPanel root = buildRoot();
        setContentPane(root);

        clockTimer = new Timer(1000, e -> refreshClock());
        clockTimer.start();
    }

    // ── Root panel ────────────────────────────────────────────────────────────
    private JPanel buildRoot() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                // Subtle radial-like gradient
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(18, 14, 26),
                        getWidth(), getHeight(), new Color(13, 17, 23));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setOpaque(true);

        root.add(buildTopBar(), BorderLayout.NORTH);
        root.add(buildCenter(), BorderLayout.CENTER);
        root.add(buildStatusBar(), BorderLayout.SOUTH);
        return root;
    }

    // ── Top bar: clock ────────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 14));
        p.setOpaque(false);

        clockLabel = new JLabel();
        clockLabel.setFont(UIConstants.F_SMALL);
        clockLabel.setForeground(new Color(170, 180, 200));
        clockLabel.setOpaque(true);
        clockLabel.setBackground(new Color(24, 30, 42));
        clockLabel.setBorder(new CompoundBorder(
                new LineBorder(new Color(44, 54, 70), 1, true),
                new EmptyBorder(5, 14, 5, 14)));
        refreshClock();
        p.add(clockLabel);
        return p;
    }

    private void refreshClock() {
        clockLabel.setText(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("EEE, MMM d, h:mm a")));
    }

    // ── Center: card ──────────────────────────────────────────────────────────
    private JPanel buildCenter() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);

        UIComponents.RoundedPanel card = new UIComponents.RoundedPanel(18,
                new Color(22, 27, 34), new Color(44, 54, 70));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(36, 36, 28, 36));
        card.setPreferredSize(new Dimension(360, 348));

        // Logo row
        JPanel logoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoRow.setOpaque(false);
        logoRow.add(buildLogoIcon());
        logoRow.add(Box.createHorizontalStrut(12));
        logoRow.add(buildLogoText());

        // Sub-heading
        JLabel sub = new JLabel("<html>NFC &amp; Barcode-Integrated Library Inventory System<br>"
                + "&#8212; Select your portal to continue.</html>");
        sub.setFont(UIConstants.F_SMALL);
        sub.setForeground(new Color(150, 160, 182));
        sub.setBorder(new EmptyBorder(16, 0, 18, 0));
        sub.setAlignmentX(LEFT_ALIGNMENT);

        // Portal buttons
        PortalButton libBtn = new PortalButton(
                "Librarian / Admin",
                "Manage catalog, transactions, reports & settings",
                "\u2666", true);
        PortalButton stuBtn = new PortalButton(
                "Student / Borrower",
                "Tap your ID to borrow or return a book",
                "\u25CF", false);

        // Footer hint
        JLabel foot = new JLabel(
                "<html><center>Librarian portal requires "
                        + "<font color='#CC2936'>staff credentials</font> to access.</center></html>");
        foot.setFont(UIConstants.F_TINY);
        foot.setForeground(new Color(90, 100, 125));
        foot.setBorder(new EmptyBorder(16, 0, 4, 0));
        foot.setAlignmentX(CENTER_ALIGNMENT);

        card.add(logoRow);
        card.add(sub);
        card.add(libBtn);
        card.add(Box.createVerticalStrut(8));
        card.add(stuBtn);
        card.add(foot);

        wrap.add(card);

        // Actions
        libBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openLibrarianLogin();
            }
        });
        stuBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(PortalScreen.this,
                        "Student Kiosk mode activated.\nPlease tap your NFC ID card to proceed.",
                        "Student / Borrower Portal", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return wrap;
    }

    private JPanel buildLogoIcon() {
        return UIHelpers.makeLogoIcon(42, 10);
    }

    private JPanel buildLogoText() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel name = new JLabel("AutoLib");
        name.setFont(new Font("Segoe UI", Font.BOLD, 21));
        name.setForeground(Color.WHITE);

        JLabel univ = new JLabel("BATANGAS STATE UNIVERSITY");
        univ.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        univ.setForeground(new Color(100, 112, 138));

        p.add(name);
        p.add(univ);
        return p;
    }

    // ── Status bar ────────────────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 10));
        p.setOpaque(false);
        p.add(UIHelpers.makeStatusDot("NFC Reader Online",    new Color(34, 197, 94)));
        p.add(UIHelpers.makeStatusDot("Barcode Scanner Online", new Color(34, 197, 94)));
        p.add(UIHelpers.makeStatusDot("Printer Offline",      new Color(200, 50, 50)));
        return p;
    }

    // ── Login transition ──────────────────────────────────────────────────────
    private void openLibrarianLogin() {
        LoginDialog dlg = new LoginDialog(this);
        dlg.setVisible(true);
        if (dlg.isAuthenticated()) {
            clockTimer.stop();
            dispose();
            SwingUtilities.invokeLater(() -> new LibrarianPortal().setVisible(true));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Inner: Portal selection button
    // ─────────────────────────────────────────────────────────────────────────
    private static class PortalButton extends JPanel {
        private boolean hover = false;

        public PortalButton(String title, String sub, String icon, boolean librarian) {
            setOpaque(false);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(14, 14, 14, 14));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
            setPreferredSize(new Dimension(288, 70));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Left: icon circle
            JLabel iconLbl = new JLabel(icon, SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(40, 50, 66));
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            iconLbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            iconLbl.setForeground(UIConstants.ACCENT_RED);
            iconLbl.setPreferredSize(new Dimension(38, 38));
            iconLbl.setMinimumSize(new Dimension(38, 38));
            iconLbl.setMaximumSize(new Dimension(38, 38));
            iconLbl.setOpaque(false);
            JPanel iconWrap = new JPanel(new GridBagLayout());
            iconWrap.setOpaque(false);
            iconWrap.setPreferredSize(new Dimension(46, 46));
            iconWrap.add(iconLbl);

            // Center: text
            JPanel txt = new JPanel();
            txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
            txt.setOpaque(false);
            txt.setBorder(new EmptyBorder(0, 10, 0, 0));

            JLabel t = new JLabel(title);
            t.setFont(new Font("Segoe UI", Font.BOLD, 13));
            t.setForeground(Color.WHITE);

            JLabel s = new JLabel("<html>" + sub + "</html>");
            s.setFont(UIConstants.F_TINY);
            s.setForeground(new Color(115, 128, 155));

            txt.add(t);
            txt.add(s);

            // Right: chevron
            JLabel arrow = new JLabel("›");
            arrow.setFont(new Font("Segoe UI", Font.PLAIN, 22));
            arrow.setForeground(new Color(90, 102, 128));

            add(iconWrap, BorderLayout.WEST);
            add(txt, BorderLayout.CENTER);
            add(arrow, BorderLayout.EAST);

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

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hover ? new Color(40, 50, 64) : new Color(30, 38, 50));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.setColor(new Color(50, 62, 80));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
