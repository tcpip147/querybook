package com.tcpip147.querybook.ui;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.SideBorder;
import com.tcpip147.querybook.document.XmlManager;
import com.tcpip147.querybook.document.XmlQuery;
import com.tcpip147.querybook.document.XmlQueryMap;
import com.tcpip147.querybook.model.Query;
import com.tcpip147.querybook.ui.component.QueryBookActionMenu;
import com.tcpip147.querybook.ui.component.QueryBookListTable;
import com.tcpip147.querybook.util.DocumentUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class QueryEditor extends JBSplitter {

    private Context ctx;
    private QueryBookListTable listTable;
    private JPanel bottomPanel;
    private List<Query> queryList = new LinkedList<>();

    public QueryEditor(Context ctx) {
        super(true);
        this.ctx = ctx;
        ctx.setQueryEditor(this);
        setProportion(0.3f);
        addFocusRequest();
        createTopPanel();
        createBottomPanel();
        addSplitterMoveEventListener();
        loadFile();
    }

    private void createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(IdeBorderFactory.createBorder(SideBorder.BOTTOM));
        topPanel.add(new QueryBookActionMenu(ctx), BorderLayout.NORTH);
        listTable = new QueryBookListTable(ctx, this);
        topPanel.add(listTable, BorderLayout.CENTER);
        setFirstComponent(topPanel);
    }

    private void createBottomPanel() {
        bottomPanel = new JPanel(new BorderLayout());
        setSecondComponent(bottomPanel);
    }

    private void addSplitterMoveEventListener() {
        addPropertyChangeListener(evt -> {
            if ("proportion".equals(evt.getPropertyName())) {
                ctx.getState().setSplitterProportion((float) evt.getNewValue());
            }
        });
    }

    public void reload() {
        clean();
        loadFile();
    }

    private void loadFile() {
        XmlQueryMap xmlQueryMap = XmlManager.getQueryMap(ctx);
        if (xmlQueryMap != null) {
            for (XmlQuery xmlQuery : xmlQueryMap.getQueries()) {
                createQuery(xmlQuery);
            }
        } else {
            ApplicationManager.getApplication().invokeLaterOnWriteThread(() -> {
                WriteCommandAction.runWriteCommandAction(ctx.getProject(), () -> {
                    FileDocumentManager.getInstance().getDocument(ctx.getFile()).setText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<queryMap>\n</queryMap>");
                });
            });
        }
    }

    private void clean() {
        listTable.clear();
        releaseMemory();
        queryList.clear();
    }

    private void createQuery(XmlQuery xmlQuery) {
        Query query = new Query(ctx, xmlQuery);
        queryList.add(query);
        listTable.addQuery(query);
    }

    private void addFocusRequest() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }

    public void releaseMemory() {
        for (Query query : queryList) {
            query.releaseMemory();
        }
    }

    public void selectQuery(int n) {
        bottomPanel.removeAll();
        if (n > -1) {
            bottomPanel.add(queryList.get(n).getEditor().getComponent());
            listTable.selectRow(n);
        }
        revalidate();
        repaint();
    }

    public void addQuery(String id, String desc, String creator, String createdDate, String sql) {
        ApplicationManager.getApplication().invokeLaterOnWriteThread(() -> {
            WriteCommandAction.runWriteCommandAction(ctx.getProject(), () -> {
                XmlQueryMap xmlQueryMap = XmlManager.getQueryMap(ctx);
                if (xmlQueryMap != null) {
                    XmlQuery xmlQuery = xmlQueryMap.addQuery(xmlQueryMap.getQueries().size());
                    xmlQuery.setId(id);
                    xmlQuery.setDescription(desc);
                    xmlQuery.setCreator(creator);
                    xmlQuery.setCreatedDate(createdDate);
                    xmlQuery.setContent(sql);
                    createQuery(xmlQuery);
                    selectQuery(queryList.size() - 1);
                    ctx.setModified(true);
                }
            });
        });
    }

    public void editQuery(int n, String id, String desc, String creator, String createdDate) {
        ApplicationManager.getApplication().invokeLaterOnWriteThread(() -> {
            WriteCommandAction.runWriteCommandAction(ctx.getProject(), () -> {
                Query query = queryList.get(n);
                XmlQuery xmlQuery = query.getXmlQuery();
                xmlQuery.setId(id);
                xmlQuery.setDescription(desc);
                xmlQuery.setCreator(creator);
                xmlQuery.setCreatedDate(createdDate);
                listTable.modifyQuery(n, query);
                ctx.setModified(true);
            });
        });
    }

    public void moveUp(int n) {
        ApplicationManager.getApplication().invokeLaterOnWriteThread(() -> {
            WriteCommandAction.runWriteCommandAction(ctx.getProject(), () -> {
                Query query = queryList.get(n);
                PsiElement queryElement = query.getXmlQuery().getNode().getPsi();
                PsiElement prev = DocumentUtils.findPrevByName(queryElement, "XmlTag:query");
                prev.getParent().addBefore(queryElement, prev);
                queryElement.delete();
                query.getXmlQuery().setNode(prev.getPrevSibling().getNode());
                Collections.swap(queryList, n, n - 1);
                listTable.swap(n, n - 1);
                ApplicationManager.getApplication().invokeLater(() -> {
                    selectQuery(n - 1);
                });
                ctx.setModified(true);
            });
        });
    }

    public void moveDown(int n) {
        ApplicationManager.getApplication().invokeLaterOnWriteThread(() -> {
            WriteCommandAction.runWriteCommandAction(ctx.getProject(), () -> {
                Query query = queryList.get(n);
                PsiElement queryElement = query.getXmlQuery().getNode().getPsi();
                PsiElement next = DocumentUtils.findNextByName(queryElement, "XmlTag:query");
                next.getParent().addAfter(queryElement, next);
                queryElement.delete();
                query.getXmlQuery().setNode(next.getNextSibling().getNode());
                Collections.swap(queryList, n, n + 1);
                listTable.swap(n, n + 1);
                ApplicationManager.getApplication().invokeLater(() -> {
                    selectQuery(n + 1);
                });
                ctx.setModified(true);
            });
        });
    }

    public void removeQuery(int n) {
        ApplicationManager.getApplication().invokeLaterOnWriteThread(() -> {
            WriteCommandAction.runWriteCommandAction(ctx.getProject(), () -> {
                Query query = queryList.get(n);
                query.releaseMemory();
                query.getXmlQuery().getNode().getPsi().delete();
                queryList.remove(n);
                listTable.removeRow(n);
                ctx.setModified(true);
            });
        });
    }

    public void clearAllQuery() {
        WriteCommandAction.runWriteCommandAction(ctx.getProject(), () -> {
            clean();
            FileDocumentManager.getInstance().getDocument(ctx.getFile()).setText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<queryMap>\n</queryMap>");
            loadFile();
            ctx.setModified(true);
        });
    }

    public void gotoOffset(int offset) {
        PsiFile psiFile = PsiManager.getInstance(ctx.getProject()).findFile(ctx.getFile());
        PsiElement psiElement = psiFile.findElementAt(offset);
        PsiElement queryElement = DocumentUtils.findParentByName(psiElement, "XmlTag:query", true);
        int index = DocumentUtils.indexOf(queryElement, "XmlTag:query");
        if (index > -1) {
            selectQuery(index);
            PsiElement text = DocumentUtils.findTextElement(queryElement);
            if (text != null) {
                Editor editor = queryList.get(index).getEditor();
                editor.getCaretModel().moveToOffset(offset - text.getNode().getStartOffset() - XmlQuery.DELIMITER.length());
                editor.getScrollingModel().scrollToCaret(ScrollType.CENTER_UP);
                editor.getContentComponent().requestFocus(false);
            }
        }
    }

    public Query getQuery(int n) {
        return queryList.get(n);
    }
}
