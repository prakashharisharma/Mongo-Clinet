package com.td;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoUtility {

	public static MongoDatabase getDatababase(MongoClient mongoClient,String dbName) {

		MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);

		System.out.println(mongoDatabase.getName());

		return mongoDatabase;
	}

	public static MongoCollection<Document> getCollection(MongoDatabase mongoDatabase, String collectionName) {
		return mongoDatabase.getCollection("testcol");
	}
	
	public static void printALLDocuments( MongoCollection<Document> collection) {
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
			}
		} finally {
			cursor.close();
		}
	}
}
