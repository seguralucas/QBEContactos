package biactiva.services.util.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import biactiva.services.convertidos.csvAJson.AbstractJsonRestEstructura;
import biactiva.services.fileHandler.CSVHandler;
import biactiva.services.fileHandler.ConstantesGenerales;
import biactiva.services.fileHandler.DirectorioManager;
import biactiva.services.singletons.ConfiguracionEntidadParticular;
import biactiva.services.singletons.RecEntAct;
import biactiva.services.singletons.RecuperadorPropierdadesJson;

public class JsonUtils {

	public static String reemplazarCorcheteParaRegex(String cadena){
		return cadena.replaceAll("\\[", "\\\\[");
	}
	
	public static JSONObject convertir(BufferedReader in) throws Exception{
		StringBuilder builder = new StringBuilder();
		String line;
        while ((line = in.readLine()) != null) {
            builder.append(line);
        }
        String jsonString = builder.toString();
		return convertir(jsonString);
	}
	
	public static JSONArray convertirArray(BufferedReader in) throws Exception{
		StringBuilder builder = new StringBuilder();
		String line;
        while ((line = in.readLine()) != null) {
            builder.append(line);
        }
        String jsonString = builder.toString();
		return convertirArray(jsonString);
	}
	
	public static JSONObject convertir(String in) throws Exception{
		JSONParser parser = new JSONParser();
		return (JSONObject) parser.parse(in);
	}
	
	public static JSONArray convertirArray(String in) throws Exception{
		JSONParser parser = new JSONParser();
		return (JSONArray) parser.parse(in);
	}
	
	private static Object getObjectJSONObject(Object json, String key){
		return ((JSONObject)json).get(key);
	}
	
	private static Object getObjectJSONArray(Object jsonArr, String datos){
		String[] datosPartidos=datos.split("\\[");
		Integer indice=Integer.parseInt(datosPartidos[1].substring(0, datosPartidos[1].length()-1));
		return ((JSONArray)((JSONObject)jsonArr).get(datosPartidos[0])).get(indice);	
	}
	
	private static boolean isJsonObject(String valor){
		return valor.equalsIgnoreCase("JSONObject");
	}
	
	public static String[] getArrayTipos(String accesoAlJson){
		String datos[]=accesoAlJson.split("\\.");
		String[] tipoDatos= new String[datos.length];
		for(int i=0;i<datos.length;i++){
			String dato=datos[i];
			String[] partes=dato.split("\\[");
			if(partes.length>1){
			   	tipoDatos[i]="JSONArray";
			}
			else
			   	tipoDatos[i]="JSONObject";
		}
		return tipoDatos;
	}
	
	public static Object getValue(String accesoAlJson, JSONObject json){
		try{
		String[] tiposDatos=getArrayTipos(accesoAlJson);
		String datos[]=accesoAlJson.split("\\.");
		Object posicionActual=json;
		Object resultado;
		for(int i=0;i<tiposDatos.length-1;i++){
			if(isJsonObject(tiposDatos[i]))/*Es jsonObject*/
					posicionActual=getObjectJSONObject(posicionActual,datos[i]);
			else/*Es un array*/
				posicionActual=getObjectJSONArray(posicionActual,datos[i]);
		}
		if(isJsonObject(tiposDatos[tiposDatos.length-1]))
			resultado=getObjectJSONObject(posicionActual,datos[tiposDatos.length-1]);
		else/*Es un array*/
			resultado=getObjectJSONArray(posicionActual,datos[tiposDatos.length-1]);
		return resultado;
		}
		catch(Exception e){
			return null;
		}
		}
	
	public static String reemplazarJsonEnString(String cadena, JSONObject json){
		ConfiguracionEntidadParticular r= RecEntAct.getInstance().getCep();
		RecuperadorPropierdadesJson rpj= r.getRecuperadorPropiedadesJson();
		String separador=r.getIdentificadorAtributo();
		int aux;
		int index = cadena.indexOf(separador);
		while(index >= 0) {
		   aux=index;
		   index = cadena.indexOf(separador, index+1);
		   if(index>=0){
			   String key=cadena.substring(aux+1, index);
			   if(JsonUtils.getValue(key, json)==null)
				   return null;
			   cadena=cadena.replaceAll(separador+key+separador, JsonUtils.getValue(key, json).toString());
			   index = cadena.indexOf(separador, index+1);
		   }
		}		
		return cadena;
	}
	
	public static void escribirJson(String path, AbstractJsonRestEstructura json){
	    try(FileWriter fw = new FileWriter(path, true);
	    	    BufferedWriter bw = new BufferedWriter(fw);
	    	    PrintWriter out = new PrintWriter(bw))
	    	{
	    		out.write(json.toStringSinEnter());
	    	} catch (IOException e) {
	    		CSVHandler.getInstance().escribirErrorException(e);
	    	}     
	}
	
	
}
