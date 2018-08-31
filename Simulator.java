package generic;

import java.io.ObjectInputStream;
import processor.pipeline.*;
import java.io.*;

import processor.Clock;
import processor.Processor;

public class Simulator {

	static Processor processor;
	static boolean simulationComplete;

	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);

		simulationComplete = false;
	}

	static void loadProgram(String assemblyProgramFile)
	{
		/*
		* TODO
		* 1. load the program into memory according to the program layout described
		*    in the ISA specification
		* 2. set PC to the address of the first instruction in the main
		* 3. set the following registers:
		*     x0 = 0
		*     x1 = 65535
		*     x2 = 65535
		*/
		processor.memorysystem.MainMemory main_mem;
		main_mem=new processor.memorysystem.MainMemory();
		int[] array=new int[10];
		int n=0;
		try{
			InputStream input = new FileInputStream(assemblyProgramFile);
			DataInputStream inst = new DataInputStream(input);
			int value;
			n=input.available()-1;
			array=new int[input.available()];
			int i=0;
			while(input.available()>0){
				value=inst.readInt();
	//			System.out.println(value);
				array[i]=value;
				i++;
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		RegisterFile registers=new RegisterFile();
		registers.setProgramCounter(array[0]);
		registers.setValue(0,0);
		registers.setValue(1,65535);
		registers.setValue(2,65535);
	//	int n=input.available()-1;
		for(int i=0;i<n;i++){
			main_mem.setWord(i,array[i+1]);
		}
		// String s=main_mem.getContentsAsString(0,5);
		// System.out.println(s);

	}

	public static void simulate()
	{
		while(simulationComplete == false)
		{
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			processor.getOFUnit().performOF();
			Clock.incrementClock();
			processor.getEXUnit().performEX();
			Clock.incrementClock();
			processor.getMAUnit().performMA();
			Clock.incrementClock();
			processor.getRWUnit().performRW();
			Clock.incrementClock();
		}

		// TODO
		// set statistics
	}

	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
