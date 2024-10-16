package org.example;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertOneModel;
import org.bson.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReutReader {

    ReutReader(){}

    /**
     * Extracts Reuters sections from a file.
     *
     * @param filePath The path to the file containing Reuters data.
     * @return A list of strings representing individual Reuters sections.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public List<String> extractReuters(String filePath) throws IOException {
        List<String> reuters = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder contentBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            contentBuilder.append(line);
        }
        reader.close();

        String fileContent = contentBuilder.toString();

        Pattern reutersPattern = Pattern.compile("<REUTERS[^>]*>(.*?)</REUTERS>", Pattern.DOTALL);
        Matcher reutersMatcher = reutersPattern.matcher(fileContent);

        while (reutersMatcher.find()) {
            String reutersSection = reutersMatcher.group(1).trim();
            reuters.add(reutersSection);
        }
        return reuters;
    }

    /**
     * Extracts title and body from a list of Reuters sections and inserts them into a MongoDB collection.
     *
     * @param reuters    The list of Reuters sections to process.
     * @param collection The MongoDB collection where the data will be inserted.
     */
    public void extractTitleAndBody(List<String> reuters, MongoCollection<Document> collection) {
        Map<String, String> titlesAndBodies = new HashMap<>();
        List<InsertOneModel<Document>> documentsToInsert = new ArrayList<>();

        for (String reutersSection : reuters) {
            String title = "null";
            String body = "null";
            Pattern titlePattern = Pattern.compile("<TITLE>(.*?)</TITLE>");
            Pattern bodyPattern = Pattern.compile("<BODY>(.*?)</BODY>", Pattern.DOTALL);
            Matcher titleMatcher = titlePattern.matcher(reutersSection);
            Matcher bodyMatcher = bodyPattern.matcher(reutersSection);

            if (titleMatcher.find()) {
                title = cleanContent(titleMatcher.group(1));
            }

            if (bodyMatcher.find()) {
                body = cleanContent(bodyMatcher.group(1));
            }
            if (title != null && body != null) {
                titlesAndBodies.put(title, body);
            }
            Document document = new Document("title", title).append("body", body);
            documentsToInsert.add(new InsertOneModel<>(document));
        }

        if (!documentsToInsert.isEmpty()) {
            BulkWriteResult result = collection.bulkWrite(documentsToInsert);
            System.out.println("Inserted " + result.getInsertedCount() + " documents");
        }

    }

    /**
     * Cleans content by removing special characters.
     *
     * @param s The string to be cleaned.
     * @return The cleaned string.
     */
    public String cleanContent(String s){
        String result = s.replaceAll("&lt;", "");
        return result.replaceAll("[^a-zA-Z0-9\\s]", "");
    }
}
