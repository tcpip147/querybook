package com.tcpip147.querybook.ui.component;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import com.tcpip147.querybook.document.XmlQuery;
import com.tcpip147.querybook.model.Query;
import com.tcpip147.querybook.ui.Context;
import com.tcpip147.querybook.ui.QueryEditor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;

public class QueryBookListTable extends JPanel {

    private final Context ctx;
    private final QueryEditor queryEditor;
    private final DefaultTableModel model;
    private final JBTable table;

    public QueryBookListTable(Context ctx, QueryEditor queryEditor) {
        setLayout(new BorderLayout());
        this.ctx = ctx;
        ctx.setListTable(this);
        this.queryEditor = queryEditor;

        model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Description");
        model.addColumn("Creator");
        model.addColumn("Created Date");
        model.addColumn("");

        table = new JBTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumn("Id").setPreferredWidth(ctx.getState().getListTableWidth()[0]);
        table.getColumn("Description").setPreferredWidth(ctx.getState().getListTableWidth()[1]);
        table.getColumn("Creator").setPreferredWidth(ctx.getState().getListTableWidth()[2]);
        table.getColumn("Created Date").setPreferredWidth(ctx.getState().getListTableWidth()[3]);
        table.getColumn("").setPreferredWidth(ctx.getState().getListTableWidth()[4]);

        /* Persistent column width that user modify */
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                TableColumnModel columnModel = table.getColumnModel();
                for (int i = 0; i < columnModel.getColumnCount(); i++) {
                    ctx.getState().getListTableWidth()[i] = columnModel.getColumn(i).getPreferredWidth();
                }
            }
        });

        /* Setting cell padding */
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(IdeBorderFactory.createEmptyBorder(JBUI.insets(0, 8)));
                return this;
            }
        });

        /* Row selection event */
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                queryEditor.selectQuery(table.getSelectedRow());
            }
        });

        /* Copy a cell text to clipboard */
        table.getActionMap().put("copy", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = model.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString();
                StringSelection selection = new StringSelection(value);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
            }
        });

        /* Double click */
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JBTable table = (JBTable) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    ctx.getEditQueryAction().actionPerformed(new AnActionEvent(null, DataManager.getInstance().getDataContext(ctx.getQueryEditor()), ActionPlaces.UNKNOWN, new Presentation(), ActionManager.getInstance(), 0));
                }
            }
        });

        /* Shortcut */
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 127) {
                    ctx.getRemoveQueryAction().actionPerformed(new AnActionEvent(null, DataManager.getInstance().getDataContext(ctx.getQueryEditor()), ActionPlaces.UNKNOWN, new Presentation(), ActionManager.getInstance(), 0));
                } else if(e.getKeyCode() == 155) {
                    ctx.getAddQueryAction().actionPerformed(new AnActionEvent(null, DataManager.getInstance().getDataContext(ctx.getQueryEditor()), ActionPlaces.UNKNOWN, new Presentation(), ActionManager.getInstance(), 0));
                }
            }
        });

        add(new JBScrollPane(table), BorderLayout.CENTER);
    }

    public void clear() {
        model.setRowCount(0);
    }

    public void addQuery(Query query) {
        XmlQuery xmlQuery = query.getXmlQuery();
        model.addRow(new Object[]{xmlQuery.getId(), xmlQuery.getDescription(), xmlQuery.getCreator(), xmlQuery.getCreatedDate()});
    }

    public void modifyQuery(int n, Query query) {
        XmlQuery xmlQuery = query.getXmlQuery();
        model.setValueAt(xmlQuery.getId(), n, 0);
        model.setValueAt(xmlQuery.getDescription(), n, 1);
        model.setValueAt(xmlQuery.getCreator(), n, 2);
        model.setValueAt(xmlQuery.getCreatedDate(), n, 3);
    }

    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    public int getRowCount() {
        return table.getRowCount();
    }

    public void swap(int n1, int n2) {
        model.moveRow(n1, n1, n2);
    }

    public void selectRow(int n) {
        table.setRowSelectionInterval(n, n);
    }

    public void removeRow(int n) {
        model.removeRow(n);
    }
}
