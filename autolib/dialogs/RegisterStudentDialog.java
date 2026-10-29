package autolib.dialogs;

import autolib.*;
import autolib.models.Student;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Register new student dialog.
 */
public class RegisterStudentDialog extends JDialog {

    private Student savedStudent = null;

    private JTextField nameField;
    private JTextField idField;
    private JComboBox<String> deptCombo;
    private JComboBox<String> yearCombo;
    private JCheckBox nfcCheck;

    public RegisterStudentDialog(Window owner) {
        super(owner, "Register Student", ModalityType.APPLICATION_MODAL);
        setSize(460, 440);
        setLocationRelativeTo(owner);
        setResizable(false);
        setUndecorated(true);
        setContentPane(buildContent());
    }

    private JPanel buildContent() {
        UIComponents.RoundedPanel card = new UIComponents.RoundedPanel(14, Color.WHITE, UIConstants.CARD_BORDER);
        card.setLayout(new BorderLayout());

        // Header
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);
        hdr.setBorder(new EmptyBorder(18, 22, 14, 18));

        JLabel title = new JLabel("Register New Student");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(UIConstants.TEXT_PRIMARY);

        JButton close = new JButton("✕");
        close.setFont(UIConstants.F_BODY);
        close.setForeground(UIConstants.TEXT_MUTED);
        close.setBorderPainted(false);
        close.setContentAreaFilled(false);
        close.setFocusPainted(false);
        close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        close.addActionListener(e -> dispose());
        hdr.add(title, BorderLayout.WEST);
        hdr.add(close, BorderLayout.EAST);

        JSeparator sep = new JSeparator();
        sep.setForeground(UIConstants.CARD_BORDER);

        // Body
        JPanel body = new JPanel();
        body.setBackground(Color.WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(16, 22, 10, 22));

        nameField = UIComponents.styledField("Surname, First Name Middle");
        idField = UIComponents.styledField("e.g. 2024-10043");
        deptCombo = UIComponents.styledCombo(new String[] { "CICS", "COE", "CAS", "COEd", "CBA", "COED" });
        yearCombo = UIComponents.styledCombo(new String[] { "1st Year", "2nd Year", "3rd Year", "4th Year" });
        nfcCheck = new JCheckBox("Register NFC ID card now");
        nfcCheck.setFont(UIConstants.F_BODY);
        nfcCheck.setForeground(UIConstants.TEXT_PRIMARY);
        nfcCheck.setOpaque(false);

        body.add(formRow("Full Name *", nameField));
        body.add(Box.createVerticalStrut(10));
        body.add(formRow("Student ID *", idField));
        body.add(Box.createVerticalStrut(10));

        JPanel twoCol = new JPanel(new GridLayout(1, 2, 10, 0));
        twoCol.setOpaque(false);
        twoCol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        twoCol.add(labeledWidget("Department", deptCombo));
        twoCol.add(labeledWidget("Year Level", yearCombo));
        body.add(twoCol);

        body.add(Box.createVerticalStrut(14));
        body.add(nfcCheck);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setBackground(new Color(248, 249, 252));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, UIConstants.CARD_BORDER));

        UIComponents.GhostButton cancelBtn = new UIComponents.GhostButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        UIComponents.PrimaryButton saveBtn = new UIComponents.PrimaryButton("\u2713 Register");
        saveBtn.addActionListener(e -> doSave());

        footer.add(cancelBtn);
        footer.add(saveBtn);

        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(hdr, BorderLayout.CENTER);
        north.add(sep, BorderLayout.SOUTH);

        card.add(north, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        card.add(footer, BorderLayout.SOUTH);

        // Draggable
        MouseAdapter drag = new MouseAdapter() {
            Point start;

            public void mousePressed(MouseEvent e) {
                start = e.getPoint();
            }

            public void mouseDragged(MouseEvent e) {
                Point l = getLocation();
                setLocation(l.x + e.getX() - start.x, l.y + e.getY() - start.y);
            }
        };
        hdr.addMouseListener(drag);
        hdr.addMouseMotionListener(drag);

        return card;
    }

    private JPanel formRow(String label, JComponent field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        JLabel l = new JLabel(label);
        l.setFont(UIConstants.F_SUBHEAD);
        l.setForeground(UIConstants.TEXT_PRIMARY);
        l.setBorder(new EmptyBorder(0, 0, 4, 0));

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        p.add(l);
        p.add(field);
        return p;
    }

    private JPanel labeledWidget(String label, JComponent widget) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel l = new JLabel(label);
        l.setFont(UIConstants.F_SUBHEAD);
        l.setForeground(UIConstants.TEXT_PRIMARY);
        l.setBorder(new EmptyBorder(0, 0, 4, 0));
        widget.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        p.add(l);
        p.add(widget);
        return p;
    }

    private void doSave() {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student name is required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student ID is required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        savedStudent = new Student(
                id, name,
                (String) deptCombo.getSelectedItem(),
                (String) yearCombo.getSelectedItem(),
                nfcCheck.isSelected(), 0, 0);
        dispose();
    }

    public Student getSavedStudent() {
        return savedStudent;
    }
}
