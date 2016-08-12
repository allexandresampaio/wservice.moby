package br.edu.ifba.moby.maps;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class TrataResutado {
	
	public static String retornaProximaDirecao(JSONObject objeto){
		String resultado = "";
		boolean controle = false;
		try {
			JSONArray routes = objeto.getJSONArray("routes");
			for (int i=0; i < routes.length(); i++){
				JSONObject f = routes.getJSONObject(i);
				JSONArray legs = f.getJSONArray("legs");
				for (int j=0; j < legs.length(); j++) {
					JSONObject g = legs.getJSONObject(j);
					JSONArray steps = g.getJSONArray("steps");
					for (int k=0; k < steps.length(); k++) {
						JSONObject h = steps.getJSONObject(k);
						if (h.has("maneuver") && controle==false){
							//JSONArray maneuver = h.getJSONArray("maneuver");
							resultado = h.getString("maneuver");
							System.out.println(resultado);
							controle = true;
						}
					}
				}
			}
		} catch (JSONException e) {
			System.out.println("Não foi possível obter a direção. ");
			e.printStackTrace();
		}
		
		System.out.println("");
		
		return resultado;
	}

}
