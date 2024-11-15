package hk.ust.comp3021.expr;

import hk.ust.comp3021.misc.*;
import hk.ust.comp3021.utils.*;
import java.util.*;

public class CallExpr extends ASTExpr {
    // Call(expr func, expr* args, keyword* keywords)
    private ASTExpr func;
    private ArrayList<ASTExpr> args = new ArrayList<>();
    private ArrayList<ASTKeyWord> keywords = new ArrayList<>();

    public CallExpr(XMLNode node) {
        // TODO: complete the definition of the constructor. Define the class as the subclass of ASTExpr.
        super(node);
        this.exprType = ExprType.Call;
        XMLNode funcNode = node.getChildByIdx(0);
        this.func = ASTExpr.createASTExpr(funcNode);
        for(int i=0;i< node.getChildByIdx(1).getChildren().size();i++){
            XMLNode node1 = node.getChildByIdx(1).getChildByIdx(i);
            args.add(ASTExpr.createASTExpr(node1));
        }
        for(int i=0;i< node.getChildByIdx(2).getChildren().size();i++){
            XMLNode node1 = node.getChildByIdx(2).getChildByIdx(i);
            keywords.add(new ASTKeyWord(node1));
        }
    }

    /*
     * Find all paths from func node to node with class type Name, which contain several cases
     * (1) if the path is func -> Attribute (attr: b) -> Name (id: self), then the name is self.b
     * (2) if the path is func -> Attribute (attr: b) -> Attribute (attr: a) -> Name (id: self), then the name is self.a.b
     * (3) if the path is func -> Name (id: bubbleSort), then the name is bubbleSort
     * @return: name of called function
     */
    public String getCalledFuncName() {
        // TODO: complete the definition of the method `getCalledFuncName`
        StringBuilder funcName = new StringBuilder();
        if(func.exprType==ExprType.Name){
            NameExpr nameExpr = (NameExpr) func;
            funcName.append(nameExpr.getId());
            return funcName.toString();
        } else if (func.exprType==ExprType.Attribute) {
            AttributeExpr attributeExpr = (AttributeExpr) func;
            if(attributeExpr.getValue().exprType==ExprType.Name){
                NameExpr nameExpr = (NameExpr) attributeExpr.getValue();
                funcName.append(nameExpr.getId()).append(".").append(attributeExpr.getNodeType());
            }else if(attributeExpr.getValue().exprType==ExprType.Attribute){
                AttributeExpr attributeExpr1 = (AttributeExpr) attributeExpr.getValue();
                NameExpr nameExpr1 = (NameExpr) attributeExpr1.getValue();
                String temp = attributeExpr1.getNodeType();
                String temp2 = attributeExpr.getValue().getNodeType();
                funcName.append(nameExpr1.getId()).append(".").append(temp).append(".").append(temp2);
            }
        }
        return funcName.toString();
    }

    @Override
    public ArrayList<ASTElement> getChildren() {
        // TODO: complete the definition of the method `getChildren`
        ArrayList<ASTElement> children = new ArrayList<>();
        children.add(func);
        children.addAll(args);
        children.addAll(keywords);
        //children.add(op);
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
