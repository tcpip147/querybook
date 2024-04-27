package com.tcpip147.querybook.document;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.XmlElementFactory;
import com.intellij.psi.xml.XmlTag;
import com.tcpip147.querybook.ui.Context;
import com.tcpip147.querybook.util.DocumentUtils;

public class XmlQuery {

    public static final String DELIMITER = "\n        ";

    private Context ctx;
    private ASTNode node;

    public XmlQuery(Context ctx, ASTNode node) {
        this.ctx = ctx;
        this.node = node;
    }

    public String getId() {
        return DocumentUtils.getAttributeValue(node.getPsi(), "id");
    }

    public void setId(String id) {
        setAttributeValue("id", id);
    }

    public String getDescription() {
        return DocumentUtils.getAttributeValue(node.getPsi(), "desc");
    }

    public void setDescription(String desc) {
        setAttributeValue("desc", desc);
    }

    public String getCreator() {
        return DocumentUtils.getAttributeValue(node.getPsi(), "creator");
    }

    public void setCreator(String creator) {
        setAttributeValue("creator", creator);
    }

    public String getCreatedDate() {
        return DocumentUtils.getAttributeValue(node.getPsi(), "createdDate");
    }

    public void setCreatedDate(String createdDate) {
        setAttributeValue("createdDate", createdDate);
    }

    public String getContent() {
        String content = DocumentUtils.getTagValue(node.getPsi());
        String[] lines = content.split(DELIMITER);
        return String.join("\n", lines).replaceAll("^\n", "");
    }

    public void setContent(String content) {
        PsiElement queryElement = node.getPsi();
        XmlTag temp = XmlElementFactory.getInstance(ctx.getProject()).createTagFromText("<query>" + DELIMITER + "<![CDATA[" + DELIMITER + content.replaceAll("\n", DELIMITER) + DELIMITER + "]]>\n</query>");
        PsiElement text = DocumentUtils.findFirstChildByName(queryElement, "XmlText");
        if (text != null) {
            text.replace(DocumentUtils.findFirstChildByName(temp, "XmlText"));
        } else {
            XmlTag query = (XmlTag) queryElement;
            query.add(DocumentUtils.findFirstChildByName(temp, "XmlText"));
        }
    }

    private void setAttributeValue(String name, String value) {
        PsiElement queryElement = node.getPsi();
        PsiElement attribute = DocumentUtils.getAttribute(queryElement, name);
        PsiElement temp = XmlElementFactory.getInstance(ctx.getProject()).createAttribute(name, value, queryElement);
        if (attribute != null) {
            attribute.replace(temp);
        } else {
            queryElement.add(temp);
        }
    }

    public ASTNode getNode() {
        return node;
    }

    public void setNode(ASTNode node) {
        this.node = node;
    }
}
