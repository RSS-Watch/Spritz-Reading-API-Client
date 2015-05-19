package org.speedreading.api;

import com.sun.org.apache.bcel.internal.util.ByteSequence;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Andrej on 15.05.2015.
 */
public class Main {

    private static List<String> consonants = Arrays.asList("b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "y", "z", "�");
    private static List<String> vowels = Arrays.asList("a", "e", "i", "o", "u", "�", "�", "�");
    private static List<String> dontSplit2 = Arrays.asList("sh", "ch", "ck", "ph", "rh", "th", "ai", "au", "ei", "eu", "oi", "ae", "oe", "ue", "�u");
    private static List<String> dontSplit3 = Arrays.asList("sch");

    public static LinkedHashMap<String, Integer> convertToSpeedreadingText(String pText) {

        LinkedHashMap<String, Integer> ret = new LinkedHashMap<>();
        ArrayList<String> words = getSplittedText(pText);

        for (String word : words) {
            ret.put(word, getORP(word));
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

    private static ArrayList<String> getSplittedText(String pText) {

        String[] words = pText.split(" ");
        ArrayList<String> ret = new ArrayList<>();

        for (String word : words) {
            while (word.length() > 13) {

                for (int i = 11, j = 12; i >= 0; i--, j--) {

                    // No good point to split found
                    if (i == 0) {
                        ret.add(word.substring(0, 11) + "-");
                        word = word.substring(11);
                        break;
                    }

                    if (dontSplit3.contains("" + word.charAt(i - 1) + word.charAt(i) + word.charAt(j))) {
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

                    if (dontSplit2.contains("" + word.charAt(i) + word.charAt(j))) {
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

    public static void main(String[] args) {

        String test = "Das ist jetzt einfach mal so ein kleiner Test, zur �berpr�fung dieser API und zur Sicherheit schreiben wir noch ein ziemlich langes Wort rein wie z.B.: Dampffahrtschiffahrtsgesellschaftsangestellter";
        String test2 = "Dampffahrtschiffahrtsgesellschaft Zusammentreffen Essensgewohnheiten";
        for (Map.Entry<String, Integer> entry : Main.convertToSpeedreadingText(test2).entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
