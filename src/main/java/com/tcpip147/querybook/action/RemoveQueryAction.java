package com.tcpip147.querybook.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.tcpip147.querybook.ui.Context;
import com.tcpip147.querybook.ui.dialog.ConfirmDialog;
import org.jetbrains.annotations.NotNull;

public class RemoveQueryAction extends AnAction {

    private Context ctx;

    public RemoveQueryAction(Context ctx) {
        super("Remove", "Remove", AllIcons.General.Remove);
        this.ctx = ctx;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        int n = ctx.getListTable().getSelectedRow();
        if (n > -1) {
            ConfirmDialog dialog = new ConfirmDialog("Remove", "Do you want to remove a query?");
            if (dialog.showAndGet()) {
                ctx.getQueryEditor().removeQuery(n);
            }
            dialog.disposeIfNeeded();
        }
    }
}
