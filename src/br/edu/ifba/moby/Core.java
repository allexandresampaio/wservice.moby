package br.edu.ifba.moby;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.omg.CORBA.portable.ApplicationException;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.edu.ifba.moby.db.FachadaMongo;

@Path("ws")
public class Core {

	private MongoDatabase db = FachadaMongo.getInstancia().getDB();
	private MongoCollection<Document> colecao = db.getCollection("moby");

	protected MongoCollection<Document> getColecao() {
		return colecao;
	}

	
	@GET
	@Path("/echo")
	@Produces(MediaType.TEXT_PLAIN)
	public String echo() {
		return "OK";
	}

	
	// Pega por um JSON e devolve a direcao
	@POST
	// era post, mudei para GET e funcionou.
	@Path("/direcionamentoMaps/")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String solicitarDirecaoMaps(JSONObject inputJsonObj)
			throws JSONException {

		String id = inputJsonObj.getString("id");
		String localAtual = inputJsonObj.getString("localAtual");
		String localDestino = inputJsonObj.getString("localDestino");
		String posicaoRelativa = inputJsonObj.getString("posicaoRelativa");

		Direcionamento direcao = new Direcionamento(id, localAtual,
				posicaoRelativa, localDestino);
		direcao.calcularDirecaoMaps("AIzaSyBIGd_k9YaqnMicgl9rdenroLKWYTtqOTk");

		Document documento = new Document();
		documento.append("id", direcao.getId());
		documento.append("localAtual", direcao.getLocalAtual());
		documento.append("localDestino", direcao.getLocalDestino());
		documento.append("posicaoRelativa", direcao.getPosicaoRelativa());
		documento.append("proximaDirecao", direcao.getProximaDirecao());
		colecao.insertOne(documento);

		return direcao.getProximaDirecao();
	}
	
	
	@GET
	@Path("/registros")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Direcionamento> listarDirecionamentos() {
		return FachadaMongo.getInstancia().find();
	}

	// Pega por um JSON e devolve a direcao
	@POST
	@Path("/direcionamentoMapsNovo/")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String solicitarDirecaoMapsNovo(JSONObject inputJsonObj)
			throws JSONException, ParseException {
		
		String dateTime = getDateTime();

		String id = inputJsonObj.getString("id");
		String localAtual = inputJsonObj.getString("localAtual");
		String posicaoRelativa = inputJsonObj.getString("posicaoRelativa");
		
		cadastrarLocalizacao(id, localAtual, dateTime);
		Direcionamento direcao = new Direcionamento(id, posicaoRelativa);
		direcao.calcularDirecaoMaps("AIzaSyBIGd_k9YaqnMicgl9rdenroLKWYTtqOTk");

		Document documento = new Document();
		documento.append("id", direcao.getId());
		documento.append("localAtual", direcao.getLocalAtual());
		documento.append("localDestino", direcao.getLocalDestino());
		documento.append("posicaoRelativa", direcao.getPosicaoRelativa());
		documento.append("proximaDirecao", direcao.getProximaDirecao());
		colecao.insertOne(documento);

		return direcao.getProximaDirecao();
		
		
	}
	
	//faz o mesmo direcionamento, soh que agora pega no banco
	//a ultima localizacao do usuario pelo id e calcula
	//ultimalocalizacao ate o destino final
	@POST
	@Path("/direcionamentoMapsESP/")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String solicitarDirecaoMapsESP(JSONObject inputJsonObj)
			throws JSONException, ParseException {
		
		String id = inputJsonObj.getString("id");
		String posicaoRelativa = inputJsonObj.getString("posicaoRelativa");

		Direcionamento direcao = new Direcionamento(id, posicaoRelativa);
		direcao.calcularDirecaoMaps("AIzaSyBIGd_k9YaqnMicgl9rdenroLKWYTtqOTk");

		Document documento = new Document();
		documento.append("id", direcao.getId());
		documento.append("localAtual", direcao.getLocalAtual());
		documento.append("localDestino", direcao.getLocalDestino());
		documento.append("posicaoRelativa", direcao.getPosicaoRelativa());
		documento.append("proximaDirecao", direcao.getProximaDirecao());
		colecao.insertOne(documento);

		return direcao.getProximaDirecao();
	}
	@POST
	@Path("/destino/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cadastrarDestino(JSONObject inputJsonObj)
			throws IOException, ApplicationException, JSONException, ParseException {

		String id = inputJsonObj.getString("id");
		String datetime = inputJsonObj.getString("datetime");
		String destino = inputJsonObj.getString("destino");
		FachadaMongo.getInstancia().insertDestino(id, destino, datetime);
	
		return  Response.ok()
				.build();
		}
	
	public void cadastrarLocalizacao(String id, String coordenada, String datetime) throws ParseException{
		FachadaMongo.getInstancia().insertUltimaLocalizacao(id, coordenada, datetime);
	}

	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date date = new Date();
		return dateFormat.format(date);
	}

}
