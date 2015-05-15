package org.speedreading.api;

import com.sun.org.apache.bcel.internal.util.ByteSequence;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andrej on 15.05.2015.
 */
public class Main {

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
                ret.add(word.substring(0, 11) + "-");
                word = word.substring(12);
            }

            ret.add(word);
        }

        return ret;
    }

    public static void main(String[] args) {

        String test = "Das ist jetzt einfach mal so ein kleiner Test, zur Überprüfung dieser API und zur Sicherheit schreiben wir noch ein ziemlich langes Wort rein wie z.B.: Dampfahrtschifffahrtsgesellschaftsangestellter";

        for (Map.Entry<String, Integer> entry : Main.convertToSpeedreadingText(test).entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
