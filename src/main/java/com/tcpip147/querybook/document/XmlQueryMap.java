package com.tcpip147.querybook.document;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.XmlElementFactory;
import com.intellij.psi.xml.XmlTag;
import com.tcpip147.querybook.ui.Context;
import com.tcpip147.querybook.util.DocumentUtils;

import java.util.ArrayList;
import java.util.List;

public class XmlQueryMap {

    private Context ctx;
    private ASTNode node;

    public XmlQueryMap(Context ctx, ASTNode node) {
        this.ctx = ctx;
        this.node = node;
    }

    public List<XmlQuery> getQueries() {
        List<PsiElement> queryList = DocumentUtils.findChildrenByName(node.getPsi(), "XmlTag:query");
        List<XmlQuery> xmlQueries = new ArrayList<>();
        for (PsiElement query : queryList) {
            xmlQueries.add(new XmlQuery(ctx, query.getNode()));
        }
        return xmlQueries;
    }

    public XmlQuery addQuery(int index) {
        PsiElement queryMapElement = node.getPsi();
        XmlTag temp = XmlElementFactory.getInstance(ctx.getProject()).createTagFromText("<query/>");
        List<PsiElement> queryList = DocumentUtils.findChildrenByName(queryMapElement, "XmlTag:query");
        if (index < queryList.size()) {
            PsiElement query = queryList.get(index);
            queryMapElement.addBefore(temp, query);
        } else {
            queryMapElement.add(temp);
        }
        List<PsiElement> newQueryList = DocumentUtils.findChildrenByName(queryMapElement, "XmlTag:query");
        return new XmlQuery(ctx, newQueryList.get(index).getNode());
    }
}
