package ach_automation;

import java.util.EventListener;

public interface AutomationSourceListener extends EventListener{
	
	void sourceValueChange(int source_port, String source_value);
	
}
