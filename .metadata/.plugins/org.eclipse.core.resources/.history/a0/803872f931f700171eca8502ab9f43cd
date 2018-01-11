package biactiva.services.ejecutores;


import java.lang.reflect.Method;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import biactiva.services.convertidor.jsonAJson.ConvertirJsonAJson;
import biactiva.services.convertidos.csvAJson.AbstractJsonRestEstructura;
import biactiva.services.fileHandler.CSVHandler;
import biactiva.services.principal.peticiones.AbstractHTTP;
import biactiva.services.principal.peticiones.EPeticiones;
import biactiva.services.principal.peticiones.GetGenerico;
import biactiva.services.singletons.ConfiguracionEntidadParticular;
import biactiva.services.singletons.RecEntAct;


public class Ejecutor {
	
	public Object ejecutorSalesAService() throws Exception{
		ConfiguracionEntidadParticular r= RecEntAct.getInstance().getCep();
		EjecutarGenericoSalesAService ejecutar= new EjecutarGenericoSalesAService();
		AbstractHTTP getGenerico= new GetGenerico(EPeticiones.GET,r.getUrlExtraer(),RecEntAct.getInstance().getCep().getCabeceraExtraer());
		JSONArray jArray= (JSONArray)getGenerico.realizarPeticion(null,null,r.getParametrosUrl());
		for(int i=0;i<jArray.size();i++){
			Object id = ((JSONObject)(jArray.get(i))).get(r.getIdIteracion());
			JSONObject entidad=(JSONObject)getGenerico.realizarPeticion(String.valueOf(id));
			ConvertirJsonAJson conv= new ConvertirJsonAJson(r,entidad);
			AbstractJsonRestEstructura resultadoMapeo=conv.convertir();
			ejecutar.procesar(resultadoMapeo, entidad);
		}
		return jArray.size();
	}
	
	public Object ejecutorSalesAServiceContacts() throws Exception{
		ConfiguracionEntidadParticular r= RecEntAct.getInstance().getCep();
		EjecutarContactosSalesAService ejecutar= new EjecutarContactosSalesAService();		
		AbstractHTTP getGenerico= new GetGenerico(EPeticiones.GET,r.getUrlExtraer(),RecEntAct.getInstance().getCep().getCabeceraExtraer());
		JSONArray jArray= (JSONArray)getGenerico.realizarPeticion(null,null,r.getParametrosUrl());
		for(int i=0;i<jArray.size();i++){
			Object id = ((JSONObject)(jArray.get(i))).get(r.getIdIteracion());
			JSONObject entidad=(JSONObject)getGenerico.realizarPeticion(String.valueOf(id));
			ConvertirJsonAJson conv= new ConvertirJsonAJson(r,entidad);
			AbstractJsonRestEstructura resultadoMapeo=conv.convertir();
			ejecutar.procesar(resultadoMapeo, entidad);
			
		}
		return jArray.size();
	}
		
	public Object ejecutorServiceASales() throws Exception{
		ConfiguracionEntidadParticular r=RecEntAct.getInstance().getCep();
		AbstractHTTP getGenerico= new GetGenerico(EPeticiones.GET,r.getUrlExtraer(),RecEntAct.getInstance().getCep().getCabeceraExtraer());
		JSONArray jArray= (JSONArray)getGenerico.realizarPeticion(null,null,r.getParametrosUrl());
		for(int i=0;i<jArray.size();i++){
			Object id = ((JSONObject)(jArray.get(i))).get(r.getIdIteracion());
			JSONObject entidad=(JSONObject)getGenerico.realizarPeticion(String.valueOf(id));
			ConvertirJsonAJson conv= new ConvertirJsonAJson(r,entidad);
			AbstractJsonRestEstructura resultadoMapeo=conv.convertir();
			System.out.println(resultadoMapeo);
		}
		return jArray.size();	
	}
	
	public Object ejecutorServiceASalesContactos() throws Exception{
		ConfiguracionEntidadParticular r=RecEntAct.getInstance().getCep();
		EjecutarContactosServiceASales ejecutar= new EjecutarContactosServiceASales();		
		AbstractHTTP getGenerico= new GetGenerico(EPeticiones.GET,r.getUrlExtraer(),RecEntAct.getInstance().getCep().getCabeceraExtraer());
		JSONArray jArray= (JSONArray)getGenerico.realizarPeticion(null,null,r.getParametrosUrl());
		for(int i=0;i<jArray.size();i++){
			Object id = ((JSONObject)(jArray.get(i))).get(r.getIdIteracion());
			JSONObject entidad=(JSONObject)getGenerico.realizarPeticion(String.valueOf(id));
			ConvertirJsonAJson conv= new ConvertirJsonAJson(r,entidad);
			AbstractJsonRestEstructura resultadoMapeo=conv.convertir();
			ejecutar.procesar(resultadoMapeo, entidad);
		}
		return jArray.size();	
	}
	


	
	public Object ejecutar(String nombreMetodo, String parametros) throws Exception{
		Class<Ejecutor> a= Ejecutor.class;
		try {
			Method m;
			Object o;
			if(parametros!=null){
				m= a.getMethod(nombreMetodo, parametros.getClass());
				o=m.invoke(this,parametros);				
			}
			else{
				m=a.getMethod(nombreMetodo);
				o=m.invoke(this);
			}
			return o;
		} catch (Exception e) {
			e.printStackTrace();
			CSVHandler.getInstance().escribirErrorException(e);
			throw new Exception("Error al ejecutar ejecutor");
		} 
	}
}
