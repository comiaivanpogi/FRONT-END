package autolib.panels;

import autolib.*;
import autolib.dialogs.RegisterStudentDialog;
import autolib.models.Student;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Students panel — list with NFC status badges, loans, overdue, register action.
 */
public class StudentsPanel extends JPanel {

    private final LibrarianPortal portal;
    private final DataStore db = DataStore.get();

    private JTextField searchField;
    private JComboBox<String> deptCombo;
    private JTable table;
    private DefaultTableModel model;
    private JLabel countLabel;

    private static final String PLACEHOLDER = "Search student by name, ID, or department...";

    public StudentsPanel(LibrarianPortal portal) {
        this.portal = portal;
        setBackground(UIConstants.CONTENT_BG);
        setLayout(new BorderLayout());
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

    // ── Toolbar ───────────────────────────────────────────────────────────────
    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new BorderLayout(10, 0));
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(0, 42));

        Object[] searchResult = UIHelpers.makeSearchBar(PLACEHOLDER, this::loadData);
        JPanel searchWrap = (JPanel) searchResult[0];
        searchField = (JTextField) searchResult[1];

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        List<String> depts = db.getDepartments();
        String[] deptItems = new String[depts.size() + 1];
        deptItems[0] = "All Departments";
        for (int i = 0; i < depts.size(); i++) deptItems[i + 1] = depts.get(i);
        deptCombo = UIComponents.styledCombo(deptItems);
        deptCombo.addActionListener(e -> loadData());

        UIComponents.PrimaryButton regBtn = new UIComponents.PrimaryButton("+ Register Student");
        regBtn.addActionListener(e -> openRegister());

        right.add(deptCombo);
        right.add(regBtn);

        bar.add(searchWrap, BorderLayout.CENTER);
        bar.add(right,      BorderLayout.EAST);
        return bar;
    }

    // ── Table card ────────────────────────────────────────────────────────────
    private JPanel buildTableCard() {
        UIComponents.RoundedPanel card = new UIComponents.RoundedPanel(
                UIConstants.CARD_RADIUS, UIConstants.CARD_BG, UIConstants.CARD_BORDER);
        card.setLayout(new BorderLayout());

        String[] cols = {"STUDENT ID", "NAME", "DEPARTMENT", "YEAR",
                         "NFC REGISTERED", "ACTIVE LOANS", "OVERDUE", "ACTIONS"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        UIHelpers.styleTable(table, 40);

        int[] widths = {110, 180, 90, 80, 120, 110, 80, 80};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        table.getColumnModel().getColumn(0).setCellRenderer(UIHelpers.monoIdRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(UIHelpers.linkRenderer(UIConstants.TABLE_LINK));
        table.getColumnModel().getColumn(2).setCellRenderer(UIHelpers.linkRenderer(UIConstants.ACCENT_BLUE));
        table.getColumnModel().getColumn(4).setCellRenderer(nfcRenderer());
        table.getColumnModel().getColumn(6).setCellRenderer(overdueRenderer());
        table.getColumnModel().getColumn(7).setCellRenderer(
                UIHelpers.actionsRenderer("\uD83D\uDC41", "\u2212"));
        table.getColumnModel().getColumn(7).setCellEditor(buildActionsEditor());
        table.setDefaultRenderer(Object.class, UIHelpers.alternatingRenderer());

        card.add(UIHelpers.makeTableScroll(table), BorderLayout.CENTER);
        return card;
    }

    // ── Pager ─────────────────────────────────────────────────────────────────
    private JPanel buildPager() {
        JLabel[] ref = new JLabel[1];
        JPanel bar = UIHelpers.makePager(
                new String[]{"‹ Prev", "1", "2", "3", "Next ›"}, "1", ref);
        countLabel = ref[0];
        return bar;
    }

    // ── Data loading ──────────────────────────────────────────────────────────
    private void loadData() {
        String q    = UIHelpers.getSearchQuery(searchField, PLACEHOLDER);
        String dept = (String) deptCombo.getSelectedItem();
        List<Student> students = db.searchStudents(q, dept);
        model.setRowCount(0);
        for (Student s : students) {
            model.addRow(new Object[]{
                    s.getStudentId(), s.getName(), s.getDepartment(), s.getYear(),
                    s.getNfcStatusText(), s.getActiveLoans(), s.getOverdueCount(), "actions"
            });
        }
        if (countLabel != null)
            countLabel.setText("Showing 1\u2013" + students.size() + " of 342 students");
    }

    private void openRegister() {
        RegisterStudentDialog dlg =
                new RegisterStudentDialog(SwingUtilities.getWindowAncestor(this));
        dlg.setVisible(true);
        if (dlg.getSavedStudent() != null) {
            db.addStudent(dlg.getSavedStudent());
            loadData();
            JOptionPane.showMessageDialog(this, "Student registered successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ── Custom renderers (specific to this panel) ─────────────────────────────
    private TableCellRenderer nfcRenderer() {
        return (tbl, val, sel, foc, row, col) -> {
            String v     = String.valueOf(val);
            boolean isYes = v.equals("Yes");
            Color fg = isYes ? UIConstants.ACCENT_GREEN  : UIConstants.ACCENT_AMBER;
            Color bg = isYes ? UIConstants.ACCENT_GREEN_BG : UIConstants.ACCENT_AMBER_BG;
            String text  = isYes ? "\u2713 Yes" : "\u26A0 Pending";
            UIComponents.StatusBadge badge = new UIComponents.StatusBadge(text, fg, bg);
            JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
            wrap.setBackground(row % 2 == 0 ? UIConstants.TABLE_ROW_EVEN : UIConstants.TABLE_ROW_ODD);
            wrap.add(badge);
            return wrap;
        };
    }

    private TableCellRenderer overdueRenderer() {
        return (tbl, val, sel, foc, row, col) -> {
            int v = 0;
            try { v = Integer.parseInt(String.valueOf(val)); } catch (Exception ignored) {}
            JLabel l = new JLabel(String.valueOf(v));
            l.setFont(UIConstants.F_TABLE_CELL);
            l.setForeground(v > 0 ? UIConstants.ACCENT_RED : UIConstants.TEXT_PRIMARY);
            l.setHorizontalAlignment(SwingConstants.CENTER);
            l.setBorder(new EmptyBorder(0, 4, 0, 0));
            l.setBackground(row % 2 == 0 ? UIConstants.TABLE_ROW_EVEN : UIConstants.TABLE_ROW_ODD);
            l.setOpaque(true);
            return l;
        };
    }

    // ── Actions editor ────────────────────────────────────────────────────────
    private TableCellEditor buildActionsEditor() {
        return new DefaultCellEditor(new JCheckBox()) {
            private final JPanel panel;
            private int curRow;
            {
                panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 5));
                panel.setBackground(UIConstants.TABLE_ROW_EVEN);
                UIComponents.IconButton view = new UIComponents.IconButton("\uD83D\uDC41");
                UIComponents.IconButton del  = new UIComponents.IconButton("\u2212");
                view.addActionListener(e -> {
                    stopCellEditing();
                    JOptionPane.showMessageDialog(StudentsPanel.this,
                            "Viewing student details for row " + (curRow + 1),
                            "Student Details", JOptionPane.INFORMATION_MESSAGE);
                });
                del.addActionListener(e -> {
                    stopCellEditing();
                    int ok = JOptionPane.showConfirmDialog(StudentsPanel.this,
                            "Remove this student record?", "Confirm Delete",
                            JOptionPane.YES_NO_OPTION);
                    if (ok == JOptionPane.YES_OPTION) {
                        db.removeStudent(curRow);
                        loadData();
                    }
                });
                panel.add(view);
                panel.add(del);
            }

            public Component getTableCellEditorComponent(
                    JTable t, Object v, boolean sel, int row, int col) {
                curRow = row;
                return panel;
            }

            public Object getCellEditorValue() { return "actions"; }
        };
    }
}
