package com.td;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

public class DeleteDoc {
	
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
		
		//

		collection.drop();
		
		collection.insertMany(Arrays.asList(
		        Document.parse("{ item: 'journal', qty: 25, size: { h: 14, w: 21, uom: 'cm' }, status: 'A' }"),
		        Document.parse("{ item: 'notebook', qty: 50, size: { h: 8.5, w: 11, uom: 'in' }, status: 'A' }"),
		        Document.parse("{ item: 'paper', qty: 100, size: { h: 8.5, w: 11, uom: 'in' }, status: 'D' }"),
		        Document.parse("{ item: 'planner', qty: 75, size: { h: 22.85, w: 30, uom: 'cm' }, status: 'D' }"),
		        Document.parse("{ item: 'postcard', qty: 45, size: { h: 10, w: 15.25, uom: 'cm' }, status: 'A' }")
		));
		
	}
	
	public static void main(String[] args) {
		MongoUtility.printALLDocuments(collection);
		System.out.println("*****************");
		delteOneMatchConditio();
		MongoUtility.printALLDocuments(collection);
		System.out.println("*****************");
		delteAllMatchCondition();
		MongoUtility.printALLDocuments(collection);
		System.out.println("*****************");
		deleteAll();
		MongoUtility.printALLDocuments(collection);
		System.out.println("*****************");
	}
	
	public static void deleteAll() {
		collection.deleteMany(new Document());
	}
	
	public static void delteAllMatchCondition() {
		collection.deleteMany(eq("status", "A"));
	}
	public static void delteOneMatchConditio() {
		collection.deleteOne(eq("status", "D"));
	}
}
