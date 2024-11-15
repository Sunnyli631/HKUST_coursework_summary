package comp4321;

import jdbm.htree.HTree;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.beans.LinkBean;
import org.htmlparser.beans.StringBean;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class WebPage implements Serializable {
    public String url;
    public boolean fetched = false;
    private final Integer pageID;
    private static final long serialVersionUID = 1L;
    private Parser parser;
    private StringBean sb;
    private String last_mod;
    private NodeList nodeList;
    private final HashSet<String> parentLinks;
    private HashSet<String> childrenURL;
    private String title = "";
    private final List<OutPrintItem> items;
    private final List<PageKeyword> pageKeywords;
    private int content_length = 0;

    WebPage(String url, Integer pageID) {
        this.url = url;
        this.sb = new StringBean();
        this.pageID = pageID;
        this.parentLinks = new HashSet<String>();
        this.pageKeywords = new ArrayList<PageKeyword>();
        this.items = new ArrayList<OutPrintItem>();
        sb.setLinks(false);
    }

    /*
     * This function is called to fetch the url
     * */
    public void fetch(Indexer indexer) throws ParserException, IOException {
        if (this.parser != null) {
            parser.reset();
        }
        URL url = new URL(this.url);
        URLConnection uc = url.openConnection();
        getWebsiteProperty(uc);

        parser = new Parser();
        parser.setConnection(uc);
        nodeList = parser.extractAllNodesThatMatch((NodeFilter) node -> true);

        extractStrings(indexer);
        extractTitle(indexer);
        getLinks();

        fetched = true;
    }

    public String getURL() { return this.url; }

    public String getTitle() {
        if (this.title != "") return this.title;
        NodeList titleList = nodeList.extractAllNodesThatMatch((NodeFilter) node -> (node instanceof TitleTag));
        TitleTag titleTag = (TitleTag) titleList.elementAt(0);
        this.title = titleTag.getTitle();
        return this.title;
    }

    public void getWebsiteProperty(URLConnection uc) {
        last_mod = uc.getHeaderField("Last-Modified");

        content_length = Integer.parseInt(uc.getHeaderField("Content-Length"));
    }

    public void extractTitle(Indexer indexer) throws IOException {
        StringTokenizer st = new StringTokenizer(getTitle());
        HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
        while (st.hasMoreTokens()) {
            String word = st.nextToken();
            if (wordMap.containsKey(word)) {
                Integer freq = wordMap.get(word);
                freq++;
                wordMap.replace(word, freq);
            } else {
                wordMap.put(word, 1);
            }
        }
        indexer.addPageWord(wordMap, pageID, true);
    }

    public void extractStrings(Indexer indexer) throws ParserException, IOException {
        this.parser.visitAllNodesWith(sb);
        sb.setURL(this.url);
        StringTokenizer st = new StringTokenizer(sb.getStrings());
        HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
        while (st.hasMoreTokens()) {
            String word = st.nextToken();
            if (wordMap.containsKey(word)) {
                Integer freq = wordMap.get(word);
                freq++;
                wordMap.replace(word, freq);
            } else {
                wordMap.put(word, 1);
            }
        }
        indexer.addPageWord(wordMap, pageID, false);
    }

    public void putFreqWords(List<PageKeyword> wordList) {
        pageKeywords.addAll(wordList);
    }

    public int documentLength() {return content_length;}

    public HashSet<String> getLinks() {
        if (childrenURL == null) {
            childrenURL = new HashSet<String>();
            LinkBean lb = new LinkBean();
            lb.setURL(this.url);
            URL[] urls = lb.getLinks();
            for (URL url : urls) {
                childrenURL.add(url.toString());
            }
        }
        return childrenURL;
    }

    public void addParent(String parentURL) {
        if (!parentLinks.contains(parentURL)) parentLinks.add(parentURL);
    }

    public int getPageID() {
        return this.pageID;
    }

    public void setFetched(boolean b) {
        this.fetched = b;
    }

    public List<OutPrintItem> getResultText() throws IOException {
        if (!items.isEmpty()) return items;

        items.add(new OutPrintItem("", OutPrintItemType.PREFIX));
        items.add(new OutPrintItem("", OutPrintItemType.INDENT));
        items.add(new OutPrintItem(this.title, OutPrintItemType.TITLE));
        items.add(new OutPrintItem("\n", OutPrintItemType.LINEBREAK));

        items.add(new OutPrintItem("", OutPrintItemType.PREFIX));
        items.add(new OutPrintItem(url, OutPrintItemType.URL));
        items.add(new OutPrintItem("\n", OutPrintItemType.LINEBREAK));

        items.add(new OutPrintItem("", OutPrintItemType.PREFIX));
        items.add(new OutPrintItem(last_mod, OutPrintItemType.DATE));

        items.add(new OutPrintItem(", ", OutPrintItemType.TEXT));
        items.add(new OutPrintItem(String.valueOf(content_length), OutPrintItemType.TEXT));
        items.add(new OutPrintItem("\n", OutPrintItemType.LINEBREAK));

        items.add(new OutPrintItem("", OutPrintItemType.PREFIX));
        for (int i = 0; i< pageKeywords.size(); i++) {
            PageKeyword pageKeyword = pageKeywords.get(i);
            items.add(new OutPrintItem(pageKeyword.word + " " + pageKeyword.frequency + "; ", OutPrintItemType.KEYWORD));
        }
        items.add(new OutPrintItem("\n", OutPrintItemType.LINEBREAK));

        for (String parent : parentLinks) {
            items.add(new OutPrintItem("", OutPrintItemType.PREFIX));
            items.add(new OutPrintItem(parent, OutPrintItemType.URL));
            items.add(new OutPrintItem("\n", OutPrintItemType.LINEBREAK));
        }

        for (String child : childrenURL) {
            items.add(new OutPrintItem("", OutPrintItemType.PREFIX));
            items.add(new OutPrintItem(child, OutPrintItemType.URL));
            items.add(new OutPrintItem("\n", OutPrintItemType.LINEBREAK));
        }

        items.add(new OutPrintItem("——————————————–——————————————–——————————————–——————————————–", OutPrintItemType.HLINE));
        items.add(new OutPrintItem("\n", OutPrintItemType.LINEBREAK));

        return items;
    }
}

