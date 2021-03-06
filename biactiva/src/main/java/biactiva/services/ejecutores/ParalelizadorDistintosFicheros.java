package biactiva.services.ejecutores;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import biactiva.services.convertidos.csvAJson.AbstractJsonRestEstructura;
import biactiva.services.fileHandler.ConvertidosJSONCSV;
import biactiva.services.fileHandler.DirectorioManager;
import biactiva.services.principal.peticiones.AbstractHTTP;
import biactiva.services.principal.peticiones.EPeticiones;
import biactiva.services.principal.peticiones.EliminarGenericoSinLog;
import biactiva.services.principal.peticiones.GetExistFieldURLQueryRightNow;
import biactiva.services.principal.peticiones.PostGenerico;
import biactiva.services.principal.peticiones.UpdateGenericoRightNow;
import biactiva.services.singletons.ConfiguracionEntidadParticular;
import biactiva.services.singletons.RecEntAct;


public class ParalelizadorDistintosFicheros {
	public static int y=0;
	public static int z=0;
	
	public void insertar() throws InterruptedException, IOException{

			        	try {
			        		ConfiguracionEntidadParticular r= RecEntAct.getInstance().getCep();
			        		File f= new File(r.getPathInformacion());
			        		File destination =DirectorioManager.getDirectorioFechaYHoraInicio("procesado.csv");

			        		if (!destination.exists()) {
			        		    f.renameTo(destination);
			        		}
				        	ConvertidosJSONCSV csvThread= new ConvertidosJSONCSV();
			        		AbstractJsonRestEstructura jsonEst=null;
			        		while(!csvThread.isFin()){
			        			jsonEst = csvThread.convertirCSVaJSONLineaALinea(destination);
			        			if(jsonEst!=null){
				        			String urlVerificar=r.getUrlVerificarExistencia(jsonEst);
			        				AbstractHTTP peticionARealizar;
			        				String idService=null;
				        			if(urlVerificar!=null){
				        				GetExistFieldURLQueryRightNow g = new GetExistFieldURLQueryRightNow(EPeticiones.GET, urlVerificar, r.getCabeceraInsertar());
				        				idService=(String)g.realizarPeticion();
				        				if(idService==null){
				        					System.out.println("Insertando");
				        					peticionARealizar = new PostGenerico(EPeticiones.POST, r.getUrlInsertar(), r.getCabeceraInsertar());
				        				}
				        				else{
				        					for(String url: jsonEst.listaUrlEliminar){
				        						url=url.replaceAll(r.getIdentificadorAtributo()+"id"+r.getIdentificadorAtributo(), idService);
				        						EliminarGenericoSinLog del = new EliminarGenericoSinLog(EPeticiones.DELETE, url, r.getCabeceraInsertar());
				        						del.realizarPeticion();
				        					}
				        					System.out.println("Actualizando: "+idService);
				        					peticionARealizar = new UpdateGenericoRightNow(EPeticiones.UPDATE, r.getUrlInsertar(), r.getCabeceraInsertar());
				        				}
				        			}
				        			else{
			        					peticionARealizar = new PostGenerico(EPeticiones.POST, r.getUrlInsertar(), r.getCabeceraInsertar());
				        			}
				        			System.out.println(jsonEst);
				        			peticionARealizar.realizarPeticion(idService,jsonEst);
			        			}
			        		}
			        		}
			        	catch (Exception e) {
							e.printStackTrace();
						}
			        }
		}

