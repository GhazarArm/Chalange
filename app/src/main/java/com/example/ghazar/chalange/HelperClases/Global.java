package com.example.ghazar.chalange.HelperClases;

/**
 * Created by ghazar on 12/26/17.
 */

public class Global {

    //return words of the string
    public static String[] getWords(String text){
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll("[^\\w]", "");
        }

        return words;
    }

}
