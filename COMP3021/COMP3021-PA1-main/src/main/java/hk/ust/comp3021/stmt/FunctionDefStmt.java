package hk.ust.comp3021.stmt;

import hk.ust.comp3021.expr.*;
import hk.ust.comp3021.misc.*;
import hk.ust.comp3021.utils.*;
import java.util.*;

public class FunctionDefStmt extends ASTStmt {
    /*
     * FunctionDef(identifier name, arguments args, stmt* body, expr*
     * decorator_list, expr? returns, ..)
     */
    private String name;
    private ASTArguments args;
    private ArrayList<ASTStmt> body = new ArrayList<>();
    private ArrayList<ASTExpr> decoratorList = new ArrayList<>();
    private ASTExpr returns = null;

    public FunctionDefStmt(XMLNode node) {
        // TODO: complete the definition of the constructor. Define the class as the subclass of ASTExpr.
        super(node);
        this.stmtType = ASTStmt.StmtType.FunctionDef;
        XMLNode nameNode = node;
        this.name = nameNode.getTagName();
        XMLNode argsNode = node.getChildByIdx(0);
        this.args = new ASTArguments(argsNode);
        if(node.hasAttribute("returns")) {
            if (!(node.getAttribute("returns").equals("none"))) {
                XMLNode returnsNode = node.getChildByIdx(3);
                this.returns = ASTExpr.createASTExpr(returnsNode);
            }
        }
        //this.iter = ASTExpr.createASTExpr(iter_node);
        for(int i=0;i<node.getChildByIdx(1).getChildren().size();i++){
            body.add(ASTStmt.createASTStmt(node.getChildByIdx(1).getChildByIdx(i)));
        }
        for(int i=0;i<node.getChildByIdx(2).getChildren().size();i++){
            decoratorList.add(ASTExpr.createASTExpr(node.getChildByIdx(2).getChildByIdx(i)));
        }
    }

    /*
     * Find all AST node whose class type is `CallExpr` shown in the AST
     * Hints: you need to traverse all the nodes in AST and check its class type.
     * We have prepared the method `getChildren` for you to ease the traversal.
     * You may need to remove the `return null` in the skeleton.
     * */
    public ArrayList<CallExpr> getAllCalledFunc() {
        // TODO: complete the definition of the method `getAllCalledFunc`
        ArrayList<CallExpr> funcList = new ArrayList<>();
        funcHelper(this, funcList);
        return funcList;
    }

    private void funcHelper(ASTElement element, ArrayList<CallExpr> list) {
        if (element instanceof CallExpr) {
            list.add((CallExpr) element);
        }

        ArrayList<ASTElement> children = element.getChildren();
        for (ASTElement child : children) {
            funcHelper(child, list);
        }
    }


    public int getParamNum() {
        return args.getParamNum();
    }

    public String getName() {
        return name;
    }

    @Override
    public ArrayList<ASTElement> getChildren() {
        // TODO: complete the definition of the method `getChildren`
        ArrayList<ASTElement> children = new ArrayList<>();
        children.add(args);
        children.addAll(body);
        children.addAll(decoratorList);
        if(returns!=null){
            children.add(returns);
        }
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
    }

}
