package br.edu.ifba.moby.maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

//http://maps.googleapis.com/maps/api/directions/output?parameters

@SuppressWarnings("deprecation")
public class Buscador {

	public static final String URL = "https://maps.googleapis.com/maps/api/directions/";

	public String pegarRota(String origin, String destination, String key) {
		String resultado = "";

		@SuppressWarnings({ "deprecation", "resource" })
		HttpClient cliente = new DefaultHttpClient();
		HttpGet get = new HttpGet(URL + "json?origin=" + origin + "&destination="
				+ destination + "&language=pt-BR&key=" + key);
		HttpResponse resposta;
		try {
			resposta = cliente.execute(get);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					resposta.getEntity().getContent()));
			resultado = br.readLine();//só lê a primeira linha. tem que pegar tudo...
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return resultado;
	}

}
