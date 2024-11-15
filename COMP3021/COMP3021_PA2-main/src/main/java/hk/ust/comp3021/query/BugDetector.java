package hk.ust.comp3021.query;

import hk.ust.comp3021.expr.*;
import hk.ust.comp3021.misc.*;
import hk.ust.comp3021.stmt.*;
import hk.ust.comp3021.utils.*;

import java.util.*;
import java.util.function.*;

public class BugDetector {
    ASTModule module = null;

    public BugDetector(ASTModule module) {
        this.module = module;
    }

    /**
     * TODO Returns all the functions that contains a bug of unclosed files in the current module
     * {@link QueryOnClass#module}
     * @return results List of strings of names of the functions
     */
    public Supplier<List<String>> detect=()->{
        List<String> results = new ArrayList<>();
        List<FunctionDefStmt> functions = module.getAllFunctions();
        for (FunctionDefStmt function : functions) {
            boolean hasUnclosedFileBug = checkForUnclosedFileBug(function);
            if (hasUnclosedFileBug) {
                results.add(function.getName());
            }
        }
        return results;
        };

    private boolean checkForUnclosedFileBug(FunctionDefStmt function) {
        ArrayList<ASTElement> allAstElements = function.getChildren();
        ArrayList<CallExpr> callExprs = function.getAllCalledFunc();
        int countOpen=0;
        int countClose=0;
        for(CallExpr callExpr:callExprs){
            for (ASTExpr allArg : callExpr.getAllArgs()) {
                if(allArg instanceof NameExpr){
                    if(((NameExpr) allArg).getId().equals("open")){
                        countOpen++;
                    }
                } else if (allArg instanceof AttributeExpr) {
                    if(((AttributeExpr) allArg).getAttr().equals("close")){
                        countClose++;
                    }
                }
            }
        }
        if(countClose<countOpen){
            return true;
        }
        return false;
    }

}
