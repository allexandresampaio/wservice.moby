package br.edu.ifba.moby.maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

//http://maps.googleapis.com/maps/api/directions/output?parameters

@SuppressWarnings("deprecation")
public class Buscador {

	public static final String URL = "https://maps.googleapis.com/maps/api/directions/";

	public String pegarRota(String origin, String destination, String key) {
		String resultado = "";

		@SuppressWarnings({ "deprecation", "resource" })
		HttpClient cliente = new DefaultHttpClient();
		HttpGet get = new HttpGet(URL + "json?origin=" + origin
				+ "&destination=" + destination + "&language=pt-BR&key=" + key);
		HttpResponse resposta;
		try {
			resposta = cliente.execute(get);
//			BufferedReader br = new BufferedReader(new InputStreamReader(
//					resposta.getEntity().getContent()));
//			resultado = br.readLine();//só lê a primeira linha. tem que pegartudo...

			InputStream br = resposta.getEntity().getContent();			
			resultado = convertStreamToString(br);
			JSONObject myObject = new JSONObject(resultado);
			resultado = TrataResutado.retornaProximaDirecao(myObject);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println("erro na conversão para JSON");
			e.printStackTrace();
		}
		return resultado;
	}

	private String convertStreamToString(InputStream br) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(br));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
