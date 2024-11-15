package hk.ust.comp3021.query;

import hk.ust.comp3021.misc.*;
import hk.ust.comp3021.stmt.FunctionDefStmt;
import hk.ust.comp3021.utils.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.*;

public class QueryOnNode {

    private HashMap<String, ASTModule> id2ASTModules;

    public QueryOnNode(HashMap<String, ASTModule> id2ASTModules) {
        this.id2ASTModules = id2ASTModules;
    }

    /**
     * TODO `findFuncWithArgGtN` find all functions whose # arguments > given `paramN` in all modules {@link QueryOnNode#id2ASTModules}
     *
     * @param paramN the number of arguments user expects
     * @return null as PA1, simply print out all functions that satisfy the requirements with format ModuleID_FuncName_LineNo
     * Hints1: use {@link ASTElement#filter(Predicate)} method to implement the function
     */
    public Consumer<Integer> findFuncWithArgGtN =
            (paramN) -> {
            for (String key : id2ASTModules.keySet()) {
                ASTModule module = id2ASTModules.get(key);
                ArrayList<FunctionDefStmt> temp = module.getAllFunctions();
                temp.stream().filter(func -> ((FunctionDefStmt) func).getParamNum() >= paramN)
                            .forEach(func -> System.out.println(module.getASTID() + "_" +
                                    ((FunctionDefStmt) func).getName() + "_" + func.getLineNo()));
                }

        };

    /*
    public void findFuncWithArgGtN(int paramN) {
        for (String key : id2ASTModules.keySet()) {
            ASTModule module = id2ASTModules.get(key);
            for (FunctionDefStmt func : module.getAllFunctions()) {
                if (func.getParamNum() >= paramN) {
                    System.out.println(module.getASTID() + "_" + func.getName() + "_" + func.getLineNo());
                }
            }
        }
    }
     */


    /**
     * TODO `calculateOp2Nums` count the frequency of each operator in all modules {@link QueryOnNode#id2ASTModules}
     *
     * @param null
     * @return op2Num as PA1,the key is operator name, and value is the frequency
     * Hints1: use {@link ASTElement#forEach(Consumer)} method to implement the function
     */
    public Supplier<HashMap<String, Integer>> calculateOp2Nums= () -> {
        HashMap<String, Integer> op2Num = new HashMap<>();

        id2ASTModules.forEach((moduleId, module) -> module.getAllOperators().forEach(element -> {
            if (element instanceof ASTEnumOp) {
                String operatorName = ((ASTEnumOp) element).getOperatorName();
                op2Num.put(operatorName, op2Num.getOrDefault(operatorName, 0) + 1);
            }
        }));
        return op2Num;
    };

    /**
     * TODO `calculateNode2Nums` count the frequency of each node in all modules {@link QueryOnNode#id2ASTModules}
     *
     * @param astID, a number to represent a specific AST or -1 for all
     * @return node2Nums as PA1,the key is node type, and value is the frequency
     * Hints1: use {@link ASTElement#groupingBy(Function, Collector)} method to implement the function
     * Hints2: if astID is invalid, return empty map
     */
    public Function<String, Map<String, Long>> calculateNode2Nums= astID -> {
        if (!isValidASTID(astID)) {
            return Collections.emptyMap();
        }
        int temp = Integer.valueOf(astID);
        Map<String, Long> node2Nums;
        if(temp==-1){
            List<ASTModule> astModuleList =  id2ASTModules.values().stream().toList();
            List<ASTElement> astElements = new ArrayList<>();
            for(ASTModule astModule:astModuleList){
                astElements.add(astModule);
                astElements.addAll(astModule.getAllNodes());
            }
            node2Nums = astElements.stream()
                    .collect(Collectors.groupingBy(ASTElement::getNodeType, Collectors.counting()));
        }else{
            ASTModule module = id2ASTModules.get(astID);
            List<ASTElement> allElements = module.getAllNodes();
            allElements.add(module);
            node2Nums = allElements.stream()
                    .collect(Collectors.groupingBy(ASTElement::getNodeType, Collectors.counting()));

        }


        //return Collections.emptyMap();
        return node2Nums;
    };
    /*
    public HashMap<String, Integer> calculateNode2Nums(String astID) {//????
        HashMap<String, Integer> temp1 = new HashMap<>();
        if (id2ASTModules.containsKey(astID)) {
            ASTModule module = id2ASTModules.get(astID);
            ArrayList<ASTElement> nodes = module.getAllNodes();
            for (ASTElement node : nodes) {
                String nodeName = node.getNodeType();
                if(temp1.containsKey(nodeName)){
                    int count = temp1.get(nodeName);
                    temp1.put(nodeName, count + 1);
                }else{
                    temp1.put(nodeName,1);
                }
            }
        }
        return temp1;

    }
     */
    private boolean isValidASTID(String astID) {
        int temp = Integer.valueOf(astID);

        if(temp>836||temp<-1){
            return false;
        }
        return true;
    }

    /**
     * TODO `processNodeFreq` sort all functions in all modules {@link QueryOnNode#id2ASTModules}
     * based on the number of nodes in FunctionDefStmt subtree
     *
     * @param null@return a list of entries sorted in descending order where the key is function name
     *         with format ModuleID_FuncName_LineNo, and value is the # nodes
     * Hints1: use {@link ASTElement#forEach(Consumer)} method to implement the function
     * Hint2: note that `countChildren` method is removed, please do not use this method
     */
    public Supplier<List<Map.Entry<String, Integer>>> processNodeFreq = () ->{
        List<Map.Entry<String, Integer>> result = new ArrayList<>();

        for (String key : id2ASTModules.keySet()) {
            ArrayList<FunctionDefStmt> temp = new ArrayList<>();
            ASTModule module = id2ASTModules.get(key);
            temp.addAll(module.getAllFunctions());
            for(FunctionDefStmt functionDefStmt:temp){
                AtomicInteger nodeCount= new AtomicInteger(1);//also count itself
                //nodeCount.add(0,0);
                functionDefStmt.forEach(node->{
                    /*
                    int tempCount = nodeCount.get(0);
                    nodeCount.set(0,tempCount+node.getChildren().size());
                     */
                    nodeCount.addAndGet(node.getChildren().size());
                });
                String functionName = module.getASTID() + "_" + functionDefStmt.getName() + "_" + functionDefStmt.getLineNo();
                result.add(new AbstractMap.SimpleEntry<>(functionName, nodeCount.get()));
            }
        }
        result.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return result;
    };

    /*
    public HashMap<String, Integer> processNodeFreq() {
        HashMap<String, Integer> nodeFreq = new HashMap<>();
        for (ASTModule module : id2ASTModules.values()) {
            for (FunctionDefStmt func : module.getAllFunctions()) {
                int numNode;
                numNode = func.countChildren();
                nodeFreq.put(func.getName(), numNode);
            }
        }
        return nodeFreq;
    }
     */

}
