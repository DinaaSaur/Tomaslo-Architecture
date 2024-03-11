package Simulator;

public class Instruction {
	String op;
	String dest;
	String src1;
	String src2;
	
	String reg1 ;
	int address;
	String label ="";
	int issue;
	int execComp;
	int wB;
	boolean l_s;
	int immediate;
	boolean im1;
	String labelTarget;
	
	public Instruction(String op, String dest, String src1, String src2) {
		this.op = op;
		this.dest = dest;
		this.src1 = src1;
		this.src2 = src2;
	}
	public Instruction(String op, String dest, String src1, int i) {
		this.op = op;
		this.dest = dest;
		this.src1 = src1;
		this.immediate = i;
		this.im1=true;
	}
	public Instruction (String op, String reg1, String address , boolean l_s){
		this.op = op;
		this.reg1 = reg1;
		this.address = Integer.parseInt(address);
		this.l_s = l_s;
	
		
	}
	public Instruction(String Label,String op, String dest, String src1, String src2) {
		this.op = op;
		this.dest = dest;
		this.src1 = src1;
		this.src2 = src2;
		this.label = Label;
	}
	public Instruction(String Label,String op, String dest, String src1, int i) {
		this.op = op;
		this.dest = dest;
		this.src1 = src1;
		this.immediate = i;
		this.im1=true;
		this.label = Label;

	}
	public Instruction (String Label,String op, String reg1, String address , boolean l_s){
		this.op = op;
		this.reg1 = reg1;
		this.address = Integer.parseInt(address);
		this.l_s = l_s;
		this.label = Label;

	
		
	}
	
	public Instruction (String op, String reg1, String Label){
		this.op = op;
		this.reg1 = reg1;
		this.labelTarget = Label;

	
		
	}
	public Instruction (boolean a,String label2, String op, String reg1, String Label){
		this.op = op;
		this.reg1 = reg1;
		this.label = label2;
		this.labelTarget = Label;

	
		
	}
	
	public String toString() {
		if(!src2.isBlank())
		return "\n{\nop: " + op + "\ndest: " + dest + "\nsrc1: " + src1 + "\nsrc2: " + src2 + "\n}\n";
		else {
			if(im1) {
				return "\n{\nop: " + op + "\ndest: " + dest + "\nsrc1: " + src1 + "\nimmediate: " + immediate + "\n}\n";
			}
			else {
				return "\n{\nop: " + op + "\nreg1: " + reg1 + "\naddress: " + address + "\n}\n";
			}
		}
	}
}