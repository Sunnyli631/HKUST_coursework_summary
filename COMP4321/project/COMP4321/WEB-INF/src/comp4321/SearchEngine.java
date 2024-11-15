package comp4321;

import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;

public class SearchEngine {
    private StringPreprocessor stringPreprocessor;
    private Indexer indexer;
    private PageTree pageTree;
    private Spider spider;
    private int pageCount;
    // variable used to store the starting URL
    private String startURL;
    private Query query;
    // set up original pageCount
    private int maxPageCount;
    private int readFromDB = -1;

    private int maxPageReturn = 50;

    private final String indexer_recname = "WordIndexer";
    private final String pageTree_recname = "PageTree";
    private final String searchEngine_recname = "SearchEngine";

    private RecordManager recman;
    private HTree data;

    public void close1() throws IOException {
        // close the classes
        recman.commit();
        recman.close();
        spider.close();
        indexer.close();
    }

    public void printAllWebPages() throws IOException {
        List<String> results = pageTree.getTextResults( "", "");
        FileOutput spider_output;

        if (!results.isEmpty()) {
            // set up the file output
            spider_output = new FileOutput("spider_result.txt");
            for (String result : results) {
                spider_output.write(result);
                System.out.println(result);
            }
            spider_output.close();
        }
    }

    public String requestHTML(String queryText) {
        String output = request(queryText);
        return "";
    }

    public String request(String queryText) {
        try {
            return query.query_request(queryText);
        } catch (IOException ex) {
            return ""; 
        }
    }

    public void setup(boolean readOnly) {
        try {
            // set up a string preprocessor (loading stopwords + stemming)
            stringPreprocessor = new StringPreprocessor();
            recman = RecordManagerFactory.createRecordManager(searchEngine_recname);
            long recid = recman.getNamedObject("data");
            if (recid != 0) {
                data = HTree.load(recman, recid);
            } else {
                data = HTree.createInstance(recman);
                recman.setNamedObject("data", data.getRecid());
            }
            if (!readOnly) {
                // set up a indexer
                indexer = new Indexer(indexer_recname, stringPreprocessor);
                // set up a page tree
                pageTree = new PageTree(pageTree_recname);
                // set up a spider
                spider = new Spider(startURL, maxPageCount, indexer, pageTree);
                // run the recursive fetching, return the number of pages fetched
                pageCount = spider.run();
                readFromDB = pageTree.getReadFromDB();
                System.out.println("\nPages read from db: " + readFromDB + "\n");
                System.out.println("Total page fetched: " + pageCount + "\n");

                pageTree.putFreqWords(indexer);

                data.put("pageCount", pageCount);
                commit();
            } else {
                pageCount = (int) data.get("pageCount");
            }
            // define how the search algorithm favours title
            double titleMul = 5.0;
            // set up Query class
            query = new Query(stringPreprocessor, indexer_recname, pageTree_recname, pageCount, 50, titleMul, false);
        } catch (IOException | ParserException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getPrevious(String queryText) {
        String[] words = queryText.split(";");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (Objects.equals(word, "")) continue;
            sb.append((new OutPrintItem(word, comp4321.OutPrintItemType.PREVIOUS)).getHTML());
        }
        return sb.toString();
    }

    public SearchEngine(String startURL, int maxPageCount) {
        this.startURL = startURL;
        this.maxPageCount = maxPageCount;
    }

    public SearchEngine() {
        this.startURL = "https://www.cse.ust.hk/~kwtleung/COMP4321/testpage.htm";
        this.maxPageCount = 300;
    }

    public void commit() throws IOException {
        pageTree.commit();
        indexer.commit();
    }

    public void runTest() throws IOException {
        // query text
        printAllWebPages();
        
        String queryText = "movie";

        String output = request(queryText);
        // System.out.println(output);
    }

    public String getKeywordCheckboxes() {
      try {
        StringBuilder sb = new StringBuilder();
        HashSet<String> words = query.getAllStems();
        for (String word : words) {
          sb.append("<input type=\"checkbox\" class=\"stem_checkbox\" name=\"word_");
          sb.append(word);
          sb.append("\" value=\"");
          sb.append(word);
          sb.append("\"/><label for=\"");
          sb.append(word);
          sb.append("\">");
          sb.append(word);
          sb.append("</label><br>");
        }
        return sb.toString();
      } catch (IOException ex) {
        return "no word found";
      }
    }

}
