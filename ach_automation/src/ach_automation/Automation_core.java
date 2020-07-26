package ach_automation;


public class Automation_core {
	
	public static String MODULE_NAME = "ach_automation";
	public static String HOST = "localhost";
	public static int PORT = 9999;
	
	public static CoreConnection core_connection;
	public static Automation_Handler core_handler;
	

	public static void main(String[] args) {
		core_handler = new Automation_Handler();
		core_connection = new CoreConnection(core_handler,HOST,PORT,MODULE_NAME,args[0]);
	}
	
}
