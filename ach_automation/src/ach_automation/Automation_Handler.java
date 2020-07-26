package ach_automation;

import org.json.JSONObject;

public class Automation_Handler implements CoreInterface{

	@Override
	public void handleMessage(JSONObject packet) {
		switch(packet.getString("type")) {
		
		//-----PAQUET COMMANDE-----
		case "packet.command":
			String[] command_args = packet.getString("command.args").split(" ");
			switch(command_args[0]) {
			
			case "add":
				//COMMAND ADD : ajout d'un module, d'une entrée ou d'une sortie
				
				break;
			}
			break;
		
		}
		
	}

}
