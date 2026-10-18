package com.example.task03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Task03Main {

    private static final String DICTIONARY = "/singular.txt";

    private static final Charset DICTIONARY_CHARSET = Charset.forName("windows-1251");

    public static void main(String[] args) throws IOException {

        try (InputStream dictionary = Task03Main.class.getResourceAsStream(DICTIONARY)) {
            List<Set<String>> anagrams = findAnagrams(dictionary, DICTIONARY_CHARSET);
            for (Set<String> anagram : anagrams) {
                System.out.println(anagram);
            }
        }

    }

    public static List<Set<String>> findAnagrams(InputStream inputStream, Charset charset) {
        if (inputStream == null) {
            return Collections.emptyList();
        }

        Set<String> uniqueWords = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toLowerCase();

                if (word.length() >= 3 && word.matches("^[а-яё]+$")) {
                    uniqueWords.add(word);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при чтении словаря", e);
        }

        Map<String, TreeSet<String>> anagramGroups = new HashMap<>();
        for (String word : uniqueWords) {
            char[] chars = word.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);

            anagramGroups.computeIfAbsent(key, k -> new TreeSet<>()).add(word);
        }

        List<Set<String>> result = new ArrayList<>();
        for (TreeSet<String> group : anagramGroups.values()) {
            if (group.size() >= 2) {
                result.add(group);
            }
        }

        result.sort(Comparator.comparing(group -> group.iterator().next()));

        return result;
    }
}
