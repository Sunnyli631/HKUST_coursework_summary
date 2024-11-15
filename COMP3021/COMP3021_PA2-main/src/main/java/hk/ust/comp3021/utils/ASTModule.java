package hk.ust.comp3021.utils;

import hk.ust.comp3021.expr.*;
import hk.ust.comp3021.misc.*;
import hk.ust.comp3021.stmt.*;

import java.util.*;


public class ASTModule extends ASTElement {
    // Module(stmt* body, ...)
    private ArrayList<ASTStmt> body;
    private String astID;

    public ASTModule(XMLNode node, String astID) {
        this.astID = astID;

        this.body = new ArrayList<>();
        for (XMLNode bodyNode : node.getChildByIdx(0).getChildren()) {
            this.body.add(ASTStmt.createASTStmt(bodyNode));
        }
    }

    public ArrayList<ASTStmt> getBody() {
        return body;
    }

    public void setAstID(String astID) {
        this.astID = astID;
    }

    public void setBody(ArrayList<ASTStmt> body) {
        this.body = body;
    }
    @Override
    public ArrayList<ASTElement> getChildren() {
        ArrayList<ASTElement> children = new ArrayList<>();
        children.addAll(body);
        return children;
    }
    public String getASTID() {
        return astID;
    }

    @Override
    public String getNodeType() {
        return "Module";
    }

    /*
     * Find all AST node whose class type is `FunctionDefStmt` shown in the AST
     * Hints: you need to traverse all the nodes in AST and check its class type.
     * We have prepared the method `getChildren` for you to ease the traversal.
     * You may need to remove the `return null` in the skeleton.
     * */
    public ArrayList<FunctionDefStmt> getAllFunctions() {
        ArrayList<ASTElement> child = getAllNodes();
        ArrayList<FunctionDefStmt> result = new ArrayList<>();
        for (ASTElement temp:child){
            if(temp instanceof FunctionDefStmt){
                result.add((FunctionDefStmt) temp);
            }
        }
        return result;
    }

    /*
     * Find all operators whose class type is `ASTEnumOp` shown in the AST.
     * Hints: We have prepared the method `getChildren` for you to ease the traversal.
     * But ASTEnumOp is not regarded as children node in AST Tree.
     * To find all operators, you need to first find the nodes whose types are BinOpExpr, BoolOpExpr, etc.
     * Then, you obtain their operators by accessing field `op`.
     * Further, Ctx_Store, Ctx_Load and Ctx_Del are not operators as well.
     * You may need to remove the `return null` in the skeleton.
     * */
    public ArrayList<ASTEnumOp> getAllOperators() {
        ArrayList<ASTElement> child = getAllNodes();
        //ArrayList<ASTExpr> astExprs = new ArrayList<>();
        ArrayList<ASTEnumOp> result = new ArrayList<>();
        for (ASTElement temp:child){
            if(temp instanceof BoolOpExpr){
                result.add(((BoolOpExpr) temp).getOp());
            }
            if(temp instanceof BinOpExpr){
                result.add(((BinOpExpr) temp).getOp());
            }
            if(temp instanceof UnaryOpExpr){
                result.add(((UnaryOpExpr) temp).getOp());
            }
            if(temp instanceof CompareExpr){
                result.addAll(((CompareExpr) temp).getOps());
            }
            if(temp instanceof AugAssignStmt){
                result.add(((AugAssignStmt) temp).getOp());
            }
        }

        return result;
    }

    /*
     * Find all AST node shown in the AST
     * Hints: you need to traverse all the nodes in AST.
     * You may need to remove the `return null` in the skeleton.
     * */
    /*
    public ArrayList<ASTElement> getAllNodes() {

        ArrayList<ASTElement> children = new ArrayList<>();
        for(ASTElement child:this.body){
            children.add(child);
            ASTElement ptr=child;
            while (ptr.getChildren()!=null){
                children.addAll(ptr.getChildren());
                ptr.
            }
            children.addAll(child.getChildren());
        }
        return children;
    }

     */
    public ArrayList<ASTElement> getAllNodes() {
        ArrayList<ASTElement> result = new ArrayList<>();

        for (ASTElement child:this.body) {
            //allNodes.add(child);
            getAllNodesHelper(child, result);
        }
        return result;
    }

    private void getAllNodesHelper(ASTElement node, ArrayList<ASTElement> result) {
        result.add(node);
        List<ASTElement> children = node.getChildren();
        for (ASTElement child : children) {
            getAllNodesHelper(child, result);
        }
    }
}
