package com.tcpip147.querybook.ui.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.AncestorListenerAdapter;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.util.ui.JBUI;
import com.tcpip147.querybook.ui.component.LabeledTextField;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.DocumentEvent;
import java.awt.*;

@Getter
public class EditDialog extends DialogWrapper {

    private LabeledTextField id;
    private LabeledTextField desc;
    private LabeledTextField creator;
    private LabeledTextField createdDate;

    public EditDialog(String title) {
        super(false);
        id = new LabeledTextField("Id: ");
        desc = new LabeledTextField("Description: ");
        creator = new LabeledTextField("Creator: ");
        createdDate = new LabeledTextField("CreatedDate: ");
        setTitle(title);
        init();
        id.addAncestorListener(new AncestorListenerAdapter() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                id.requestFocusText();
            }
        });

        DocumentAdapter adapter = new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                checkValidation();
            }
        };

        id.onChanged(adapter);
        desc.onChanged(adapter);
        creator.onChanged(adapter);
        createdDate.onChanged(adapter);

        checkValidation();
    }

    private void checkValidation() {
        if (!id.getText().isEmpty() && !desc.getText().isEmpty() && !creator.getText().isEmpty() && isValidDateFormat(createdDate.getText())) {
            setOKActionEnabled(true);
        } else {
            setOKActionEnabled(false);
        }
    }

    private boolean isValidDateFormat(String date) {
        return !date.isEmpty() && date.matches("[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBorder(IdeBorderFactory.createEmptyBorder(JBUI.insets(10, 5, 10, 5)));
        panel.setPreferredSize(new Dimension(300, 120));
        panel.add(id);
        panel.add(desc);
        panel.add(creator);
        panel.add(createdDate);
        return panel;
    }

    @NotNull
    @Override
    protected DialogStyle getStyle() {
        return DialogStyle.COMPACT;
    }
}
