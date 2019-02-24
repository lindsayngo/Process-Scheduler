import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;


//A separate Process class to handle all properties/values/times of a process
public class Process{

			int arrivalTime; //A
			int b;//B. CPUburstTime means how long the process needs to run for
			int totalCPUtime;//C
			int m;	//M
			int pid;		 		//ProcessID

			int OGCPUtime;
			
			String state;
			int blockedTime;
			int finishTime;
			int turnaroundTime;
			int waitTime;
			
			int cpubursttime;
			int cpubursttimeleft;//represent the total cpu burst time left
			int cpuleft; //represent the total cpu time left
			int ioBurst;//time in prison
			int previouscpubursttime;
			
			//for round robin
			int quantum;
			boolean preempted;
			
			//for hprn
			int hprn;
			int numcyclesinrunningst;
			
		Process(int PID, int A, int B, int C, int M){
			pid = PID;
			arrivalTime = A;
			//CPUburstTime = randomOS(B);
			b = B;
			totalCPUtime = C;
			OGCPUtime = C;
			m = M;
			state = "unstarted";
			cpuleft = C;
			cpubursttimeleft = cpubursttime;
			
			blockedTime = 0;
			waitTime = 0;
			
			quantum = 2;
			preempted = false;
			
			numcyclesinrunningst = 1;
			
		}
		



}
