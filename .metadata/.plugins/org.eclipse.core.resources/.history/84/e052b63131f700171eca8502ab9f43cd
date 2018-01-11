package biactiva.services.principal.peticiones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import biactiva.services.convertidos.csvAJson.AbstractJsonRestEstructura;
import biactiva.services.fileHandler.DirectorioManager;
import biactiva.services.util.json.JsonUtils;

public class GetExistFieldURLQueryRightNow extends AbstractHTTP{





	public GetExistFieldURLQueryRightNow(EPeticiones peticion, String url, JSONObject cabecera) {
		super(peticion, url, cabecera);
	}


	@Override
	protected Object procesarPeticionOK(BufferedReader in, String id, int responseCode) throws Exception {
		// No utilizado
		return null;
	}


	@Override
	protected Object procesarPeticionOK(BufferedReader in, int responseCode) throws Exception {
		JSONObject jsonObject = JsonUtils.convertir(in);
		JSONArray jsonArrayItems= (JSONArray) jsonObject.get("items");
		JSONObject jsonCuerpo=(JSONObject) jsonArrayItems.get(0);
		JSONArray resultados=(JSONArray) jsonCuerpo.get("rows");
		if(resultados.size()>=1)
			return ((JSONArray)resultados.get(0)).get(0);
		return null;
	}


	@Override
	protected Object procesarPeticionOK(BufferedReader in, AbstractJsonRestEstructura json, int responseCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected Object procesarPeticionOK(BufferedReader in, AbstractJsonRestEstructura json, String id, int responseCode)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
