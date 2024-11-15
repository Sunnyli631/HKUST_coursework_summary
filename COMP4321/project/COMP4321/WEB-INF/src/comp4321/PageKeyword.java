package comp4321;

import java.io.Serializable;
import java.util.*;

public class PageKeyword implements Serializable {
    public int frequency = 1;
    public String word;
    public boolean isTitle;
    static double log2_recp = 1.0/Math.log(2);
    private Integer pageID;
    private double len_recp = 1.0;
    private static final long serialVersionUID = 1L;
    PageKeyword(String word, Integer pageID, boolean isTitle, double len_recp) {
        this.word = word;
        this.pageID = pageID;
        this.isTitle = isTitle;
        this.len_recp = len_recp;
    }

    public void addFreq(int freq) {
        this.frequency += freq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return (o instanceof String) && Objects.equals(this.word, o);
    }

    public double tfxidf(int N, int df, double titleMul) {
        if (!isTitle) titleMul = 1.0;
        System.out.println(pageID + ", " + frequency + ", " + N + ", " + df);
        return log2_recp * frequency * Math.log((double) N / df) * titleMul;
    }

    public double getLenRecp() {
        return len_recp;
    }

    public Integer getPageID() {return pageID;}
}

class PageKeywordFreqComparator implements Comparator<PageKeyword> {
    @Override
    public int compare(PageKeyword o1, PageKeyword o2) {
        return o2.frequency - o1.frequency;
    }
}
