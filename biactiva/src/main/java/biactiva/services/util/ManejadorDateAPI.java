package biactiva.services.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import biactiva.services.fileHandler.ConstantesGenerales;

public class ManejadorDateAPI {
	
	private static ManejadorDateAPI instance;
	private String fechaGuardada;
	private Date fechaHasta;
	private String fechaDesdeSales;
	private String fechaHastaSales;
	private String fechaDesdeService;
	private String fechaHastaService;
	private File f= new File(ConstantesGenerales.PATH_HORA_ULTIMA_EJECUCION);
	private SimpleDateFormat dateFormatEnArchivo=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private ManejadorDateAPI(){
		String line;
		try(BufferedReader br = new BufferedReader(new FileReader(f)) ) {			
			if( (line=br.readLine())!=null){
				this.fechaGuardada= line;
				fechaHasta = new Date();
				initFechaDesdeSales(line);
				initFechaHastaSales();
				initFechaDesdeService(line);
				initFechaHastaService();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public synchronized static ManejadorDateAPI getInstance(){
		if(null == instance){
			instance = new ManejadorDateAPI();
		}
		return instance;
	}


	
	
	public void saveNewDate(){
		String line;
		try(FileWriter fw= new FileWriter(f,false)) {			
			fw.write(getFechaHastaSales());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initFechaHastaSales() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
		this.fechaHastaSales=dateFormat.format(this.fechaHasta)+"T"+dateFormat2.format(this.fechaHasta)+"%2B00:00";
	}
	
	private void initFechaDesdeSales(String line){
		Date date;
		try {
			date = dateFormatEnArchivo.parse(line);
			DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat dateFormat3 = new SimpleDateFormat("HH:mm:ss");
			this.fechaDesdeSales=dateFormat2.format(date)+"T"+dateFormat3.format(date)+"%2B00:00";
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void initFechaHastaService() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
		this.fechaHastaService=dateFormat.format(this.fechaHasta)+"T"+dateFormat2.format(this.fechaHasta)+"Z";
	}
	
	private void initFechaDesdeService(String line){
		Date date;
		try {
			date = dateFormatEnArchivo.parse(line);
			DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat dateFormat3 = new SimpleDateFormat("HH:mm:ss");
			this.fechaDesdeService=dateFormat2.format(date)+"T"+dateFormat3.format(date)+"Z";
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InterruptedException{
		System.out.println(ManejadorDateAPI.getInstance().getFechaDesdeService());
		System.out.println(ManejadorDateAPI.getInstance().getFechaHastaService());
		System.out.println(ManejadorDateAPI.getInstance().getFechaDesdeSales());
		System.out.println(ManejadorDateAPI.getInstance().getFechaHastaSales());
	}
	
	public String getFechaDesdeSales() {
		return fechaDesdeSales;
	}

	public void setFechaDesdeSales(String fechaDesde) {
		this.fechaDesdeSales = fechaDesde;
	}

	public String getFechaHastaSales() {
		return fechaHastaSales;
	}
	public void setFechaHastaSales(String fechaDesde) {
		this.fechaHastaSales = fechaDesde;
	}
	
	public String getFechaDesdeService() {
		return fechaDesdeService;
	}

	public void setFechaDesdeService(String fechaDesde) {
		this.fechaDesdeService = fechaDesde;
	}

	public String getFechaHastaService() {
		return fechaHastaService;
	}
	public void setFechaHastaService(String fechaDesde) {
		this.fechaHastaService = fechaDesde;
	}
	
	
}
