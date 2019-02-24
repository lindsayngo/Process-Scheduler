import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;

public class FCFS{
	
	public static void fcfs(ArrayList <Process> sortedprcs, File randomlist, Boolean v) throws FileNotFoundException {
				
			Queue <Process> readyprcs = new LinkedList <Process>();
			ArrayList <Process> blockedprcs = new ArrayList <Process>(); //array list of blocked processes
		 	ArrayList <Process> terminatedprcs = new ArrayList <Process>();
		 	ArrayList <Process> allPrcs = new ArrayList <Process>();
			Process processrunning = null;
	
		 	Scanner scanfile = new Scanner (randomlist);
	
		 	System.out.println();
		 	
			int cycle = 0;
	 		int iousage = 0;
			
	 		for(int z = 0; z < sortedprcs.size(); z++) {
	 			allPrcs.add(sortedprcs.get(z));
	 		}
	 		
	 
		
		 	while(true) {	
		 	
		 		
	//	 		System.out.println("in the sorted processes: " );
	//	 		for(int u = 0; u < sortedprcs.size(); u ++) {
	//	 			System.out.println(sortedprcs.get(u).pid);
	//	 		}
	 			
		 		if(v == true) {
		 			System.out.print("\nBefore cycle " + cycle + "  ");

		 		for(int w = 0; w < allPrcs.size(); w ++) {
		 			int print = 0;
		 			
		 			if(allPrcs.get(w).state.equals("ready") || allPrcs.get(w).state.equals("terminated")) {
		 				print = 0;
		 			}
		 			
		 			else if(allPrcs.get(w).state.equals("blocked")) {
		 				print = allPrcs.get(w).ioBurst + 1;
		 			}
		 			
		 			else if(allPrcs.get(w).state.equals("running")) {
		 				print = allPrcs.get(w).cpubursttime + 1;
		 			}
		 			
		 			System.out.printf(allPrcs.get(w).state + " " + print + "   ");
	
		 			}
		 		}
				//check if the running process is terminated
		 		if(processrunning != null && processrunning.totalCPUtime == 0) {
		 			//Process is terminated
		 			processrunning.state = "terminated";
		 			processrunning.finishTime = cycle;
		 			//System.out.println(processrunning.pid + " is now terminated");
		 			Process terminated = processrunning;
		 			terminatedprcs.add(terminated);
		 			
		 			processrunning = null;
		 		}//end of checking if the process is terminated
		 		
				
		 		//check if process needs to do IOtime
		 		else if(processrunning != null && processrunning.cpubursttime == 0) {
		 			//System.out.println("Process needs to do IO time");
		 			//Process needs to do IO time
		 			processrunning.state = "blocked";
		 			
		 			Process blockedprc = processrunning;
		 	 		blockedprc.ioBurst = blockedprc.previouscpubursttime * blockedprc.m;
		 	 		//System.out.println("process running ioTime: "+ blockedprc.ioTime);
		 	 		
		 			blockedprcs.add(blockedprc);
		 			//System.out.println(blockedprc.pid + " is now inside the blockedprcs.");
		 			
		 			processrunning = null;
		 		}//end of checking if the process running cpu burst time is 0 and must be put in the blocked processes
		 		
		 		
		 		//if cpurburst time = 0, insert into
		 		//if total cpu time = 0, terminate
		 		//every time process comes out of blocked process, calculate new cpubursttime and iotime
		 		//once a process terminates or cpubursttime = 0, take a new process from the combined ready queue
		 		
				ArrayList<Process> arriving = new ArrayList<Process>();
				Iterator<Process> itrReadyPrcs = sortedprcs.iterator();
			
				while(itrReadyPrcs.hasNext()) {
					Process curr = itrReadyPrcs.next();
					if(curr.arrivalTime == cycle) {
						//System.out.println("process "+ curr.pid + " is now ready to arrive and is put in the ready queue");
						curr.state = "ready";
						arriving.add(curr);
						itrReadyPrcs.remove(); 
					}
				}
				
				ArrayList<Process> unblocked = new ArrayList<Process>();
				Iterator<Process> itrBlockedPrcs = blockedprcs.iterator();	
				
				while(itrBlockedPrcs.hasNext()) {
					Process curr = itrBlockedPrcs.next();
					if (curr.ioBurst == 0) {
						//System.out.println("process "+ curr.pid + " is now ready to be unblocked and is put in the ready queue");
						curr.state = "ready";
						unblocked.add(curr);
						itrBlockedPrcs.remove();
					}
				}
				arriving.addAll(unblocked);
				
				//combine the two lists
				ArrayList <Process> combined = new ArrayList <Process>();
				Iterator <Process> newiterator = arriving.iterator();
				
				while(newiterator.hasNext()) {
					Process curr = newiterator.next();
					combined.add(curr);
				}
		
				
				//because we have not organized them by arrival time yet
				//sort the arraylist of ready processes so that if two have the same arrival time,
				//the process with the lower PID will be placed before the higher PID process
				//BREAKING TIES
				//arraylist arriving represents the unsorted list of ready processes
				boolean finished = true;
				int count = 1;
	
				while(finished == true) {
					
					finished = false;//reset after each iteration of the arraylist arriving
	
					for (int pos = 0; pos < combined.size() - count; pos++) {//for loop that goes through each 
						Process first = combined.get(pos);
						Process second = combined.get(pos+1);
						if (first.arrivalTime > second.arrivalTime) {
							//make sure that the process with the earlier arrival time goes before the process with the later arrival time
							combined.set(pos, second); //set earlier arrival time process first
							combined.set(pos+1, first);//set later arrival time process after
							finished = true;
						}
						if (first.arrivalTime == second.arrivalTime) {
							//if the processes have the same arrival time, make sure the process with the
							//earlier/lower pid is before the later/higher pid
							if (first.pid  > second.pid) {
								combined.set(pos, second); //set earlier pid process first
								combined.set(pos+1, first); //set later pid process after
								finished = true;
							}
							
						}
		
					}//end of for loop
					
					count++;//increase count each time until all processes in array are checked
		
				}//end of while loop
				
				readyprcs.addAll(combined);
		 		
		 		if(processrunning == null) {
		 			//System.out.println("no process is running- trying to grab a new one");
		 			//if there is no current process running
		 			//there are still processes ready in the readyqueue
		 			if(!readyprcs.isEmpty()) {
			 			processrunning = readyprcs.remove(); //remove the top and start running
			 			//System.out.println("process running: " + processrunning.pid);
			 	 		processrunning.state = "running";
			 	 		processrunning.cpubursttime = randomOS(processrunning.b, scanfile);
			 	 		//System.out.println("process running cpubursttime: " + processrunning.cpubursttime);
			 	 		if(processrunning.cpubursttime > processrunning.totalCPUtime) {
			 	 			processrunning.cpubursttime = processrunning.totalCPUtime;
			 	 		}
			 	 		processrunning.previouscpubursttime = processrunning.cpubursttime;
		 	 		
		 			}
		 			
	
		 			if(processrunning == null && blockedprcs.isEmpty() && readyprcs.isEmpty()) {
		 				//System.out.println("there is no blockedprcs or readyprcs left- all processes are terminated");
		 				break;
		 			}
		 			
		 		}//end of if there is no process running	
		 		
		 
		 		//calculate the CPU burst times and total CPU times
		 		//processrunning.cpubursttime = randomOS(processrunning.b, scanfile);
		 		//System.out.println(processrunning.cpubursttime);
		 		
		 		if(processrunning != null) {
			 
			 		processrunning.cpubursttime -= 1;
			 		//System.out.println("process cpubursttime " + processrunning.cpubursttime + " (after -1)");
			 		
			 		//System.out.println(processrunning.totalCPUtime);
			 		processrunning.totalCPUtime -= 1;
			 		//System.out.println("process total cpu time " + processrunning.totalCPUtime + " (after -1)");
		 		}
		 		
		 		for(int d = 0; d < blockedprcs.size(); d ++) {
		 			blockedprcs.get(d).ioBurst -= 1;
		 			blockedprcs.get(d).blockedTime += 1;
		 			//during each cycle, decrease by 1 the processes in the blocked process array list
		 		}
		 		
		 		if(!blockedprcs.isEmpty()) {
		 			iousage += 1;
		 		}
		 		
		 		Iterator <Process> itr = readyprcs.iterator();
		 		while(itr.hasNext()) {
		 			Process readyproc = itr.next();
		 			readyproc.waitTime += 1;
		 		}
		 		
		 	
		 		cycle ++;
		 		
		 	
		 			
		 	}//end of while loop
		
		 	scanfile.close();
		 		
		 	
		 	//PRINT SUMMARIES
		 	int totalFinishTime = cycle;
			int totaltt = 0;

			int totalwt = 0;
			int totalcpu = 0;
			
			
			//print for EACH process
System.out.println("\n");
			for(int s = 0; s < allPrcs.size(); s ++) {
				Process current = allPrcs.get(s);
				System.out.println("Process " + s);
				System.out.println("(" + current.arrivalTime + "," + current.b + "," + current.OGCPUtime + "," + current.m + ")");
				System.out.println("Finishing time: " + current.finishTime);
				int tt = current.finishTime - current.arrivalTime;
				System.out.println("Turnaround time: " + tt);
				totaltt += tt;
				System.out.println("I/O time: " + current.blockedTime);
				//totalio += current.blockedTime;
				System.out.println("Waiting time: " + current.waitTime);
				totalwt += current.waitTime;
				totalcpu += current.OGCPUtime;
				System.out.println();
			}
					
//				System.out.println("(" + A + "," + B + "," + C + "," + M + ")");
//				System.out.println("Finishing time: ");
//				System.out.println("Turnaround time: ");
//				/*	FOR EACH PROCESS PRINT:
//				 * 1. (A,B,C,M)
//				 * 2. Finishing time
//				 * 3. Turnaround time (Finishing time - A)
//				 * 4. I/O time (time in Blocked state)
//				 * 5. Waiting time (time in Ready state)		
					
			
			//print TOTAL summary
				System.out.println("Finishing time: " + totalFinishTime);
				
				double cpu = (double) totalcpu / (double) totalFinishTime;
				System.out.println("CPU Utilization: " + String.format("%.6f", cpu));
				
				double iou = (double) iousage / (double) totalFinishTime;
				System.out.println("IO Utilization: " + String.format("%.6f", iou));
				
				double throughput = (100 / (double) totalFinishTime) * (double) allPrcs.size();
				System.out.println("Throughput: " + String.format("%.6f", throughput) + " processes per hundred cycles");
				
				double avgtt = (double) totaltt/ (double) allPrcs.size();
				System.out.println("Average turnaround time: " + String.format("%.6f", avgtt));
				
				double avgwt = (double) totalwt/ (double) allPrcs.size();
				System.out.println("Average waiting time: " + String.format("%.6f", avgwt));
					
				System.out.println("\nThe above was calculated using the scheduling method using First Come First Served");

//				/*AFTER ALL PROCESSES
//				 * Finishing time (for when all the processes have finished)
//				 * CPU Utilization = % of time some job is running
//				 * I/O Utilization = % of time some job is blocked
//				 * Throughput = processes completed per hundred time units
//				 * Avg turnaround time
//				 * Avg waiting time
//				 * */	
			
			
			
		 }//end of FCFS method
	
	//to calculate io burst time every time a processs is blocked
		public static int randomOS(int B, Scanner scanner) {
			int integer = scanner.nextInt();
	//		System.out.println("random OS called, random integer found is: " + integer);
	//		System.out.println("B: " + B);
			int newint = 1 + integer % B;
	//		System.out.println("calculated by randomOS cpubursttime: " + newint);
			return newint;
		}

	
}//end of FCFS class