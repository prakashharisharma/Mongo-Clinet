package com.td;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;
import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;

//http://mongodb.github.io/mongo-java-driver/3.4/driver/getting-started/quick-start/
//https://docs.mongodb.com/manual/tutorial/insert-documents/

public class MongoExample {

	private static final String TEST_MONGO_DB_NAME = "testDB";

	private static final String TEST_COLLECTION = "testCol";

	private static MongoClient mongoClient = null;

	private static MongoDatabase database = null;

	private static MongoCollection<Document> collection = null;

	public static void main(String[] args) {

		// create mongoClient
		mongoClient = new MongoClient("localhost", 27017);

		database = getDatababase(TEST_MONGO_DB_NAME);

		collection = getCollection(TEST_COLLECTION);

		// Create Document
		Document doc = new Document("name", "MongoDB").append("type", "database").append("count", 1)
				.append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
				.append("info", new Document("x", 203).append("y", 102));

		insertOne(doc);

		// insertMany();

		collectionCount();
		findFirst();
		findAll();
		
		//singleDocFilterMatch();
		
		//multipleDocFilterMatch();
		
	//	updateSingleDoc();
		//updateManyDoc();
		
		//deleteSingleDoc();
		//deleteManyDoc();
		getIndexList();
	}

	public static MongoDatabase getDatababase(String dbName) {

		MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);

		System.out.println(mongoDatabase.getName());

		return mongoDatabase;
	}

	public static MongoCollection<Document> getCollection(String collectionName) {
		return database.getCollection("testcol");
	}

	public static void findAll() {
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
			}
		} finally {
			cursor.close();
		}
	}

	public static void collectionCount() {
		System.out.println(collection.count());
	}

	public static void insertMany() {
		List<Document> documents = new ArrayList<Document>();

		for (int i = 0; i < 100; i++) {
			documents.add(new Document("i", i));
		}

		collection.insertMany(documents);

	}

	public static void insertOne(Document doc) {
		collection.insertOne(doc);
	}

	public static void findFirst() {
		Document myDoc = collection.find().first();

		System.out.println(myDoc.toJson());
	}
	
	public static void singleDocFilterMatch() {
		Document myDoc = collection.find(eq("i", 71)).first();
		System.out.println(myDoc.toJson());
	}
	
	public static void multipleDocFilterMatch() {
		Block<Document> printBlock = new Block<Document>() {
		     @Override
		     public void apply(final Document document) {
		         System.out.println(document.toJson());
		     }
		};

		collection.find(gt("i", 50)).forEach(printBlock);
	}
	
	public static void updateSingleDoc() {
		collection.updateOne(eq("i", 10), new Document("$set", new Document("i", 110)));
	}
	public static void updateManyDoc() {
		UpdateResult updateResult = collection.updateMany(lt("i", 100), inc("i", 100));
		System.out.println(updateResult.getModifiedCount());
	}
	
	public static void deleteSingleDoc() {
		collection.deleteOne(eq("i", 110));
	}
	
	public static void deleteManyDoc() {
		DeleteResult deleteResult = collection.deleteMany(gte("i", 100));
		System.out.println(deleteResult.getDeletedCount());
	}
	
	public static void createSingleIndex() {
		collection.createIndex(Indexes.ascending("name"));
		//collection.createIndex(Indexes.descending("stars"));
	}
	
	public static void createCompoundIndex() {
		collection.createIndex(Indexes.ascending("stars", "name"));
		//collection.createIndex(Indexes.descending("stars", "name"));
		
		//collection.createIndex(Indexes.compoundIndex(Indexes.descending("stars"), Indexes.ascending("name")));
		
	}
	public static void createTextIndex() {
		collection.createIndex(Indexes.text("name"));
	}
	
	public static void uniqueIndex() {
		IndexOptions indexOptions = new IndexOptions().unique(true);
		collection.createIndex(Indexes.ascending("name", "stars"), indexOptions);
	}
	
	public static void getIndexList() {
		for (Document index : collection.listIndexes()) {
		    System.out.println(index.toJson());
		}
	}
}
