package hk.ust.comp3021.misc;

import hk.ust.comp3021.expr.*;
import hk.ust.comp3021.utils.*;
import java.util.*;

public class ASTKeyWord extends ASTElement {
    /*
     * keyword = (identifier? arg, expr value)
     * attributes (int lineno, int colOffset, int? endLineno, int? endColOffset)
     */
    private String arg;
    private ASTExpr value;

    public ASTKeyWord(XMLNode node) {
        // TODO: complete the definition of the constructor. Define the class as the subclass of ASTElement.
        super(node);
        XMLNode argNode = node;
        XMLNode valueNode = node.getChildByIdx(0);
        this.arg = argNode.getTagName();
        this.value = ASTExpr.createASTExpr(valueNode);
    }

    @Override
    public ArrayList<ASTElement> getChildren() {
        // TODO: complete the definition of the method `getChildren`
        ArrayList<ASTElement> children = new ArrayList<>();
        //children.addAll(elts);
        children.add(value);
        return children;
    }

    @Override
    public int countChildren() {
        // TODO: complete the definition of the method `countChildren`
        ArrayList<ASTElement> children = getChildren();
        int count=1;
        if(children.size()>0){
            for (ASTElement child : children){
                count+=child.countChildren();
            }
            //return count;
        }
        return count;
    }

    @Override
    public String getNodeType() {
        return "keyword";
    }

    @Override
    public void printByPos(StringBuilder str) {
        // TODO: (Bonus) complete the definition of the method `printByPos`
    }

    /**
     * Attention: You may need to define more methods to update or access the field of the class `User`
     * Feel free to define more method but remember not
     * (1) removing the fields or methods in our skeleton.
     * (2) changing the type signature of `public` methods
     * (3) changing the modifiers of the fields and methods, e.g., changing a modifier from "private" to "public"
     */
    public void yourMethod() {

    }
}
