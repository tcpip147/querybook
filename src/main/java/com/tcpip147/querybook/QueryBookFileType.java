package com.tcpip147.querybook;

import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class QueryBookFileType extends LanguageFileType {

    public static final QueryBookFileType INSTANCE = new QueryBookFileType();
    public static final Icon ICON = IconLoader.getIcon("/jar-gray.png", QueryBookFileType.class);
    public static final String EXTENSION = "queryml";

    protected QueryBookFileType() {
        super(XMLLanguage.INSTANCE);
    }

    @Override
    public @NonNls @NotNull String getName() {
        return "QueryML File";
    }

    @Override
    public @NlsContexts.Label @NotNull String getDescription() {
        return "Query Markup Language File";
    }

    @Override
    public @NlsSafe @NotNull String getDefaultExtension() {
        return EXTENSION;
    }

    @Override
    public Icon getIcon() {
        return ICON;
    }
}
