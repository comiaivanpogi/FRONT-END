package autolib.components;

import autolib.UIConstants;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Text input component library for AutoLib.
 *
 * Components:
 *  - {@link StyledTextField}   — single-line text input with built-in placeholder
 *  - {@link StyledPasswordField} — password input with show/hide toggle
 *  - {@link SearchField}       — self-contained search bar (icon + field + clear)
 *  - {@link StyledComboBox}    — styled drop-down selector
 *  - {@link StyledSpinner}     — numeric stepper
 *  - {@link FormField}         — label stacked above any input (label + component)
 *
 * Usage:
 * <pre>
 *   TextBoxes.StyledTextField title = new TextBoxes.StyledTextField("e.g. Introduction to Java");
 *   TextBoxes.SearchField     search = new TextBoxes.SearchField("Search books...", this::loadData);
 *   TextBoxes.FormField       form   = new TextBoxes.FormField("Book Title *", title);
 * </pre>
 */
public final class TextBoxes {

    private TextBoxes() {}

    // ─────────────────────────────────────────────────────────────────────────
    // StyledTextField — text field with built-in placeholder rendering
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Single-line text field that paints placeholder text internally when
     * empty and unfocused — no external focus listeners needed.
     *
     * <pre>
     *   TextBoxes.StyledTextField f = new TextBoxes.StyledTextField("e.g. admin");
     *   String value = f.getValue();   // returns "" if placeholder is showing
     * </pre>
     */
    public static class StyledTextField extends JTextField {
        private final String placeholder;

        public StyledTextField(String placeholder) {
            this.placeholder = placeholder;
            setFont(UIConstants.F_BODY);
            setForeground(UIConstants.TEXT_PRIMARY);
            setBackground(UIConstants.INPUT_BG);
            setBorder(new CompoundBorder(
                    new LineBorder(UIConstants.INPUT_BORDER, 1, true),
                    new EmptyBorder(7, 10, 7, 10)));
        }

        /** Returns the typed value, or {@code ""} when only placeholder is shown. */
        public String getValue() {
            return getText().trim();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setFont(getFont());
                g2.setColor(UIConstants.INPUT_PLACEHOLDER);
                Insets ins = getInsets();
                int y = getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 1;
                g2.drawString(placeholder, ins.left + 2, y);
                g2.dispose();
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // StyledPasswordField — password field with show / hide toggle
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Password input field with an inline show/hide toggle button (👁).
     *
     * <pre>
     *   TextBoxes.StyledPasswordField pass = new TextBoxes.StyledPasswordField();
     *   String pw = new String(pass.getPassword());
     * </pre>
     */
    public static class StyledPasswordField extends JPanel {
        private final JPasswordField field;
        private boolean visible = false;

        public StyledPasswordField() {
            setLayout(new BorderLayout());
            setBackground(UIConstants.INPUT_BG);
            setBorder(new CompoundBorder(
                    new LineBorder(UIConstants.INPUT_BORDER, 1, true),
                    new EmptyBorder(0, 10, 0, 4)));

            field = new JPasswordField();
            field.setFont(UIConstants.F_BODY);
            field.setForeground(UIConstants.TEXT_PRIMARY);
            field.setBackground(UIConstants.INPUT_BG);
            field.setBorder(new EmptyBorder(7, 0, 7, 0));
            field.setOpaque(false);

            // Show/hide toggle button
            JButton toggle = new JButton("\uD83D\uDC41");
            toggle.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
            toggle.setForeground(UIConstants.TEXT_MUTED);
            toggle.setBorderPainted(false);
            toggle.setContentAreaFilled(false);
            toggle.setFocusPainted(false);
            toggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            toggle.setToolTipText("Show / hide password");
            toggle.addActionListener(e -> {
                visible = !visible;
                field.setEchoChar(visible ? (char) 0 : '\u2022');
                toggle.setForeground(visible ? UIConstants.ACCENT_RED : UIConstants.TEXT_MUTED);
            });

            add(field,  BorderLayout.CENTER);
            add(toggle, BorderLayout.EAST);
        }

        /** Returns the entered password as a char array. */
        public char[] getPassword() { return field.getPassword(); }

        /** Clears the password field. */
        public void clear() { field.setText(""); }

        /** Pass-through: add action listener triggered on Enter key. */
        public void addActionListener(ActionListener l) { field.addActionListener(l); }

        /** Pass-through: attach a document listener for live validation. */
        public void addDocumentListener(DocumentListener dl) {
            field.getDocument().addDocumentListener(dl);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SearchField — icon + text field + optional clear button, self-contained
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Self-contained search bar: magnifying-glass icon on the left, text field
     * in the centre, and an ✕ clear button on the right that appears when text
     * is present. Fires {@code onChange} on every keystroke.
     *
     * <pre>
     *   TextBoxes.SearchField sf = new TextBoxes.SearchField("Search books...", this::loadData);
     *   String query = sf.getQuery();
     * </pre>
     */
    public static class SearchField extends JPanel {
        private final JTextField field;
        private final String     placeholder;
        private final JButton    clearBtn;

        public SearchField(String placeholder, Runnable onChange) {
            this.placeholder = placeholder;
            setBackground(Color.WHITE);
            setLayout(new BorderLayout());
            setBorder(new CompoundBorder(
                    new LineBorder(UIConstants.INPUT_BORDER, 1, true),
                    new EmptyBorder(0, 10, 0, 6)));

            // Search icon
            JLabel ico = new JLabel("\uD83D\uDD0D ");
            ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
            ico.setForeground(UIConstants.TEXT_MUTED);

            // Text field
            field = new JTextField();
            field.setFont(UIConstants.F_BODY);
            field.setBackground(Color.WHITE);
            field.setBorder(new EmptyBorder(7, 4, 7, 0));
            field.setOpaque(false);
            field.setForeground(UIConstants.INPUT_PLACEHOLDER);
            field.setText(placeholder);

            field.addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) {
                    if (field.getText().equals(placeholder)) {
                        field.setText("");
                        field.setForeground(UIConstants.TEXT_PRIMARY);
                    }
                    updateClearBtn();
                }
                @Override public void focusLost(FocusEvent e) {
                    if (field.getText().isEmpty()) {
                        field.setText(placeholder);
                        field.setForeground(UIConstants.INPUT_PLACEHOLDER);
                    }
                    updateClearBtn();
                }
            });

            // Clear button (✕)
            clearBtn = new JButton("\u2715");
            clearBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            clearBtn.setForeground(UIConstants.TEXT_MUTED);
            clearBtn.setBorderPainted(false);
            clearBtn.setContentAreaFilled(false);
            clearBtn.setFocusPainted(false);
            clearBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            clearBtn.setVisible(false);
            clearBtn.addActionListener(e -> {
                field.setText("");
                field.requestFocus();
                updateClearBtn();
                if (onChange != null) onChange.run();
            });

            // Document listener fires onChange + updates clear button
            if (onChange != null) {
                field.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                    public void insertUpdate (javax.swing.event.DocumentEvent e) { fire(); }
                    public void removeUpdate (javax.swing.event.DocumentEvent e) { fire(); }
                    public void changedUpdate(javax.swing.event.DocumentEvent e) { fire(); }
                    void fire() { updateClearBtn(); onChange.run(); }
                });
            }

            add(ico,      BorderLayout.WEST);
            add(field,    BorderLayout.CENTER);
            add(clearBtn, BorderLayout.EAST);
        }

        private void updateClearBtn() {
            String t = field.getText();
            clearBtn.setVisible(!t.isEmpty() && !t.equals(placeholder));
        }

        /**
         * Returns the actual search query, or {@code ""} when empty / showing
         * placeholder.
         */
        public String getQuery() {
            String t = field.getText();
            return (t.isEmpty() || t.equals(placeholder)) ? "" : t.trim();
        }

        /** Programmatically sets the search text and fires the onChange callback. */
        public void setQuery(String text) { field.setText(text); }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // StyledComboBox — generic drop-down selector
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Styled JComboBox with the standard AutoLib font and border.
     *
     * <pre>
     *   TextBoxes.StyledComboBox combo =
     *       new TextBoxes.StyledComboBox(new String[]{"All Sections", "Engineering", "Humanities"});
     *   String selected = (String) combo.getSelectedItem();
     * </pre>
     */
    public static class StyledComboBox extends JComboBox<String> {
        public StyledComboBox(String[] items) {
            super(items);
            setFont(UIConstants.F_BODY);
            setBackground(UIConstants.INPUT_BG);
            setForeground(UIConstants.TEXT_PRIMARY);
            setBorder(new LineBorder(UIConstants.INPUT_BORDER, 1, true));
        }

        /** Convenience — returns the selected item as a String. */
        public String getSelected() {
            Object sel = getSelectedItem();
            return sel != null ? sel.toString() : "";
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // StyledSpinner — numeric stepper
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Integer spinner with the standard AutoLib styling and configurable range.
     *
     * <pre>
     *   TextBoxes.StyledSpinner copies = new TextBoxes.StyledSpinner(1, 1, 50);
     *   int value = copies.getIntValue();
     * </pre>
     */
    public static class StyledSpinner extends JSpinner {
        public StyledSpinner(int initial, int min, int max) {
            super(new SpinnerNumberModel(initial, min, max, 1));
            setFont(UIConstants.F_BODY);
            setBorder(new LineBorder(UIConstants.INPUT_BORDER, 1, true));
        }

        /** Returns the current integer value of the spinner. */
        public int getIntValue() {
            return (int) getValue();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // FormField — label + any input stacked vertically
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Vertical label-above-input composite. Accepts any {@link JComponent} as
     * the input so it works with StyledTextField, StyledPasswordField,
     * StyledComboBox, StyledSpinner, etc.
     *
     * <pre>
     *   TextBoxes.FormField title = new TextBoxes.FormField(
     *       "Book Title *",
     *       new TextBoxes.StyledTextField("e.g. Introduction to Java"));
     *   panel.add(title);
     * </pre>
     */
    public static class FormField extends JPanel {
        private final JLabel    label;
        private final JComponent input;

        public FormField(String labelText, JComponent input) {
            this.input = input;
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);

            label = new JLabel(labelText);
            label.setFont(UIConstants.F_SUBHEAD);
            label.setForeground(UIConstants.TEXT_PRIMARY);
            label.setAlignmentX(LEFT_ALIGNMENT);
            label.setBorder(new EmptyBorder(0, 0, 5, 0));

            input.setAlignmentX(LEFT_ALIGNMENT);
            input.setMaximumSize(new Dimension(Integer.MAX_VALUE, input.getPreferredSize().height));

            add(label);
            add(input);
        }

        /**
         * Convenience constructor — creates a {@link StyledTextField} automatically.
         *
         * <pre>
         *   TextBoxes.FormField author = new TextBoxes.FormField("Author", "Surname, First Name");
         * </pre>
         */
        public FormField(String labelText, String placeholder) {
            this(labelText, new StyledTextField(placeholder));
        }

        /** Returns the input component so callers can read its value. */
        public JComponent getInput() { return input; }

        /**
         * Shortcut — if the input is a {@link StyledTextField}, returns its
         * trimmed value; otherwise returns {@code ""}.
         */
        public String getValue() {
            if (input instanceof StyledTextField)
                return ((StyledTextField) input).getValue();
            if (input instanceof JTextField)
                return ((JTextField) input).getText().trim();
            return "";
        }

        /** Adds an error hint label below the input (shown in red). */
        public void showError(String message) {
            // Remove old error if present
            clearError();
            JLabel err = new JLabel(message);
            err.setName("__error__");
            err.setFont(UIConstants.F_SMALL);
            err.setForeground(UIConstants.ACCENT_RED);
            err.setAlignmentX(LEFT_ALIGNMENT);
            err.setBorder(new EmptyBorder(3, 0, 0, 0));
            add(err);
            revalidate();
            repaint();
        }

        /** Removes any error hint previously shown. */
        public void clearError() {
            for (Component c : getComponents()) {
                if (c instanceof JLabel && "__error__".equals(c.getName())) {
                    remove(c);
                }
            }
            revalidate();
            repaint();
        }
    }
}
