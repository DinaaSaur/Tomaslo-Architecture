package Simulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	static ArrayList<Instruction> instructionArray;
	static ArrayList<Instruction> pending;
	static Register regFile[];
	static MAStation aRes[];
	static MAStation mRes[];
	static LStation lRes[];
	static SStation sRes[];
	static int clk = 0;
	static int latencyADD_D = -1;
	static int latencySUB_D = -1;
	static int latencyMUL_D = -1;
	static int latencyDIV_D = -1;
	static int latencyMUL = -1;
	static int latencyDIV = -1;
	static int latencySUB_I = -1;
	static int latencyL_D = -1;
	static int latencyS_D = -1;
	static int latencyADD = -1;
	static int latencySUB = -1;
	static int latencyADD_I = 1;
	static int latencyBNEZ = 1;
	static int pc=0;
	static int issuePC=0;
	static int ares;
	static int mres;
	static int lres;
	static int sres;
	static int regSize;
	static double[] Cache = new double[256];
	static boolean firstlastP=true;
	static int total;
	static ArrayList<KVP> done;
	static boolean branching;


	
	public Main(int aResSize,int mResSize,int sResSize,int lResSize,int RegsSize) {
		Main.instructionArray = new ArrayList<>();
		Main.done = new ArrayList<>();
		Main.pending = new ArrayList<>();
		Main.lRes = new LStation[lResSize];
		Main.aRes = new MAStation[aResSize];
		Main.mRes = new MAStation[mResSize];
		Main.sRes = new SStation[sResSize];
		Main.regFile = new Register[RegsSize];
		//Main.Cache = new double[1024];
		for (int i = 0; i < aRes.length; i++) {
			aRes[i] = new MAStation();
		}
		for (int i = 0; i < mRes.length; i++) {
			mRes[i] = new MAStation();
		}
		for(int i =0; i<lRes.length;i++) {
			lRes[i] = new LStation();
		}
		for(int i =0; i<sRes.length;i++) {
			sRes[i] = new SStation();
		}
	}
//	public static void init(int aResSize,int mResSize,int RegsSize) {
//		Main.aRes = new MAStation[aResSize];
//		Main.mRes = new MAStation[mResSize];
//		Main.regFile = new Register[RegsSize];
//	}
	public static void printRegFiles() {
		for(int i=0;i<regFile.length/2;i++) {
			System.out.println("Register F"+i+": Q: "+regFile[i].q + ". Value: "+regFile[i].value);
		}
		for(int i=regFile.length/2;i<regFile.length;i++) {
			System.out.println("Register R"+i+": Q: "+regFile[i].q + ". Value: "+(int)regFile[i].value);
		}
	}
	public static void printInstructionArray() {
		for(int i =0;i<instructionArray.size();i++) {
			System.out.println("Instruction num: " + (i+1) + " operation: "+ instructionArray.get(i).op +" " );
		}
	}
	
	public static void readDataFromFile(String path) {
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(path));
			String line = reader.readLine();

			while (line != null) {
				String[] values = line.split(" ");
				if((values[0].equals("L.D")||(values[0].equals("S.D"))||(values[0].equals("MUL.D"))||(values[0].equals("SUB.D"))||(values[0].equals("ADD.D"))||(values[0].equals("DIV.D"))
						||(values[0].equals("ADD"))||(values[0].equals("SUB"))||(values[0].equals("ADDI"))||(values[0].equals("SUBI"))||(values[0].equals("MUL"))||(values[0].equals("DIV"))||(values[0].equals("BNEZ")))){
				if ((values[0].equals("L.D") ) || (values[0].equals( "S.D"))) {
				 Instruction i = new Instruction(values[0], values[1],values[2],true);
				 instructionArray.add(i);
				// line = reader.readLine();
				}else if((values[0].equals("ADDI"))||(values[0].equals("SUBI"))) {
					Instruction i = new Instruction(values[0], values[1], values[2], Integer.parseInt(values[3]));
					instructionArray.add(i);
				}else if(values[0].equals("BNEZ")) {
					Instruction i = new Instruction(values[0], values[1], values[2]);
					instructionArray.add(i);
					
				}
				else {
				Instruction i = new Instruction(values[0], values[1], values[2], values[3]);
				instructionArray.add(i);
				}
//				System.out.println(line);
				// read next line
				line = reader.readLine();
			}else {
				if ((values[1].equals("L.D") ) || (values[1].equals( "S.D"))) {
					 Instruction i = new Instruction(values[0], values[1],values[2],values[3],true);
					 instructionArray.add(i);
					 line = reader.readLine();
					}else if((values[1].equals("ADDI"))||(values[1].equals("SUBI"))) {
						Instruction i = new Instruction(values[0], values[1], values[2],values[3], Integer.parseInt(values[4]));
						instructionArray.add(i);
					}else if(values[1].equals("BNEZ")) {
						Instruction i = new Instruction(true,values[0], values[1], values[2],values[3]);
						instructionArray.add(i);
						
					}
					else {
						//System.out.println(values.length);
						//System.out.println(values[0].toString());
					Instruction i = new Instruction(values[0], values[1], values[2], values[3],values[4]);
					instructionArray.add(i);
					}
//					System.out.println(line);
					// read next line
					line = reader.readLine();
			}
			}
			total = instructionArray.size();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void readRegsFromFile(String path) {
		BufferedReader reader;
		int i = 0;
		
		try {
			reader = new BufferedReader(new FileReader(path));
			String line = reader.readLine();
			
			while (line != null) {
				String[] values = line.split(" ");
				Register r = new Register(Double.parseDouble(values[0]));
				regFile[i++] = r;
				line = reader.readLine();
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 public static void readLatencyFromFile(String path) {
	        BufferedReader reader;

	        try {
	            reader = new BufferedReader(new FileReader(path));
	            String line = reader.readLine();

	            while (line != null) {
	                updateLatencyValues(line);
	                line = reader.readLine();
	            }

	            reader.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        System.out.println("Latency values updated:");
	        System.out.println("ADD.D: " + latencyADD_D);
	        System.out.println("SUB.D: " + latencySUB_D);
	        System.out.println("MUL.D: " + latencyMUL_D);
	        System.out.println("DIV.D: " + latencyDIV_D);
	        System.out.println("SUB.I: " + latencySUB_I);
	        System.out.println("L.D: " + latencyL_D);
	        System.out.println("S.D: " + latencyS_D);
	    }
	 public static void updateLatencyValues(String line) {
	        String[] parts = line.split("\\s+");

	        if (parts.length == 2) {
	            String instruction = parts[0];
	            int latency = Integer.parseInt(parts[1]);

	            switch (instruction) {
	                case "ADD.D":
	                    latencyADD_D = latency;
	                    break;
	                case "ADD":
	                	latencyADD = latency;
	                	break;
	                case "SUB.D":
	                    latencySUB_D = latency;
	                    break;
	               
	                case "MUL.D":
	                    latencyMUL_D = latency;
	                    break;
	                case "MUL":
	                    latencyMUL = latency;
	                    break;
	                case "DIV.D":
	                    latencyDIV_D = latency;
	                    break;
	                case "DIV":
	                	latencyDIV = latency;
	                	break;
	                case "SUB.I":
	                    latencySUB_I = latency;
	                    break;
	                case "SUB":
	                	latencySUB = latency;
	                	break;
	                case "L.D":
	                    latencyL_D = latency;
	                    break;
	                case "S.D":
	                    latencyS_D = latency;
	                    break;
	                default:
	                    System.out.println("Unrecognized instruction: " + instruction);
	                    break;
	            }
	        }
	    }
	
	public static int checkAM(char op) {
		if (op == 'A') {
			for (int i = 0; i < aRes.length; i++) {
				//System.out.println(aRes[i].toString());
				if (aRes[i].busy == false) {
					return i;
				}
			}
		} else {
			for (int i = 0; i < mRes.length; i++) {
				if (mRes[i].busy == false) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public static int checkLS(char op) {
		if (op == 'L') {
			for (int i = 0; i < lRes.length; i++) {
				if (lRes[i].busy == false) {
					return i;
				}
			}
		} else {
			for (int i = 0; i < sRes.length; i++) {
				if (sRes[i].busy == false) {
					return i;
				}
			}
		}
		return -1;
	}
	
	
	public static void addToRes(Instruction curr, char c, int index, int cyc) {
		if (c == 'A') {
			MAStation dest = aRes[index];
			
			Register reg2 = null;
			int immediate=-5;
			if(!curr.op.equals("BNEZ")) {
				Register reg1 = regFile[Integer.parseInt(curr.src1.substring(1) + "")];
			if(!curr.im1) {
			 reg2 = regFile[Integer.parseInt(curr.src2.substring(1) + "")];
			 if (reg2.q == "0") {
					dest.vK = reg2.value;
				} else {
					dest.qK = reg2.q;
				} 
			}
			else {
				immediate = curr.immediate;
				dest.vK=immediate;
				if(reg1.q.equals("0")) {
					dest.vJ = reg1.value;
				}else {
					dest.qJ = reg1.q;
				}
			}
			dest.busy = true;
			dest.op = curr.op;
			if (reg1.q == "0") {
				dest.vJ = reg1.value;
			} else {
				dest.qJ = reg1.q;
			}
			regFile[Integer.parseInt(curr.dest.substring(1) + "")].q = "A" + index;
			}
			else {
				Register reg3 = regFile[Integer.parseInt(curr.reg1.substring(1)+"")];
				dest.busy = true;
				dest.op = curr.op;
				if(reg3.q == "0") {
					dest.vJ = reg3.value;
				}
				else {
					dest.qJ = reg3.q;
				}
				dest.a = curr.labelTarget;
//				regFile[Integer.parseInt(curr.reg1.substring(1) + "")].q = "A" + index;
			}
			
			
			dest.remainingCycles = cyc;
			aRes[index] = dest;
		} else if (c == 'M'){
			MAStation dest = mRes[index];
			Register reg1 = regFile[Integer.parseInt(curr.src1.substring(1) + "")];
			Register reg2 = regFile[Integer.parseInt(curr.src2.substring(1) + "")];
			dest.busy = true;
			dest.op = curr.op;
			if (reg1.q == "0") {
				dest.vJ = reg1.value;
			} else {
				dest.qJ = reg1.q;
			}
			if (reg2.q == "0") {
				dest.vK = reg2.value;
			} else {
				dest.qK = reg2.q;
			}
			
			regFile[Integer.parseInt(curr.dest.substring(1) + "")].q = "M" + index;
			dest.remainingCycles = cyc;
			mRes[index] = dest;
		} else if (c == 'L'){
			LStation row = lRes[index];//row of L station
			Register reg1 = regFile[Integer.parseInt(curr.reg1.substring(1) + "")];//register that needs to have its q updated
			int address = curr.address;
			row.busy = true;
			row.address = address;

			regFile[Integer.parseInt(curr.reg1.substring(1) + "")].q = "L" + index;
			row.remainingCycles = cyc;
			lRes[index] = row;
		}
		else if (c == 'S'){
			SStation row = sRes[index];
			Register reg1 = regFile[Integer.parseInt(curr.reg1.substring(1) + "")];
			int address = curr.address;
			row.busy = true;
			row.address = address;
			
			if (reg1.q == "0") {
				row.v = reg1.value;
				
		}
			else {
				row.q = reg1.q;
			}
			
			// Bus is an array of Key Value Pairs [(R1,10.2),(R4,12.0),(R8,9.0)]  Bus.add(R3,18.3)
			//method WriteBack
		
			
			
			row.remainingCycles = cyc;
			sRes[index] = row;
		}
			
	}
	
	public static void issue() {
		if(instructionArray.size()>issuePC) {
		Instruction curr = instructionArray.get(issuePC);
		//System.out.println(issuePC);
		
		int cyc = 0;
		switch(curr.op) {
			case "ADD.D":cyc = latencyADD_D;
			int ret = checkAM('A');
			if (ret > -1) {
				curr.issue = clk;
				addToRes(curr, 'A', ret,cyc);
				issuePC++;
			} else {
				
			}
			break;
			case "SUB.D":cyc = latencySUB_D;
			 ret = checkAM('A');
			if (ret > -1) {
				curr.issue = clk;
				addToRes(curr, 'A', ret,cyc);
				issuePC++;
			} else {
				
			}
			break;
			case "ADD" :cyc = latencyADD;
			 ret = checkAM('A');
			if (ret > -1) {
				curr.issue = clk;
				addToRes(curr, 'A', ret,cyc);
				issuePC++;
			} else {
				
			}
			break;
			case "ADDI" :cyc = latencyADD_I;
			 ret = checkAM('A');
			if (ret > -1) {
				curr.issue = clk;
				addToRes(curr, 'A', ret,cyc);
				issuePC++;
			} else {
				
			}
			break;
			case "SUB" :cyc = latencySUB;
			 ret = checkAM('A');
			if (ret > -1) {
				curr.issue = clk;
				addToRes(curr, 'A', ret,cyc);
				issuePC++;
			} else {
				
			}
			break;
			case "SUBI" :cyc = latencySUB_I;
				 ret = checkAM('A');
				if (ret > -1) {
					curr.issue = clk;
					addToRes(curr, 'A', ret,cyc);
					issuePC++;
				} else {
					
				}
				break;
			case "BNEZ":cyc = latencyBNEZ;
			 ret = checkAM('A');
			if (ret > -1) {
				curr.issue = clk;
				branching = true;
				addToRes(curr, 'A', ret,cyc);
				issuePC++;
			} else {
				
			}
			break;
			case "MUL.D": cyc= latencyMUL_D;
			int ret2 = checkAM('M');
			if (ret2 > -1) {
				curr.issue = clk;
				addToRes(curr, 'M', ret2 ,cyc);
				issuePC++;

			} else {
				
			}
			break;
			case "DIV.D":cyc= latencyDIV_D;
			 ret2 = checkAM('M');
			if (ret2 > -1) {
				curr.issue = clk;
				addToRes(curr, 'M', ret2 ,cyc);
				issuePC++;

			} else {
				
			}
			break;
			case "MUL":cyc= latencyMUL;
			 ret2 = checkAM('M');
			if (ret2 > -1) {
				curr.issue = clk;
				addToRes(curr, 'M', ret2 ,cyc);
				issuePC++;

			} else {
				
			}
			break;
			case "DIV" :cyc= latencyDIV;
				 ret2 = checkAM('M');
				if (ret2 > -1) {
					curr.issue = clk;
					addToRes(curr, 'M', ret2 ,cyc);
					issuePC++;

				} else {
					
				}
				break;
			case "L.D" : cyc = latencyL_D;
			int ret3 = checkLS('L');
			if(ret3>-1) {
				curr.issue = clk;
				addToRes(curr, 'L', ret3 ,cyc);
				issuePC++;

				
			}
			   break;
			case "S.D" : cyc = latencyS_D;
			int ret4 = checkLS('L');
			if(ret4>-1) {
				curr.issue = clk;
				addToRes(curr, 'S', ret4 ,cyc);
				issuePC++;

			}
			break;
		//	case 
			default: break;
			
		}
		}
	}
	static boolean first = true;

	public static void startClock() {
		if(clk==0) {
			for(int i=0;i<Cache.length;i++) {
				System.out.println("Mem "+ i+": " + Cache[i] +" ");
			}
			System.out.println(".......................................");

		}
				//print clk info
		if (pc >= total) {
			for(int i=0;i<Cache.length;i++) {
				System.out.println("Mem "+ i +": " + Cache[i] +" ");
			}
			return;
			
		}
//		if(clk>40) {
//			return;
//		}
		
		clk++;
		
		System.out.println("Clock Cycle: "+clk);
		System.out.println("-----------------------");
		if(!first) {
		execute();
		WB2();
		}
		if (!branching) {
		issue();
		}
		first=false;
		
		printRegFiles();
		
		
		
		printAll();
		
		System.out.println("..........................................................");



		startClock();
		
		
	}
	public static void printAll() {
		System.out.println("ADD/SUB RESERVATION STATION:");
		for(int i =0; i<aRes.length;i++) {
			
			System.out.println(aRes[i].toString());
		}
		System.out.println("MUL/DIV RESERVATION STATION:");

		for(int i =0; i<mRes.length;i++) {
			System.out.println(mRes[i].toString());
		}
		System.out.println("LOAD RESERVATION STATION:");

		for(int i =0; i<lRes.length;i++) {
			System.out.println(lRes[i].toString());
		}
		System.out.println("STORE RESERVATION STATION:");

		for(int i =0; i<sRes.length;i++) {
			System.out.println(sRes[i].toString());
		}
		
	}
	//2
	//exec1
	//1
	//exec2
	//0
	
	public static void execute() {
		for(int i=0;i<aRes.length;i++) {
			if ((aRes[i].busy == true) && ((aRes[i].qJ == "") && (aRes[i].qK == ""))) {
				aRes[i].remainingCycles --;
			}
		}
		
		for(int i=0;i<mRes.length;i++) {
			if ((mRes[i].busy == true) && ((mRes[i].qJ == "") && (mRes[i].qK == ""))) {
				mRes[i].remainingCycles --;
			}
		}
		
		//
		
	
		
		for(int i=0;i<aRes.length;i++) {
			if(aRes[i].remainingCycles==-1) {
				
			
				pc++;
				switch (aRes[i].op) {
				case "ADD":
				case "ADDI":
				case "ADD.D" : WB(executeAdd(aRes[i]),i,'A'); break;
				case "SUB":
				case "SUB.D":
				case "SUBI":WB(executeSub(aRes[i]),i,'A'); break;
				case "BNEZ": executeBranch(aRes[i]) ; branching = false; break;
				default: break;
				
					
				
				}
				aRes[i]= new MAStation();
				
			}
		}
		for(int i = 0; i<mRes.length;i++) {
			if(mRes[i].remainingCycles==-1) {
				pc++;
				
				switch (mRes[i].op) {
				case "MUL":
				case "MUL.D": WB(executeMul(mRes[i]),i,'M'); break;
				case "DIV":
				case "DIV.D": WB(executeDiv(mRes[i]),i,'M'); break;
				default: break;
				
				
				
				}
				mRes[i]= new MAStation();
			
			}
		}
		for(int i=0; i<lRes.length;i++) {
			//System.out.println(lRes.length);
			if(lRes[i].busy == true)  {
				lRes[i].remainingCycles--;
			}
			if(lRes[i].remainingCycles==-1) {
				pc++;
				WB(executeLoad(lRes[i]),i,'L');
				lRes[i] = new LStation();
			}
			
		}
		for(int i=0; i<sRes.length;i++) {
			if((sRes[i].busy == true) && sRes[i].q ==""){
				sRes[i].remainingCycles--;
			}
			if(sRes[i].remainingCycles==-1) {
				pc++;
				executeStore(sRes[i]); 
				sRes[i] = new SStation();
			}
		
		}
		
	}
	
	
	
	public static void executeBranch(MAStation a) {
		if(a.vJ==0)
			return;
		String target = a.a;
		int now = issuePC;
		for(int i =0;i<instructionArray.size();i++) {
			if(!instructionArray.get(i).label.isEmpty()) {
			if(instructionArray.get(i).label.equals(target)) {
				issuePC = i;
				if(now>issuePC) {
					int dif = now-issuePC;
					total+=dif;
				}
				else {
					int sum = issuePC-now;
					total-=sum;
				}
			}
			}
			
			
		}
	}
	public static double executeAdd(MAStation a) {
		return a.vJ + a.vK;
		
	}
	public static double executeLoad(LStation a) {
		return Cache[a.address];
		
	}
	public static void executeStore(SStation a) {
		//for(int i=0;i<)
		Cache[a.address]= a.v;
		
	}
	public static double executeSub(MAStation a) {
		return a.vJ - a.vK;
		
	}
	public static double executeMul(MAStation a) {
		return a.vJ * a.vK;
		
	}
	public static double executeDiv(MAStation a) {
		return a.vJ / a.vK;
		
	}
	
	public static void WB(double value, int target,char x) {
		String c = target +"";
		String e = x+c;
		
	
		KVP s = new KVP(e,value);
		done.add(s);
		
		
		
	}
	
	public static void WB2() {
		if(done.isEmpty())
			return;
		Register[] a =regFile;
		
		double value = done.get(0).value;
		String e = done.get(0).Loc;
		
		for(int i =0;i<a.length;i++) {
			if(a[i].q.equals(e)) {
				regFile[i].q="0";
				regFile[i].value=value;
				break;
			}
		}
		
		for(int i=0;i<aRes.length;i++) {
			if(aRes[i].qJ.equals(e)) {
				aRes[i].qJ = "";
				aRes[i].vJ = value;
			}
			if(aRes[i].qK.equals(e)) {
				aRes[i].qK = "";
				aRes[i].vK = value;
			}
		}
		for(int i=0;i<mRes.length;i++) {
			if(mRes[i].qJ.equals(e)) {
				mRes[i].qJ = "";
				mRes[i].vJ = value;
			}
			if(mRes[i].qK.equals(e)) {
				mRes[i].qK = "";
				mRes[i].vK = value;
			}
		}
		for(int i=0;i<sRes.length;i++) {
			if(sRes[i].q.equals(e)) {
				sRes[i].q = "";
				sRes[i].v = value;
			}
			
		}
		done.remove(0);
		
	}
	
	
	
	public static void main(String[] args) {
		
		System.out.println("Please enter file name for your code, registers, and latencies:");
		Scanner sc = new Scanner(System.in);
		String path = sc.nextLine();
		String pathReg = sc.nextLine();
		String pathLat = sc.nextLine();
		System.out.println("Please enter sizes of aRes, mRes, and RegFile size respectively:");
		 ares = Integer.parseInt(sc.nextLine());
		 mres = Integer.parseInt(sc.nextLine());
		 regSize = Integer.parseInt(sc.nextLine());
		 sres=Integer.parseInt(sc.nextLine());
		 lres=Integer.parseInt(sc.nextLine());

		sc.close();
		Cache[10] = 10.0;
		Cache[12] = 12.0;
//		String path = "code.txt";
//		String pathReg = "regs.txt";
//		String pathLat = "cycles.txt";
		
		
		
		
		new Main(ares,mres,sres,lres,regSize);
		
		readDataFromFile("src/" + path);
		//System.out.println(instructionArray.toString());
		printInstructionArray();
		readRegsFromFile("src/" + pathReg);
		readLatencyFromFile("src/" + pathLat);
		printRegFiles();
//		System.out.println(instructionArray);
//		System.out.println(instructionArray.get(pc));
//		System.out.println(aRes[0]);
//		System.out.println(mRes[0]);
		Main.startClock();
	}
}