package biactiva.services.ejecutores;

import org.json.simple.JSONObject;

import biactiva.services.convertidos.csvAJson.AbstractJsonRestEstructura;

public interface IEjecutar {
	
	public void procesar(AbstractJsonRestEstructura resultadoMapeo, JSONObject jsonActual) throws Exception;
	
}
