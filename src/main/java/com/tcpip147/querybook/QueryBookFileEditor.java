package com.tcpip147.querybook;

import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.NavigatableFileEditor;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.tcpip147.querybook.ui.Context;
import com.tcpip147.querybook.ui.TabFolder;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeListener;

public class QueryBookFileEditor implements NavigatableFileEditor {

    private final Context ctx;

    public QueryBookFileEditor(@NotNull Project project, @NotNull VirtualFile file) {
        ctx = new Context(project, file);
        ctx.setState(project.getService(PersistentState.class));
        ctx.setFileEditor(this);
        file.putUserData(Context.KEY, ctx);
        ctx.setTabFolder(new TabFolder(ctx));
    }

    @Override
    public @NotNull JComponent getComponent() {
        return ctx.getTabFolder();
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return null;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) @NotNull String getName() {
        return "QueryBookFileEditor";
    }

    @Override
    public void setState(@NotNull FileEditorState fileEditorState) {

    }

    @Override
    public boolean isModified() {
        return ctx.isModified();
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {
        ctx.setPropertyChangeListener(propertyChangeListener);
    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {

    }

    @Override
    public void dispose() {
        ctx.getTabFolder().releaseMemory();
        Disposer.dispose(this);
    }

    @Override
    public <T> @Nullable T getUserData(@NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T t) {

    }

    @Override
    public boolean canNavigateTo(@NotNull Navigatable navigatable) {
        return true;
    }

    @Override
    public void navigateTo(@NotNull Navigatable navigatable) {
        OpenFileDescriptor fileDescriptor = (OpenFileDescriptor) navigatable;
        ctx.getQueryEditor().gotoOffset(fileDescriptor.getOffset());
    }

    @Override
    public VirtualFile getFile() {
        return ctx.getFile();
    }
}
