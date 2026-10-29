package autolib.dialogs;

import autolib.*;
import autolib.models.Book;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * "Add New Book" modal dialog.
 * Matches reference image 4: white card with form fields + auto-generated barcode chips.
 */
public class AddBookDialog extends JDialog {

    private Book savedBook = null;
    private final DataStore db = DataStore.get();

    private JTextField titleField;
    private JTextField authorField;
    private JTextField isbnField;
    private JTextField callNumField;
    private JTextField deweyField;
    private JComboBox<String> sectionCombo;
    private JSpinner   copiesSpinner;
    private JPanel     barcodesPanel;
    private List<String> generatedBarcodes = new ArrayList<>();

    public AddBookDialog(Window owner) {
        super(owner, "Add New Book", ModalityType.APPLICATION_MODAL);
        setSize(520, 560);
        setLocationRelativeTo(owner);
        setResizable(false);
        setUndecorated(true);
        setContentPane(buildContent());
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dimmed overlay suggestion via background
            }
        };
        root.setBackground(new Color(0,0,0,100));

        UIComponents.RoundedPanel card = new UIComponents.RoundedPanel(14, Color.WHITE, UIConstants.CARD_BORDER);
        card.setLayout(new BorderLayout());

        // ── Header ──────────────────────────────────────────────────────────
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);
        hdr.setBorder(new EmptyBorder(18, 22, 14, 18));

        JLabel title = new JLabel("Add New Book");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(UIConstants.TEXT_PRIMARY);

        JButton closeBtn = new JButton("✕");
        closeBtn.setFont(UIConstants.F_BODY);
        closeBtn.setForeground(UIConstants.TEXT_MUTED);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());

        hdr.add(title,    BorderLayout.WEST);
        hdr.add(closeBtn, BorderLayout.EAST);

        JSeparator sep = new JSeparator();
        sep.setForeground(UIConstants.CARD_BORDER);

        // ── Form body ────────────────────────────────────────────────────────
        JPanel body = new JPanel();
        body.setBackground(Color.WHITE);
        body.setLayout(new GridBagLayout());
        body.setBorder(new EmptyBorder(14, 22, 14, 22));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 4, 0);

        // Book Title (full width)
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2; gbc.weightx=1.0;
        body.add(fieldLabel("Book Title *"), gbc);

        gbc.gridy=1;
        titleField = UIComponents.styledField("e.g. Introduction to Programming");
        body.add(titleField, gbc);

        // Authors | ISBN (two columns)
        gbc.gridy=2; gbc.gridwidth=1; gbc.weightx=0.55;
        body.add(fieldLabel("Author(s) *"), gbc);
        gbc.gridx=1; gbc.weightx=0.45; gbc.insets=new Insets(4,8,4,0);
        body.add(fieldLabel("ISBN"), gbc);

        gbc.gridy=3; gbc.gridx=0; gbc.weightx=0.55; gbc.insets=new Insets(4,0,4,0);
        authorField = UIComponents.styledField("Surname, First Name");
        body.add(authorField, gbc);
        gbc.gridx=1; gbc.weightx=0.45; gbc.insets=new Insets(4,8,4,0);
        isbnField = UIComponents.styledField("978-x-xxx-xxxxx-x");
        body.add(isbnField, gbc);

        // Call Number | Dewey
        gbc.gridy=4; gbc.gridx=0; gbc.weightx=0.55; gbc.insets=new Insets(4,0,4,0);
        body.add(fieldLabel("Call Number"), gbc);
        gbc.gridx=1; gbc.weightx=0.45; gbc.insets=new Insets(4,8,4,0);
        body.add(fieldLabel("Dewey Decimal"), gbc);

        gbc.gridy=5; gbc.gridx=0; gbc.insets=new Insets(4,0,4,0);
        callNumField = UIComponents.styledField("e.g. QA76.9");
        body.add(callNumField, gbc);
        gbc.gridx=1; gbc.insets=new Insets(4,8,4,0);
        deweyField = UIComponents.styledField("e.g. 005.73");
        body.add(deweyField, gbc);

        // Section | Copies
        gbc.gridy=6; gbc.gridx=0; gbc.insets=new Insets(4,0,4,0);
        body.add(fieldLabel("Section / Category"), gbc);
        gbc.gridx=1; gbc.insets=new Insets(4,8,4,0);
        body.add(fieldLabel("Number of Copies"), gbc);

        gbc.gridy=7; gbc.gridx=0; gbc.insets=new Insets(4,0,4,0);
        sectionCombo = UIComponents.styledCombo(new String[]{
            "Science & Technology","Engineering","Humanities","Business & Econ","Health Sciences"
        });
        body.add(sectionCombo, gbc);

        gbc.gridx=1; gbc.insets=new Insets(4,8,4,0);
        copiesSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        copiesSpinner.setFont(UIConstants.F_BODY);
        copiesSpinner.setBorder(new LineBorder(UIConstants.INPUT_BORDER, 1, true));
        copiesSpinner.addChangeListener(e -> refreshBarcodes());
        body.add(copiesSpinner, gbc);

        // Barcodes area (full width)
        gbc.gridy=8; gbc.gridx=0; gbc.gridwidth=2; gbc.insets=new Insets(10,0,4,0);
        body.add(fieldLabel("Auto-generate Barcodes for Copies"), gbc);

        barcodesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        barcodesPanel.setBackground(Color.WHITE);
        gbc.gridy=9; gbc.insets=new Insets(0,0,4,0);
        body.add(barcodesPanel, gbc);

        refreshBarcodes();

        // ── Footer ───────────────────────────────────────────────────────────
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setBackground(new Color(248,249,252));
        footer.setBorder(new MatteBorder(1,0,0,0, UIConstants.CARD_BORDER));

        UIComponents.GhostButton cancelBtn = new UIComponents.GhostButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        UIComponents.PrimaryButton saveBtn = new UIComponents.PrimaryButton("\u2713 Save Book");
        saveBtn.addActionListener(e -> doSave());

        footer.add(cancelBtn);
        footer.add(saveBtn);

        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(hdr, BorderLayout.CENTER);
        north.add(sep, BorderLayout.SOUTH);

        card.add(north,  BorderLayout.NORTH);
        card.add(body,   BorderLayout.CENTER);
        card.add(footer, BorderLayout.SOUTH);

        root.add(card, BorderLayout.CENTER);
        return root;
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.F_SUBHEAD);
        l.setForeground(UIConstants.TEXT_PRIMARY);
        return l;
    }

    private void refreshBarcodes() {
        barcodesPanel.removeAll();
        generatedBarcodes.clear();
        int copies = (int) copiesSpinner.getValue();
        // Re-peek at next barcode index (non-destructive preview)
        int base = DataStore.get().totalBooks(); // rough offset
        for (int i = 0; i < Math.min(copies, 6); i++) {
            String bc = db.nextBarcode();
            generatedBarcodes.add(bc);
            JLabel chip = new JLabel(bc);
            chip.setFont(new Font("Consolas", Font.PLAIN, 11));
            chip.setForeground(UIConstants.TEXT_SECONDARY);
            chip.setBackground(new Color(242,244,248));
            chip.setOpaque(true);
            chip.setBorder(new CompoundBorder(
                new LineBorder(UIConstants.INPUT_BORDER, 1, true),
                new EmptyBorder(3, 8, 3, 8)
            ));
            barcodesPanel.add(chip);
        }
        if (copies > 6) {
            JLabel more = new JLabel("+" + (copies - 6) + " more");
            more.setFont(UIConstants.F_SMALL);
            more.setForeground(UIConstants.TEXT_MUTED);
            barcodesPanel.add(more);
        }
        barcodesPanel.revalidate();
        barcodesPanel.repaint();
    }

    private void doSave() {
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Book title is required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (authorField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Author(s) is required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String barcode = generatedBarcodes.isEmpty() ? db.nextBarcode() : generatedBarcodes.get(0);
        int copies = (int) copiesSpinner.getValue();
        Book book = new Book(
            barcode,
            titleField.getText().trim(),
            authorField.getText().trim(),
            callNumField.getText().trim(),
            (String) sectionCombo.getSelectedItem(),
            copies, copies
        );
        book.setIsbn(isbnField.getText().trim());
        book.setDeweyDecimal(deweyField.getText().trim());
        savedBook = book;
        dispose();
    }

    public Book getSavedBook() { return savedBook; }
}
