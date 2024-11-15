package hk.ust.comp3021.stmt;

import hk.ust.comp3021.expr.*;
import hk.ust.comp3021.misc.*;
import hk.ust.comp3021.utils.*;

public abstract class ASTStmt extends ASTElement {
    enum StmtType {
        FunctionDef, ClassDef, Return, Assign, AugAssign, For, While, If,
        Expr, Break, Continue
    }

    protected StmtType stmtType;

    public ASTStmt(int lineno, int colOffset, int endLineno, int endColOffset, StmtType stmtType) {
        super(lineno, colOffset, endLineno, endColOffset);
        this.stmtType = stmtType;
    }

    public ASTStmt(XMLNode node) {
        super(node);
        this.stmtType = ASTStmt.StmtType.valueOf(node.getTagName());
    }

    @Override
    public String getNodeType() {
        return this.stmtType.name();
    }

    /**
     * Create ASTStmt from the XNL Node based on the tag name
     *
     * @param node: the XML Node from which to generate ASTStmt
     * @return: created ASTStmt
     *
     * You may need to remove the `return null` from the skeleton.
     */
    public static ASTStmt createASTStmt(XMLNode node) {
        // TODO: complete the definition of the method `createASTStmt`
        String name = node.getTagName();

        //ASTStmt stmt;
        return switch (name) {
            case "FunctionDef" -> new FunctionDefStmt(node);
            case "ClassDef" -> new ClassDefStmt(node);
            case "Return" -> new ReturnStmt(node);
            case "Assign" -> new AssignStmt(node);
            case "AugAssign" -> new AugAssignStmt(node);
            case "For" -> new ForStmt(node);
            case "While" -> new WhileStmt(node);
            case "If" -> new IfStmt(node);
            case "Expr" -> new ExprStmt(node);
            case "Break" -> new BreakStmt(node);
            case "Continue" -> new ContinueStmt(node);
            default -> throw new IllegalArgumentException("Invalid tag name: " + name);
        };
        //FunctionDef, ClassDef, Return, Assign, AugAssign, For, While, If,
        //        Expr, Break, Continue
        //return stmt;
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
    public void yourMethod() {
    }
}
