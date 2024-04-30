package com.tcpip147.querybook;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.codeInsight.template.emmet.tokens.StringLiteralToken;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.light.JavaIdentifier;
import com.intellij.psi.impl.source.tree.ElementType;
import com.intellij.psi.impl.source.tree.java.PsiJavaTokenImpl;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.tcpip147.querybook.util.DocumentUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class GotoHandler implements GotoDeclarationHandler {

    @Override
    public PsiElement @Nullable [] getGotoDeclarationTargets(@Nullable PsiElement psiElement, int offset, Editor editor) {
        if (editor.getVirtualFile().getFileType() == JavaFileType.INSTANCE) {
            if (psiElement instanceof PsiJavaTokenImpl && ((PsiJavaTokenImpl) psiElement).getTokenType() == ElementType.STRING_LITERAL) {
                String find = psiElement.getText().replaceAll("\"", "");
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
                                if (find.equals(DocumentUtils.getAttributeValue(query, "id"))) {
                                    return new PsiElement[]{query};
                                }
                            }
                        }
                    }
                }
            }
        }
        return new PsiElement[0];
    }
}
