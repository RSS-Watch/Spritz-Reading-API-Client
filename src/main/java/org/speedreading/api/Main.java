package org.speedreading.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrej on 15.05.2015.
 */
public class Main {

    private static List<String> consonants = Arrays.asList("b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "y", "z", "�");
    private static List<String> vowels = Arrays.asList("a", "e", "i", "o", "u", "�", "�", "�");
    private static List<String> twoLetterSyllables = getListFrom("twoLetterSyllables.data");
    private static List<String> threeLetterSyllables = getListFrom("threeLetterSyllables.data");

    public static ArrayList<WordORP> convertToSpeedReadingText(String pText) {

        ArrayList<WordORP> ret = new ArrayList<>();

        for (String word : getSplitText(pText)) {
            ret.add(new WordORP(word, getORP(word)));
        }

        return ret;
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

    private static ArrayList<String> getSplitText(String pText) {

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

    private static ArrayList<String> getListFrom(String pFile) {

        ArrayList<String> retList = new ArrayList<>();

        try {

            BufferedReader in = new BufferedReader(new FileReader(pFile));
            String line;

            while ((line = in.readLine()) != null)
                retList.add(line);

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return retList;

    }

    public static void main(String[] args) {

        String test = "Dampfschiffers. Dampfschiffers Dampffahrtschiffahrtsgesellschaft Zusammentreffen, Essensgewohnheiten... Dampffahrtschifffahrtsgesellschaft.";
        for (WordORP entry : Main.convertToSpeedReadingText(test)) {
            System.out.println(entry.getWord() + " : " + entry.getOrp());
        }

    }
}
