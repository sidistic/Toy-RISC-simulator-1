package processor.pipeline;

public class OF_EX_LatchType {
	int instruction;
	int op1;
	int op2;
	int imm_value;
	boolean EX_enable;

	public OF_EX_LatchType()
	{
		EX_enable = false;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public void setOp1(int op1){
		this.op1=op1;
	}

	public void setOp2(int op2){
		this.op2=op2;
	}

	public void setImm(int imm){
		this.imm_value=imm;
	}

}
