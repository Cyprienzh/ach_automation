package ach_automation;

import java.util.ArrayList;

public abstract class Automation_processor extends Automation_source implements AutomationSourceListener {

	public static ArrayList<Automation_processor> processor_list = new ArrayList<Automation_processor>();
	
	public Automation_processor(String pName) throws RoutingException {
		super(pName);
		processor_list.add(this);
	}
	
	public static Automation_processor getProcessorByName(String pName) {
		for(Automation_processor proc : processor_list) {
			if(proc.getSourceName().equals(pName)) {return proc;}
		}
		return null;
	}
	
	public static ArrayList<Automation_processor> getProcessors() {return processor_list;}
	
	

}
