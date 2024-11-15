package comp4321;

import org.htmlparser.util.ParserException;

import java.lang.Thread;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URL;
import java.util.HashSet;

/*
* This is the Spider class
* */

public class Spider {
    private String startURL;
    private int max_n = 30;
    private int pageCount = 0;
    private PageTree pageTree;
    private Indexer indexer;

    Spider(String startURL, int max_n, Indexer indexer, PageTree pageTree) throws IOException {
        this.startURL = startURL; // start from this page
        this.max_n = max_n; // amount of pages to be searched

        this.indexer = indexer;
        this.pageTree = pageTree;
        this.pageCount = pageTree.readPages();
    }

    // The close function is manually called when the class is deconstructed
    public void close() throws IOException {
        pageTree.close();
    }

    // The run function start the recursive crawler
    public int run() throws IOException, ParserException {
        // add startPage to page tree
        HashSet<Integer> pageIDs = new HashSet<Integer>();
        if (pageCount >= max_n) return pageCount;
        if (pageTree.WebPageFetched(pageTree.getPageID(startURL))) {
            pageIDs = pageTree.notFetched();
        } else {
            Integer pageID = pageTree.addPage(startURL);
            pageIDs.add(pageID);
        }
        
        while (!pageIDs.isEmpty()) {
            HashSet<Integer> _pageIDs = new HashSet<Integer>();
            for (Integer id : pageIDs) {
                _pageIDs.addAll(fetchWebpage(id));
            }
            pageIDs = _pageIDs;
        }
        
        System.out.print("\r");
        return pageCount;
    }

    private HashSet<Integer> fetchWebpage(Integer pageID) throws IOException, ParserException {
        HashSet<Integer> pageIDs = new HashSet<Integer>();
        WebPage webpage;
        if (pageID == 0) return pageIDs;
        // If the page fetched reach the limit
        if (this.pageCount >= this.max_n) return pageIDs;
        // return when the webpage is fetched
        if (pageTree.WebPageFetched(pageID)) return pageIDs;
        webpage = pageTree.getWebPage(pageID);
        System.out.print("\r");
        int process = (int) Math.floor((double) pageCount / max_n * 15);
        for (int i=0;i<15;i++) {
            if (i < process) System.out.print("=");
            else System.out.print("-");
        }
        process = (int) Math.floor((double) pageCount / max_n * 100);
        System.out.print(" " + process + "% - ");
        System.out.print("Fetching PageID " + pageID);
        webpage.fetch(indexer);
        pageTree.setFetched(pageID);
        this.pageCount++;
        HashSet<String> urls = webpage.getLinks();

        for (String url : urls) {
            Integer childID = pageTree.addParent(webpage.url, url);
            pageIDs.add(childID);
        }

        return pageIDs;
    }

}

