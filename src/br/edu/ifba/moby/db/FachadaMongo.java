package br.edu.ifba.moby.db;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.edu.ifba.moby.Direcionamento;




public class FachadaMongo {

	private static FachadaMongo instancia = null;

	private static final String HOST = "localhost";
	private static final int PORT = 27017;
	private static final String DB_NAME = "moby";

	private FachadaMongo() {
	}

	public static FachadaMongo getInstancia() {
		if (instancia == null) {
			instancia = new FachadaMongo();
		}
		return instancia;
	}

	public MongoDatabase getDB() {
		MongoClient mongoClient = new MongoClient(HOST, PORT);
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		return db;
	}
	
	///////////////////////////// /////////////////////////////
	public MongoCollection getColecao(String colecao) {
		MongoCollection col = FachadaMongo.getInstancia().getDB().getCollection(colecao);
		return col;
	}

	///////////////////////////// /////////////////////////////
	// FIND
	public List<Direcionamento> find() {
		MongoCollection colecao = FachadaMongo.getInstancia().getColecao("moby");
		List<Direcionamento> direcionamentos = new ArrayList<Direcionamento >();
		FindIterable<Document> i = colecao.find();
		i.forEach(new Block<Document>() {
			@Override
			public void apply(Document documento) {
				Direcionamento direcionamento = new Direcionamento();
				direcionamento.setId(documento.get("id").toString());//essa id é do moby, com _id eh do mongo
				direcionamento.setLocalAtual(documento.getString("localAtual"));
				direcionamento.setDestino(documento.getString("localDestino"));
				direcionamento.setPosicaoRelativa(documento.getString("posicaoRelativa"));
				direcionamento.setProximaDirecao(documento.getString("proximaDirecao"));
				direcionamentos.add(direcionamento);
			}
		});
		return direcionamentos;
	}

}