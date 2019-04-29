package com.google.engedu.anagrams;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private HashSet<String> wordSet;
    private ArrayList<String> wordList;
    private HashMap<String,ArrayList<String>> lettersToWord;
    private HashMap<Integer,ArrayList<String>> sizeToWords;
    private int wordLength;


    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        wordList = new ArrayList<>();
        wordSet=new HashSet<>();
        lettersToWord=new HashMap<String,ArrayList<String>>();
        sizeToWords=new  HashMap<Integer,ArrayList<String>>();
        wordLength = DEFAULT_WORD_LENGTH;
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            if(lettersToWord.containsKey(sortLetters(word)))
            {
                ArrayList<String> list = lettersToWord.get(sortLetters(word));
                list.add(word);
                lettersToWord.put(sortLetters(word), list);

            }
            else
            {
                ArrayList<String> var = new ArrayList<String>();
                var.add(word);
                lettersToWord.put(sortLetters(word),var);
            }
            if(sizeToWords.containsKey(word.length()))
            {
                ArrayList<String> list = sizeToWords.get(word.length());
                list.add(word);
                sizeToWords.put(word.length(), list);

            }
            else
            {
                ArrayList<String> var = new ArrayList<String>();
                var.add(word);
                sizeToWords.put(word.length(),var);
            }
        }

    }

    private String sortLetters(String s) {

        char[] c = s.toCharArray();
        Arrays.sort(c);
        String sorted = new String(c);
        return sorted;
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    ArrayList<String> result = new ArrayList<String>();

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();


        result=lettersToWord.get(sortLetters(targetWord));
        ArrayList<String> result1 = new ArrayList<String>();
        result1=getAnagramsWithOneMoreLetter(targetWord);
        for(String word:result1)
            result.add(word);
        return result;

    }


    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char alphabet = 'a';alphabet<='z';alphabet++) {

            String a = word + alphabet;
            if (lettersToWord.containsKey(sortLetters(word + alphabet))) {
                ArrayList<String> temp = lettersToWord.get(sortLetters(a));
                for (String item : temp) {
                    result.add(item);
                }
            }

        }
        return result;
    }


    public String pickGoodStarterWord() {
        ArrayList<String> wordListSize = sizeToWords.get(wordLength);
        Random r = new Random();
        int size = wordListSize.size();
        int n = r.nextInt(size);
        for (int i = n; i < n + size; i++) {
            String word = wordListSize.get(i % size);
            ArrayList<String> arr = lettersToWord.get(sortLetters(word));
            if (arr.size() >= MIN_NUM_ANAGRAMS) {
                wordLength++;
                if(wordLength==8)
                    wordLength=DEFAULT_WORD_LENGTH;
                return word;
            }
        }
        wordLength++;
        return "stop";
    }
}