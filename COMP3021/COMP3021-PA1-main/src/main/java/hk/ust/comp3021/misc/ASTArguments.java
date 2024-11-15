package hk.ust.comp3021.misc;

import hk.ust.comp3021.expr.*;
import hk.ust.comp3021.utils.*;
import java.util.*;

public class ASTArguments extends ASTElement {
    public class ASTArg extends ASTElement {
        /*
         * arg = (identifier arg, expr? annotation, ...)
         *       attributes (int lineno, int colOffset, int? endLineno, int? endColOffset)
         */
        private String arg;
        private ASTExpr annotation;

        public ASTArg(XMLNode node) {
            // TODO: complete the definition of the constructor. Define the class as the subclass of ASTElement.
            super(node);

            this.arg = node.getAttribute("arg");
            if (!node.hasAttribute("annotation")) {
                this.annotation = ASTExpr.createASTExpr(node.getChildByIdx(0));
            } else annotation = null;
        }

        @Override
        public ArrayList<ASTElement> getChildren() {
            // TODO: complete the definition of the method `getChildren`
            ArrayList<ASTElement> children = new ArrayList<>();
            //children.addAll(elts);
            children.add(annotation);
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

        @Override
        public String getNodeType() {
            return "arg";
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
    /*
     * arguments = (.., arg* args, ..., expr* defaults)
     */

    private ArrayList<ASTArg> args = new ArrayList<>();
    private ArrayList<ASTExpr> defaults = new ArrayList<>();

    public ASTArguments(XMLNode node) {
        // TODO: complete the definition of the constructor. Define the class as the subclass of ASTElement.
        super(node);
        //this.exprType = ASTExpr.ExprType.Name;
        //XMLNode value_node = node.getChildByIdx(0);
        XMLNode argsNode = node.getChildByIdx(1);
        XMLNode defaultsNode = node.getChildByIdx(node.getNumChildren()-1);
        //XMLNode node1 = node.getChildByIdx(0).getChildByIdx(i);
        if(argsNode.getNumChildren()!=0){
            List<XMLNode> temp=argsNode.getChildren();
            for(XMLNode temp1:temp) {
                args.add(new ASTArg(temp1));
            }
        }
        //XMLNode node1 = node.getChildByIdx(1).getChildByIdx(i);
        if(defaultsNode.getNumChildren()!=0){
            List<XMLNode> temp=defaultsNode.getChildren();
            for(XMLNode temp1:temp) {
                defaults.add(ASTExpr.createASTExpr(temp1));
            }
        }
        /*if(defaultsNode.getNumChildren()!=0) {
            defaults.add(ASTExpr.createASTExpr(defaultsNode));
        }*/
        //this.kind = kind_node.getTagName();
    }


    /*
    * Return the number of ASTArg child nodes
    */
    public int getParamNum() {
        // TODO: complete the definition of the method `getParamNum`
        ArrayList<ASTElement> elements = new ArrayList<>();
        elements.addAll(args);
        elements.addAll(defaults);
        int count=0;
        for(ASTElement temp:elements){
            if(temp instanceof ASTArg){
                count++;
            }
        }
        return count;
    }

    @Override
    public ArrayList<ASTElement> getChildren() {
        // TODO: complete the definition of the method `getChildren`
        ArrayList<ASTElement> children = new ArrayList<>();
        //children.addAll(elts);
        children.addAll(args);
        children.addAll(defaults);
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

    @Override
    public String getNodeType() {
        return "arguments";
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
