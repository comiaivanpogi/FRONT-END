package autolib.panels;

import autolib.*;
import autolib.models.Transaction;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Transactions panel — full list of borrow/return/overdue records.
 */
public class TransactionsPanel extends JPanel {

    private final DataStore db = DataStore.get();
    private DefaultTableModel model;
    private JLabel countLabel;

    private static final String PLACEHOLDER = "Search transactions...";

    public TransactionsPanel(LibrarianPortal portal) {
        setBackground(UIConstants.CONTENT_BG);
        setLayout(new BorderLayout());
        build();
    }

    public void refresh() { loadData(); }

    private void build() {
        JPanel wrap = new JPanel(new BorderLayout(0, 12));
        wrap.setBackground(UIConstants.CONTENT_BG);
        wrap.setBorder(new EmptyBorder(20, 20, 20, 20));

        // ── Toolbar ──────────────────────────────────────────────────────────
        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setOpaque(false);
        toolbar.setPreferredSize(new Dimension(0, 42));

        // Search bar via UIHelpers
        Object[] searchResult = UIHelpers.makeSearchBar(PLACEHOLDER, null);
        JPanel searchWrap = (JPanel) searchResult[0];
        // (no live-filter needed for transactions — table shows all)

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        JComboBox<String> typeFilter = UIComponents.styledCombo(
                new String[]{"All Types", "Borrowed", "Returned", "Overdue"});
        UIComponents.PrimaryButton exportBtn = new UIComponents.PrimaryButton("\u2193 Export");
        exportBtn.addActionListener(e -> JOptionPane.showMessageDialog(
                this, "Export feature coming soon.", "Export", JOptionPane.INFORMATION_MESSAGE));
        right.add(typeFilter);
        right.add(exportBtn);

        toolbar.add(searchWrap, BorderLayout.CENTER);
        toolbar.add(right, BorderLayout.EAST);

        // ── Table card ────────────────────────────────────────────────────────
        UIComponents.RoundedPanel card = new UIComponents.RoundedPanel(
                UIConstants.CARD_RADIUS, UIConstants.CARD_BG, UIConstants.CARD_BORDER);
        card.setLayout(new BorderLayout());

        String[] cols = {"DATE/TIME", "STUDENT", "STUDENT ID", "BOOK TITLE", "BARCODE", "TYPE"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        UIHelpers.styleTable(table, 38);

        // Column widths
        int[] widths = {130, 160, 100, 200, 90, 90};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Type column — badge renderer
        table.getColumnModel().getColumn(5).setCellRenderer(
                UIHelpers.badgeRenderer(UIComponents::txBadge));

        // Default text renderer — alternating rows
        table.setDefaultRenderer(Object.class, UIHelpers.alternatingRenderer());

        card.add(UIHelpers.makeTableScroll(table), BorderLayout.CENTER);

        // ── Count bar ─────────────────────────────────────────────────────────
        countLabel = new JLabel();
        countLabel.setFont(UIConstants.F_SMALL);
        countLabel.setForeground(UIConstants.TEXT_MUTED);

        wrap.add(toolbar,     BorderLayout.NORTH);
        wrap.add(card,        BorderLayout.CENTER);
        wrap.add(countLabel,  BorderLayout.SOUTH);
        add(wrap, BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");
        List<Transaction> txs = db.getTransactions();
        model.setRowCount(0);
        for (Transaction t : txs) {
            model.addRow(new Object[]{
                    t.getTimestamp().format(fmt),
                    t.getStudentName(),
                    t.getStudentId(),
                    t.getBookTitle(),
                    t.getBookBarcode(),
                    t.getTypeText()
            });
        }
        countLabel.setText("Showing " + txs.size() + " transactions");
    }
}
