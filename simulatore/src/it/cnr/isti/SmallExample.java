package it.cnr.isti;

import it.cnr.isti.federation.Allocation;
import it.cnr.isti.federation.Federation;
import it.cnr.isti.federation.FederationQueue;
import it.cnr.isti.federation.FederationQueueProfile;
import it.cnr.isti.federation.FederationQueueProvider;
import it.cnr.isti.federation.application.Application;
import it.cnr.isti.federation.resources.FederationDatacenter;
import it.cnr.isti.federation.resources.VmProvider;
import it.cnr.isti.networking.InternetEstimator;
import it.cnr.isti.test.DataSette;
import it.cnr.isti.test.SimpleApplication;
import it.cnr.isti.test.TestResult;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;

public class SmallExample
{
	public static List<FederationDatacenter> dcList;
	public static InternetEstimator net;
	
	
	public static void main (String[] args) throws Exception
	{	
		for (int i=0; i<1; i++)
			start(2,2);		
	}
	
	public static TestResult start(int cloudlet, int datacenter)
	{
		//Log.disable();
		
		int num_user = 1;   // users
        Calendar calendar = Calendar.getInstance();
        boolean trace_flag = true;  // trace events
        
        int hostPerDatacenter = 4 * cloudlet;

        // Initialize the CloudSim library
        CloudSim.init(num_user, calendar, trace_flag);
		        
        // create datacenters
        // dcList = DataSette.generateDatanceters(datacenter, hostPerDatacenter, hostPerDatacenter, 4, 8);
        dcList = DataSette.generateDatanceters2(datacenter, hostPerDatacenter,1,1);
        
		// create the federation
        Federation fed = new Federation("Federation");
        fed.setDatacenters(dcList);
		CloudSim.addEntity(fed);
		VmProvider.userId = fed.getId();
		
		// create the applications
		List<Application> applications = new ArrayList<Application>();
		//applications.add(new SimpleApplication());
		//applications.add(new SimpleApplication());
		//applications.add(new ThreeTierBusinessApplication(1, 1, 1));
		applications.add(DataSette.getApplication(cloudlet));
		
		// create the queue
		FederationQueueProfile queueProfile = FederationQueueProfile.getDefault();
		FederationQueue queue = FederationQueueProvider.getFederationQueue(queueProfile, fed, applications);
		CloudSim.addEntity(queue);
		
		// create the end of the simulation
		CloudSim.terminateSimulation(1000000); // in milliseconds
		
		// print all the entites
		// for (SimEntity entity: CloudSim.getEntityList())
			// System.out.println(entity.getId()+ " - "+entity.getName());
		
		
		// here we start the simulation
		CloudSim.startSimulation();
		
		// print the debts
		// dc1.printDebts();
		
		List<Cloudlet> newList = fed.getReceivedCloudlet();
		printCloudletList(newList);

		// Print the debt of each user to each datacenter
		// datacenter0.printDebts();
		
		Log.printLine("\n========== ALLOCATIONS ==========");
		for (Allocation a: fed.getAllocations())
		{
			// Log.printLine(a.getDuration());
			TestResult res = new TestResult();
			res.simtime = a.getSimDuration();
			res.realtime = a.getRealDuration();
			
			if (a.isCompleted() == false)
				System.out.println("Failed");
			else
				System.out.println(a.getRealDuration());
			
			return res;
		}
		
		return null;
	}
	
	
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
				+ "Data center ID" + indent + "VM ID" + indent + "Time" + indent
				+ "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
				Log.print("SUCCESS");

				Log.printLine(indent + indent + cloudlet.getResourceId()
						+ indent + indent + indent + cloudlet.getVmId()
						+ indent + indent
						+ dft.format(cloudlet.getActualCPUTime()) + indent
						+ indent + dft.format(cloudlet.getExecStartTime())
						+ indent + indent
						+ dft.format(cloudlet.getFinishTime()));
			}
		}
	}
}
