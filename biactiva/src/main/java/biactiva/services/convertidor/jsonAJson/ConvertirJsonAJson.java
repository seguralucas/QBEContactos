package biactiva.services.convertidor.jsonAJson;


import org.json.simple.JSONObject;

import biactiva.services.convertidos.csvAJson.AbstractJsonRestEstructura;
import biactiva.services.convertidos.csvAJson.JSONFiber;
import biactiva.services.fileHandler.CSVHandler;
import biactiva.services.fileHandler.ConstantesGenerales;
import biactiva.services.singletons.ConfiguracionEntidadParticular;
import biactiva.services.util.json.JsonUtils;

public class ConvertirJsonAJson {
	
	private ConfiguracionEntidadParticular configEnt;
	private JSONObject obtenedorDeDatos;
	private AbstractJsonRestEstructura jsonAbstract;
	public ConvertirJsonAJson(ConfiguracionEntidadParticular configEnt, JSONObject obtenedorDeDatos) {
		super();
		this.configEnt = configEnt;
		this.obtenedorDeDatos=obtenedorDeDatos;
	}
	
	public AbstractJsonRestEstructura convertir() throws Exception{
		jsonAbstract= new JSONFiber(configEnt);
		for(Object key:configEnt.getRecuperadorPropiedadesJson().getJsonPropiedades().keySet()){
			try{
			Object o=JsonUtils.getValue(key.toString(), obtenedorDeDatos);
				if(o!=null){
					String valorExtraido=o.toString();
					jsonAbstract.agregarCampo(key.toString(), valorExtraido);
				}
				else
					jsonAbstract.agregarCampo(key.toString(), (String)o);
			}catch(Exception e){
//				e.printStackTrace();
				CSVHandler.getInstance().escribirErrorException(e);
				System.out.println(e.getMessage());
			}
		}
		return jsonAbstract;
	}

	public AbstractJsonRestEstructura getJsonAbstract() {
		return jsonAbstract;
	}
	
	@Override
	public String toString() {
		return obtenedorDeDatos.toString().replace("\\", "").replace(",", ",\n");
	}
	
	public String toStringSinEnter(){
		return obtenedorDeDatos.toString().replace("\\", "");
	}
	
	public String toStringNormal(){
		return obtenedorDeDatos.toString();
	}


	
	
	
	
	
	
	
}
