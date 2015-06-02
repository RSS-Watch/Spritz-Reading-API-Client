package org.speedreading.api;

/**
 * String-Integer pair to store a word with it's Optimal Recognition Point.
 *
 * @author Andrej Schäfer
 * @author Julian Sauer
 */
public class WordORP {
    private String word;
    private Integer orp;

    public WordORP(String pword, int porp) {
        this.word = pword;
        this.orp = porp;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String pword) {
        this.word = pword;
    }

    public Integer getOrp() {
        return orp;
    }

    public void setOrp(Integer porp) {
        this.orp = porp;
    }
}
