package hk.ust.comp3021.parallel;

import hk.ust.comp3021.query.*;
import hk.ust.comp3021.utils.*;

import java.util.*;
//import java.util.function.Function;

public class QueryWorker implements Runnable {
    public HashMap<String, ASTModule> id2ASTModules;
    public String queryID;
    public String astID;
    public String queryName;
    public Object[] args;
    public int mode;
    private Object result;

    public QueryWorker(HashMap<String, ASTModule> id2ASTModules,
                       String queryID, String astID,
                       String queryName, Object[] args, int mode) {
        this.id2ASTModules = id2ASTModules;
        this.queryID = queryID;
        this.astID = astID;
        this.queryName = queryName;
        this.args = args;
        this.mode = mode;
    }

    public Object getResult() {
        return result;
    }

    public void run() {
        if (mode == 0) {
            runSerial();
        } else if (mode == 1) {
            runParallel();
        } else if (mode == 2) {
            runParallelWithOrder();
        }
    }

    /**
     * TODO: Implement `runSerial` to process current query command and store the results in `result`
     *
     * Hint1: you must invoke the methods in {@link QueryOnNode}, {@link QueryOnMethod} and {@link QueryOnClass}
     * to achieve the query
     */
    private void runSerial() {
        QueryOnNode queryOnNode = new QueryOnNode(id2ASTModules);
        QueryOnClass queryOnClass = new QueryOnClass(id2ASTModules.get(astID));
        QueryOnMethod queryOnMethod =  new QueryOnMethod(id2ASTModules.get(astID));
        switch (queryName){
            case "findFuncWithArgGtN":
                result = queryOnNode.findFuncWithArgGtN.apply((Integer) args[0]);
                break;
            case "calculateOp2Nums":
                result = queryOnNode.calculateOp2Nums.get();
                break;
            case "calculateNode2Nums":
                result = queryOnNode.calculateNode2Nums.apply((String) args[0]);//may wrong, not sure -1 condition
                break;
            case "processNodeFreq":
                result = queryOnNode.processNodeFreq.get();
                break;
            case "findSuperClasses":
                result = queryOnClass.findSuperClasses.apply((String) args[0]);
                break;
            case "haveSuperClass":
                result = queryOnClass.haveSuperClass.apply((String) args[0],(String) args[1]);
                break;
            case "findOverridingMethods":
                result = queryOnClass.findOverridingMethods.get();
                break;
            case "findAllMethods":
                result = queryOnClass.findAllMethods.apply((String) args[0]);
                break;
            case "findClassesWithMain":
                result = queryOnClass.findClassesWithMain.get();
                break;
            case "findEqualCompareInFunc":
                result = queryOnMethod.findEqualCompareInFunc.apply((String) args[0]);
                break;
            case "findFuncWithBoolParam":
                result = queryOnMethod.findFuncWithBoolParam.get();
                break;
            case "findUnusedParamInFunc":
                result = queryOnMethod.findUnusedParamInFunc.apply((String) args[0]);
                break;
            case "findDirectCalledOtherB":
                result = queryOnMethod.findDirectCalledOtherB.apply((String) args[0]);
                break;
            case "answerIfACalledB":
                result = queryOnMethod.answerIfACalledB.test((String) args[0],(String) args[1]);
                break;
            default:
                result = null;
                break;
        }

    }

    /**
     * TODO: Implement `runParallel` to process current query command and store the results in `result` where
     * queryOnNode should be conducted with multiple threads
     *Hint1: you must invoke the methods in {@link QueryOnNode}, {@link QueryOnMethod} and {@link QueryOnClass}
     * to achieve the query
     * Hint2: you can let methods in queryOnNode to work on single AST by changing the arguments when creating
     *{@link QueryOnNode} object
     * Hint3: please use {@link Thread} to achieve multi-threading
     * Hint4: you can invoke {@link QueryWorker#runSerial()} to reuse its logic
     */
    private void runParallel() {
        QueryOnClass queryOnClass = new QueryOnClass(id2ASTModules.get(astID));
        QueryOnMethod queryOnMethod =  new QueryOnMethod(id2ASTModules.get(astID));
        int numTreads = id2ASTModules.keySet().size();
        Thread[] threads = new Thread[numTreads];
        switch (queryName){
            case "findFuncWithArgGtN":
                for(int i=0;i<numTreads;i++){
                    String key = String.valueOf(i);
                    HashMap<String,ASTModule> temp = new HashMap<>();
                    ASTModule astModule = id2ASTModules.get(key);
                    if(astModule==null){
                        continue;
                    }else {
                        temp.put(key, id2ASTModules.get(key));
                    }
                    QueryOnNode queryOnNode = new QueryOnNode(temp);
                    Runnable r = ()-> {
                        queryOnNode.findFuncWithArgGtN.apply((Integer) args[0]);
                    };
                    threads[i]= new Thread(r);
                    threads[i].start();
                }
                break;
            case "calculateOp2Nums":
                HashMap<String,Integer> result=new HashMap<>();
                for(int i=0;i<numTreads;i++){
                    String key = String.valueOf(i);
                    HashMap<String,ASTModule> temp = new HashMap<>();
                    ASTModule astModule = id2ASTModules.get(key);
                    if(astModule==null){
                        continue;
                    }else {
                        temp.put(key, id2ASTModules.get(key));
                    }
                    QueryOnNode queryOnNode = new QueryOnNode(temp);
                    threads[i]=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            result.putAll(queryOnNode.calculateOp2Nums.get());
                        }
                    });
                    threads[i].start();
                }
                this.result=result;
                break;
            case "calculateNode2Nums":
                HashMap<String,Long> resultTwo=new HashMap<>();
                for(int i=0;i<numTreads;i++){
                    String key = String.valueOf(i);
                    HashMap<String,ASTModule> temp = new HashMap<>();
                    ASTModule astModule = id2ASTModules.get(key);
                    if(astModule==null){
                        continue;
                    }else {
                        temp.put(key, id2ASTModules.get(key));
                    }
                    QueryOnNode queryOnNode = new QueryOnNode(temp);
                    threads[i]=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            resultTwo.putAll(
                                    queryOnNode.calculateNode2Nums.apply(String.valueOf(args[0])));
                        }
                    });
                    threads[i].start();
                }
                this.result=resultTwo;
                break;
            case "processNodeFreq":
                List<Map.Entry<String, Integer>> resultThree=new ArrayList<>();
                for(int i=0;i<numTreads;i++){
                    String key = String.valueOf(i);
                    HashMap<String,ASTModule> temp = new HashMap<>();
                    ASTModule astModule = id2ASTModules.get(key);
                    if(astModule==null){
                        continue;
                    }else {
                        temp.put(key, id2ASTModules.get(key));
                    }
                    QueryOnNode queryOnNode = new QueryOnNode(temp);
                    threads[i]=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            resultThree.addAll(queryOnNode.processNodeFreq.get());
                        }
                    });
                    threads[i].start();
                }
                this.result=resultThree;
                break;
            case "findSuperClasses":
                this.result = queryOnClass.findSuperClasses.apply((String) args[0]);
                break;
            case "haveSuperClass":
                this.result = queryOnClass.haveSuperClass.apply((String) args[0],(String) args[1]);
                break;
            case "findOverridingMethods":
                this.result = queryOnClass.findOverridingMethods.get();
                break;
            case "findAllMethods":
                this.result = queryOnClass.findAllMethods.apply((String) args[0]);
                break;
            case "findClassesWithMain":
                this.result = queryOnClass.findClassesWithMain.get();
                break;
            case "findEqualCompareInFunc":
                this.result = queryOnMethod.findEqualCompareInFunc.apply((String) args[0]);
                break;
            case "findFuncWithBoolParam":
                this.result = queryOnMethod.findFuncWithBoolParam.get();
                break;
            case "findUnusedParamInFunc":
                this.result = queryOnMethod.findUnusedParamInFunc.apply((String) args[0]);
                break;
            case "findDirectCalledOtherB":
                this.result = queryOnMethod.findDirectCalledOtherB.apply((String) args[0]);
                break;
            case "answerIfACalledB":
                this.result = queryOnMethod.answerIfACalledB.test((String) args[0],(String) args[1]);
                break;
            default:
                this.result = null;
                break;
        }
        for (Thread thread : threads) {
            if(thread!=null) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


        /**
         * TODO: Implement `runParallelWithOrder` to process current query command and store the results in `result` where
         * the current query should wait until the prerequisite has been computed
         *
         * Hint1: you must invoke the methods in {@link QueryOnNode}, {@link QueryOnMethod} and {@link QueryOnClass}
         * to achieve the query
         * Hint2: you can invoke {@link QueryWorker#runParallel()} to reuse its logic
         * Hint3: please use {@link Thread} to achieve multi-threading
         * Hint4: you can add new methods or fields in current class
         */
    private void runParallelWithOrder() {
        runParallel();
    }

}
