package com.tcpip147.querybook.ui.component;

import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LabeledTextField extends JPanel {

    private JBTextField textField;

    public LabeledTextField(String name) {
        setLayout(new GridBagLayout());
        JBLabel label = new JBLabel(name);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setPreferredSize(new Dimension(80, 30));
        GridBagConstraints c = new GridBagConstraints();
        add(label, c);
        textField = new JBTextField();
        c = new GridBagConstraints();
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
    }

    public void requestFocusText() {
        textField.requestFocusInWindow();
    }

    public void onChanged(DocumentAdapter adapter) {
        textField.getDocument().addDocumentListener(adapter);
    }
}
