package com.td;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.currentDate;
import static com.mongodb.client.model.Updates.set;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class UupdateDoc {

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
		        Document.parse("{ item: 'canvas', qty: 100, size: { h: 28, w: 35.5, uom: 'cm' }, status: 'A' }"),
		        Document.parse("{ item: 'journal', qty: 25, size: { h: 14, w: 21, uom: 'cm' }, status: 'A' }"),
		        Document.parse("{ item: 'mat', qty: 85, size: { h: 27.9, w: 35.5, uom: 'cm' }, status: 'A' }"),
		        Document.parse("{ item: 'mousepad', qty: 25, size: { h: 19, w: 22.85, uom: 'cm' }, status: 'P' }"),
		        Document.parse("{ item: 'notebook', qty: 50, size: { h: 8.5, w: 11, uom: 'in' }, status: 'P' }"),
		        Document.parse("{ item: 'paper', qty: 100, size: { h: 8.5, w: 11, uom: 'in' }, status: 'D' }"),
		        Document.parse("{ item: 'planner', qty: 75, size: { h: 22.85, w: 30, uom: 'cm' }, status: 'D' }"),
		        Document.parse("{ item: 'postcard', qty: 45, size: { h: 10, w: 15.25, uom: 'cm' }, status: 'A' }"),
		        Document.parse("{ item: 'sketchbook', qty: 80, size: { h: 14, w: 21, uom: 'cm' }, status: 'A' }"),
		        Document.parse("{ item: 'sketch pad', qty: 95, size: { h: 22.85, w: 30.5, uom: 'cm' }, status: 'A' }")
		));
		
	}
	public static void main(String[] args) {
		MongoUtility.printALLDocuments(collection);
		System.out.println("*****************");
		updateSingleDoc();
		MongoUtility.printALLDocuments(collection);
		System.out.println("*****************");
		updateMultipleDoc();
		MongoUtility.printALLDocuments(collection);
		System.out.println("*****************");
		replaceDoc();
		
		MongoUtility.printALLDocuments(collection);
		System.out.println("*****************");
	}
	
	public static void updateSingleDoc() {
		collection.updateOne(eq("item", "paper"),
		        combine(set("size.uom", "cm"), set("status", "P"), currentDate("lastModified")));
	}
	
	public static void updateMultipleDoc() {
		collection.updateMany(lt("qty", 50),
		        combine(set("size.uom", "in"), set("status", "P"), currentDate("lastModified")));
	}
	
	public static void replaceDoc() {
		collection.replaceOne(eq("item", "paper"),
		        Document.parse("{ item: 'paper', instock: [ { warehouse: 'A', qty: 60 }, { warehouse: 'B', qty: 40 } ] }"));
	}
	
}
