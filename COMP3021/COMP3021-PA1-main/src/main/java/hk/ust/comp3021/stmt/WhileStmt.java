package hk.ust.comp3021.stmt;

import hk.ust.comp3021.expr.*;
import hk.ust.comp3021.misc.*;
import hk.ust.comp3021.utils.*;
import java.util.ArrayList;

public class WhileStmt extends ASTStmt {
    // While(expr test, stmt* body, stmt* orelse)
    private ASTExpr test;
    private ArrayList<ASTStmt> body = new ArrayList<>();
    private ArrayList<ASTStmt> orelse = new ArrayList<>();

    public WhileStmt(XMLNode node) {
        // TODO: complete the definition of the constructor. Define the class as the subclass of ASTExpr.
        super(node);
        this.stmtType = ASTStmt.StmtType.While;
        XMLNode testNode = node.getChildByIdx(0);
        this.test = ASTExpr.createASTExpr(testNode);
        //this.iter = ASTExpr.createASTExpr(iter_node);
        for(int i=0;i<node.getChildByIdx(1).getChildren().size();i++){
            body.add(ASTStmt.createASTStmt(node.getChildByIdx(1).getChildByIdx(i)));
        }
        for(int i=0;i<node.getChildByIdx(2).getChildren().size();i++){
            orelse.add(ASTStmt.createASTStmt(node.getChildByIdx(2).getChildByIdx(i)));
        }
    }

    @Override
    public ArrayList<ASTElement> getChildren() {
        // TODO: complete the definition of the method `getChildren`
        ArrayList<ASTElement> children = new ArrayList<>();
        children.add(test);
        children.addAll(body);
        children.addAll(orelse);
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
     * Attention: You may need to define more methods to update or access the field
     * of the class ASTStmt, i.e., getters or setters Feel free to define more
     * method but remember not
     * (1) removing the fields or methods in our skeleton.
     * (2) changing the type signature of `public` methods
     * (3) changing the modifiers of the fields and methods, e.g., changing a modifier from "private"
     * to "public"
     */
    @Override
    public void yourMethod() {
        //int fake = 1;
    }

}
