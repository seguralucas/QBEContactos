package biactiva.services.ejecutores;

import org.json.simple.JSONObject;

import biactiva.services.convertidos.csvAJson.AbstractJsonRestEstructura;
import biactiva.services.principal.peticiones.EPeticiones;
import biactiva.services.principal.peticiones.GetExistFieldURLQueryRightNow;
import biactiva.services.principal.peticiones.GetGenerico;
import biactiva.services.principal.peticiones.PostGenerico;
import biactiva.services.principal.peticiones.UpdateGenericoRightNow;
import biactiva.services.singletons.ConfiguracionEntidadParticular;
import biactiva.services.singletons.RecEntAct;
import biactiva.services.util.json.JsonUtils;

public class EjecutarContactosSalesAService implements IEjecutar {

	@Override
	public void procesar(AbstractJsonRestEstructura resultadoMapeo, JSONObject jsonActual) throws Exception {
		ConfiguracionEntidadParticular r= RecEntAct.getInstance().getCep();
		String urlFianl=r.getUrlVerificarExistencia(jsonActual);
		String idContacto=null;
		if(urlFianl!=null){
			GetExistFieldURLQueryRightNow get = new GetExistFieldURLQueryRightNow(EPeticiones.GET,urlFianl,r.getCabeceraInsertar());
			idContacto=(String)get.realizarPeticion();
		}
		String tipoContacto=(String)jsonActual.get("PersonDEO_TipoDeContacto_c");
		if(idContacto==null){
			PostGenerico post= new PostGenerico(EPeticiones.POST,r.getUrlInsertar(),r.getCabeceraInsertar());
			addContactType(tipoContacto,null,resultadoMapeo);
			System.out.println(resultadoMapeo);
			System.out.println("Insertando");
			post.realizarPeticion( resultadoMapeo );
		}
		else{
			GetGenerico getGenerico = new GetGenerico(EPeticiones.GET, r.getUrlInsertar(), r.getCabeceraInsertar());
			JSONObject jsonContactoService=(JSONObject)getGenerico.realizarPeticion(idContacto);
			addContactType(tipoContacto,jsonContactoService,resultadoMapeo);
			System.out.println(resultadoMapeo);
			System.out.println("Actualizando..");
			UpdateGenericoRightNow update= new UpdateGenericoRightNow(EPeticiones.UPDATE, r.getUrlInsertar(),r.getCabeceraInsertar());
			update.realizarPeticion(idContacto, resultadoMapeo);
		}
		System.out.println("************");		
	}
	
	private void addContactType(String tipoContacto, JSONObject jsonContacto, JSONObject jsonContactoAInsertar) throws Exception{
		if(tipoContacto==null)
			return ;
		if(jsonContacto==null){
			jsonContactoAInsertar.put("contactType", mapeoContactType(tipoContacto,1));
			return;
		}
		JSONObject contactType=(JSONObject)jsonContacto.get("contactType");
		if(contactType==null){
			jsonContactoAInsertar.put("contactType",  mapeoContactType(tipoContacto,1));
			return ;
		}
		if(mapeoContactTypeSalesAService(tipoContacto,0).equals(((Long)contactType.get("id"))))
			return ;
		
		JSONObject jsonCustomFields=(JSONObject)jsonContacto.get("customFields");
		if(jsonCustomFields==null)
			return;
		JSONObject jsonCO=(JSONObject)jsonCustomFields.get("c");
		if(jsonCO==null)
			return;
		for(int i=2;i<=3;i++){		
			contactType=(JSONObject)jsonCO.get("contact_type"+i);
			if(contactType==null){
				jsonContactoAInsertar.put("customFields", mapeoContactType(tipoContacto,i));
				return ;
			}
			if(mapeoContactTypeSalesAService(tipoContacto,i).equals(((Long)contactType.get("id"))))
				return ;
		}

	}

	
	private JSONObject mapeoContactType(String contactType, int nroContactType) throws Exception{
		if(nroContactType == 1){
			JSONObject informacionContactType= JsonUtils.convertir("{\"id\":"+mapeoContactTypeSalesAService(contactType,nroContactType)+"}");
			return informacionContactType;
		}
		JSONObject aux= new JSONObject();
		JSONObject aux2= new JSONObject();
		JSONObject informacionContactType;
		informacionContactType= JsonUtils.convertir("{\"id\":"+mapeoContactTypeSalesAService(contactType,nroContactType)+"}");
		aux.put("contact_type"+nroContactType,informacionContactType );
		aux2.put("c",aux );
		return aux2;
	}
	
	private Long mapeoContactTypeSalesAService(String contactType, int nroContactType){
		if(nroContactType==1){
			if(contactType.equalsIgnoreCase("TIPOC1"))
				return 1L;
			else if(contactType.equalsIgnoreCase("TIPOC2"))
				return 2L;
			else if(contactType.equalsIgnoreCase("TIPOC3"))
				return 3L;
			else if(contactType.equalsIgnoreCase("TIPOC4"))
				return 4L;
			else if(contactType.equalsIgnoreCase("TIPOC5"))
				return 5L;
		}
		else if(nroContactType == 2){
			if(contactType.equalsIgnoreCase("TIPOC1"))
				return 16L;
			else if(contactType.equalsIgnoreCase("TIPOC2"))
				return 17L;
			else if(contactType.equalsIgnoreCase("TIPOC3"))
				return 18L;
			else if(contactType.equalsIgnoreCase("TIPOC4"))
				return 19L;
			else if(contactType.equalsIgnoreCase("TIPOC5"))
				return 20L;			
		}
		else{
			if(contactType.equalsIgnoreCase("TIPOC1"))
				return 21L;
			else if(contactType.equalsIgnoreCase("TIPOC2"))
				return 22L;
			else if(contactType.equalsIgnoreCase("TIPOC3"))
				return 23L;
			else if(contactType.equalsIgnoreCase("TIPOC4"))
				return 24L;
			else if(contactType.equalsIgnoreCase("TIPOC5"))
				return 25L;
		}
		return null;
	}

//	public static void main(String[] args) throws Exception {
//		EjecutarContactosSalesAService get = new EjecutarContactosSalesAService();
//		JSONObject a=JsonUtils.convertir("{\"contactType\":{\"id\":2},\"customFields\":{\"c\":{\"contact_type2\":{\"id\":19} ,\"contact_type3\":null}}}");
//		JSONObject b= JsonUtils.convertir("{}");
////		JSONObject b=JsonUtils.convertir("{\"customFields\":{\"CO\":{\"contact_type1\":{\"id\":2}}}}");
//		get.addContactType("TIPOC4",a,b);
//		System.out.println(b);
//	}
}
