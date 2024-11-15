package hk.ust.comp3021.expr;

import hk.ust.comp3021.misc.*;
import hk.ust.comp3021.utils.*;
import java.util.*;


public class CompareExpr extends ASTExpr {
    // Compare(expr left, cmpop* ops, expr* comparators)
    private ASTExpr left;
    private ArrayList<ASTEnumOp> ops = new ArrayList<>();
    private ArrayList<ASTExpr> comparators = new ArrayList<>();

    public CompareExpr(XMLNode node) {
        // TODO: complete the definition of the constructor. Define the class as the subclass of ASTExpr.
        super(node);
        this.exprType = ExprType.Compare;
        XMLNode leftnode = node.getChildByIdx(0);
        this.left = ASTExpr.createASTExpr(leftnode);
        //for(int i=0;i< node.getChildByIdx(1).getNumChildren();i++){
        for(int i=0;i< node.getChildByIdx(1).getChildren().size();i++){
            XMLNode node1 = node.getChildByIdx(1).getChildByIdx(i);
            ops.add(new ASTEnumOp(node1));
        }
        for(int i=0;i< node.getChildByIdx(2).getChildren().size();i++){
            XMLNode node1 = node.getChildByIdx(2).getChildByIdx(i);
            comparators.add(ASTExpr.createASTExpr(node1));
        }
    }

    @Override
    public ArrayList<ASTElement> getChildren() {
        // TODO: complete the definition of the method `getChildren`
        ArrayList<ASTElement> children = new ArrayList<>();
        children.add(left);
        children.addAll(ops);
        children.addAll(comparators);
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
    public void yourMethod() {

    }

}
