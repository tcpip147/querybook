package com.tcpip147.querybook.ui.component;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.SideBorder;
import com.intellij.util.ui.JBUI;
import com.tcpip147.querybook.action.*;
import com.tcpip147.querybook.ui.Context;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

public class QueryBookActionMenu extends JPanel {

    private final Context ctx;

    public QueryBookActionMenu(Context ctx) {
        super(new BorderLayout());
        this.ctx = ctx;
        setBorder(new CompoundBorder(IdeBorderFactory.createBorder(SideBorder.BOTTOM), IdeBorderFactory.createEmptyBorder(JBUI.insets(1))));
        createLeftGroup();
        createRightGroup();
    }

    private void createLeftGroup() {
        ActionManager actionManager = ActionManager.getInstance();
        DefaultActionGroup group = new DefaultActionGroup();
        AddQueryAction addQueryAction = new AddQueryAction(ctx);
        ctx.setAddQueryAction(addQueryAction);
        group.add(addQueryAction);
        RemoveQueryAction removeQueryAction = new RemoveQueryAction(ctx);
        ctx.setRemoveQueryAction(removeQueryAction);
        group.add(removeQueryAction);
        group.add(new DuplicateQueryAction(ctx));
        EditQueryAction editQueryAction = new EditQueryAction(ctx);
        ctx.setEditQueryAction(editQueryAction);
        group.add(editQueryAction);
        group.add(new MoveUpQueryAction(ctx));
        group.add(new MoveDownQueryAction(ctx));

        ActionToolbar toolbar = actionManager.createActionToolbar("QueryBookLeftGroup", group, true);
        toolbar.setLayoutPolicy(ActionToolbar.WRAP_LAYOUT_POLICY);
        toolbar.setTargetComponent(this);
        add(toolbar.getComponent(), BorderLayout.CENTER);
    }

    private void createRightGroup() {
        ActionManager actionManager = ActionManager.getInstance();
        DefaultActionGroup group = new DefaultActionGroup();
        group.add(new ClearAllQueryAction(ctx));

        ActionToolbar toolbar = actionManager.createActionToolbar("QueryBookRightGroup", group, true);
        toolbar.setLayoutPolicy(ActionToolbar.WRAP_LAYOUT_POLICY);
        toolbar.setTargetComponent(this);
        add(toolbar.getComponent(), BorderLayout.EAST);
    }
}
