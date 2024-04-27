package com.tcpip147.querybook.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.tcpip147.querybook.ui.Context;
import com.tcpip147.querybook.ui.dialog.EditDialog;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddQueryAction extends AnAction {

    private final Context ctx;

    public AddQueryAction(Context ctx) {
        super("Add (Insert)", "Add", AllIcons.General.Add);
        this.ctx = ctx;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        EditDialog dialog = new EditDialog("Add");
        dialog.getCreator().setText(ctx.getState().getUserName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dialog.getCreatedDate().setText(sdf.format(new Date()));
        if (dialog.showAndGet()) {
            String id = dialog.getId().getText();
            String desc = dialog.getDesc().getText();
            String creator = dialog.getCreator().getText();
            String createdDate = dialog.getCreatedDate().getText();
            ctx.getState().setUserName(creator);
            ctx.getQueryEditor().addQuery(id, desc, creator, createdDate, "");
        }
        dialog.disposeIfNeeded();
    }
}
