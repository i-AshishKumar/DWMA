package org.example;

import com.mongodb.client.*;
import org.bson.Document;
import java.io.IOException;
import java.util.*;

public class Main {


    public static void main(String[] args) throws IOException {
        MongoClient connection = MongoClients.create("mongodb+srv://ashishkumarg0877:root@cluster0.zkkiefe.mongodb.net/");

        MongoDatabase db = connection.getDatabase("ReuterDb");
        System.out.println("Connected Successfully");

        MongoCollection<Document> collection = db.getCollection("news_articles");
        System.out.println("Collection Retrieved Successfully");

        BOWSentiment bow = new BOWSentiment();
        List<String> negative = bow.readFileWords("opinion-lexicon-English/negative-words.txt");
        List<String> positive = bow.readFileWords("opinion-lexicon-English/positive-words.txt");

        List<String> titlesList  = new ArrayList<>();

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document article = cursor.next();
                String title = article.getString("title");
                titlesList.add(title);
            }
        } catch (Exception e) {
            throw new IOException("Error retrieving articles: " + e.getMessage());
        }

        bow.writeToCSV(titlesList, positive, negative);
        bow.readDisplayCSV();
    }
}