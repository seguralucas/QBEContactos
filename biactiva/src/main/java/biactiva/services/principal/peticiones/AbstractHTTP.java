package biactiva.services.principal.peticiones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import biactiva.services.convertidos.csvAJson.AbstractJsonRestEstructura;
import biactiva.services.fileHandler.CSVHandler;
import biactiva.services.fileHandler.ConstantesGenerales;
import biactiva.services.fileHandler.DirectorioManager;
import biactiva.services.singletons.ApuntadorDeEntidad;
import biactiva.services.singletons.ConfiguracionEntidadParticular;
import biactiva.services.singletons.RecEntAct;
import biactiva.services.singletons.entidadesARecuperar.IPeticiones;
import biactiva.services.singletons.entidadesARecuperar.Peticion;
import biactiva.services.singletons.entidadesARecuperar.PeticionEntidad;
import biactiva.services.singletons.entidadesARecuperar.RecuperadorPeticiones;
import biactiva.services.util.json.JsonUtils;

public abstract class AbstractHTTP {
	public static final String PROPIEDADES_EXTRA="propiedadesExtras";
	protected EPeticiones peticion;
	protected String url;
	protected String urlUltimaPeticion;
	protected JSONObject cabecera;


	public AbstractHTTP(EPeticiones peticion, String url,JSONObject cabecera){
		this.peticion=peticion;
		this.url=url;
		this.cabecera=cabecera;
	}
	
	
	public Object realizarPeticion(){
		return realizarPeticion(null,null,null);
	}
		
	public Object realizarPeticion(AbstractJsonRestEstructura json){
		 return realizarPeticion(null,json,null);
	 }
	 
	public Object realizarPeticion(String id){
		 return realizarPeticion(id,null,null);
	}
	
	public Object realizarPeticion(String id,AbstractJsonRestEstructura json){
		 return realizarPeticion(id,json,null);
	}

	public Object realizarPeticion(String id,String parametros){
		 return realizarPeticion(id,null,parametros);
	}
	public Object realizarPeticion(AbstractJsonRestEstructura json, String parametros){
		 return realizarPeticion(null,json,parametros);
	}
	
	public Object realizarPeticion(String id,AbstractJsonRestEstructura json, String parametros){
	        try{
	            Peticion pet=PeticionEntidad.getInstance().getPeticion(this.peticion);
	        	WSConector ws;
	        	urlUltimaPeticion=this.url+(id==null?"":"/"+id)+(parametros==null?"":"?"+parametros);
	        	System.out.println(urlUltimaPeticion);
	        	ws = new WSConector(this.peticion,urlUltimaPeticion,this.cabecera,pet);

	        	HttpURLConnection conn=ws.getConexion();
	        	if(json!=null){
		        	DataOutputStream wr = new DataOutputStream(
		        			conn.getOutputStream());
		        	wr.write(json.toStringNormal().getBytes("UTF-8"));
		        	wr.flush();
		        	wr.close();
	        	}
	            int responseCode = conn.getResponseCode();
	            BufferedReader in=null;
	            Object o;
	            if(responseCode == pet.getCodigoResponseEsperado()){
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getInputStream()));
	            	if(id!=null){
	            		if(json!=null)
	            			o=procesarPeticionOK(in, json, id,responseCode);
	            		else
	            			o=procesarPeticionOK(in, id,responseCode);
	            	}
	            	else{
	            		if(json!=null)
	            			o=procesarPeticionOK(in, json, responseCode);
	            		else
	            			o=procesarPeticionOK(in, responseCode);
	            	}
	            }
	            else{
	            	if(conn.getErrorStream()!=null){
		            	in = new BufferedReader(
			                    new InputStreamReader(conn.getErrorStream()));
	            	}
	            	if(id!=null){
	            		if(json!=null)
	            			o=procesarPeticionError(in, json, id,responseCode);
	            		else
	            			o=procesarPeticionError(in, id,responseCode);
	            	}
	            	else{
	            		if(json!=null)
	            			o=procesarPeticionError(in, json, responseCode);
	            		else
	            			o=procesarPeticionError(in, responseCode);
	            	}
	            	
	            }
	            return o;	 
	            }	                
          catch (ConnectException e) {
				try {
					CSVHandler.getInstance().escribirCSV(CSVHandler.PATH_ERROR_SERVER_NO_ALCANZADO, url+"/"+id==null?"":id);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return null;
			}
          catch (Exception e) {
        	  e.printStackTrace();
				if(id!=null)
					CSVHandler.getInstance().escribirErrorException("Error al realizar request "+ url+"/"+id==null?"":id+" de la entidad "+ApuntadorDeEntidad.getInstance().getEntidadActual(),e,false);
				return null;
			}
      }
	
	abstract protected Object procesarPeticionOK(BufferedReader in, String id,int responseCode) throws Exception;
	abstract protected  Object procesarPeticionOK(BufferedReader in, int responseCode) throws Exception;
	abstract protected  Object procesarPeticionOK(BufferedReader in, AbstractJsonRestEstructura json, int responseCode) throws Exception;
	abstract protected Object procesarPeticionOK(BufferedReader in, AbstractJsonRestEstructura json, String id,int responseCode) throws Exception;

	protected Object procesarPeticionError(BufferedReader in, String id, int responseCode) throws Exception{
		String path=(ConstantesGenerales.reemplazarArrobas(ConstantesGenerales.PATH_ERROR_HTTP_TXT, this.peticion.getAccion(),String.valueOf(responseCode)));		
	    File fichero = DirectorioManager.getDirectorioFechaYHoraInicio(path);
	    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
        String inputLine;
        out.println(this.urlUltimaPeticion);
        while ((inputLine = in.readLine()) != null) {
         	out.println(inputLine);
        }
        out.println(ConstantesGenerales.SEPARADOR_ERROR_PETICION);
        out.close();
        return null;
	}
	
	protected Object procesarPeticionError(BufferedReader in, int responseCode) throws Exception{
		return this.procesarPeticionError(in, (String)null, responseCode);
	}
	
	protected Object procesarPeticionError(BufferedReader in, AbstractJsonRestEstructura json,int responseCode) throws Exception{
		return this.procesarPeticionError(in, json, null, responseCode);
    }
	
	protected Object procesarPeticionError(BufferedReader in, AbstractJsonRestEstructura json, String id, int responseCode) throws Exception{
		String path=(ConstantesGenerales.reemplazarArrobas(ConstantesGenerales.PATH_ERROR_HTTP_TXT, this.peticion.getAccion(),String.valueOf(responseCode)));
	    File fichero = DirectorioManager.getDirectorioFechaYHoraInicio(path);
	    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
        out.println(json.toString());
        out.println(ConstantesGenerales.SEPARADOR_ERROR_JSON);
	    out.println(urlUltimaPeticion);
        out.println(ConstantesGenerales.SEPARADOR_ERROR_PETICION);
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
         	out.println(inputLine);
        }
        out.println(ConstantesGenerales.SEPARADOR_ERROR_PETICION);
        out.close();
        if(json.getLine()!=null)
        	CSVHandler.getInstance().escribirCSV(ConstantesGenerales.reemplazarArrobas("error_@0_servidor_codigo_@1.csv", this.peticion.getAccion(),String.valueOf(responseCode)),json.getLine());            
        else{
        	String nombre=(ConstantesGenerales.reemplazarArrobas(ConstantesGenerales.PATH_ERROR_HTTP_JSONS, this.peticion.getAccion(),String.valueOf(responseCode)));
    	    path = DirectorioManager.getDirectorioFechaYHoraInicio(nombre).getAbsolutePath();
    	    JsonUtils.escribirJson(path, json);
    	}
        return null;	
	}
		
	protected JSONArray getJsonArrayDondeIterar(String ubicacionIteracion, BufferedReader in) throws Exception{
		JSONArray jArray;
		if(ubicacionIteracion!=null){
			JSONObject json= JsonUtils.convertir(in);
			jArray=(JSONArray)json.get(ubicacionIteracion);
		}
		else
			jArray = JsonUtils.convertirArray(in);
		return jArray;
	}
}
