package com.tcpip147.querybook.ui.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class ConfirmDialog extends DialogWrapper {

    private String message;

    public ConfirmDialog(String title, String message) {
        super(false);
        setTitle(title);
        this.message = message;
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBorder(IdeBorderFactory.createEmptyBorder(JBUI.insets(10, 5, 10, 5)));
        panel.setPreferredSize(new Dimension(300, 0));
        panel.add(new JBLabel(message));
        return panel;
    }

    @NotNull
    @Override
    protected DialogStyle getStyle() {
        return DialogStyle.NO_STYLE;
    }
}
