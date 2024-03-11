package Simulator;

public class MAStation {
	boolean busy;
	String op;
	double vJ;
	double vK;
	String qJ;
	String qK;
	String a;
	int remainingCycles ;
	
	public MAStation() {
		busy = false;
		op = "";
		vJ = 0;
		vK = 0;
		qJ = "";
		qK = "";
		a = "";
		remainingCycles = -5;
	}
	
//	public MAStation(String op, double vJ, double vK, String qJ, String qK, String a) {
//		busy = true;
//		this.op = op;
//		this.vJ = vJ;
//		this.vK = vK;
//		this.qJ = qJ;
//		this.qK = qK;
//		this.a = a;
//	}
	
	public String toString() {
		return "Busy: "+busy + " , Operation: " + op + " , Value1: " + vJ + " , Value2:  " + vK + " , Q1: " + qJ + " , Q2: " + qK + " , A: " + a + " , Remaining Cycles: "+ remainingCycles +"\n";
	}
}