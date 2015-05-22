package org.speedreading.api;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Andrej on 15.05.2015.
 */
public class SpeedReadingAPI {

    private List<String> consonants;
    private List<String> vowels;
    private List<String> twoLetterSyllables;
    private List<String> threeLetterSyllables;

    public SpeedReadingAPI() {

        consonants = Arrays.asList("b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "y", "z", "ß");
        vowels = Arrays.asList("a", "e", "i", "o", "u", "ä", "ö", "ü");
        twoLetterSyllables = getListFrom("/twoLetterSyllables.data");
        threeLetterSyllables = getListFrom("/threeLetterSyllables.data");

    }

    private static Integer getORP(String pWord) {

        int length = pWord.length();
        int orp;

        switch (length) {
            case 1:
                orp = 0;
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                orp = 1;
                break;
            case 6:
            case 7:
            case 8:
            case 9:
                orp = 2;
                break;
            case 10:
            case 11:
            case 12:
            case 13:
                orp = 3;
                break;
            default:
                orp = 0;
        }

        return orp;
    }

    public ArrayList<WordORP> convertToSpeedReadingText(String pText) {

        ArrayList<WordORP> ret = new ArrayList<>();

        for (String word : getSplitText(pText)) {
            ret.add(new WordORP(word, getORP(word)));
        }

        return ret;
    }

    private ArrayList<String> getSplitText(String pText) {

        String[] words = pText.split(" ");
        ArrayList<String> ret = new ArrayList<>();

        for (String word : words) {
            while (word.length() > 13) {

                int i = 11,
                        j = 12;

                // Prevents short word endings (i.e. "let- ters." instead of "letter- s.")
                String temp = word.substring(11);
                if (temp.length() < 4) {
                    if (!(consonants.contains(temp.charAt(temp.length() - 1)) || vowels.contains(temp.charAt(temp.length() - 1)))) {
                        i--;
                        j--;
                    }
                    i--;
                    j--;
                }

                for (; i >= 0; i--, j--) {

                    // No good point to split found
                    if (i == 0) {
                        ret.add(word.substring(0, 11) + "-");
                        word = word.substring(11);
                        break;
                    }

                    if (threeLetterSyllables.contains("" + word.charAt(i - 1) + word.charAt(i) + word.charAt(j))) {
                        if (i > 1) {
                            i--;
                            j--;
                            continue;
                        } else {
                            ret.add(word.substring(0, 11) + "-");
                            word = word.substring(11);
                            break;
                        }
                    }

                    if (twoLetterSyllables.contains("" + word.charAt(i) + word.charAt(j))) {
                        continue;
                    }

                    // Split if letters duplicate
                    if (word.charAt(i) == word.charAt(j)) {
                        ret.add(word.substring(0, j) + "-");
                        word = word.substring(j);
                        break;
                    }

                    // Split between two consonants
                    if (consonants.contains("" + word.charAt(i)) && consonants.contains("" + word.charAt(j))) {
                        ret.add(word.substring(0, j) + "-");
                        word = word.substring(j);
                        break;
                    }

                }

            }

            ret.add(word);
        }

        return ret;
    }

    private ArrayList<String> getListFrom(String pFileName) {

        ArrayList<String> retList = new ArrayList<>();

        InputStream in = this.getClass().getResourceAsStream(pFileName);
        Scanner s = new Scanner(in);
        while (s.hasNext()) {
            retList.add(s.next());
        }

        return retList;

    }
}
