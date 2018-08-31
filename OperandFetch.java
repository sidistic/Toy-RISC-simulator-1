package processor.pipeline;

import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}

	public static int binConv(String s){//converts two's complement (32 bit) to decimal value
		int result=0;
		int size=32;
		int mul=1;
		for(int i=31; i >= 0; i--)
		{
			if(s.charAt(i)=='1')
			{
				result+=mul;
			}
			mul*=2;
		}
		return result;
	}

	public static String n_32bit(int n,String s){
		for(int i=n;i<32;i++){
			s=s.substirng(0,1)+s;
		}
		return s;
	}

	public static String check_opcode(String opcode){
		Set<String> R3 = new HashSet<String>();
    R3.addAll(Arrays.asList(new String[] {"0000","00010","00100","00110","01000","01010","01100","01110","10000","10010","10100"}));
		Set<String> R2I = new HashSet<String>();
		R2I.addAll(Arrays.asList(new Integer[] {"00001","00011","00101","00111","01001","01011","01101","01111","10001","10101","10110","10111"}));
		Set<String> R2I_alt = new HashSet<String>();
    R2I_alt.addAll(Arrays.asList(new Integer[] {"11001","11010","11011","11100"}));
		String jmp="10100";
		String end="11101";
		if(R3.contains(opcode)){return "R3";}
		else if(R2I.contains(opcode)){return "R2I";}
		else if(R2I_alt.contains(opcode)){return "R2I_alt";}
		else if(opcode==jmp){return "jmp";}
		else if(opcode==end){return "end";}
	}

	public static Vector<String> R3(String instruction){
		String rs1=instruction.substring(5,10);
		String rs2=instruction.substring(10,15);
		String rd=instruction.substring(15,20);
		Vector v = new Vector();
		v.add(rs1);
		v.add(rs2);
		v.add(rd);
		return v;
	}

	public static vector<String> R2I_or_R2I_alt(String instruction){
		String rs1=instruction.substring(5,10);
		String rd=instruction.substring(10,15);
		String immediate=instruction.substirng(15,32);
		Vector v = new Vector();
		v.add(rs1);
		v.add(rd);
		v.add(immediate);
		return v;
	}

	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO
			int instruction=containingProcessor.getInstruction();
			String bin_instruct=Integer.toBinaryString(instruction);
			//opcode
			String opcode=bin_instruct.substring(0,5);
			//imm
			int imm_value=0;
			int op1=0;
			int op2=0;
			boolean IsStr;
			if(opcode=="10111"){
				IsStr=true;
			}
			if(check_opcode(opcode)=="R2I"){
				Vector v=R2I_or_R2I_alt(bin_instruct);
				imm_value=binConv(n_32bit(v.get(2)));
				op1=binConv(n_32bit(v.get(0)));
				if(IsStr){
					op2=binConv(n_32bit(v.get(1)));
				}
			}
			else if(check_opcode(opcode)=="R2I_alt"){
				Vector v=R2I_or_R2I_alt(bin_instruct);
				imm_value=binConv(n_32bit(v.get(2)));
				op1=binConv(n_32bit(v.get(0)));
				if(!IsStr){
					op2=binConv(n_32bit(v.get(1)));
				}
			}
			else if(check_opcode=="jmp"){
				imm_value=binConv(n_32bit(bin_instruct.substirng(10,32)));
				op1=binConv(n_32bit(v.get(0)));
				if(!IsStr){
					op2=binConv(n_32bit(v.get(1)));
				}
			}
			else if(check_opcode=="R3"){
				Vector v=R2I_or_R2I_alt(bin_instruct);
				op1=binConv(n_32bit(v.get(0)));
				if(!IsStr){
					op2=binConv(n_32bit(v.get(1)));
				}
			}
			OF_EX_Latch.setInstruction(instruction);
			OF_EX_Latch.setOp1(op1);
			OF_EX_Latch.setOp2(op2);
			OF_EX_Latch.setImm(imm_value);
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}
}
