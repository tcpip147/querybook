package com.tcpip147.querybook;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileDocumentManagerListener;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.tcpip147.querybook.ui.Context;
import org.jetbrains.annotations.NotNull;

public class FileSaver implements FileDocumentManagerListener {

    @Override
    public void beforeAllDocumentsSaving() {
        saveAll();
        FileDocumentManagerListener.super.beforeAllDocumentsSaving();
    }

    @Override
    public void beforeDocumentSaving(@NotNull Document document) {
        save(document);
        FileDocumentManagerListener.super.beforeDocumentSaving(document);
    }

    @Override
    public void beforeAnyDocumentSaving(@NotNull Document document, boolean explicit) {
        save(document);
        FileDocumentManagerListener.super.beforeAnyDocumentSaving(document, explicit);
    }

    private void saveAll() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        if (projects != null) {
            for (Project project : projects) {
                VirtualFile[] files = FileEditorManager.getInstance(project).getOpenFiles();
                for (VirtualFile file : files) {
                    save(FileDocumentManager.getInstance().getDocument(file));
                }
            }
        }
    }

    private void save(Document document) {
        VirtualFile file = FileDocumentManager.getInstance().getFile(document);
        if (QueryBookFileType.EXTENSION.equals(file.getExtension())) {
            Context ctx = file.getUserData(Context.KEY);
            if (ctx != null) {
                if (ctx.isModified()) {
                    ctx.setModified(false);
                }
            }
        }
    }
}
