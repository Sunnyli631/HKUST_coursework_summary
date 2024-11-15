package hk.ust.comp3021.query;

import hk.ust.comp3021.expr.*;
import hk.ust.comp3021.misc.*;
import hk.ust.comp3021.stmt.*;
import hk.ust.comp3021.utils.*;

import java.util.*;
import java.util.function.*;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;

public class QueryOnClass {

    ASTModule module = null;

    public QueryOnClass(ASTModule module) {
        this.module = module;
    }
    /**
     * TODO Given class name `className`, `findSuperClasses` finds all the super classes of
     *  it in the current module {@link QueryOnClass#module}
     * @param className the name of class
     * @return results List of strings where each represents the name of a class that satisfy the requirement
     * Hint1: you can implement a helper function which receives the class name and
     * returns the ClassDefStmt object.
     * Hint2: You can first find the direct super classes, and then RECURSIVELY finds the
     * super classes of the direct super classes.
     */
    public Function<String, List<String>> findSuperClasses = (className) -> {
        List<String> superClasses = new ArrayList<>();
        findSuperClassesRecursively(className, superClasses,1);
        return superClasses;
    };

    private void findSuperClassesRecursively(String className, List<String> superClasses,int mode) {
        ArrayList<ASTElement> cClasses = module.getAllNodes();
        List<ClassDefStmt> classDefStmt = new ArrayList<>();
        for(ASTElement classes: cClasses){
            if(classes instanceof ClassDefStmt){
                classDefStmt.add((ClassDefStmt) classes);
            }
        }
        List<ClassDefStmt> neededClassName = new ArrayList<>();
        for(ClassDefStmt classes: classDefStmt){
            if(classes.getName().equals(className)){
                neededClassName.add(classes);
            }
        }

        if (!neededClassName.isEmpty()) {
            for(ClassDefStmt classDefStmt1:neededClassName){
                List<String> directSuperClasses = classDefStmt1.getBases().stream()
                        .filter(base -> base instanceof NameExpr)
                        .map(base -> ((NameExpr) base).getId())
                        .toList();

                superClasses.addAll(directSuperClasses);

                if(mode==1) {
                    for (String directSuperClass : directSuperClasses) {
                        findSuperClassesRecursively(directSuperClass, superClasses,1);
                    }
                }
            }


        }
    }

     /**
     * TODO Given class name `classA` and `classB` representing two classes A and B,
     *  `haveSuperClass` checks whether B is a super class of A in the current module.
     *  {@link QueryOnClass#module}
     * @param classA the name of class A.
     * @param classB the name of class B
     * @return returns true if B is A's super class, otherwise false.
     * Hint1: you can just reuse {@link QueryOnClass#findSuperClasses}
     */
    public BiFunction<String, String, Boolean> haveSuperClass = (classA,classB)->{
         List<String> superClassesOfA = findSuperClasses.apply(classA);
         if(!superClassesOfA.isEmpty()){
             for(String classes:superClassesOfA){
                 if(classes.equals(classB)){
                     return true;
                 }
             }
         }
         return false;
     };


    /**TODO Returns all the overriding methods within the current module
     * {@link QueryOnClass#module}
     * @return results List of strings of the names of overriding methods.
     * Note: If there are multiple overriding functions with the same name, please include name
     * in the result list for MULTIPLE times. You can refer to the test case.
     * Hint1: you can implement a helper function that first finds the methods that a class
     *  directly contains.
     * Hint2: you can reuse the results of {@link QueryOnClass#findSuperClasses}
     */
    public Supplier<List<String>> findOverridingMethods = ()->{
        ArrayList<String> result = new ArrayList<>();
        ArrayList<ASTElement> cClasses = module.getAllNodes();
        List<ClassDefStmt> classDefStmt = new ArrayList<>();
        for(ASTElement classes: cClasses){
            if(classes instanceof ClassDefStmt){
                classDefStmt.add((ClassDefStmt) classes);
            }
        }
        for(ClassDefStmt classDefStmt1:classDefStmt) {
            List<String> superClassesOf1 = new ArrayList<>();//= findSuperClasses.apply(classDefStmt1.getName());
            //List<String> superClasses = new ArrayList<>();
            findSuperClassesRecursively(classDefStmt1.getName(), superClassesOf1,0);
            List<ClassDefStmt> superClass = new ArrayList<>();
            for(ClassDefStmt classDefStmt2:classDefStmt){
                for(String temp: superClassesOf1){
                    if(temp.equals(classDefStmt2.getName())){
                        superClass.add(classDefStmt2);
                    }
                }
            }
            List<FunctionDefStmt> funcofstmt1 = classDefStmt1.findAllDirMethods();

            for(ClassDefStmt stmt:superClass){
                List<FunctionDefStmt> funcofsuperclass = stmt.findAllDirMethods();
                for(FunctionDefStmt functionDefStmt:funcofsuperclass){
                    for(FunctionDefStmt defStmt:funcofstmt1) {
                        if (defStmt.getName().equals(functionDefStmt.getName())) {
                            result.add(defStmt.getName());
                        }
                    }
                }
            }

        }
        return result;
    };
    //private List<FunctionDefStmt> findDirMethods()
    /**
     * TODO Returns all the methods that a class possesses in the current module
     * {@link QueryOnClass#module}
     * @param className the name of the class
     * @return results List of strings of names of the methods it possesses
     * Note: the same function name should appear in the list only once, due to overriding.
     * Hint1: you can implement a helper function that first finds the methods that a class
     *  directly contains.
     * Hint2: you can reuse the results of {@link QueryOnClass#findSuperClasses}
     */
    public Function<String, List<String>> findAllMethods = (className)->{
        List<String> result = new ArrayList<>();
        List<ClassDefStmt> classDefStmt = new ArrayList<>();
        ArrayList<ASTElement> cClasses = module.getAllNodes();
        for(ASTElement classes: cClasses){
            if(classes instanceof ClassDefStmt){
                classDefStmt.add((ClassDefStmt) classes);
            }
        }
        List<ClassDefStmt> neededClassName = new ArrayList<>();
        for(ClassDefStmt classes: classDefStmt){
            if(classes.getName().equals(className)){
                neededClassName.add(classes);
            }
        }
        if (!neededClassName.isEmpty()) {
            for(ClassDefStmt classDefStmt1:neededClassName){
                List<String> superClass = new ArrayList<>();
                findSuperClassesRecursively(classDefStmt1.getName(), superClass,1);
                List<ClassDefStmt> superClassList = new ArrayList<>();
                for(String temp:superClass){
                    for(ClassDefStmt classDefStmt2:classDefStmt){
                        if(temp.equals(classDefStmt2.getName())){
                            superClassList.add(classDefStmt2);
                        }
                    }
                }
                List<FunctionDefStmt> allFunc = new ArrayList<>();
                for(ClassDefStmt classDefStmt2:superClassList){
                    allFunc.addAll(classDefStmt2.findAllDirMethods());
                }
                for(FunctionDefStmt functionDefStmt:classDefStmt1.findAllDirMethods()){
                    result.add(functionDefStmt.getName());
                }
                for(FunctionDefStmt functionDefStmt:allFunc){
                    result.add(functionDefStmt.getName());
                }
            }
        }
        return result;
    };

     /**
     * TODO Returns all the classes that possesses a main function in the current module
     * {@link QueryOnClass#module}
     * @return results List of strings of names of the classes
     * Hint1: You can reuse the results of {@link QueryOnClass#findAllMethods}
     */
     //@SuppressWarnings("unchecked")
     public Supplier<List<String>> findClassesWithMain = ()->{
        List<String> result = new ArrayList<>();
        ArrayList<ASTElement> cClasses = module.getAllNodes();
        List<ClassDefStmt> classDefStmt = new ArrayList<>();
        for(ASTElement classes: cClasses){
            if(classes instanceof ClassDefStmt){
                classDefStmt.add((ClassDefStmt) classes);
            }
        }
        for(ClassDefStmt classDefStmt1:classDefStmt) {
            List<String> listOfFunc = (List<String>) findAllMethods.apply(classDefStmt1.getName());
            for(String name:listOfFunc){
                if(name.equals("main")){
                    result.add(classDefStmt1.getName());
                }
            }
        }
        return result;
     };

}

