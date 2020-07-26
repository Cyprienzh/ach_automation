package ach_automation;

import org.json.JSONObject;

public interface CoreInterface {
	
	//Fonction appelée lorsqu'un message arrive depuis le noyau ACH
	public void handleMessage(JSONObject packet);
}
