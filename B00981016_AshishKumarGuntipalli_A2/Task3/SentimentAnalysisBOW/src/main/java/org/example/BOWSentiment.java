package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BOWSentiment {

    /**
     * Reads a file and returns its content as a list of words.
     *
     * @param filePath The path to the file to be read.
     * @return A list containing words from the file.
     */
    public List<String> readFileWords(String filePath) {
        List<String> wordsList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordsList.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordsList;
    }

    /**
     * Creates a bag of words from the given text.
     *
     * @param text The text to create the bag of words from.
     * @return A map representing the bag of words with word frequencies.
     */
    public Map<String, Integer> createBagOfWords(String text) {
        Map<String, Integer> bagOfWords = new HashMap<>();

        String[] words = text.split("\\s+");

        for (String word : words) {
            word = word.toLowerCase();
            bagOfWords.put(word, bagOfWords.getOrDefault(word, 0) + 1);
        }
        return bagOfWords;
    }

    /**
     * Counts the sentiment of a document using a bag of words approach.
     *
     * @param document      The document to analyze.
     * @param bagOfWords    The bag of words to use for sentiment analysis.
     * @param positiveWords A list of positive words.
     * @param negativeWords A list of negative words.
     * @return A SentimentAnalysisResult object containing matched words and sentiment score.
     */
    public SentimentAnalysisResult countSentiment(String document, Map<String, Integer> bagOfWords, List<String> positiveWords, List<String> negativeWords) {
        List<String> matchedWords = new ArrayList<>();
        int sentimentScore = 0;

        for (Map.Entry<String, Integer> entry : bagOfWords.entrySet()) {
            String word = entry.getKey();
            int count = entry.getValue();

            if (positiveWords.contains(word)) {
                matchedWords.add(word);
                sentimentScore += count;
            } else if (negativeWords.contains(word)) {
                matchedWords.add(word);
                sentimentScore -= count;
            }
        }

        return new SentimentAnalysisResult(document, matchedWords, sentimentScore);
    }

    /**
     * Writes sentiment analysis results to a CSV file.
     *
     * @param titlesList A list of titles.
     * @param positive   A list of positive words.
     * @param negative   A list of negative words.
     * @throws IOException If an I/O error occurs.
     */
    public void writeToCSV(List<String> titlesList, List<String> positive, List<String> negative) throws IOException {
        List<String[]> allRows = new ArrayList<>();

        String[] headings = {"News#", "Title Content", "Matched Words", "Score", "Polarity"};
        allRows.add(headings);

        for (int i = 0; i < titlesList.size(); i++) {
            String titleContent = titlesList.get(i);
            int newsNo = i + 1;

            Map<String, Integer> bagOfWords = createBagOfWords(titleContent);
            SentimentAnalysisResult sentimentResult = countSentiment(titleContent, bagOfWords, positive, negative);
            List<String> matchedWords = sentimentResult.getMatchedWords();
            int sentimentScore = sentimentResult.getSentimentScore();

            String polarity;
            if (sentimentScore > 0) {
                polarity = "Positive";
            } else if (sentimentScore < 0) {
                polarity = "Negative";
            } else {
                polarity = "Neutral";
            }

            String[] row = {String.valueOf(newsNo), titleContent, String.join(", ", matchedWords), String.valueOf(sentimentScore), polarity};
            allRows.add(row);
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter("sentiments.csv"))) {
            writer.writeAll(allRows);
            System.out.println("Data written successfully to sentiments.csv");
        } catch (IOException e) {
            throw new IOException("Error writing to CSV: " + e.getMessage());
        }
    }

    /**
     * Reads and displays data from a CSV file on console.
     */
    public void readDisplayCSV(){
        try (CSVReader reader = new CSVReader(new FileReader("sentiments.csv"))) {
            List<String[]> rows = reader.readAll();
            int rowCount = 0;

            List<Integer> columnWidths = getColumnWidths(rows);

            for (String[] row : rows) {
                if (rowCount == 0) {
                    printSeparator(columnWidths);
                    printRow(row, columnWidths);
                    printSeparator(columnWidths);
                } else {
                    printRow(row, columnWidths);
                }
                rowCount++;
            }

            System.out.println("\nTotal rows: " + rowCount);
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints a row of data with proper formatting.
     *
     * @param row          The row of data to print.
     * @param columnWidths The widths of columns.
     */
    private static void printRow(String[] row, List<Integer> columnWidths) {
        for (int i = 0; i < row.length; i++) {
            String cell = row[i];
            System.out.printf("| %-"+ columnWidths.get(i) +"s ", cell); // Adjust the width as needed
        }
        System.out.println("|");
    }

    /**
     * Prints a separator line between rows.
     *
     * @param columnWidths The widths of columns.
     */
    private static void printSeparator(List<Integer> columnWidths) {
        for (int width : columnWidths) {
            System.out.print("+");
            for (int i = 0; i < width + 2; i++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
    }

    /**
     * Calculates the widths of columns in a list of rows.
     *
     * @param rows The list of rows.
     * @return A list of column widths.
     */
    private static List<Integer> getColumnWidths(List<String[]> rows) {
        List<Integer> columnWidths = new ArrayList<>();

        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                if (columnWidths.size() <= i) {
                    columnWidths.add(row[i].length());
                } else {
                    int width = Math.max(columnWidths.get(i), row[i].length());
                    columnWidths.set(i, width);
                }
            }
        }

        return columnWidths;
    }
}
