package hk.ust.comp3021.utils;

import java.io.*;
import java.util.*;

public class ASTParser {
    private final String xmlFileID;
    private boolean isErr;
    private XMLNode rootXMLNode;
    private ASTModule rootASTModule;

    public ASTParser(String xmlFileID) {
        this.xmlFileID = xmlFileID;

        this.isErr = false;
        this.rootXMLNode = null;
        this.rootASTModule = null;
    }

    public boolean isErr() {
        return isErr;
    }

    public ASTModule getASTModule() {
        return rootASTModule;
    }

    public void parse() {
        // parse the XML Tree into rootXMLNode
        parse2XMLNode();
        // obtain the module node as the first child of ast node
        rootXMLNode = rootXMLNode.getChildByIdx(0);
        // create AST Tree and return the root node ASTModule
        rootASTModule = new ASTModule(rootXMLNode, xmlFileID);
    }

    /**
     * 1. Parse the XML Tree inside given XML File whose path is `xmlFilePath`
     * 2. Initialize the rootXMLNode as the root node of XML Tree, the tag Name of rootXMLNode should be ast
     * 3. If any exception throws, please set the field `isErr` to true. Otherwise, `isErr` is false.
     *
     * Hints:
     * For the following XML snippet:
     * <Assign type_comment="None" lineno="4" col_offset="8" end_lineno="4" end_col_offset="19">
     *     <targets>
     *         <Name id="tail" lineno="4" col_offset="8" end_lineno="4" end_col_offset="12">
     *             <Store/>
     *         </Name>
     *     </targets>
     *     <Constant value="None" kind="None" lineno="4" col_offset="15" end_lineno="4" end_col_offset="19"/>
     * </Assign>
     *
     * There are five XML nodes in total. Each XMLNode has two fields, i.e., attributes and children.
     * Attributes are key-value pairs. For instance, for xml node whose tag is Assign, the key-value pairs contains
     * `id: tail`. Children are a list of XMLNode, for instance, for Assign node, it has two children (targets, Constant).
     *
     * Noticed that in each line, there could be a self-closing tag, e.g., <Store/>, or a closing tag, e.g., </Name>, or
     * an opening tag <Name>. Please carefully organize the parent-children relation and initialize the attributes.
     *
     */

    public void parse2XMLNode() {
        // TODO: complete the definition of the method `parse2XMLNode`
        //not sure, need to test
        String path = "resources/pythonxml/python_" + xmlFileID + ".xml";
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            //XMLNode rootXMLNode = null;
            XMLNode curNode = new XMLNode("ast");
            curNode.setParent(null);
            this.rootXMLNode=curNode;

            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("</")) {
                    if(curNode.getParent()==null){
                        continue;
                    }
                    curNode = curNode.getParent();
                    continue;
                } else if (trimmedLine.endsWith("/>")) {
                    String aline = getTagName(trimmedLine);
                    String[] temps = aline.split(" ");
                    XMLNode child = new XMLNode(temps[0]);
                    helper(aline,child,1);
                    curNode.addChild(child);
                    child.setParent(curNode);
                } else if (trimmedLine.startsWith("<")) {
                    String aline = getTagName(trimmedLine);
                    //String tagName = aline.substring(1,aline.length()-1);
                    String[] temps = aline.split(" ");
                    XMLNode child = new XMLNode(temps[0]);
                    helper(aline,child,0);
                    curNode.addChild(child);
                    child.setParent(curNode);
                    curNode=child;
                } /*else {
                    if (!nodeStack.isEmpty()) {
                        XMLNode currentNode = nodeStack.peek();
                        int startIndex = trimmedLine.indexOf('>') + 1;
                        int endIndex = trimmedLine.lastIndexOf('<');
                        if (startIndex < endIndex) {
                            String text = trimmedLine.substring(startIndex, endIndex).trim();
                            currentNode.getAttributes().put("text", text);
                        }
                    }
                }*/
            }
            //this.rootXMLNode = rootXMLNode;
        } catch (IOException e) {
            isErr = true;
        }
    }
    private void helper(String aline,XMLNode ccNode,int a){
        String[] temps = aline.split(" ");
        if(temps.length > 1){
            //Map<String,String> attr = new HashMap<>();
            for (int i = 1;i<temps.length;i++) {
                String[] items = temps[i].split("=");
                if(items.length>1) {
                    ccNode.getAttributes().put(items[0], items[1].replaceAll("\"", ""));
                }
            }
        }/*else{
            XMLNode cNode = new XMLNode(aline);
            if(a==1){
                cNode.setParent(ccNode);
                ccNode.addChild(cNode);
            }
        }*/
    }
    /**/
    private String getTagName(String line) {
        int start = line.indexOf("<") + 1;
        int end = line.indexOf(" ");
        if (end == -1) {
            end = line.indexOf(">");
        }
        if(line.endsWith("/>")){
            end = line.indexOf("/");
        }
        return line.substring(start, end);
    }




    /**
     * Attention: You may need to define more methods to parse XML file
     * Feel free to define more method but remember not
     * (1) removing the fields or methods in our skeleton.
     * (2) changing the type signature of `public` methods
     * (3) changing the modifiers of the fields and methods, e.g., changing a modifier from "private" to "public"
     */
    public XMLNode getRootXMLNode() {
        return rootXMLNode;
    }
}
