package br.edu.ifba.moby;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bson.Document;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.edu.ifba.moby.db.FachadaMongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
		
@Path("ws")
public class Core {
	
	private MongoDatabase db = FachadaMongo.getInstancia().getDB();
	private MongoCollection<Document> colecao = db.getCollection("moby");
	protected MongoCollection<Document> getColecao(){
		return colecao;
	}
	
	@GET
	@Path("/echo")
	@Produces(MediaType.TEXT_PLAIN)
	public String echo() {
		return "OK";
	}
	
	//Pega pela URL e cria um JSON
	@POST//era post, mudei para GET e funcionou.
	@Path("/direcionamento/{id}/{localAtual}/{posicaoRelativa}")
	@Produces(MediaType.APPLICATION_JSON)
	public Direcionamento solicitarDirecao(
			@PathParam("id") String id,
			@PathParam("localAtual") String localAtual,
			@PathParam("posicaoRelativa") String posicaoRelativa) {
		
		Direcionamento direcao = new Direcionamento(id, localAtual, posicaoRelativa);
		direcao.calcularDirecao();
		
		Document documento = new Document();
		documento.append("id", direcao.getId());
		documento.append("localAtual", direcao.getLocalAtual());
		documento.append("posicaoRelativa", direcao.getPosicaoRelativa());
		documento.append("proximaDirecao", direcao.getProximaDirecao());
		colecao.insertOne(documento);
		
		return direcao;	
	}
	
	//Pega por um JSON e devolve a direcao
	@POST//era post, mudei para GET e funcionou.
	@Path("/direcionamento/")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String solicitarDirecao(JSONObject inputJsonObj) throws JSONException {
		
		String id = inputJsonObj.getString("id");
		String localAtual = inputJsonObj.getString("localAtual");
		String posicaoRelativa = inputJsonObj.getString("posicaoRelativa");
		
		Direcionamento direcao = new Direcionamento(id, localAtual, posicaoRelativa);
		direcao.calcularDirecao();	
		
		Document documento = new Document();
		documento.append("id", direcao.getId());
		documento.append("localAtual", direcao.getLocalAtual());
		documento.append("posicaoRelativa", direcao.getPosicaoRelativa());
		documento.append("proximaDirecao", direcao.getProximaDirecao());
		colecao.insertOne(documento);
		
		return direcao.getProximaDirecao();	
	}
	
	@GET
	@Path("/direcao")
	@Produces(MediaType.TEXT_PLAIN)
	public String getDirecao() {	
		Direcionamento d = new Direcionamento("123", "aqui", "norte");
		d.calcularDirecao();
		return d.getProximaDirecao();
	}
	
	@GET
	@Path("/tostring")
	@Produces(MediaType.TEXT_PLAIN)
	public String toString() {	
		Direcionamento d = new Direcionamento("123", "aqui", "norte");
		d.calcularDirecao();
		return d.toString();
	}
	
	@GET
	@Path("/direcaoMaps")
	@Produces(MediaType.TEXT_PLAIN)
	public String getDirecaoMaps() {	
		Direcionamento d = new Direcionamento("123", "-14.858849,-40.820753", "norte", "-14.859793,-40.821965");
		d.calcularDirecaoMaps("AIzaSyBIGd_k9YaqnMicgl9rdenroLKWYTtqOTk");
		return d.getProximaDirecao();
	}
	
	//Pega por um JSON e devolve a direcao
		@POST//era post, mudei para GET e funcionou.
		@Path("/direcionamentoMaps/")
		@Produces(MediaType.TEXT_PLAIN)
		@Consumes(MediaType.APPLICATION_JSON)
		public String solicitarDirecaoMaps(JSONObject inputJsonObj) throws JSONException {
			
			String id = inputJsonObj.getString("id");
			String localAtual = inputJsonObj.getString("localAtual");
			String localDestino = inputJsonObj.getString("localDestino");
			String posicaoRelativa = inputJsonObj.getString("posicaoRelativa");
			
			Direcionamento direcao = new Direcionamento(id, localAtual, posicaoRelativa, localDestino);
			direcao.calcularDirecaoMaps("AIzaSyBIGd_k9YaqnMicgl9rdenroLKWYTtqOTk");	
			
			Document documento = new Document();
			documento.append("id", direcao.getId());
			documento.append("localAtual", direcao.getLocalAtual());
			documento.append("localDestino", direcao.getDestino());
			documento.append("posicaoRelativa", direcao.getPosicaoRelativa());
			documento.append("proximaDirecao", direcao.getProximaDirecao());
			colecao.insertOne(documento);
			
			return direcao.getProximaDirecao();	
		}
}
