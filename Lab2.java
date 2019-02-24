import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class Lab2{
	
	private static boolean verbose = false;
	private static boolean print = true;
	private static boolean print2 = true;

	public static ArrayList <Process> readInput (String[] args) throws UnsupportedEncodingException{

		ArrayList<Process> arrProcesses = new ArrayList<Process>();
		File inputFile = null; 

		// open file for reading 
		Scanner input = null;
		if (args.length == 1) {
			inputFile = new File(args[0]);
		} else if (args.length == 2) {
			inputFile = new File(args[1]);
			if (args[0].equals("--verbose")) verbose = true;

		}
		else {
			System.err.println("Incorrect commandline input");
			System.exit(0);

		}

		try {
			
			input = new Scanner (inputFile);

		} catch (FileNotFoundException e) {
			
			System.err.printf("Error: cannot read the input file %s\n\n", args[0]);
			System.exit(1);
		}
		

		Queue <Process> unsortedq = new LinkedList <Process>();

	

			File rrfile = new File("src/randomnums.txt");
			int num_of_processes = input.nextInt();

			for(int i = 0; i < num_of_processes; i ++) {
				String a = input.next();
				int A = Integer.parseInt(a.substring(1, a.length()));//process arrival time
				int B = input.nextInt(); //use randomOS(B) to obtain CPU burst time
				int C = input.nextInt(); //total CPU time needed for process
				String m = input.next();
				int M = Integer.parseInt(m.substring(0, m.length()-1)); //multiply with preceding CPU burst time to get the I/O burst time
				Process process = new Process(i, A, B, C, M);
				unsortedq.add(process);
			}
			if(print == true) {
			System.out.print("Unsorted: " + num_of_processes);
			}
			Iterator <Process> qiter = unsortedq.iterator ();
			
			//add each process from the original queue into the array list
			while(qiter.hasNext()){
				Process currproc = qiter.next();
				if(print == true) {
				System.out.print(" (" + currproc.arrivalTime + "," + currproc.b + "," + currproc.totalCPUtime + "," + currproc.m + ")");
				}
				arrProcesses.add(currproc);
			}
			print = false;
			System.out.println();

			
			//below sorts the array list of processes by arrival time first
			//then inserting them into the ready queue
			ArrayList <Process> duplicates = new ArrayList <Process>();
			ArrayList <Process> rrduplicates = new ArrayList<Process>();
			//System.out.print("Sorted: " + num_of_processes + " ");
			while(arrProcesses.size() != 0) {
				Process min = arrProcesses.get(0);
				for(int j = 0; j < arrProcesses.size(); j++) {
					if(arrProcesses.get(j).arrivalTime < min.arrivalTime) {
						min = arrProcesses.get(j);
					}
					if(arrProcesses.get(j).arrivalTime == min.arrivalTime) {
						if(arrProcesses.get(j).pid < min.pid) {
							min = arrProcesses.get(j);
						}
					}
				}
				
				arrProcesses.remove(min);
				duplicates.add(min);
				rrduplicates.add(min);
	
			}
			
			//first arrival times are at the front of the queue
			//all processes in the queue have the state = ready
			if(print2 == true) {
				System.out.print("Sorted: " + num_of_processes);
				for(int h = 0; h < rrduplicates.size(); h ++) {
					Process currentProcess = duplicates.get(h);
					System.out.print(" (" + currentProcess.arrivalTime + "," + currentProcess.b + "," + currentProcess.totalCPUtime + "," + currentProcess.m + ")");
					print2 = false;
					}
			}
			
			input.close();
			
			return duplicates;
		

//		catch(FileNotFoundException e) {
//			System.err.printf("Issue opening the file");
//		}
//		
//		return duplicates;
			
	}//end of readInput method

	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException {

		File file = new File("random-numbers.txt");
	
		ArrayList <Process> arrPrcs = readInput(args);
		ArrayList <Process> arrPrcs2 = readInput(args);
		ArrayList <Process> arrPrcs3 = readInput(args);
		ArrayList <Process> arrPrcs4 = readInput(args);
		
		FCFS.fcfs(arrPrcs, file, verbose);
		System.out.println("\n\n\n");
		RR.rr(arrPrcs2, file, verbose);
		System.out.println("\n\n\n");
		LCFS.lcfs(arrPrcs3, file, verbose);
		System.out.println("\n\n\n");
		HPRN.hprn(arrPrcs4, file, verbose);
		System.out.println("\n\n\n");
		
	}


}//end of lab2 class

