package comp4321;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.helper.FastIterator;
import jdbm.htree.HTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PageTree {
    private final RecordManager recman;
    /*
    * htreeForward(String url, int index)
    * htreeInverted(int index, comp4321.WebPage webpage)
    * url can be get from webpage
    * */

    private final HTree fPage, iPage;
    private int id_index = 0;
    private int pageCount = -1;
    private int readFromDB = 0;

    private HTree getHTree(String ht_name) throws IOException {
        long recid = recman.getNamedObject(ht_name);
        HTree htree;
        if (recid != 0) {
            htree = HTree.load(recman, recid);
        } else {
            htree = HTree.createInstance(recman);
            recman.setNamedObject(ht_name, htree.getRecid());
        }
        return htree;
    }

    PageTree(String rec_name) throws IOException {
        recman = RecordManagerFactory.createRecordManager(rec_name);
        fPage = getHTree("fPage");
        iPage = getHTree("iPage");
    }

    public int getPageCount() throws IOException {
        if (pageCount != -1) return pageCount;
        return readPages();
    }

    public int readPages() throws IOException {
        System.out.print("Pages read from db file: ");
        int count = 0;
        FastIterator fi = fPage.values();
        Object obj;
        String number;
        int strLen = 0;
        while ((obj = fi.next()) != null) {
            if (((WebPage) obj).fetched) {
                count++;
                for (int i=0;i<strLen;i++) {System.out.print("\b");}
                number = String.valueOf(count);
                strLen = number.length();
                System.out.print(number);
            }
        }
        readFromDB = count;
        pageCount = count;
        return count;
    }

    public int getReadFromDB() { return this.readFromDB; }

    public void close() throws IOException {
        recman.commit();
        recman.close();
    }

    public Integer addPage(String url) throws IOException {
        if (contains(url)) return (Integer) iPage.get(url);
        Integer pageID = encode();
        fPage.put(pageID, new WebPage(url, pageID));
        iPage.put(url, pageID);
        return pageID;
    }

    public WebPage getWebPage(Integer pageID) throws IOException {
        if (!contains(pageID)) return null;
        return (WebPage) fPage.get(pageID);
    }

    public boolean contains(String url) throws IOException {
        return iPage.get(url) != null;
    }

    public boolean contains(Integer pageID) throws IOException {
        return fPage.get(pageID) != null;
    }

    public Integer getPageID(String url) throws IOException {
        Object obj;
        if ((obj = iPage.get(url)) == null) return 0;
        return (Integer) obj;
    }

    public boolean WebPageFetched(int pageID) throws IOException {
        if (fPage.get(pageID) == null) return false;
        return ((WebPage) fPage.get(pageID)).fetched;
    }

    public void setFetched(int pageID) throws IOException {
        Object obj;
        if ((obj = fPage.get(pageID)) == null) return;
        ((WebPage) obj).setFetched(true);
    }

    public HashSet<Integer> notFetched() throws IOException {
        FastIterator fi = fPage.keys();
        Object obj;
        HashSet<Integer> pageIDs = new HashSet<Integer>();
        while ((obj = fi.next()) != null) {
            if (!WebPageFetched((Integer) obj)) {
                pageIDs.add((Integer) obj);
            }
        }
        return pageIDs;
    }

    public Integer addParent(String parentURL, String childURL) throws IOException {
        if (!contains(childURL)) {
            addPage(childURL);
        }
        Integer childId = (Integer) iPage.get(childURL);
        WebPage childPage = (WebPage) fPage.get(childId);
        childPage.addParent(parentURL);
        return childId;
    }

    public Integer encode() throws IOException {
        final int increment = 16807;
        final int modulus = (int) 1e9 + 7;
        id_index += increment;
        id_index %= modulus;
        if (fPage.get(id_index) != null) return encode();
        return id_index;
    }

    public void putFreqWords(Indexer indexer) throws IOException {
        FastIterator fi = fPage.values();
        Object obj;
        while ((obj = fi.next()) != null) {
            WebPage webpage = (WebPage) obj;
            List<PageKeyword> pageKeywords = indexer.getPageWords(webpage.getPageID(), false);
            webpage.putFreqWords((pageKeywords == null) ? new ArrayList<PageKeyword>() : pageKeywords.subList(0, Math.min(pageKeywords.size(), 5)));
        }
    }

    public List<String> getTextResults(String prefix, String indent) throws IOException {
        List<String> results = new ArrayList<String>();
        FastIterator fi = fPage.values();
        Object obj;
        int count = 0;
        while ((obj = fi.next()) != null) {
            WebPage webpage = (WebPage) obj;
            if (webpage.fetched) {
                List<OutPrintItem> items = webpage.getResultText();
                StringBuilder sb = new StringBuilder();
                for (OutPrintItem item : items) {
                    sb.append(item.getText(prefix, indent));
                }
                results.add(sb.toString());
                count++;
            }
        }
        return results;
    }

    public String firstURL() throws IOException {
        return ((WebPage) fPage.values().next()).url;
    }

    public void commit() throws IOException {
        recman.commit();
    } 
}