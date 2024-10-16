package org.example;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class provides methods for cleaning text data by removing single characters,
 * XML tags and entities, and stop words.
 */
public class DataCleaner {

    /**
     * Removes single characters from the given text.
     *
     * @param text The input text to clean.
     * @return The text with single characters removed.
     */
    public String removeSingleCharacters(String text) {
        return text.replaceAll("\\b\\w{1}\\b", "");
    }

    /**
     * Removes XML tags and entities from the given text.
     *
     * @param text The input text containing XML tags and entities.
     * @return The text with XML tags and entities removed.
     */
    public String removeXmlTagsAndEntities(String text) {
        // Remove XML tags
        text = text.replaceAll("<[^>]+>", " ");

        // Remove XML entities
        text = text.replaceAll("&[^;]+;", " ");

        return text;
    }

    /**
     * Creates and returns a set of stop words.
     *
     * @return A set containing stop words.
     */
    private static Set<String> getStopWords() {

        String[] stopWordsArray = {
                "i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself",
                "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself",
                "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
                "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had",
                "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as",
                "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through",
                "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off",
                "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how",
                "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not",
                "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should",
                "now"
        };

        return new HashSet<>(Arrays.asList(stopWordsArray));
    }

    /**
     * Removes stop words from the given text.
     *
     * @param text The input text from which to remove stop words.
     * @return The text with stop words removed.
     */
    public String removeStopWords(String text) {
        // Get the set of stop words
        Set<String> stopWords = getStopWords();

        // Tokenize the text into words
        String[] words = text.split("\\s+");

        // Remove stop words from the text
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!stopWords.contains(word.toLowerCase())) {
                result.append(word).append(" ");
            }
        }
        return result.toString().trim();
    }
}
