package hk.ust.comp3021.stmt;

import hk.ust.comp3021.expr.*;
import hk.ust.comp3021.misc.*;
import hk.ust.comp3021.utils.*;
import java.util.*;

public class ClassDefStmt extends ASTStmt {
    /*
     * ClassDef(identifier name,
     *         expr* bases,
     *         keyword* keywords,
     *         stmt* body,
     *         expr* decorator_list,...)
     */
    private String name;
    private ArrayList<ASTExpr> bases = new ArrayList<>();
    private ArrayList<ASTKeyWord> keywords = new ArrayList<>();
    private ArrayList<ASTStmt> body = new ArrayList<>();
    private ArrayList<ASTExpr> decoratorList = new ArrayList<>();

    public ClassDefStmt(XMLNode node) {
        // TODO: complete the definition of the constructor. Define the class as the subclass of ASTExpr.
        super(node);
        this.stmtType = ASTStmt.StmtType.ClassDef;
        XMLNode nameNode = node;
        this.name = nameNode.getTagName();
        int count;
        count = node.getChildByIdx(0).getChildren().size();
        if (count>0) {
            for (int i = 0; i < count; i++) {
                bases.add(ASTExpr.createASTExpr(node.getChildByIdx(0).getChildByIdx(i)));
            }
        }
        count = node.getChildByIdx(1).getChildren().size();
        if (count>0) {
            for (int i = 0; i < count; i++) {
                keywords.add(new ASTKeyWord(node.getChildByIdx(1).getChildByIdx(i)));
            }
        }
        count = node.getChildByIdx(2).getChildren().size();
        if (count>0) {
            for (int i = 0; i < count; i++) {
                body.add(ASTStmt.createASTStmt(node.getChildByIdx(2).getChildByIdx(i)));
            }
        }
        count = node.getChildByIdx(3).getChildren().size();
        if (count>0) {
            for (int i = 0; i < count; i++) {
                decoratorList.add(ASTExpr.createASTExpr(node.getChildByIdx(3).getChildByIdx(i)));
            }
        }

    }

    @Override
    public ArrayList<ASTElement> getChildren() {
        // TODO: complete the definition of the method `getChildren`
        ArrayList<ASTElement> children = new ArrayList<>();
        children.addAll(bases);
        children.addAll(keywords);
        children.addAll(body);
        children.addAll(decoratorList);
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
    }
}
