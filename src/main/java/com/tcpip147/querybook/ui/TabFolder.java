package com.tcpip147.querybook.ui;

import com.intellij.openapi.command.undo.UndoUtil;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.ui.tabs.JBTabsPosition;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.TabsListener;
import com.intellij.ui.tabs.impl.JBEditorTabs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TabFolder extends JBEditorTabs {

    private Context ctx;
    private Editor defaultEditor;

    public TabFolder(Context ctx) {
        super(ctx.getProject(), ctx.getFileEditor());
        this.ctx = ctx;
        setTabsPosition(JBTabsPosition.bottom);
        createQueryEditor();
        createDefaultEditor();
        addListener(new TabsListener() {
            @Override
            public void selectionChanged(@Nullable TabInfo oldSelection, @Nullable TabInfo newSelection) {
                ctx.getQueryEditor().reload();
                ctx.getState().setDefaultSelectedTab(getIndexOf(newSelection));
            }
        });
        select(getTabAt(ctx.getState().getDefaultSelectedTab()), true);
    }

    private void createQueryEditor() {
        QueryEditor queryEditor = new QueryEditor(ctx);
        TabInfo tabInfo = new TabInfo(queryEditor);
        tabInfo.setText("QueryML");
        addTab(tabInfo);
    }

    private void createDefaultEditor() {
        FileType fileType = FileTypeManager.getInstance().getFileTypeByExtension("xml");
        Document document = FileDocumentManager.getInstance().getDocument(ctx.getFile());
        defaultEditor = EditorFactory.getInstance().createEditor(document, ctx.getProject(), fileType, false);
        defaultEditor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                ctx.setModified(true);
            }
        });
        UndoUtil.disableUndoFor(defaultEditor.getDocument());
        TabInfo tabInfo = new TabInfo(defaultEditor.getComponent());
        tabInfo.setText("Text");
        addTab(tabInfo);
    }

    public void releaseMemory() {
        ctx.getQueryEditor().releaseMemory();
        EditorFactory.getInstance().releaseEditor(defaultEditor);
    }
}
