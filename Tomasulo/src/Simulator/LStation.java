package Simulator;

public class LStation {
	boolean busy;
	int address;
	int remainingCycles;
	
	
	public LStation () {
		this.busy = false;
		this.address = -1;
		this.remainingCycles = -5;
	}
	
	public String toString() {
		return "Busy: " +busy + " , Address: " + address + " , Remaining Cycles: "+ remainingCycles +"\n";
	}

}
