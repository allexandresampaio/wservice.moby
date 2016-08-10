package br.edu.ifba.moby;

import java.util.Random;

import br.edu.ifba.moby.maps.Buscador;

public class Direcionamento {

	private String id;
	private String localAtual;
	private String posicaoRelativa;
	private String proximaDirecao;
	private String destino;

	public Direcionamento(String id, String localAtual, String posicaoRelativa) {
		this.id = id;
		this.localAtual = localAtual;
		this.posicaoRelativa = posicaoRelativa;
		this.proximaDirecao = null;
		this.destino = null;
	};

	public Direcionamento(String id, String localAtual, String posicaoRelativa,
			String destino) {
		this.id = id;
		this.localAtual = localAtual;
		this.destino = destino;
		this.posicaoRelativa = posicaoRelativa;
		this.proximaDirecao = null;
	};

	public void calcularDirecaoMaps(String key){
		Buscador buscador = new Buscador();
		String rota;
		rota = buscador.pegarRota(localAtual, destino, key);
		
		System.out.println(rota);
		
		this.proximaDirecao = rota;
	}

	public void calcularDirecao() {

		Random gerador = new Random();
		int direcao = gerador.nextInt(4) + 1;

		switch (direcao) {
		case 1:
			this.proximaDirecao = "direita";
			break;
		case 2:
			this.proximaDirecao = "esquerda";
			break;
		case 3:
			this.proximaDirecao = "em frente";
			break;
		case 4:
			this.proximaDirecao = "retorne";
			break;
		default:
			this.proximaDirecao = "#";
			;
		}

		System.out.println(this.toString());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocalAtual() {
		return localAtual;
	}

	public void setLocalAtual(String localAtual) {
		this.localAtual = localAtual;
	}

	public String getPosicaoRelativa() {
		return posicaoRelativa;
	}

	public void setPosicaoRelativa(String posicaoRelativa) {
		this.posicaoRelativa = posicaoRelativa;
	}

	public String getProximaDirecao() {
		return proximaDirecao;
	}

	public void setProximaDirecao(String proximaDirecao) {
		this.proximaDirecao = proximaDirecao;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	@Override
	public String toString() {
		return "Direcionamento [id=" + id + ", localAtual=" + localAtual
				+ ", posicaoRelativa=" + posicaoRelativa + ", proximaDirecao="
				+ proximaDirecao + "]";
	}

}
