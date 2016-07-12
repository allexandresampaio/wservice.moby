package br.edu.ifba.moby.db;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class FachadaMongo {

	private static FachadaMongo instancia = null;
	
	private static final String HOST = "localhost";
	private static final int PORT = 27017;
	private static final String DB_NAME = "moby";
	
	private FachadaMongo(){}
	
	public static FachadaMongo getInstancia (){
		if (instancia == null){
			instancia = new FachadaMongo();
		}
		return instancia;
	}
	
	public MongoDatabase getDB() {
			MongoClient mongoClient = new MongoClient(HOST, PORT); 
			MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		return db;
	}
}
