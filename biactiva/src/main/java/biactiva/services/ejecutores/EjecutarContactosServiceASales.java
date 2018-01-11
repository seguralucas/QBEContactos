package biactiva.services.ejecutores;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import biactiva.services.convertidos.csvAJson.AbstractJsonRestEstructura;
import biactiva.services.principal.peticiones.EPeticiones;
import biactiva.services.principal.peticiones.GetGenerico;
import biactiva.services.principal.peticiones.PostGenerico;
import biactiva.services.principal.peticiones.UpdateGenericoRightNow;
import biactiva.services.singletons.ConfiguracionEntidadParticular;
import biactiva.services.singletons.RecEntAct;

public class EjecutarContactosServiceASales implements IEjecutar{

	@Override
	public void procesar(AbstractJsonRestEstructura resultadoMapeo, JSONObject jsonActual) throws Exception {
		ConfiguracionEntidadParticular r=RecEntAct.getInstance().getCep();
		Long id= (Long)jsonActual.get("id");
		this.getMailService(id,resultadoMapeo.getJson());
		this.getPhonesService(id,resultadoMapeo.getJson());
		ArrayList<String> listaContactType= getContactsTypes(jsonActual);
		String urlFianl=r.getUrlVerificarExistencia(jsonActual);
		GetGenerico getContactosEnSales = new GetGenerico(EPeticiones.GET, urlFianl, r.getCabeceraInsertar());//Puede haber más de un contacto con el mismo DNI pero distinto contact_type
		JSONArray contactosEnSales = (JSONArray)getContactosEnSales.realizarPeticion();
		for(String contactType: listaContactType){
			String idSales=null;
			for(int j=0;j<contactosEnSales.size();j++){
				JSONObject contactoSalesActual= (JSONObject)contactosEnSales.get(j);
				String contactTypeContactoActualSales= (String)contactoSalesActual.get("PersonDEO_TipoDeContacto_c");
				if(contactTypeContactoActualSales!=null && contactTypeContactoActualSales.equals(contactType)){
					idSales=(String)contactoSalesActual.get("PartyNumber");
				}
			}
			resultadoMapeo.put("PersonDEO_TipoDeContacto_c", contactType);
			if(idSales!=null){
				System.out.println("Actualizar... "+idSales);
				System.out.println(resultadoMapeo);
				UpdateGenericoRightNow update = new UpdateGenericoRightNow(EPeticiones.UPDATE, r.getUrlInsertar(), r.getCabeceraInsertar());
				update.realizarPeticion(idSales,resultadoMapeo);
			}
			else{
				System.out.println("insertar");
				System.out.println(resultadoMapeo);
				PostGenerico post = new PostGenerico(EPeticiones.POST, r.getUrlInsertar(), r.getCabeceraInsertar());
				post.realizarPeticion(resultadoMapeo);

			}
			System.out.println("***********************************************");
	}
	}
		private ArrayList<String> getContactsTypes(JSONObject jsonContacto){
			ArrayList<String> listaContactType= new ArrayList<String>();
			JSONObject contactType=(JSONObject)jsonContacto.get("contactType");
			Long idContactType;
			JSONObject aux;
			if(contactType!=null){
				idContactType=(Long)contactType.get("id");
				listaContactType.add(mapeoContactTypeServiceSales(idContactType));
			}
			aux=((JSONObject)((JSONObject)jsonContacto.get("customFields")).get("c"));
			for(int i=2;i<=3;i++){
				contactType=(JSONObject)aux.get("contact_type"+i);
				if(contactType!=null){
					idContactType=(Long)contactType.get("id");
					listaContactType.add(mapeoContactTypeServiceSales(idContactType));
				}
			}
			return listaContactType;
		}
		
		private String mapeoContactTypeServiceSales(Long id){
			if(id.equals(1l) || id.equals(16l) || id.equals(21l))
				return "TIPOC1";
			if(id.equals(2l) || id.equals(17l) || id.equals(22l))
				return "TIPOC2";	
			if(id.equals(3l) || id.equals(18l) || id.equals(23l))
				return "TIPOC3";	
			if(id.equals(4l) || id.equals(19l) || id.equals(24l))
				return "TIPOC4";	
			else if(id.equals(5l) || id.equals(20l) || id.equals(25l))
				return "TIPOC5";	
			return null;
		}
		
		private void getMailService(Long idContactoService, JSONObject jsonContactoAInsertar){
			ConfiguracionEntidadParticular r= RecEntAct.getInstance().getCep();
			GetGenerico getListaEmails = new GetGenerico(EPeticiones.GET, r.getUrlExtraer()+"/"+idContactoService+"/emails", r.getCabeceraExtraer());
			JSONArray jsonEmails = (JSONArray) getListaEmails.realizarPeticion();
			for(int i=0;i<jsonEmails.size();i++){
				JSONObject jsonObj=(JSONObject)jsonEmails.get(i);
				String urlEmail=jsonObj.get("href").toString();
				String idMail=String.valueOf(urlEmail.charAt(urlEmail.length()-1));
				if(idMail.equals("0")){
					JSONObject jsonMailParticular=(JSONObject)getListaEmails.realizarPeticion(idMail);
					String mail=(jsonMailParticular.get("address").toString());
					jsonContactoAInsertar.put("EmailAddress", mail);
				}
			}
		}
		
		private void getPhonesService(Long idContactoService, JSONObject jsonContactoAInsertar){
			ConfiguracionEntidadParticular r= RecEntAct.getInstance().getCep();
			GetGenerico getListaPhones = new GetGenerico(EPeticiones.GET, r.getUrlExtraer()+"/"+idContactoService+"/phones", r.getCabeceraExtraer());
			JSONArray jsonPhones = (JSONArray) getListaPhones.realizarPeticion();
			for(int i=0;i<jsonPhones.size();i++){
				JSONObject jsonObj=(JSONObject)jsonPhones.get(i);
				String urlPhones=jsonObj.get("href").toString();
				String idPhone=String.valueOf(urlPhones.charAt(urlPhones.length()-1));
				if(idPhone.equals("0") || idPhone.equals("1")){
					JSONObject jsonMailParticular=(JSONObject)getListaPhones.realizarPeticion(idPhone);
					String mail=(jsonMailParticular.get("number").toString());
					jsonContactoAInsertar.put(idPhone.equals("0")?"MobileNumber":"WorkPhoneNumber", mail);
				}
			}
		}
		
		

}


