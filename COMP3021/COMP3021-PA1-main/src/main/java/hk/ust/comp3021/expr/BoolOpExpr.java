package hk.ust.comp3021.expr;

import hk.ust.comp3021.misc.*;
import hk.ust.comp3021.utils.*;
import java.util.*;

public class BoolOpExpr extends ASTExpr {
    // BoolOp(boolop op, expr* values)
    private ASTEnumOp op;
    private ArrayList<ASTExpr> values = new ArrayList<>();

    public BoolOpExpr(XMLNode node) {
        // TODO: complete the definition of the constructor. Define the class as the subclass of ASTExpr.
        super(node);
        this.exprType = ExprType.BoolOp;
        XMLNode opNode = node.getChildByIdx(0);
        //List Node1 = node.getChildByIdx(1).getChildren();
        for(int i=0;i< node.getChildByIdx(1).getChildren().size();i++){
            XMLNode node1 = node.getChildByIdx(1).getChildByIdx(i);
            values.add(ASTExpr.createASTExpr(node1));
        }
        //this.values = new ArrayList<ASTExpr>(Node1);
        this.op = new ASTEnumOp(opNode);
        //this.right = ASTExpr.createASTExpr(rightNode);
    }

    @Override
    public ArrayList<ASTElement> getChildren() {
        // TODO: complete the definition of the method `getChildren`
        //children.add(left);
        ArrayList<ASTElement> children = new ArrayList<>();
        children.add(op);
        children.addAll(values);
        return children;
    }
    @Override
    public int countChildren() {
        // TODO: complete the definition of the method `countChildren`
        ArrayList<ASTElement> children = getChildren();
        int count=1;
        if(!children.isEmpty()){
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
    public void yourMethod() {

    }
}
