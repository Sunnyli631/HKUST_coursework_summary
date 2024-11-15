package comp4321;

import lib.IRUtilities.Porter;

import java.io.IOException;
import java.util.HashSet;
import java.util.StringTokenizer;

public class StringPreprocessor {
    private HashSet<String> sw;
    private Porter porter;

    public boolean isStopWord(String word) {return sw.contains(word.toLowerCase()); }

    private int addStopWords(String stopWords) {
        StringTokenizer st = new StringTokenizer(stopWords);
        this.sw = new HashSet<String>();
        int count = 0;
        while (st.hasMoreTokens()) {
            sw.add(st.nextToken().toLowerCase());
            count++;
        }
        return count;
    }

    StringPreprocessor() throws IOException {
        this.porter = new Porter();
        FileInput stopword_input = new FileInput("stopwords.txt");
        String stopwords = stopword_input.read();
        stopword_input.close();

        // add stopwords
        int stopWordCount = addStopWords(stopwords);
        System.out.println("stopword size: " + stopWordCount);
    }

    public String toStem(String str) { return porter.stripAffixes(str); }
}
