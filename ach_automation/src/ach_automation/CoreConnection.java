package ach_automation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLSocket;

import org.json.JSONObject;

public class CoreConnection extends Thread{
	
	public final static int DISCONNECTED = 0;
	public final static int CONNECTED = 1;
	public final static int CLOSED = 2;
	
	private SSLSocket module_socket;
	private String module_name;
	private String module_key;
	private String core_host;
	private int core_port;
	private CoreInterface core_interface;
	private int connection_state;
	
	private PrintWriter os;
	private BufferedReader is;
	
	public CoreConnection(CoreInterface pCore, String pHost, int pPort, String pName, String pKey) {
		this.core_interface = pCore;
		this.core_host = pHost;
		this.core_port = pPort;
		this.module_name = pName;
		this.module_key = pKey;
		this.connection_state = DISCONNECTED;
	}
	
	public void run() {
		try {
			//Ouverture de la connexion au noyau ACH
			module_socket = SSLSocketKeystoreFactory.getSocketWithCert(this.core_host, this.core_port, this.core_interface.getClass().getResourceAsStream("/PUBLIC_OWNET.jks"), "OwnetClientPassword", SSLSocketKeystoreFactory.SecureType.TLSv1_2);
			module_socket.startHandshake();
		}
		catch(KeyManagementException | NoSuchAlgorithmException | CertificateException | KeyStoreException | IOException e) {e.printStackTrace();}
		
		try {
			//Ouverture du flux sortant
			this.os = new PrintWriter(new OutputStreamWriter(module_socket.getOutputStream()));
			//Construction du paquet IDENTITE
			JSONObject identity_json = new JSONObject();
			identity_json.put("type", "packet.identity");
			identity_json.put("identity.module_name", this.module_name);
			identity_json.put("identity.module_key", this.module_key);
			//Envoi du paquet IDENTITE
			this.os.println(identity_json);
			this.os.flush();		
		}
		catch(IOException e) {e.printStackTrace();}
		
		try {
			//Ouverture du flux entrant
			this.is = new BufferedReader(new InputStreamReader(this.module_socket.getInputStream()));
		}
		catch(IOException e) {e.printStackTrace();}
		String tmp_mes;
		
		//Boucle de lecture des messages entrants
		while(connection_state != CLOSED) {
			try {
				tmp_mes = is.readLine();
				//Conversion du message reçu en structure JSON
				JSONObject received_message = new JSONObject(tmp_mes);
				switch(received_message.getString("type")) {
				case "packet.ack":
					//PAQUET ACKNOWLEDGMENT
					this.connection_state = CONNECTED;
					break;
				default:
					//Paquet envoye a default au noyau du module
					this.core_interface.handleMessage(received_message);
					break;
					
				}
			}
			catch(IOException e) {e.printStackTrace();}
		}
	}
	
	
	
}
