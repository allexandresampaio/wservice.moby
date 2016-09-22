package br.edu.ifba.moby.db;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.codehaus.jackson.map.util.Comparators;

import com.mongodb.Block;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.edu.ifba.moby.Destino;
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

	// /////////////////////////// /////////////////////////////
	public MongoCollection getColecao(String colecao) {
		MongoCollection col = FachadaMongo.getInstancia().getDB()
				.getCollection(colecao);
		return col;
	}

	// /////////////////////////// /////////////////////////////
	// FIND
	public List<Direcionamento> find() {
		MongoCollection colecao = FachadaMongo.getInstancia()
				.getColecao("moby");
		List<Direcionamento> direcionamentos = new ArrayList<Direcionamento>();
		FindIterable<Document> i = colecao.find();
		i.forEach(new Block<Document>() {
			@Override
			public void apply(Document documento) {
				Direcionamento direcionamento = new Direcionamento();
				direcionamento.setId(documento.get("id").toString());
				direcionamento.setLocalAtual(documento.getString("localAtual"));
				direcionamento.setDestino(documento.getString("localDestino"));
				direcionamento.setPosicaoRelativa(documento
						.getString("posicaoRelativa"));
				direcionamento.setProximaDirecao(documento
						.getString("proximaDirecao"));
				direcionamentos.add(direcionamento);
			}
		});
		return direcionamentos;
	}

	// PEGA O ULTIMO LOCAL DE DESTINO DO CARA
	public String findDestino(String id) {
		MongoCollection colecao = FachadaMongo.getInstancia().getColecao(
				"destinos");
		List<Destino> coordenadas = new ArrayList<Destino>();
		FindIterable<Document> i = colecao.find(new Document("id", id)).sort(
				new Document("datetime", 1));
		i.forEach(new Block<Document>() {
			@Override
			public void apply(Document documento) {
				Destino destino = new Destino();
				destino.setCoordenadas(documento.getString("coordenadas")
						.toString());
				destino.setId(documento.get("id").toString());
				destino.setDatetime(documento.getDate("datetime"));
				coordenadas.add(destino);
			}
		});
		return coordenadas.get(coordenadas.size()-1).getCoordenadas();
	}

	public void insertDestino(String id, String coordenada, String datetime) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		
		this.getColecao("destinos").insertOne(
				new Document("id", id).append(
						"coordenadas", coordenada).append(
								"datetime", format.parse(datetime)));
		}
}