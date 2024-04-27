package com.tcpip147.querybook.document;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.tcpip147.querybook.ui.Context;
import com.tcpip147.querybook.util.DocumentUtils;

public class XmlManager {

    public static XmlQueryMap getQueryMap(Context ctx) {
        PsiFile psiFile = PsiManager.getInstance(ctx.getProject()).findFile(ctx.getFile());
        PsiElement xmlDocument = DocumentUtils.findFirstChildByName(psiFile, "PsiElement(XML_DOCUMENT)");
        if (xmlDocument != null) {
            PsiElement queryMap = DocumentUtils.findFirstChildByName(xmlDocument, "XmlTag:queryMap");
            if (queryMap != null) {
                return new XmlQueryMap(ctx, queryMap.getNode());
            }
        }
        return null;
    }
}
