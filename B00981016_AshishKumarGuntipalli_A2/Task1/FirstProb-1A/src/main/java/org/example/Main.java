package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        MongoClient connection = MongoClients.create("mongodb+srv://ashishkumarg0877:root@cluster0.zkkiefe.mongodb.net/");
        MongoDatabase db = connection.getDatabase("ReuterDb");
        System.out.println("Connected to MongoDB Successfully");

        ReutReader r = new ReutReader();
        String filePath = "reut2-009.sgm";
        List<String> reutersText = r.extractReuters(filePath);

        db.createCollection("news_articles");
        System.out.println("Collection Created Successfully");
        MongoCollection<Document> collection = db.getCollection("news_articles");

        r.extractTitleAndBody(reutersText,collection);

        System.out.println("Added documents to Collection Successfully");

    }
}