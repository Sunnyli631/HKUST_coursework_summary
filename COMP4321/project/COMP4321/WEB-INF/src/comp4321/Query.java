package comp4321;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.helper.FastIterator;
import jdbm.htree.HTree;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Query {
    private HashMap<String, Integer> map;
    private StringPreprocessor sp;
    private int pageCount;
    private int outputCount;
    private int max_result;
    private double titleMul;
    private boolean saveQuery;

    private HTree fPage;
    private HTree iKeyword, iTitle;
    
    Query(StringPreprocessor stringPreprocessor, String indexer_recname, String pageTree_recname, int pageCount, int max_result, double titleMul, boolean saveQuery) throws IOException {
        this.sp = stringPreprocessor;
        this.pageCount = pageCount;
        this.max_result = max_result;
        this.titleMul = titleMul;
        this.saveQuery = saveQuery;
        this.map = new HashMap<String, Integer>();

        this.fPage = getHTree(pageTree_recname, "fPage");

        this.iKeyword = getHTree(indexer_recname, "iKeyword");
        this.iTitle = getHTree(indexer_recname, "iTitle");
    }

    private HashMap<Integer, PageScore> getScores(String word, int frequency) throws IOException {
        HashMap<Integer, PageScore> pageScores = new HashMap<Integer, PageScore>();
        String stem = sp.toStem(word);
        Object obj;
        if ((obj = iTitle.get(stem)) != null) {
            WordInverted wordInverted = (WordInverted) obj;
            wordInverted.getScore(pageScores, pageCount, titleMul, frequency);
        }

        if ((obj = iKeyword.get(stem)) != null) {
            WordInverted wordInverted = (WordInverted) obj;
            wordInverted.getScore(pageScores, pageCount, titleMul, frequency);
        }

        return pageScores;
    }

    private HTree getHTree(String rec_name, String ht_name) throws IOException {
        RecordManager recman = RecordManagerFactory.createRecordManager(rec_name);
        long recid = recman.getNamedObject(ht_name);
        if (recid != 0) {
            return HTree.load(recman, recid);
        }
        return null;
    }

  public static String extractCharsBetweenQuotes(String input) {
    StringBuilder result = new StringBuilder();
    boolean isOpeningQuoteFound = false;
    boolean containsQuote = false;
    for (char c : input.toCharArray()) {
      if (c == '"') {
        containsQuote = true;
        break;
      }
    }
    if(containsQuote) {
      for (char c : input.toCharArray()) {
        if (c == '"') {
          if (isOpeningQuoteFound) {
            break;
          }
          isOpeningQuoteFound = true;
        } else if (isOpeningQuoteFound) {
          result.append(c);
        }
      }
    }else {
      return input;
    }

    return result.toString();
  }

    public String query_request(String query) throws IOException {
        Object obj;
        outputCount = 0;
        map.clear();
        query = query.replaceAll("[^A-Za-z0-9\" ]", "");
        String processedQuery = extractCharsBetweenQuotes(query);
        StringTokenizer st = new StringTokenizer(processedQuery);
        long query_size = 0;
        while (st.hasMoreTokens()) {
            String word = st.nextToken();
            if (!sp.isStopWord(word)) {
                String stem = sp.toStem(word);
                if (!map.containsKey(stem)) {
                    map.put(stem, 1);
                } else {
                    Integer freq = map.get(stem);
                    freq++;
                    map.replace(stem, freq);
                }
            }
        }
        List<PageScore> pageScores = new ArrayList<PageScore>();
        HashMap<Integer, PageScore> pageMap = new HashMap<Integer, PageScore>();
        int kwSize = map.size();
        int count = 0;
        System.out.println();
        for (Map.Entry<String, Integer> keyword : map.entrySet()) {
            Integer freq = keyword.getValue();
            query_size += (long) freq * freq;
            System.out.print("\r");
            count++;
            int process = (int) Math.floor((double) count / kwSize * 15);
            for (int i=0;i<15;i++) {
                System.out.print((i <= process) ? "=" : "-");
            }
            System.out.print(" ");
            process = (int) Math.floor((double) count / kwSize * 100);
            System.out.print(process + "%");

            HashMap<Integer, PageScore> kwPageMap = getScores(keyword.getKey(), keyword.getValue());
            for (Map.Entry<Integer, PageScore> item : kwPageMap.entrySet()) {
                if (pageMap.containsKey(item.getKey())) {
                    pageMap.get(item.getKey()).merge(item.getValue());
                } else {
                    pageMap.put(item.getKey(), item.getValue());
                }
            }
        }

        System.out.println("\rFinish Retrieving Pages\t\t\t");

        for (Map.Entry<Integer, PageScore> item : pageMap.entrySet()) {
            pageScores.add(item.getValue());
        }

        map.clear();
        double query_recp = 1.0/Math.sqrt(query_size);

        for (int i=0;i<pageScores.size();i++) {
            pageScores.get(i).multiply(query_recp);
        }

        System.out.println("Outputting Pages");

        pageScores.sort(new PageScoreComparator());
        if (saveQuery) {
            for (int i=0;i<pageScores.size();i++) {
                writeQueryFile(query, pageScores.get(i));
            }
        }

        return getSearchResult(pageScores);
    }

    private void writeQueryFile(String query, PageScore pageScore) throws IOException {
        FileOutput fileOutput = new FileOutput("query_" + query);
        fileOutput.write(pageScore.pageID + " " + pageScore.getScore() + "\n");
    }

    public String readPrevious(String query) {
        try {
            List<PageScore> pageScores = readQueryFile(query);
            if (pageScores == null) return "";
            return getSearchResult(pageScores);
        } catch (IOException ex) {
            return "";
        }
    }

    public String getSearchResult(List<PageScore> pageScores) throws IOException {
        Object obj;
        StringBuilder sb = new StringBuilder();
        outputCount = Math.min(max_result, pageScores.size());
        sb.append("\n").append("<tr><td colspan=2>Page Returned: " + outputCount + "</td></tr>");
        System.out.print("\n");
        for (int i=0;i<max_result && i<pageScores.size();i++) {
            sb.append("<tr>");
            System.out.print("\r");
            int process = (int) Math.floor((double) (i+1) / Math.min(max_result, pageScores.size())*15);
            for (int j=0;j<15;j++) {
                if (j <= process) System.out.print("=");
                else System.out.print("-");
            }
            process = (int) Math.floor((double) (i+1) / Math.min(max_result, pageScores.size())*100);
            System.out.print(" " + process + "%");

            if ((obj = fPage.get(pageScores.get(i).pageID)) == null) continue;
            WebPage webpage = (WebPage) obj;
            sb.append("<td>");
            sb.append((new OutPrintItem(String.valueOf(pageScores.get(i).getScore()), OutPrintItemType.SCORE).getHTML()));
            sb.append("</td>");
            List<OutPrintItem> items = webpage.getResultText();
            sb.append("<td>");
            for (OutPrintItem item : items) {
                sb.append(item.getHTML());
            }
            sb.append("</td>");
            sb.append("</tr>");
        }

        System.out.print("\r\n\n");
        return sb.toString();
    }

    public List<PageScore> readQueryFile(String query) throws FileNotFoundException {
        List<PageScore> pageScores = new ArrayList<PageScore>();
        FileInput input = new FileInput("query_" + query);
        String text = input.read();
        String[] lines = text.split("\n");
        for (String line : lines) {
            String[] vars = line.split(" ");
            if (vars.length < 5) continue;
            pageScores.add(new PageScore(line));
        }
        return pageScores;
    }

    public String getSimilar_request() {
        return "";
    }

    public int getPageReturned() {return outputCount;}

  public HashSet<String> getAllStems() throws IOException {
    HashSet<String> output = new HashSet<String>();
    Object obj;
    FastIterator fi = iTitle.keys();
    while ((obj = fi.next()) != null) {
      String word = (String) obj;
      if (!sp.isStopWord(word)) output.add((String) obj);
    }
    fi = iKeyword.keys();
    while ((obj = fi.next()) != null) {
      String word = (String) obj;
      if (!sp.isStopWord(word)) output.add((String) obj);
    }
    return output;
  }
}
