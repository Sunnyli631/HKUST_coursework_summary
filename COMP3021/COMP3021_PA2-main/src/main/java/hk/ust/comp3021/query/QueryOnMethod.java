package hk.ust.comp3021.query;

import hk.ust.comp3021.expr.*;
import hk.ust.comp3021.misc.*;
import hk.ust.comp3021.stmt.*;
import hk.ust.comp3021.utils.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;
import java.util.stream.Stream;

public class QueryOnMethod {
    /**
     * IMPORTANT: for all test cases for QueryOnMethod, we would not involve class
     */
    ASTModule module = null;

    public QueryOnMethod(ASTModule module) {
        this.module = module;
    }

    /**
     * TODO `findEqualCompareInFunc` find all comparison expression with operator \"==\" in current module {@link QueryOnMethod#module}
     *
     * @param funcName the name of the function to be queried
     * @return results List of strings where each represents a comparison expression, in format, lineNo:colOffset-endLineNo:endColOffset
     * Hints1: if func does not exist in current module, return empty list
     * Hints2: use {@link ASTElement#filter(Predicate)} method to implement the function
     */
    public Function<String, List<String>> findEqualCompareInFunc = (funcName) -> {
        List<String> result = new ArrayList<>();
        for (ASTElement astElement : module.getAllNodes()) {
            AtomicReference<String> temp = new AtomicReference<>();
            astElement.filter(func -> func instanceof CompareExpr)
                    .forEach(answer -> {
                        CompareExpr compareExpr = (CompareExpr) answer;
                        for(ASTEnumOp compareExpr1:compareExpr.getOps()){
                            if(compareExpr1.getOperatorName().equals("Eq")){
                                String template = compareExpr.getLineNo()+":"+compareExpr.getColOffset()
                                        +"-"+compareExpr.getEndLineNo()+":"+compareExpr.getEndColOffset();
                                temp.set(template);
                            }
                        }
                    });
            if (temp.get()!=null) {
                result.add(temp.get());
            }

        }
        return result;
    };

    /**
     * TODO `findFuncWithBoolParam` find all functions that use boolean parameter
     * as if condition in current module {@link QueryOnMethod#module}
     * @param null
     * @return List of strings where each represents the name of function that satisfy the requirements
     * Hints1: the boolean parameter is annotated with type bool
     * Hints2: as long as the boolean parameter shown in the {@link IfStmt#getTest()} expression, we say it's used
     * Hints3: use {@link ASTElement#filter(Predicate)} method to implement the function
     */
    public Supplier<List<String>> findFuncWithBoolParam = ()->{
        List<String> result = new ArrayList<>();
        for (ASTElement astElement : module.getAllNodes()) {
            astElement.filter(func -> func instanceof FunctionDefStmt)
                    .forEach(answer -> {
                        ArrayList<ASTElement> astElements=((FunctionDefStmt)answer).getChildren();
                        astElements.add(answer);
                        for(ASTElement astElement1:astElements){
                            if (astElement1 instanceof ASTArguments){
                                for(ASTElement child:astElement1.getChildren()) {
                                    if (child instanceof ASTArguments.ASTArg) {
                                        if(((NameExpr)((ASTArguments.ASTArg) child).getAnnotation()).getId().equals("bool")){
                                            String temp= ((ASTArguments.ASTArg) child).getArg();
                                            for (ASTElement astElement2 : astElements) {
                                                if (astElement2 instanceof IfStmt) {
                                                    if(((NameExpr)((IfStmt) astElement2).getTest()).getId().equals(temp)) {
                                                        result.add(((FunctionDefStmt) astElement).getName());
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }

                    });
        }
        return result;
    };


    /**
     * TODO Given func name `funcName`, `findUnusedParamInFunc` find all unused parameter in current module {@link QueryOnMethod#module}
     *
     * @param funcName to be queried function name
     * @return results List of strings where each represents the name of an unused parameter
     * Hints1: if a variable is read, the ctx is `Load`, otherwise `Store` if written
     * Hints2: for the case where variable is written before read, we use line number and col offset to
     * check if the write operation is conducted before the first place where the parameter is read
     * Hints3: use {@link ASTElement#filter(Predicate)} method to implement the function
     * Hints4: if func does not exist in current module, return empty list
     */
    //@SuppressWarnings("unchecked")
    public Function<String, List<String>> findUnusedParamInFunc = (funcName)->{
        List<String> result = new ArrayList<>();
        List<String> listOfUsedPara = new ArrayList<>();
        List<String> allPara = new ArrayList<>();
        List<FunctionDefStmt> allFunc = module.getAllFunctions();
        FunctionDefStmt requiredFunc = null;
        for(FunctionDefStmt func:allFunc){
            if(func.getName().equals(funcName)){
                requiredFunc=func;
            }
        }
        if(requiredFunc==null){
            return Collections.emptyList();
        }else {
            List<ASTArguments.ASTArg> argList = requiredFunc.getArgs().getArgs();
            for (ASTArguments.ASTArg arg:argList){
                allPara.add(arg.getArg());
            }
            Stream<ASTStmt> exprStmtArrayList=requiredFunc.getBody()
                    .stream().filter(func -> func instanceof ExprStmt);
            List<ASTStmt> exprStmtArray= exprStmtArrayList.toList();
            Stream<ASTStmt> assignStmtList=requiredFunc.getBody()
                    .stream().filter(func -> func instanceof AssignStmt);
            List<ASTStmt> assignStmtArray= assignStmtList.toList();

            List<NameExpr> assignName = new ArrayList<>();
            List<NameExpr> exprName = new ArrayList<>();
            List<String> paraInAssign = new ArrayList<>();
            List<String> paraInExpr = new ArrayList<>();
            for(ASTStmt astStmt:assignStmtArray){
                //assignStmts.add((AssignStmt) astStmt);
                for (ASTElement child : ((AssignStmt) astStmt).getChildren()) {
                    if(child instanceof NameExpr&& ((NameExpr) child).getCtx().getNodeType().equals("Ctx_Store")){
                        paraInAssign.add(((NameExpr) child).getId());
                        assignName.add((NameExpr) child);
                    }
                }

            }
            for(ASTStmt astStmt:exprStmtArray){
                //ExprStmts.add((ExprStmt) astStmt);
                for (ASTElement child : ((ExprStmt) astStmt).getChildren()) {
                    if(child instanceof CallExpr){
                        for (ASTExpr allArg : ((CallExpr) child).getAllArgs()) {
                            //String temp = ((NameExpr) allArg).getCtx().getOperatorName();
                            if(allArg instanceof NameExpr&&((NameExpr) allArg).getCtx().getNodeType().equals("Ctx_Load")){
                                paraInExpr.add(((NameExpr) allArg).getId());
                                exprName.add((NameExpr) allArg);
                            }
                        }

                    }
                }
            }

            for (ASTArguments.ASTArg arg:argList){
                String temp = arg.getArg();
                if(paraInExpr.contains(temp)&&!paraInAssign.contains(temp)){
                    listOfUsedPara.add(temp);
                }else if(paraInExpr.contains(temp)&&paraInAssign.contains(temp)){
                    NameExpr callExpr = null;
                    NameExpr assignExpr = null;
                    for(NameExpr child:exprName){
                        if(child.getId().equals(temp)){
                            callExpr=child;
                        }
                    }
                    for(NameExpr child:assignName){
                        if(child.getId().equals(temp)){
                            assignExpr=child;
                        }
                    }
                    if(callExpr!=null&&assignExpr!=null) {
                        if (callExpr.getLineNo() < assignExpr.getLineNo()) {
                            listOfUsedPara.add(temp);
                        } else if ((callExpr.getLineNo() == assignExpr.getLineNo())
                                && callExpr.getColOffset() < assignExpr.getColOffset()) {
                            listOfUsedPara.add(temp);
                        }else{
                            result.add(temp);
                        }
                    }
                }else {
                    result.add(temp);
                }
            }
        }



        return result;
    };


    /**
     * TODO Given func name `funcName`, `findDirectCalledOtherB`
     * find all functions being direct called by functions other than B in current module {@link QueryOnMethod#module}
     * @param funcName the name of function B
     * @return results List of strings where each represents the name of a function that satisfy the requirement
     * Hints1: there is no class in the test cases for this code pattern, thus, no function names such as a.b()
     * Hints2: for a call expr foo(), we can directly use the called function name foo to location the implementation
     * Hints3: use {@link ASTElement#filter(Predicate)} method to implement the function
     * Hints4: if func does not exist in current module, return empty list
     */
    public Function<String, List<String>> findDirectCalledOtherB = (funcName) -> {
        List<String> results = new ArrayList<>();

        Stream<FunctionDefStmt> func = module.getAllFunctions().stream();
        List<FunctionDefStmt> allFunc = func.toList();
        Stream<FunctionDefStmt> funcWithoutB = module.getAllFunctions().stream()
                .filter(node ->!node.getName().equals(funcName));
        List<FunctionDefStmt> allFuncWithoutB = funcWithoutB.toList();
        if (!allFuncWithoutB.isEmpty()) {
            for(FunctionDefStmt functionDefStmt:allFuncWithoutB) {
                //ArrayList<ASTElement> temp = new ArrayList<>();
                ArrayList<CallExpr> directCalls = new ArrayList<>();
                for(ASTElement astElement:functionDefStmt.getChildren()){
                    if (astElement instanceof ExprStmt){
                        if(((ExprStmt) astElement).getValue() instanceof CallExpr){
                            CallExpr temp= (CallExpr) ((ExprStmt) astElement).getValue();
                            directCalls.add(temp);
                        }
                    }
                }
                for (CallExpr directCall : directCalls) {
                    for(ASTExpr astExpr:directCall.getAllArgs()){
                        if(astExpr instanceof NameExpr){
                            String functionName = ((NameExpr) astExpr).getId();
                            for(FunctionDefStmt functionDefStmt1:allFunc) {
                                if (functionName.equals(functionDefStmt1.getName())&&!results.contains(functionName)) {
                                    results.add(functionName);
                                }
                            }
                        }
                    }
                }
            }
        }

        return results;
    };

    /**
     * TODO Given func name `funcNameA` and `funcNameB`,
     * `answerIfACalledB` checks if A calls B directly or transitively in current module {@link QueryOnMethod#module}
     * @param funcNameA the name of function A
     * @param funcNameB the name of function B
     * @return a boolean return value to answer yes or no
     * Hints1: there is no class in the test cases for this code pattern, thus, no function names such as a.b()
     * Hints2: for a call expr foo(), we can directly use the called function name foo to location the implementation
     * Hints3: use {@link ASTElement#filter(Predicate)} method to implement the function
     */

    public BiPredicate<String, String> answerIfACalledB = (funcNameA, funcNameB) -> {
        return isFunctionCalledRecursively(funcNameA,funcNameB);
    };;

    public boolean isFunctionCalledRecursively(String funcNameA, String funcNameB) {
        Stream<FunctionDefStmt> allFunc = module.getAllFunctions().stream();
        List<FunctionDefStmt> fFunc = allFunc.toList();
        Stream<FunctionDefStmt> fFunctionA = module.getAllFunctions().stream()
                .filter(func -> func.getName().equals(funcNameA));
        List<FunctionDefStmt> funcA = fFunctionA.toList();

        if (!funcA.isEmpty()) {
            for (FunctionDefStmt functionA : funcA) {
                ArrayList<CallExpr> directCalls = new ArrayList<>();
                for(ASTElement astElement:functionA.getChildren()){
                    if (astElement instanceof ExprStmt){
                        if(((ExprStmt) astElement).getValue() instanceof CallExpr){
                            CallExpr temp= (CallExpr) ((ExprStmt) astElement).getValue();
                            directCalls.add(temp);
                        }
                    }
                }
                for (CallExpr directCall : directCalls) {
                    for (ASTExpr astExpr : directCall.getAllArgs()) {
                        if (astExpr instanceof NameExpr) {
                            String temp = ((NameExpr) astExpr).getId();
                            if(temp.equals(funcNameB)){
                                return true;
                            }
                            //answerIfACalledB.test(((NameExpr) astExpr).getId(),funcNameB);
                            if(isFunctionCalledRecursively(temp,funcNameB)){
                                return true;
                            }
                        }
                    }

                }

            }
        }

        return false;
    }

}
