package biactiva.services.principal;


import java.io.File;
import java.io.FileWriter;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import biactiva.services.ejecutores.Ejecutor;
import biactiva.services.ejecutores.ParalelizadorDistintosFicheros;
import biactiva.services.fileHandler.CSVHandler;
import biactiva.services.fileHandler.DirectorioManager;
import biactiva.services.singletons.ApuntadorDeEntidad;
import biactiva.services.singletons.ConfiguracionEntidadParticular;
import biactiva.services.singletons.EOutputs;
import biactiva.services.singletons.RecEntAct;
import biactiva.services.util.ManejadorDateAPI;

public class Principal {
	public static final String UPDATE_CONTACTOS="UPDATE_CONTACTOS";
	public static final String INSERTAR_CONTACTOS="INSERTAR_CONTACTOS";
	public static final String INSERTAR_INCIDENTES="INSERTAR_INCIDENTES";
	public static final String BORRAR_INCIDENTES="BORRAR_INCIDENTES";
	

	
	public static void main(String[] args) throws Exception {
    	ApuntadorDeEntidad ap=ApuntadorDeEntidad.getInstance();
    	System.out.println("ddd");
    	if(ap==null)
    		return;
    	while(ap.siguienteEntidad()){
    		RecEntAct.getInstance().getCep().mostrarConfiguracion();
	    	ParalelizadorDistintosFicheros p = new ParalelizadorDistintosFicheros();
	    	p.insertar();
	    }
	}


	
}
