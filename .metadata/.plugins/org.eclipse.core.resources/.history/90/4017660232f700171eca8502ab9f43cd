package biactiva.services.principal;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import biactiva.services.fileHandler.CSVHandler;
import biactiva.services.singletons.entidadesARecuperar.SFTPPropiedades;

public class SFTP {
	
	private SFTPPropiedades sftpPropiedades;
	
	
	public SFTP(SFTPPropiedades sftpPropiedades) {
		this.sftpPropiedades=sftpPropiedades;
	}
	


	public void transferirFichero(String pathInput, String pathOutput) {
    try {
    System.out.println("Tratando de acceder al SFTP");
    JSch jsch = new JSch();
    jsch.addIdentity(sftpPropiedades.getKeyFile());
    Session session = jsch.getSession(sftpPropiedades.getUser(), sftpPropiedades.getHost(), sftpPropiedades.getPuerto());
    java.util.Properties config = new java.util.Properties(); 
    config.put("StrictHostKeyChecking", "no");
    session.setConfig(config);
    session.connect();
    Channel channel = session.openChannel("sftp");
    channel.setInputStream(System.in);
    channel.setOutputStream(System.out);
    channel.connect();
    System.out.println("Conexión exitosa con el SFTP");
    ChannelSftp c = (ChannelSftp) channel;
    String fileName = pathInput;
    c.put(fileName, pathOutput);
    c.exit();
    session.disconnect();
    System.out.println("Transferencia correcta");

	} catch (Exception e) {
	    e.printStackTrace();
	    CSVHandler.getInstance().escribirErrorException(e);
	}
	}


public SFTPPropiedades getSftpPropiedades() {
	return sftpPropiedades;
}



public void setSftpPropiedades(SFTPPropiedades sftpPropiedades) {
	this.sftpPropiedades = sftpPropiedades;
}

}
