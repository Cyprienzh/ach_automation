package ach_automation;

import org.json.JSONObject;

public interface CoreInterface {
	
	public void handleMessage(JSONObject packet);

}
