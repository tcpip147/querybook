package com.tcpip147.querybook;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.tree.ElementType;
import com.intellij.psi.impl.source.tree.java.PsiJavaTokenImpl;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ProcessingContext;
import com.tcpip147.querybook.util.DocumentUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class QueryBookCompletionContributor extends CompletionContributor {

    public QueryBookCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), new CompletionProvider<>() {
            public void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet resultSet) {
                PsiElement psiElement = parameters.getPosition();
                if (psiElement != null) {
                    if (psiElement instanceof PsiJavaTokenImpl && ((PsiJavaTokenImpl) psiElement).getTokenType() == ElementType.STRING_LITERAL) {
                        String literal = psiElement.getText();
                        literal = literal.substring(1, literal.length() - 1);
                        literal = literal.replaceAll("IntellijIdeaRulezzz ", "");
                        Project project = psiElement.getProject();
                        Collection<VirtualFile> files = FileTypeIndex.getFiles(QueryBookFileType.INSTANCE, GlobalSearchScope.projectScope(project));
                        for (VirtualFile file : files) {
                            PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
                            PsiElement xmlDocument = DocumentUtils.findFirstChildByName(psiFile, "PsiElement(XML_DOCUMENT)");
                            if (xmlDocument != null) {
                                PsiElement queryMap = DocumentUtils.findFirstChildByName(xmlDocument, "XmlTag:queryMap");
                                if (queryMap != null) {
                                    List<PsiElement> queryList = DocumentUtils.findChildrenByName(queryMap, "XmlTag:query");
                                    for (PsiElement query : queryList) {
                                        String id = DocumentUtils.getAttributeValue(query, "id");
                                        if (id.length() >= literal.length() && id.substring(0, literal.length()).equals(literal)) {
                                            resultSet.addElement(LookupElementBuilder.create(id));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
