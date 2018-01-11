package biactiva.services.singletons;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import biactiva.services.fileHandler.ConstantesGenerales;
import biactiva.services.fileHandler.DirectorioManager;

public class RecuperadorPropiedadesConfiguracionGenerales {
	HashMap<String, String> mapPropiedades;
	private static RecuperadorPropiedadesConfiguracionGenerales instance;
    private RecuperadorPropiedadesConfiguracionGenerales(){
    	mapPropiedades=new HashMap<String,String>();
        Properties props = new Properties();
        try{
		props.load(new FileReader(ConstantesGenerales.PATH_CONFIGURACION+"/ConfiguracionGeneral.properties"));
		for(String key : props.stringPropertyNames()) 
			  mapPropiedades.put(key, props.getProperty(key));
        }
        catch(Exception e){
        	e.printStackTrace();
        	try(FileWriter fw= new FileWriter(new File("ErrorTemprano.txt"))) {
        		e.printStackTrace();
				fw.write("Error al recuperar las propierdades del fichero: ConfiguracionGeneral.properties");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
    }

    
    public static synchronized RecuperadorPropiedadesConfiguracionGenerales getInstance() {
    	if(instance==null)
    		instance=new RecuperadorPropiedadesConfiguracionGenerales();
    	return instance;
    }

    private String getValueMap(String key){
    	return mapPropiedades.get(key);
    }

	public String getSeparadorCSV() {
		return getValueMap("separadorCSV");
	}
	
	public String getSeparadorCSVREGEX() {
		return "\\"+getValueMap("separadorCSV");
	}

	public int getNivelParalelismo() {
		return Integer.parseInt(getValueMap("nivelParalelismo"));
	}

	public String getUsaProxy() {
		return getValueMap("usaProxy");
	}

	public String getIpProxy() {
		return getValueMap("ipProxy");
	}

	public String getPuertoProxy() {
		return getValueMap("puertoProxy");
	}
	public String getAction() {
		return getValueMap("action");
	}
	
	public String getEntidadesAIntegrar() {
		return getValueMap("entidadesAIntegrar");
	}

	public String getIdentificadorAtributo() {
		return getValueMap("identificadorAtributo");
	}
	
	public boolean isBorrarDataSetAlFinalizar() {
		String borrarDataSetAlFinalizar=getValueMap("borrarDataSetAlFinalizar");
		return borrarDataSetAlFinalizar==null?false:borrarDataSetAlFinalizar.equalsIgnoreCase("true");
	}
	
	public String getCabecera() {
		return getValueMap("cabecera");
	}
	
	    
}
