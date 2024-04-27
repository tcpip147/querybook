package com.tcpip147.querybook;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "QueryBookState")
@Storage(StoragePathMacros.WORKSPACE_FILE)
@Getter
@Setter
public class PersistentState implements PersistentStateComponent<PersistentState> {

    private Float splitterProportion = 0.3f;
    private Integer[] listTableWidth = new Integer[]{300, 300, 100, 100, 500};
    private String userName;
    private int defaultSelectedTab = 0;

    @Override
    public @Nullable PersistentState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull PersistentState state) {
        splitterProportion = nvl(state.splitterProportion, splitterProportion);
        listTableWidth = nvl(state.listTableWidth, listTableWidth);
        userName = nvl(state.userName, userName);
        defaultSelectedTab = nvl(state.defaultSelectedTab, defaultSelectedTab);
    }

    private <T> T nvl(T a, T b) {
        if (a == null) {
            return b;
        }
        return a;
    }
}
