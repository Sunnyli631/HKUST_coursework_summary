package hk.ust.comp3021.expr;

import hk.ust.comp3021.misc.*;
import hk.ust.comp3021.utils.*;
import java.util.*;

public class NameExpr extends ASTExpr {
    // Name(identifier id, expr_context ctx)
    private String id;
    private ASTEnumOp ctx;

    public NameExpr(XMLNode node)  {
        // TODO: complete the definition of the constructor. Define the class as the subclass of ASTExpr.
        super(node);
        this.exprType = ExprType.Name;
        //XMLNode value_node = node.getChildByIdx(0);
        String idNode = node.getAttribute("id");
        XMLNode ctxNode = node.getChildByIdx(0);
        this.id = idNode;
        this.ctx = new ASTEnumOp(ctxNode);
        //this.kind = kind_node.getTagName();
    }

    @Override
    public ArrayList<ASTElement> getChildren() {
        // TODO: complete the definition of the method `getChildren`
        ArrayList<ASTElement> children = new ArrayList<>();
        //children.addAll(ops);
        children.add(ctx);
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
    public String getId() {
        return id;
    }

}
