package com.tcpip147.querybook.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiElement;
import com.tcpip147.querybook.model.Query;
import com.tcpip147.querybook.ui.Context;
import com.tcpip147.querybook.ui.dialog.EditDialog;
import org.jetbrains.annotations.NotNull;

public class EditQueryAction extends AnAction {

    private Context ctx;

    public EditQueryAction(Context ctx) {
        super("Edit", "Edit", AllIcons.Actions.Edit);
        this.ctx = ctx;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        int n = ctx.getListTable().getSelectedRow();
        if (n > -1) {
            Query query = ctx.getQueryEditor().getQuery(n);
            EditDialog dialog = new EditDialog("Edit");
            dialog.getId().setText(query.getXmlQuery().getId());
            dialog.getDesc().setText(query.getXmlQuery().getDescription());
            dialog.getCreator().setText(query.getXmlQuery().getCreator());
            dialog.getCreatedDate().setText(query.getXmlQuery().getCreatedDate());
            if (dialog.showAndGet()) {
                String id = dialog.getId().getText();
                String desc = dialog.getDesc().getText();
                String creator = dialog.getCreator().getText();
                String createdDate = dialog.getCreatedDate().getText();
                ctx.getQueryEditor().editQuery(n, id, desc, creator, createdDate);
            }
            dialog.disposeIfNeeded();
        }
    }
}
