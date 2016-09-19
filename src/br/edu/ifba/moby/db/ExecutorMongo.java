package br.edu.ifba.moby.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.client.MongoDatabase;

public class ExecutorMongo {
	
	public static void main(String[] args) {
		
		MongoDatabase db = FachadaMongo.getInstancia().getDB();
		
		DBCollection collection = (DBCollection) db.getCollection("usuarios");
//		collection.insert(new BasicDBObject().append("nome", "Tex"));
		
		save("Jose", 1, collection);
//		save("Maria", 2, collection);
//		save("Joana", 3, collection);
//		save("Pablo", 4, collection);
//		save("Tay", 5, collection);
//		save("Jai", 6, collection);
//		save("Mai", 7, collection);
//		save("Lay", 8, collection);
		
	}
	
	public static void save (String nome, int id, DBCollection collection){
		BasicDBObject documento = new BasicDBObject();
		documento.put("_id", id);
		documento.put("nome", nome);
		collection.save(documento);
	}

}
