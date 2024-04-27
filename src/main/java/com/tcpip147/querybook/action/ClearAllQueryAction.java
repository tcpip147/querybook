package com.tcpip147.querybook.action;

import com.intellij.icons.ExpUiIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.tcpip147.querybook.ui.Context;
import com.tcpip147.querybook.ui.dialog.ConfirmDialog;
import org.jetbrains.annotations.NotNull;

public class ClearAllQueryAction extends AnAction {

    private Context ctx;

    public ClearAllQueryAction(Context ctx) {
        super("Clear All", "Clear All", ExpUiIcons.Actions.ClearCash);
        this.ctx = ctx;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        ConfirmDialog dialog = new ConfirmDialog("Clear All", "Do you want to clear all query in this file?");
        if (dialog.showAndGet()) {
            ctx.getQueryEditor().clearAllQuery();
        }
        dialog.disposeIfNeeded();
    }
}
