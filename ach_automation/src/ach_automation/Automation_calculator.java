package ach_automation;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;

public class Automation_calculator extends Automation_processor{
	
	private String proc_formula;
	private int proc_deg;
	private Double proc_x;
	private Double proc_y;
	private Double proc_z;
	private DoubleEvaluator eval;
	private StaticVariableSet<Double> variables;
	
	public Automation_calculator(String pName, String pFormula) throws RoutingException{
		super(pName);
		this.proc_formula = pFormula;
		this.proc_x = null;
		this.proc_y = null;
		this.proc_z = null;
		
		this.eval = new DoubleEvaluator();
		this.variables = new StaticVariableSet<Double>();
		
		//DEFINITION DU NOMBRE D'INCONNUES
		if(pFormula.contains("z")) {
			this.proc_deg = 3;
		}
		else if(pFormula.contains("y")) {
			this.proc_deg = 2;
		}
		else if(pFormula.contains("x")) {
			this.proc_deg = 1;
		}
		else {
			this.proc_deg = 0;
		}
	}

	@Override
	public void sourceValueChange(int input_port, String input_value) {
		if(input_port == 1) {
			this.proc_x = Double.valueOf(input_value);
		}
		else if(input_port == 2) {
			this.proc_y = Double.valueOf(input_value);
		}
		else if(input_port == 3) {
			this.proc_z = Double.valueOf(input_value);
		}
		this.doCalcul();
	}
	
	private void doCalcul() {
		if(this.proc_deg == 3 && this.proc_x != null && this.proc_y != null && this.proc_z != null) {
			this.variables.set("x", this.proc_x);
			this.variables.set("y", this.proc_y);
			this.variables.set("z", this.proc_z);
		}
		else if(this.proc_deg == 2 && this.proc_x != null && this.proc_y != null) {
			this.variables.set("x", this.proc_x);
			this.variables.set("y", this.proc_y);
		}
		else if(this.proc_deg == 1 && this.proc_x != null) {
			this.variables.set("x", this.proc_x);
		}
		String result_tmp = String.valueOf(this.eval.evaluate(proc_formula, variables));
		this.setValue(result_tmp, 0);
	}

}
