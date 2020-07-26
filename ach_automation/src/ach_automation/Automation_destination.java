package ach_automation;

import java.util.ArrayList;

import org.json.JSONObject;

public class Automation_destination extends Automation_source implements AutomationSourceListener {
	
	private static ArrayList<Automation_destination> destination_list = new ArrayList<Automation_destination>();
	
	public Automation_destination(String pName) throws RoutingException {
		super(pName);
	}

	@Override
	public void sourceValueChange(int source_port, String source_value) {
		//Analyse du nom de la destination pour envoi. Format du nom : <module>:<destinataire>:<nom_valeur>
		String[] split_dest = super.getSourceName().split(":");
		
		//Creation d'un packet data
		JSONObject data_json = new JSONObject();
		data_json.put("type", "packet.data");
		data_json.put("data.dst", split_dest[1]);
		data_json.put("data.name", split_dest[2]);
		
		//Envoi du paquet au module spécifié 
		Automation_core.core_connection.sendData(split_dest[0], data_json.toString());	
	}
	
	public static Automation_destination getDestinationByName(String pName) {
		for(Automation_destination dest_tmp : destination_list) {
			if(dest_tmp.getSourceName().equals(pName)) {return dest_tmp;}
		}
		return null;
	}
	
	public static ArrayList<Automation_destination> getDestinations() {return destination_list;}

}
