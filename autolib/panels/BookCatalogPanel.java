package autolib.panels;

import autolib.*;
import autolib.dialogs.AddBookDialog;
import autolib.models.Book;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Book Catalog panel — searchable table with status badges, add/delete actions.
 */
public class BookCatalogPanel extends JPanel {

    private final LibrarianPortal portal;
    private final DataStore db = DataStore.get();

    private JTextField searchField;
    private JComboBox<String> sectionCombo;
    private JComboBox<String> statusCombo;
    private JTable table;
    private DefaultTableModel model;
    private JLabel countLabel;

    private static final String PLACEHOLDER = "Search by title, author, ISBN, call number...";

    public BookCatalogPanel(LibrarianPortal portal) {
        this.portal = portal;
        setBackground(UIConstants.CONTENT_BG);
        setLayout(new BorderLayout(0, 0));
        build();
    }

    public void refresh() { loadData(); }

    private void build() {
        JPanel wrap = new JPanel(new BorderLayout(0, 12));
        wrap.setBackground(UIConstants.CONTENT_BG);
        wrap.setBorder(new EmptyBorder(20, 20, 20, 20));

        wrap.add(buildToolbar(),   BorderLayout.NORTH);
        wrap.add(buildTableCard(), BorderLayout.CENTER);
        wrap.add(buildPager(),     BorderLayout.SOUTH);

        add(wrap, BorderLayout.CENTER);
        loadData();
    }

    // ── Toolbar: search + filters + add ──────────────────────────────────────
    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new BorderLayout(10, 0));
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(0, 42));

        // Search bar — live filter on every keystroke
        Object[] searchResult = UIHelpers.makeSearchBar(PLACEHOLDER, this::loadData);
        JPanel searchWrap = (JPanel) searchResult[0];
        searchField = (JTextField) searchResult[1];

        // Right: section / status combos + add button
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        List<String> sections = db.getSections();
        String[] secItems = new String[sections.size() + 1];
        secItems[0] = "All Sections";
        for (int i = 0; i < sections.size(); i++) secItems[i + 1] = sections.get(i);
        sectionCombo = UIComponents.styledCombo(secItems);
        sectionCombo.addActionListener(e -> loadData());

        statusCombo = UIComponents.styledCombo(
                new String[]{"All Status", "Available", "Borrowed", "All Out"});
        statusCombo.addActionListener(e -> loadData());

        UIComponents.PrimaryButton addBtn = new UIComponents.PrimaryButton("+ Add Book");
        addBtn.addActionListener(e -> openAddBook());

        right.add(sectionCombo);
        right.add(statusCombo);
        right.add(addBtn);

        bar.add(searchWrap, BorderLayout.CENTER);
        bar.add(right,      BorderLayout.EAST);
        return bar;
    }

    // ── Table card ────────────────────────────────────────────────────────────
    private JPanel buildTableCard() {
        UIComponents.RoundedPanel card = new UIComponents.RoundedPanel(
                UIConstants.CARD_RADIUS, UIConstants.CARD_BG, UIConstants.CARD_BORDER);
        card.setLayout(new BorderLayout());

        String[] cols = {"BARCODE", "TITLE", "AUTHOR", "CALL NO.", "SECTION",
                         "COPIES", "AVAILABLE", "STATUS", "ACTIONS"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
            public Class<?> getColumnClass(int c) {
                return (c == 7 || c == 8) ? JPanel.class : String.class;
            }
        };

        table = new JTable(model);
        UIHelpers.styleTable(table, 38);

        // Column widths
        int[] widths = {80, 200, 130, 70, 110, 55, 70, 90, 80};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Renderers (using shared factories where possible)
        table.getColumnModel().getColumn(0).setCellRenderer(UIHelpers.monoIdRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(UIHelpers.linkRenderer(UIConstants.TABLE_LINK));
        table.getColumnModel().getColumn(2).setCellRenderer(UIHelpers.linkRenderer(UIConstants.ACCENT_BLUE));
        table.getColumnModel().getColumn(4).setCellRenderer(UIHelpers.linkRenderer(UIConstants.ACCENT_BLUE));
        table.getColumnModel().getColumn(7).setCellRenderer(
                UIHelpers.badgeRenderer(UIComponents::bookStatusBadge));
        table.getColumnModel().getColumn(8).setCellRenderer(
                UIHelpers.actionsRenderer("\uD83D\uDC41", "\u2212"));
        table.getColumnModel().getColumn(8).setCellEditor(buildActionsEditor());
        table.setDefaultRenderer(String.class, UIHelpers.alternatingRenderer());

        card.add(UIHelpers.makeTableScroll(table), BorderLayout.CENTER);
        return card;
    }

    // ── Pager bar ─────────────────────────────────────────────────────────────
    private JPanel buildPager() {
        JLabel[] ref = new JLabel[1];
        JPanel bar = UIHelpers.makePager(
                new String[]{"‹ Prev", "1", "2", "3", "…", "209", "Next ›"}, "1", ref);
        countLabel = ref[0];
        return bar;
    }

    // ── Data loading ──────────────────────────────────────────────────────────
    private void loadData() {
        String q   = UIHelpers.getSearchQuery(searchField, PLACEHOLDER);
        String sec = (String) sectionCombo.getSelectedItem();
        String st  = (String) statusCombo.getSelectedItem();
        List<Book> books = db.searchBooks(q, sec, st);

        model.setRowCount(0);
        for (Book b : books) {
            model.addRow(new Object[]{
                    b.getBarcode(), b.getTitle(), b.getAuthor(),
                    b.getCallNumber(), b.getSection(),
                    b.getTotalCopies(), b.getAvailableCopies(),
                    b.getStatusText(), "actions"
            });
        }
        if (countLabel != null)
            countLabel.setText("Showing 1\u2013" + books.size() + " of 1,248 books");
    }

    private void openAddBook() {
        AddBookDialog dlg = new AddBookDialog(SwingUtilities.getWindowAncestor(this));
        dlg.setVisible(true);
        if (dlg.getSavedBook() != null) {
            db.addBook(dlg.getSavedBook());
            loadData();
            JOptionPane.showMessageDialog(this, "Book added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ── Actions cell editor ───────────────────────────────────────────────────
    private TableCellEditor buildActionsEditor() {
        return new DefaultCellEditor(new JCheckBox()) {
            private final JPanel panel;
            private int currentRow;

            {
                panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 3));
                panel.setBackground(UIConstants.TABLE_ROW_EVEN);
                UIComponents.IconButton view = new UIComponents.IconButton("\uD83D\uDC41");
                UIComponents.IconButton del  = new UIComponents.IconButton("\u2212");
                view.addActionListener(e -> {
                    stopCellEditing();
                    JOptionPane.showMessageDialog(BookCatalogPanel.this,
                            "View details for row " + (currentRow + 1),
                            "Book Details", JOptionPane.INFORMATION_MESSAGE);
                });
                del.addActionListener(e -> {
                    stopCellEditing();
                    int ok = JOptionPane.showConfirmDialog(BookCatalogPanel.this,
                            "Remove this book from catalog?", "Confirm Delete",
                            JOptionPane.YES_NO_OPTION);
                    if (ok == JOptionPane.YES_OPTION) {
                        db.removeBook(currentRow);
                        loadData();
                    }
                });
                panel.add(view);
                panel.add(del);
            }

            public Component getTableCellEditorComponent(
                    JTable t, Object v, boolean sel, int row, int col) {
                currentRow = row;
                return panel;
            }

            public Object getCellEditorValue() { return "actions"; }
        };
    }
}
