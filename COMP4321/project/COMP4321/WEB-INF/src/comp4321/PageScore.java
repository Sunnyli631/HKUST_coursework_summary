package comp4321;

import java.util.Comparator;

public class PageScore {
    public Integer pageID;
    private double titleScore = 0.0;
    private double keywordScore = 0.0;
    private double title_len_recp = 1.0;
    private double doc_len_recp = 1.0;
    private double totalScore = -1;

    PageScore(Integer pageID) {
        this.pageID = pageID;
    }

    PageScore(String line) {
        line = line.replace("\n", "");
        String[] items = line.split(" ");
        this.pageID = Integer.parseInt(items[0]);
        this.titleScore = Double.parseDouble(items[1]);
        this.keywordScore = Double.parseDouble(items[2]);
        this.title_len_recp = Double.parseDouble(items[3]);
        this.doc_len_recp = Double.parseDouble(items[4]);
    }

    public void multiply(double multiplier) {
        titleScore *= multiplier;
        keywordScore *= multiplier;
    }

    public void setLength(double length_recp, boolean isTitle) {
        if (isTitle) title_len_recp = length_recp;
        else doc_len_recp = length_recp;
    }

    public void addScore(double score, boolean isTitle) {
        if (isTitle) titleScore += score;
        else keywordScore += score;
    }

    public double getScore() {
        if (totalScore != -1) return totalScore;
        System.out.println(getLine());
        return (totalScore = (titleScore + keywordScore)* doc_len_recp);
    }

    public String getLine() {
        return pageID + " " + titleScore + " " + keywordScore + " " + title_len_recp + " " + doc_len_recp + "\n";
    }

    public PageScore merge(PageScore other) {
        this.titleScore += other.titleScore;
        this.keywordScore += other.keywordScore;
        return this;
    }
}

class PageScoreComparator implements Comparator<PageScore> {
    @Override
    public int compare(PageScore o1, PageScore o2) {
        if (o2.getScore() > o1.getScore()) return 1;
        if (o2.getScore() < o1.getScore()) return -1;
        return 0;
    }
}