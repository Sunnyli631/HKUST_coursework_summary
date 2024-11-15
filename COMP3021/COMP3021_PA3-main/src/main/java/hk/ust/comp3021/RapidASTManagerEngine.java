package hk.ust.comp3021;

import hk.ust.comp3021.parallel.*;
import hk.ust.comp3021.utils.*;
import java.util.concurrent.*;
import java.util.*;


public class RapidASTManagerEngine {
    private final HashMap<String, ASTModule> id2ASTModules = new HashMap<>();
    private final List<Object> allResults = new ArrayList<>();

    public HashMap<String, ASTModule> getId2ASTModule() {
        return id2ASTModules;
    }

    public List<Object> getAllResults() {
        return allResults;
    }

    /**
     * TODO: Implement `processXMLParsingPool` to load a list of XML files in parallel
     *
     * @param xmlDirPath the directory of XML files to be loaded
     * @param xmlIDs a list of XML file IDs
     * @param numThread the number of threads you are allowed to use
     *
     * Hint1: you can use thread pool {@link ExecutorService} to implement the method
     * Hint2: you can use {@link ParserWorker#run()}
     */
    public void processXMLParsingPool(String xmlDirPath, List<String> xmlIDs, int numThread) {
        ExecutorService executor = Executors.newFixedThreadPool(numThread);
        for (String xmlID : xmlIDs) {
            ParserWorker worker = new ParserWorker(xmlID,xmlDirPath,id2ASTModules);
            executor.execute(worker);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            int count=0;
        }
    }
    /**
     * TODO: Implement `processXMLParsingDivide` to load a list of XML files in parallel
     *
     * @param xmlDirPath the directory of XML files to be loaded
     * @param xmlIDs a list of XML file IDs
     * @param numThread the number of threads you are allowed to use
     *
     * Hint1: you can **only** use {@link Thread} to implement the method
     * Hint2: you can use {@link ParserWorker#run()}
     * Hint3: please distribute the files to be loaded for each thread manually and try to achieve high efficiency
     */
    public void processXMLParsingDivide(String xmlDirPath, List<String> xmlIDs, int numThread) {
        Thread[] threads = new Thread[numThread];
        int numFiles = xmlIDs.size();
        int filesPerThread=numFiles/numThread;
        int remainingFiles;
        if(numFiles>numThread){
            remainingFiles = numFiles % numThread;
        }else{
            remainingFiles=0;
        }
        int startIndex=0;
        int endIndex=0;
        for (int i = 0; i < numThread; i++) {
            endIndex = startIndex+filesPerThread;
            if(remainingFiles>0){
                endIndex++;
                remainingFiles--;
            }
            for(int j=startIndex;j<endIndex;j++) {
                ParserWorker worker = new ParserWorker(xmlIDs.get(j), xmlDirPath, id2ASTModules);
                threads[i] = new Thread(worker);
                //threads[i].start();
                threads[i].run();
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            startIndex=endIndex;
        }
        for(int i=0;i<numThread;i++){
            if(threads[i]!=null){
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * TODO: Implement `processCommands` to conduct a list of queries on ASTs based on execution mode
     *
     * @param commands a list of queries, you can refer to test cases to learn its format
     * @param executionMode mode 0 to mode 2
     *
     * Hint1: you need to invoke {@link RapidASTManagerEngine#executeCommandsSerial(List)}
     * {@link RapidASTManagerEngine#executeCommandsParallel(List)}
     *                      and {@link RapidASTManagerEngine#executeCommandsParallelWithOrder(List)}
     */
    public List<Object> processCommands(List<Object[]> commands, int executionMode) {
        List<QueryWorker> workers = new ArrayList<>();
        for(Object[] objects:commands){
            workers.add(new QueryWorker(id2ASTModules, (String) objects[0],
                    (String) objects[1], (String) objects[2], (Object[]) objects[3],executionMode));
        }
        if (executionMode == 0) {
            executeCommandsSerial(workers);
        } else if (executionMode == 1) {
            executeCommandsParallel(workers);
        } else if (executionMode == 2) {
            executeCommandsParallelWithOrder(workers);
        }
        return allResults;
    }

    /**
     * TODO: Implement `executeCommandsSerial` to handle a list of `QueryWorker`
     *
     * @param workers a list of workers that should be executed sequentially
     */
    private void executeCommandsSerial(List<QueryWorker> workers) {
        List<Object> results = new ArrayList<>();
        for(QueryWorker worker:workers){
           worker.run();
           results.add(worker.getResult());
        }
        allResults.addAll(results);
    }

    /**
     * TODO: Implement `executeCommandsParallel` to handle a list of `QueryWorker`
     *
     * @param workers a list of workers that should be executed in parallel
     * Hint1: you can **only** use {@link Thread} to implement the method
     * Hint2: you can use unlimited number of threads
     */
    private void executeCommandsParallel(List<QueryWorker> workers) {
        List<Object> results = new ArrayList<>();
        Thread[] threads = new Thread[workers.size()];

        for(int i=0;i< workers.size();i++){
            threads[i] = new Thread(workers.get(i));
            //threads[i].run();
            //results.add(workers.get(i).getResult());
        }
        for(int i=0;i< workers.size();i++){
            //threads[i] = new Thread(workers.get(i));
            threads[i].run();
            results.add(workers.get(i).getResult());
        }

        for (Thread thread : threads) {
            try {
                //System.out.println(Thread.currentThread().getName());
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        allResults.addAll(results);
    }

    /**
     * TODO: Implement `executeCommandsParallelWithOrder` to handle a list of `QueryWorker`
     *
     * @param workers a list of workers that should be executed in parallel with correct order
     *
     * Hint1: you can invoke {@link RapidASTManagerEngine#executeCommandsParallel(List)} to reuse its logic
     * Hint2: you can use unlimited number of threads
     * Hint3: please design the order of queries running in parallel based on the calling dependence of method
     *                in queryOnClass
     */
    private void executeCommandsParallelWithOrder(List<QueryWorker> workers) {
       List<QueryWorker> newWorkers = new ArrayList<>();
       for(QueryWorker worker:workers){
           if(worker.queryName.equals("findSuperClasses")){
               newWorkers.add(worker);
           }
       }
       for(QueryWorker worker:workers){
           if(worker.queryName.equals("findAllMethods")){
               newWorkers.add(worker);
           } else if(worker.queryName.equals("haveSuperClass")){
               newWorkers.add(worker);
           } else if(worker.queryName.equals("findOverridingMethods")){
               newWorkers.add(worker);
           }
       }
       for(QueryWorker worker:workers){
           if(worker.queryName.equals("findClassesWithMain")){
               newWorkers.add(worker);
           }
       }
       executeCommandsParallel(newWorkers);
    }
    /*
    class
    * findSuperClasses
    * haveSuperClass
    * findOverridingMethods
    * findAllMethods
    * findClassesWithMain
     */
    /**
     * TODO: Implement `processCommandsInterLeaved` to handle a list of commands
     * @param commands a list of import and query commands that should be executed in parallel
     * Hint1: you can **only** use {@link Thread} to create threads
     * Hint2: you can use unlimited number of threads
     * Hint3: please design the order of commands, where for specific ID, AST load should be executed before query
     * Hint4: threads would write into/read from {@link RapidASTManagerEngine#id2ASTModules} at the same time, please
     *synchronize them carefully
     * Hint5: you can invoke {@link QueryWorker#run()} and {@link ParserWorker#run()}
     * Hint6: order of queries should be consistent to that in given commands, no need to consider
     *redundant computation now
     */
    public List<Object> processCommandsInterLeaved(List<Object[]> commands) {
        List<Object> result = new ArrayList<>();
        Thread[] threads = new Thread[20];
        List<Object[]> newCommands = new ArrayList<>();
        List<Integer> count = new ArrayList<>();
        for(Object[] objects:commands) {
            if (objects[2].equals("processXMLParsing")) {
                newCommands.add(objects);
                count.add(Integer.valueOf((String) objects[1]));
                //commands.remove(objects);
            }

        }
        for(Object[] command:commands){
            if(count.contains(Integer.valueOf((String) command[1]))&&!"processXMLParsing".equals((String) command[2])){
                newCommands.add(command);
            }
        }
        for(Object[] objects:newCommands){
            if(objects[2].equals("processXMLParsing")){
                int index = Integer.parseInt(objects[1].toString().trim());
                Object[] dirPath = (Object[]) objects[3];
                threads[index] =
                        new Thread(new ParserWorker((String) objects[1], dirPath[0].toString(),id2ASTModules));
                threads[index].run();//error
            }else{
                int index = Integer.parseInt(objects[1].toString().trim());
                try {
                    //if(objects[1].toString())
                    threads[index].join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                QueryWorker queryWorker = new QueryWorker(id2ASTModules, (String) objects[0],
                        (String) objects[1], (String) objects[2], (Object[]) objects[3],2);
                threads[index] =
                        new Thread(queryWorker);
                threads[index].run();
                result.add(queryWorker.getResult());
            }
        }
        allResults.addAll(result);
        return allResults;
    }


    /**
     * TODO: Implement `processCommandsInterLeavedTwoThread` to handle a list of commands
     *
     * @param commands a list of import and query commands that should be executed in parallel
     * Hint1: you can **only** use {@link Thread} to create threads
     * Hint2: you can only use two threads, one for AST load, another for query
     * Hint3: please design the order of commands, where for specific ID, AST load should be executed before query
     * Hint4: threads would write into/read from {@link RapidASTManagerEngine#id2ASTModules} at the same time, please
     *synchronize them carefully
     * Hint5: you can invoke {@link QueryWorker#run()} and {@link ParserWorker#run()}
     * Hint6: order of queries should be consistent to that in given commands, no need to consider
     *redundant computation now
     */
    public List<Object> processCommandsInterLeavedTwoThread(List<Object[]> commands) {
        List<Object> result = new ArrayList<>();
        Thread[] threads = new Thread[2];
        List<Object[]> newCommands = new ArrayList<>();
        List<Integer> count = new ArrayList<>();
        for(Object[] objects:commands) {
            if (objects[2].equals("processXMLParsing")) {
                newCommands.add(objects);
                count.add(Integer.valueOf((String) objects[1]));
                //commands.remove(objects);
            }

        }
        for(Object[] command:commands){
            if(count.contains(Integer.valueOf((String) command[1]))&&!"processXMLParsing".equals((String) command[2])){
                newCommands.add(command);
            }
        }
        for(Object[] objects:newCommands){
            if(objects[2].equals("processXMLParsing")){
                //int index = Integer.parseInt(objects[1].toString().trim());
                Object[] dirPath = (Object[]) objects[3];
                threads[0] =
                        new Thread(new ParserWorker((String) objects[1], dirPath[0].toString(),id2ASTModules));
                threads[0].run();
                try {
                    threads[0].join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else{
                try {
                    //int index = Integer.parseInt(objects[1].toString().trim());
                    //if(objects[1].toString())
                    threads[0].join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                QueryWorker queryWorker = new QueryWorker(id2ASTModules, (String) objects[0],
                        (String) objects[1], (String) objects[2], (Object[]) objects[3],2);
                threads[1] = new Thread(queryWorker);
                threads[1].run();
                result.add(queryWorker.getResult());
            }
        }
        allResults.addAll(result);
        return allResults;
    }

    /**
     * TODO: (Bonus) Implement `processCommandsInterLeavedTwoThread` to handle a list of commands
     *@param commands a list of import and query commands that should be executed in parallel
     *@param numThread number of threads you are allowed to use
     *Hint1: you can only distribute commands on your need
     *Hint2: please design the order of commands, where for specific ID, AST load should be executed before query
     *Hint3: threads would write into/read from {@link RapidASTManagerEngine#id2ASTModules} at the same time, please
     *synchronize them carefully
     *Hint4: you can invoke {@link QueryWorker#run()} and {@link ParserWorker#run()}
     */
    public List<Object> processCommandsInterLeavedFixedThread(List<Object[]> commands, int numThread) {
        // TODO: Bonus: interleaved parsing and query with given number of threads
        // TODO: separate parser tasks and query tasks with the goal of efficiency
        List<Object> result = new ArrayList<>();
        Thread[] threads = new Thread[numThread];
        List<Object[]> newCommands = new ArrayList<>();
        List<Integer> count = new ArrayList<>();
        List<String> countID = new ArrayList<>();
        List<Object[]> sortCommandList = new ArrayList<>();

        List<List<Object[]>> commandList = new ArrayList<>();
        for(Object[] command:commands){
            if(command[2].equals("processXMLParsing")){
                sortCommandList.add(command);
            }
        }
        for(Object[] command:commands){
            if(!command[2].equals("processXMLParsing")){
                sortCommandList.add(command);
            }
        }
        for(Object[] objects:commands) {
            if(!countID.contains(((String) objects[1]))) {
                countID.add(((String) objects[1]));
            }
        }


        int threadCount =0;
        int prevThreadCount = numThread-1;
        for(Object[] objects1:sortCommandList){
            if(objects1[2].equals("processXMLParsing")){
                //int index = Integer.parseInt(objects[1].toString().trim());
                Object[] dirPath = (Object[]) objects1[3];
                threads[threadCount] =
                        new Thread(new ParserWorker((String) objects1[1], dirPath[0].toString(),id2ASTModules));
                threads[threadCount].run();
                try {
                    threads[threadCount].join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else{
                try {
                    //int index = Integer.parseInt(objects[1].toString().trim());
                    //if(objects[1].toString())
                    //threads[prevThreadCount].join();
                    threads[threadCount].join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                QueryWorker queryWorker = new QueryWorker(id2ASTModules, (String) objects1[0],
                        (String) objects1[1], (String) objects1[2], (Object[]) objects1[3],2);
                threads[threadCount] = new Thread(queryWorker);
                threads[threadCount].run();
                result.add(queryWorker.getResult());
            }
            threadCount++;
            threadCount = threadCount % numThread;
            prevThreadCount++;
            prevThreadCount = prevThreadCount % numThread;
        }



        allResults.addAll(result);
        return allResults;
    }
}
