package com.td;

import org.bson.BsonType;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;


import java.util.Arrays;
//https://docs.mongodb.com/manual/tutorial/query-documents/
public class QueryDoc {

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
		
		//Execute Only Once
		
		collection.drop();
		
		collection.insertMany(Arrays.asList(
		        Document.parse("{ item: 'journal', qty: 25, size: { h: 14, w: 21, uom: 'cm' }, status: 'A',tags: ['blank', 'red'], dim_cm: [ 14, 21 ] }"),
		        Document.parse("{ item: 'notebook', qty: 50, size: { h: 8.5, w: 11, uom: 'in' }, status: 'A',tags: ['red', 'blank'], dim_cm: [ 14, 21 ] }"),
		        Document.parse("{ item: 'paper', qty: 100, size: { h: 8.5, w: 11, uom: 'in' }, status: 'D',tags: ['red', 'blank', 'plain'], dim_cm: [ 14, 21 ] }"),
		        Document.parse("{ item: 'planner', qty: 75, size: { h: 22.85, w: 30, uom: 'cm' }, status: 'D' }"),
		        Document.parse("{ item: 'postcard', qty: 45, size: { h: 10, w: 15.25, uom: 'cm' }, status: 'A' }"),
		        Document.parse("{'_id': 1, 'item': null}"),
		        Document.parse("{'_id': 2}")
		));
		//
		
	}
	
	public static void main(String[] args) {
		//findAll();
		//findByEquility();
		//findByIn();
		//findByAND();
		//findByOR();
		
		//findByAND_OR();
		//findEmbeddedDoc();
		//QueryArray();
		//QueryArrayElement();
		//QueryArrayCriteria();
		//QueryArrayIndexPos();
		//QueryArrayByLength();
		//selectFields();
		QueryForMissingNullFields();
	}
	
	public static void findAll() {
		//SELECT * FROM inventory
		FindIterable<Document> findIterable = collection.find(new Document());
		
		for(Document doc : findIterable) {
			System.out.println(doc.toJson());
		}
	}
	
	public static void findByEquility() {
		//SELECT * FROM inventory WHERE status = "D"
		
		FindIterable<Document>  findIterable = collection.find(eq("status", "D"));
		print(findIterable);
	}
	
	public static void findByIn() {
		//SELECT * FROM inventory WHERE status in ("A", "D")
		FindIterable<Document> findIterable = collection.find(in("status", "A", "D"));
		print(findIterable);
	}
	public static void findByAND() {
		//SELECT * FROM inventory WHERE status = "A" AND qty < 30
		FindIterable<Document> findIterable = collection.find(and(eq("status", "A"), lt("qty", 30)));
		print(findIterable);
	}
	public static void findByOR() {
		//SELECT * FROM inventory WHERE status = "A" OR qty < 30
		FindIterable<Document> findIterable = collection.find(or(eq("status", "A"), lt("qty", 30)));
		print(findIterable);
	}
	
	public static void findByAND_OR() {
		//SELECT * FROM inventory WHERE status = "A" AND ( qty < 30 OR item LIKE "p%")
		FindIterable<Document> findIterable = collection.find(
		        and(eq("status", "A"),
		                or(lt("qty", 30), regex("item", "^p")))
		);
		print(findIterable);
	}
	public static void findEmbeddedDoc() {
		//FindIterable<Document> findIterable = collection.find(eq("size", Document.parse("{ h: 14, w: 21, uom: 'cm' }")));
		FindIterable<Document> findIterable = collection.find(eq("size.uom", "in"));
		print(findIterable);
	}
	public static void QueryArray() {
		FindIterable<Document> findIterable = collection.find(eq("tags", Arrays.asList("red", "blank")));
		print(findIterable);
	}
	public static void QueryArrayElement() {
		//FindIterable<Document> findIterable = collection.find(eq("tags", "red"));
		FindIterable<Document> findIterable = collection.find(gt("dim_cm", 10));
		print(findIterable);
	}
	public static void QueryArrayCriteria() {
		//FindIterable<Document> findIterable = collection.find(elemMatch("dim_cm", Document.parse("{ $gt: 10, $lt: 30 }")));
		//print(findIterable);
	}
	public static void QueryArrayIndexPos() {
		FindIterable<Document> findIterable  = collection.find(gt("dim_cm.1", 25));
		print(findIterable);
	}
	public static void QueryArrayByLength() {
		FindIterable<Document> findIterable  = collection.find(size("tags", 3));
		print(findIterable);
	}
	public static void selectFields() {
		FindIterable<Document> findIterable  = collection.find(eq("status", "A"))
		        .projection(fields(include("item", "status"), excludeId()));
		print(findIterable);
	}
	public static void QueryForMissingNullFields() {
		//The eq("item", null) query matches documents that either contain the item field whose value is null or that do not contain the item field.
		
		FindIterable<Document> findIterable = collection.find(eq("item", null));
		print(findIterable);
		System.out.println("**************");
		//The type("item", BsonType.NULL) query matches only documents that contain the item field whose value is null;
		findIterable = collection.find(type("item", BsonType.NULL));
		
		print(findIterable);
		System.out.println("**************");
		//The exists("item", false) query matches documents that do not contain the item field:
		findIterable = collection.find(exists("item", false));
		
		print(findIterable);
	}
	
	public static void print(FindIterable<Document>  findIterable) {
		for(Document doc : findIterable) {
			System.out.println(doc.toJson());
		}
	}
}
