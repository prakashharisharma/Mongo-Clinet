package com.td;

import java.util.Arrays;
import java.util.Collections;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class InsertDoc {

	private static final String TEST_MONGO_DB_NAME = "testDB";

	private static final String TEST_COLLECTION = "inventory";

	private static MongoClient mongoClient = null;

	private static MongoDatabase mongoDatabase = null;

	private static MongoCollection<Document> collection = null;
	
	static {
		// create mongoClient
		mongoClient = new MongoClient("localhost", 27017);

		mongoDatabase = MongoUtility.getDatababase(mongoClient,TEST_MONGO_DB_NAME);

		collection = MongoUtility.getCollection(mongoDatabase,TEST_COLLECTION);
	}
	
	public static void main(String[] args) {
		insertSingleDocument();
		System.out.println("SINGLE DOCUMENT");
		MongoUtility.printALLDocuments(collection);
		insertMultipleDocuments();
		System.out.println("MULTIPLE DOCUMENTS");
		MongoUtility.printALLDocuments(collection);
	}
	
	public static void insertSingleDocument() {
		Document canvas = new Document("item", "canvas")
		        .append("qty", 100)
		        .append("tags", Collections.singletonList("cotton"));

		Document size = new Document("h", 28)
		        .append("w", 35.5)
		        .append("uom", "cm");
		canvas.put("size", size);

		collection.insertOne(canvas);
	}
	
	public static void insertMultipleDocuments() {
		Document journal = new Document("item", "journal")
		        .append("qty", 25)
		        .append("tags", Arrays.asList("blank", "red"));

		Document journalSize = new Document("h", 14)
		        .append("w", 21)
		        .append("uom", "cm");
		journal.put("size", journalSize);

		Document mat = new Document("item", "mat")
		        .append("qty", 85)
		        .append("tags", Collections.singletonList("gray"));

		Document matSize = new Document("h", 27.9)
		        .append("w", 35.5)
		        .append("uom", "cm");
		mat.put("size", matSize);

		Document mousePad = new Document("item", "mousePad")
		        .append("qty", 25)
		        .append("tags", Arrays.asList("gel", "blue"));

		Document mousePadSize = new Document("h", 19)
		        .append("w", 22.85)
		        .append("uom", "cm");
		mousePad.put("size", mousePadSize);

		collection.insertMany(Arrays.asList(journal, mat, mousePad));
	}
}
