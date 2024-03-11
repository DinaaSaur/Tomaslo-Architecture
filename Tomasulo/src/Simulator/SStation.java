package Simulator;

public class SStation {
	boolean busy;
	int address;
	double v;
	String q;
	int remainingCycles;
	
	public SStation () {
		this.busy = false;
		this.address = -1;
		this.v = 0.0;
		this.q = "";
		this.remainingCycles = -5;
		
	}
	
	public String toString() {
		return "Busy: "+busy  + " , Value: " + v + " , Q: " + q + " , Address: " + address + " , Remaining Cycles: "+ remainingCycles +"\n";
	}

}