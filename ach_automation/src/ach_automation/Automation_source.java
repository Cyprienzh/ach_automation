/**
 * Classe Automation_Source : matérialise une source à l'agent d'automatisation du system ACH
 * La source comprend différents ports d'entrées et de sorties qui permettent une gestion complexes du routing entrant.
 * Elle possède un nom unique qui ne peut être réutilisé pour une autre source, destination ou processeur
 */

package ach_automation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.EventListenerList;

public class Automation_source {
	
	private static ArrayList<Automation_source> source_list = new ArrayList<Automation_source>();
	
	private String source_name;
	private String[] source_value;
	private EventListenerList listeners = new EventListenerList();
	
	private Map<AutomationSourceListener, Integer> input_port_list = new HashMap<AutomationSourceListener, Integer>();
	private Map<AutomationSourceListener, Integer> output_port_list = new HashMap<AutomationSourceListener, Integer>();
	
	public Automation_source (String pName) throws RoutingException{
		//Verification de non-existence d'une source, destination ou processeur du meme nom
		for(Automation_source source_chck : source_list) {
			if(source_chck.source_name == pName) {throw new RoutingException("Nom déjà réservé pour une autre source, destination ou processeur");}
		}
		//Definition des paramètres
		this.source_name = pName;
		this.source_value = new String[32];
		//Ajout à la liste des sources
		source_list.add(this);
	}
	
	public void setValue(String pValue, int output_port) {
		this.source_value[output_port] = pValue; // Changement de la valeur du port de la source
		//Envoi de la nouvelle valeur aux destinations ou processeurs connectés
		for(AutomationSourceListener listener : listeners.getListeners(AutomationSourceListener.class)) {
			if(this.output_port_list.get(listener) == output_port) {
				listener.sourceValueChange(this.input_port_list.get(listener), this.source_value[output_port]);
			}
		}
	}
	
	public void addSourceListener(AutomationSourceListener listener, int in_port, int out_port) {
		//Ajout de la destination ou processeur à la liste de diffusion
		listeners.add(AutomationSourceListener.class, listener);
		//Mise en mémoire des ports d'entrée et sortie de la destination ou du processeur
		this.input_port_list.put(listener, in_port);
		this.output_port_list.put(listener, out_port);
	}
	
	
	public static Automation_source getSourceByName(String pName) {
		for(Automation_source source_tmp : source_list) {
			if(source_tmp.source_name.equals(pName)) {return source_tmp;}
		}
		return null;
	}
	
	public static ArrayList<Automation_source> getSources() {return source_list;}
	public String getSourceName() {return this.source_name;}
	
	public class RoutingException extends Exception {
		private static final long serialVersionUID = 1L;
		public RoutingException(String message) {super(message);}
	}
}
