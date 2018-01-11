package biactiva.services.ejecutores;

import org.json.simple.JSONObject;

import biactiva.services.convertidos.csvAJson.AbstractJsonRestEstructura;
import biactiva.services.principal.peticiones.EPeticiones;
import biactiva.services.principal.peticiones.GetExistFieldURLQueryRightNow;
import biactiva.services.principal.peticiones.PostGenerico;
import biactiva.services.principal.peticiones.UpdateGenericoRightNow;
import biactiva.services.singletons.ConfiguracionEntidadParticular;
import biactiva.services.singletons.RecEntAct;

public class EjecutarGenericoSalesAService implements IEjecutar{
	
	public void procesar(AbstractJsonRestEstructura resultadoMapeo, JSONObject jsonActual){
		ConfiguracionEntidadParticular r= RecEntAct.getInstance().getCep();
		String urlVerificarExistencia=r.getUrlVerificarExistencia(jsonActual);
		String id=null;
		if(urlVerificarExistencia!=null){
			GetExistFieldURLQueryRightNow get = new GetExistFieldURLQueryRightNow(EPeticiones.GET,urlVerificarExistencia,r.getCabeceraInsertar());
			id=(String)get.realizarPeticion();
		}
		if(id==null){
			System.out.println("Insertando...");
			PostGenerico post= new PostGenerico(EPeticiones.POST,r.getUrlInsertar(),r.getCabeceraInsertar());
			post.realizarPeticion( resultadoMapeo );
		}
		else{
			System.out.println("Actualizando...");
			UpdateGenericoRightNow update= new UpdateGenericoRightNow(EPeticiones.UPDATE,r.getUrlInsertar(),r.getCabeceraInsertar());
			update.realizarPeticion( id, resultadoMapeo);
		}
	}
}
