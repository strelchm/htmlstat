package ru.strelchm.htmlstat.db.model;

import java.util.Comparator;

public class WordStatistics {
    private String word;
    private long count;

    public WordStatistics(String word, long count) {
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public static class WordStatisticsComparator implements Comparator<WordStatistics> {
        @Override
        public int compare(WordStatistics o1, WordStatistics o2) {
            if (o1.getCount() != o2.getCount()) {
                return Long.compare(o2.getCount(), o1.getCount());
            } else {
                return o1.getWord().compareTo(o2.getWord());
            }
        }
    }

}
