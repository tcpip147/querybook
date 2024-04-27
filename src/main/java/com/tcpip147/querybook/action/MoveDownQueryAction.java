package com.tcpip147.querybook.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.tcpip147.querybook.ui.Context;
import org.jetbrains.annotations.NotNull;

public class MoveDownQueryAction extends AnAction {

    private Context ctx;

    public MoveDownQueryAction(Context ctx) {
        super("Move Down", "Move Down", AllIcons.Chooser.Bottom);
        this.ctx = ctx;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        int n = ctx.getListTable().getSelectedRow();
        if (n > -1 && n < ctx.getListTable().getRowCount() - 1) {
            ctx.getQueryEditor().moveDown(n);
        }
    }
}
