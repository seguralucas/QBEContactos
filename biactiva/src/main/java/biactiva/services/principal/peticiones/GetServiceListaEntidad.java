package biactiva.services.principal.peticiones;

import java.io.BufferedReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import biactiva.services.convertidor.jsonAJson.ConvertirJsonAJson;
import biactiva.services.convertidos.csvAJson.AbstractJsonRestEstructura;
import biactiva.services.singletons.ConfiguracionEntidadParticular;
import biactiva.services.singletons.RecEntAct;
import biactiva.services.util.json.JsonUtils;

public class GetServiceListaEntidad extends AbstractHTTP{



	public GetServiceListaEntidad(EPeticiones peticion, String url, JSONObject cabecera) {
		super(peticion, url, cabecera);
	}

	@Override
	protected Object procesarPeticionOK(BufferedReader in, String id, int responseCode) throws Exception {
		JSONObject json = JsonUtils.convertir(in);
		ConfiguracionEntidadParticular r = RecEntAct.getInstance().getCep();
		ConvertirJsonAJson conv= new ConvertirJsonAJson(r,json);
		AbstractJsonRestEstructura jsonRest= conv.convertir();
		System.out.println(jsonRest);
		String url=r.getUrlVerificarExistencia(json);
		if(url==null)
			return null;
		GetExistsFieldInSales get = new GetExistsFieldInSales(EPeticiones.GET, url, r.getCabeceraInsertar());
		return get.realizarPeticion();
	}

	@Override
	protected Object procesarPeticionOK(BufferedReader in, int responseCode) throws Exception {
		ConfiguracionEntidadParticular r=RecEntAct.getInstance().getCep();
		JSONArray json=super.getJsonArrayDondeIterar(r.getIterarSobre(), in);
		for(int i=0;i<json.size();i++){
			Long id = (Long)((JSONObject)(json.get(i))).get("id");
			String idSales=(String)this.realizarPeticion(String.valueOf(id));
		}
		return json.size();
	}

	@Override
	protected Object procesarPeticionOK(BufferedReader in, AbstractJsonRestEstructura json, int responseCode)
			throws Exception {
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
