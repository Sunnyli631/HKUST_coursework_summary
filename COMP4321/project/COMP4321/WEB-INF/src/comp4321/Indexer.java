package comp4321;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.helper.FastIterator;
import jdbm.htree.HTree;

import java.io.IOException;
import java.util.*;

/*
* This is the Indexer class, it can perform word -> wordID
* */

public class Indexer {
    private final RecordManager recman;
    // fKeyword is the forward index, storing pageID -> {words}
    // iKeyword is the inverted index, storing word -> {pageID}
    private final HTree fKeyword, iKeyword;

    // For title
    // fTitle is the forward index, storing pageID -> {words}
    // iTitle is the inverted index, storing word -> {pageID}
    private final HTree fTitle, iTitle;

    // String Preprocessing
    StringPreprocessor sp;

    public HTree getHTree(String ht_name) throws IOException {
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

    Indexer(String rec_name, StringPreprocessor stringPreprocessor) throws IOException {
        recman = RecordManagerFactory.createRecordManager(rec_name);

        fKeyword = getHTree("fKeyword");
        iKeyword = getHTree("iKeyword");
        fTitle = getHTree("fTitle");
        iTitle = getHTree("iTitle");

        this.sp = stringPreprocessor;
    }

    public void commit() throws IOException {
        recman.commit();
    } 

    public void close() throws IOException {
        recman.commit();
        recman.close();
    }

    private List<PageKeyword> sortWordFreq(HTree input) throws IOException {
        List<PageKeyword> output = new ArrayList<PageKeyword>();
        FastIterator fi = input.values();
        Object obj;
        while ((obj = fi.next()) != null) {
            output.add((PageKeyword) obj);
        }
        output.sort(new PageKeywordFreqComparator());
        return output;
    }

    public List<PageKeyword> getPageWords(Integer pageID, boolean isTitle) throws IOException {
        Object obj = (isTitle) ? fTitle.get(pageID) : fKeyword.get(pageID);
        if (obj == null) return null;
        HTree htree = getHTree((String) obj);
        return sortWordFreq(htree);
    }

    public void addPageWord(HashMap<String, Integer> wordFreq, Integer pageID, boolean isTitle) throws IOException {
        Set<String> words = wordFreq.keySet();
        long doc_len = 0;
        for (String word : words) {
            if (!sp.isStopWord(word)) {
                long freq = (long) wordFreq.get(word);
                doc_len += freq * freq;
            }
        }
        double len_recp = 1.0/Math.sqrt(doc_len);
        for (Map.Entry<String, Integer> word : wordFreq.entrySet()) {
            if (!sp.isStopWord(word.getKey())) {
                addword(word.getKey(), pageID, word.getValue(), len_recp, isTitle);
            }
        }
    }

    public HTree getPageIDTree(int pageID, boolean isTitle) throws IOException {
        HTree htree = null;
        String htname;
        if (isTitle) {
            htname = pageID + "_fTitle";
            if (fTitle.get(pageID) == null) {
                htree = getHTree(htname);
                fTitle.put(pageID, htname);
            }
        } else {
            htname = pageID + "_fKeyword";
            if (fKeyword.get(pageID) == null) {
                htree = getHTree(htname);
                fKeyword.put(pageID, htname);
            }
        }
        if (htree == null) {
            htree = getHTree(htname);
        }
        return htree;
    }

    public WordInverted getWordPageList(String word, boolean isTitle) throws IOException {
        WordInverted wordInverted;
        if (isTitle) {
            if (iTitle.get(word) == null) {
                iTitle.put(word, new WordInverted(word, true));
            }
            wordInverted = (WordInverted) iTitle.get(word);
        } else {
            if (iKeyword.get(word) == null) {
                iKeyword.put(word, new WordInverted(word, false));
            }
            wordInverted = (WordInverted) iKeyword.get(word);
        }
        return wordInverted;
    }

    private boolean addword(String word, int pageID, int frequency, double len_recp, boolean isTitle) throws IOException {
        if (sp.isStopWord(word)) return false;
        String stem = sp.toStem(word);
        if (stem.isEmpty() || stem.isBlank()) return false;

        HTree htree = getPageIDTree(pageID, isTitle);
        Object obj;
        PageKeyword pageKeyword;
        if ((obj = htree.get(stem)) == null) {
            pageKeyword = new PageKeyword(stem, pageID, isTitle, len_recp);
            htree.put(stem, pageKeyword);
        } else {
            pageKeyword = (PageKeyword) obj;
        }
        pageKeyword.addFreq(frequency);

        WordInverted pageList = getWordPageList(stem, isTitle);
        pageList.addPageKeyword(pageKeyword);

        return true;
    }
}
