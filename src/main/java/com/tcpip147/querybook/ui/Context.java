package com.tcpip147.querybook.ui;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.tcpip147.querybook.PersistentState;
import com.tcpip147.querybook.QueryBookFileEditor;
import com.tcpip147.querybook.action.EditQueryAction;
import com.tcpip147.querybook.ui.component.QueryBookListTable;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@Getter
@Setter
public class Context {

    public static final Key<Context> KEY = Key.create("CONTEXT");

    private Project project;
    private VirtualFile file;
    private QueryBookFileEditor fileEditor;
    private QueryEditor queryEditor;
    private PropertyChangeListener propertyChangeListener;
    private TabFolder tabFolder;
    private QueryBookListTable listTable;
    private boolean modified;
    private PersistentState state;
    private EditQueryAction editQueryAction;

    public Context(@NotNull Project project, @NotNull VirtualFile file) {
        this.project = project;
        this.file = file;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
        propertyChangeListener.propertyChange(new PropertyChangeEvent(fileEditor, FileEditor.PROP_MODIFIED, !modified, modified));
    }
}
