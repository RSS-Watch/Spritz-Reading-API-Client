package org.speedreading.api;

/**
 * Created by Andrej on 19.05.2015.
 */
public class WordORP {
    private String word;
    private Integer orp;

    public WordORP(String pword, int porp) {
        this.word = pword;
        this.orp = porp;
    }

    public String getWordrd() {
        return word;
    }

    public void setWordrd(String pword) {
        this.word = pword;
    }

    public Integer getOrp() {
        return orp;
    }

    public void setOrp(Integer porp) {
        this.orp = porp;
    }
}
