package comp4321;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordInverted implements Serializable {
    public String word;
    private List<PageKeyword> pageKeywords;
    private int maxtf = 1;
    private boolean isTitle;

    WordInverted(String word, boolean isTitle) {
        this.pageKeywords = new ArrayList<PageKeyword>();
        this.word = word;
        this.isTitle = isTitle;
    }

    public void addPageKeyword(PageKeyword keyword) {
        pageKeywords.add(keyword);
        maxtf = Math.max(maxtf, keyword.frequency);
    }

    public void getScore(HashMap<Integer, PageScore> pageScores, int N, double titleMul, int frequency) {
        for (PageKeyword keyword : pageKeywords) {
            double score = keyword.tfxidf(N, pageKeywords.size(), titleMul) * frequency / maxtf;
            if (pageScores.containsKey(keyword.getPageID())) {
                PageScore page = pageScores.get(keyword.getPageID());
                page.addScore(score, isTitle);
                page.setLength(keyword.getLenRecp(), keyword.isTitle);
            } else {
                PageScore page = new PageScore(keyword.getPageID());
                page.setLength(keyword.getLenRecp(), keyword.isTitle);
                page.addScore(score, isTitle);
                pageScores.put(keyword.getPageID(), page);
            }
        }
    }
}
