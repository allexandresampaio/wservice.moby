package br.edu.ifba.moby;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
		
@Path("ws")
public class Core {
	
	@GET
	@Path("/echo")
	@Produces(MediaType.TEXT_PLAIN)
	public String echo() {
		return "OK";
	}
	
	@GET//era post, mudei para GET e funcionou.
	@Path("/direcionamento/{id}/{localAtual}/{posicaoRelativa}")
	@Produces(MediaType.APPLICATION_JSON)
	public Direcionamento solicitarDirecao(
			@PathParam("id") String id,
			@PathParam("localAtual") String localAtual,
			@PathParam("posicaoRelativa") String posicaoRelativa) {
		
		Direcionamento direcao = new Direcionamento(id, localAtual, posicaoRelativa);
		direcao.calcularDirecao();		
		return direcao;	
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
}
