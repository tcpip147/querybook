package com.tcpip147.querybook.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.tcpip147.querybook.ui.Context;
import org.jetbrains.annotations.NotNull;

public class MoveUpQueryAction extends AnAction {

    private Context ctx;

    public MoveUpQueryAction(Context ctx) {
        super("Move Up", "Move Up", AllIcons.Chooser.Top);
        this.ctx = ctx;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        int n = ctx.getListTable().getSelectedRow();
        if (n > 0) {
            ctx.getQueryEditor().moveUp(n);
        }
    }
}
