package autolib;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Librarian login dialog — appears when the Librarian / Admin portal is
 * selected.
 * Default credentials: username = admin | password = admin123
 */
public class LoginDialog extends JDialog {

    private boolean authenticated = false;
    private JTextField userField;
    private JPasswordField passField;
    private JLabel errorLabel;

    public LoginDialog(Frame owner) {
        super(owner, "Librarian Login", true);
        setSize(400, 340);
        setLocationRelativeTo(owner);
        setResizable(false);
        setUndecorated(true);
        setContentPane(buildContent());
    }

    private JPanel buildContent() {
        JPanel root = new UIComponents.RoundedPanel(14, Color.WHITE, UIConstants.CARD_BORDER);
        root.setLayout(new BorderLayout());

        // Header bar (dark) — title + close button + drag support
        JPanel header = UIHelpers.makeDialogHeader("AutoLib  ·  Staff Login", this);
        header.setPreferredSize(new Dimension(400, 72));

        // Body
        JPanel body = new JPanel();
        body.setBackground(Color.WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(24, 28, 24, 28));

        // Username
        JLabel userLbl = new JLabel("Username");
        userLbl.setFont(UIConstants.F_SUBHEAD);
        userLbl.setForeground(UIConstants.TEXT_PRIMARY);
        userLbl.setAlignmentX(LEFT_ALIGNMENT);
        userLbl.setBorder(new EmptyBorder(0, 0, 5, 0));

        userField = UIComponents.styledField("e.g. admin");
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        userField.setAlignmentX(LEFT_ALIGNMENT);

        // Password
        JLabel passLbl = new JLabel("Password");
        passLbl.setFont(UIConstants.F_SUBHEAD);
        passLbl.setForeground(UIConstants.TEXT_PRIMARY);
        passLbl.setAlignmentX(LEFT_ALIGNMENT);
        passLbl.setBorder(new EmptyBorder(14, 0, 5, 0));

        passField = new JPasswordField();
        passField.setFont(UIConstants.F_BODY);
        passField.setForeground(UIConstants.TEXT_PRIMARY);
        passField.setBackground(UIConstants.INPUT_BG);
        passField.setBorder(new CompoundBorder(
                new LineBorder(UIConstants.INPUT_BORDER, 1, true),
                new EmptyBorder(7, 10, 7, 10)));
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        passField.setAlignmentX(LEFT_ALIGNMENT);

        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(UIConstants.F_SMALL);
        errorLabel.setForeground(UIConstants.ACCENT_RED);
        errorLabel.setAlignmentX(LEFT_ALIGNMENT);
        errorLabel.setBorder(new EmptyBorder(8, 0, 0, 0));

        // Login button
        UIComponents.PrimaryButton loginBtn = new UIComponents.PrimaryButton("Sign In →");
        loginBtn.setAlignmentX(LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        loginBtn.setBorder(new EmptyBorder(9, 0, 9, 0));

        loginBtn.addActionListener(e -> doLogin());
        passField.addActionListener(e -> doLogin());

        body.add(userLbl);
        body.add(userField);
        body.add(passLbl);
        body.add(passField);
        body.add(errorLabel);
        body.add(Box.createVerticalStrut(6));
        body.add(loginBtn);

        JLabel hint = new JLabel("Default: admin / admin123");
        hint.setFont(UIConstants.F_TINY);
        hint.setForeground(UIConstants.TEXT_MUTED);
        hint.setAlignmentX(LEFT_ALIGNMENT);
        hint.setBorder(new EmptyBorder(10, 0, 0, 0));
        body.add(hint);

        root.add(header, BorderLayout.NORTH);
        root.add(body,   BorderLayout.CENTER);

        return root;
    }

    private void doLogin() {
        String u = userField.getText().trim();
        String p = new String(passField.getPassword()).trim();
        if (u.equals("admin") && p.equals("admin123")) {
            authenticated = true;
            dispose();
        } else {
            errorLabel.setText("Incorrect username or password.");
            passField.setText("");
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}
