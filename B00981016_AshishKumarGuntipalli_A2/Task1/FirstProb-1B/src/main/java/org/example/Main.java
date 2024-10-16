package org.example;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("ReutersParser")
                .master("local[*]")
                .getOrCreate();

        String filePath = "file:///home/ashishkumarg0877/reut2-009.sgm";
        JavaRDD<String> lines = spark.read().textFile(filePath).javaRDD();
        String content = String.join(" ", lines.collect());

        // Cleaning the content
        DataCleaner clean = new DataCleaner();
        content = clean.removeXmlTagsAndEntities(content);
        content = content.replaceAll("[^a-zA-Z\\s]",  " ");
        content = clean.removeSingleCharacters(content);
        content = clean.removeStopWords(content);

        String[] words = content.split("\\s+");
        Map<String, Integer> wordFreqMap = new HashMap<>();
        for (String word : words) {
            wordFreqMap.put(word, wordFreqMap.getOrDefault(word, 0) + 1);
        }

        List<String> minFreqWords = new ArrayList<>();
        int minFreq = Integer.MAX_VALUE;
        List<String> maxFreqWords = new ArrayList<>();
        int maxFreq = Integer.MIN_VALUE;

        for (Map.Entry<String, Integer> entry : wordFreqMap.entrySet()) {
            int freq = entry.getValue();
            String word = entry.getKey();

            if (freq < minFreq) {
                minFreq = freq;
                minFreqWords.clear();
                minFreqWords.add(word);
            } else if (freq == minFreq) {
                minFreqWords.add(word);
            }

            if (freq > maxFreq) {
                maxFreq = freq;
                maxFreqWords.clear();
                maxFreqWords.add(word);
            } else if (freq == maxFreq) {
                maxFreqWords.add(word);
            }
        }

        System.out.println("First 20 words with minimum frequency (" + minFreq + "):");
        int count = 0;
        for (String word : minFreqWords) {
            System.out.println(word);
            count++;
            if (count >= 20) break;
        }
        System.out.println("Words with maximum frequency (" + maxFreq + "): " + maxFreqWords);

        System.out.println("No. words with min frequnecy: "+minFreqWords.size());
        System.out.println("No. words with max frequnecy: "+maxFreqWords.size());

        spark.stop();
    }
}
