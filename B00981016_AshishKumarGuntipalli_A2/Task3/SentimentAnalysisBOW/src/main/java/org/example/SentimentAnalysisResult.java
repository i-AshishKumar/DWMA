package org.example;

import java.util.List;

/**
 * Represents the result of sentiment analysis.
 */
public class SentimentAnalysisResult {
    private String document;
    private List<String> matchedWords;
    private int sentimentScore;

    /**
     * Constructs a SentimentAnalysisResult object.
     *
     * @param document       The document analyzed for sentiment.
     * @param matchedWords   The list of words matched during analysis.
     * @param sentimentScore The sentiment score calculated from the analysis.
     */
    public SentimentAnalysisResult(String document, List<String> matchedWords, int sentimentScore) {
        this.document = document;
        this.matchedWords = matchedWords;
        this.sentimentScore = sentimentScore;
    }

    /**
     * Retrieves the document associated with this sentiment analysis result.
     *
     * @return The document.
     */
    public String getDocument() {
        return document;
    }

    /**
     * Retrieves the list of words matched during sentiment analysis.
     *
     * @return The list of matched words.
     */
    public List<String> getMatchedWords() {
        return matchedWords;
    }

    /**
     * Retrieves the sentiment score calculated from the analysis.
     *
     * @return The sentiment score.
     */
    public int getSentimentScore() {
        return sentimentScore;
    }
}
