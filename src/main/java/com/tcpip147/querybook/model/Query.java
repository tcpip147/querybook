package com.tcpip147.querybook.model;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.tcpip147.querybook.document.XmlQuery;
import com.tcpip147.querybook.ui.Context;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class Query {

    private Context ctx;
    private XmlQuery xmlQuery;
    private Editor editor;

    public Query(Context ctx, XmlQuery xmlQuery) {
        this.ctx = ctx;
        this.xmlQuery = xmlQuery;

        FileType fileType = FileTypeManager.getInstance().getFileTypeByExtension("sql");
        Document document = EditorFactory.getInstance().createDocument(xmlQuery.getContent());
        editor = EditorFactory.getInstance().createEditor(document, ctx.getProject(), fileType, false);
        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                ctx.setModified(true);
            }
        });
        document.addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                ApplicationManager.getApplication().invokeLaterOnWriteThread(() -> {
                    WriteCommandAction.runWriteCommandAction(ctx.getProject(), () -> {
                        xmlQuery.setContent(document.getText());
                    });
                });
            }
        });
    }

    public void releaseMemory() {
        EditorFactory.getInstance().releaseEditor(editor);
    }
}
