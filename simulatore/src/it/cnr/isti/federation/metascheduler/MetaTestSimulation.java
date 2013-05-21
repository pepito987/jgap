package it.cnr.isti.federation.metascheduler;

import it.cnr.isti.federation.application.Application;
import it.cnr.isti.federation.mapping.CObjectiveFitness;
import it.cnr.isti.federation.mapping.ConfigurationFitness;
import it.cnr.isti.federation.mapping.Constant;
import it.cnr.isti.federation.mapping.MakePolicy;
import it.cnr.isti.federation.resources.VmProvider;
import it.cnr.isti.test.ThreeTierBusinessApplication;
import it.cnr.it.giuseppe.SimulationCloudletUtility;
import it.cnr.it.giuseppe.SimulationUtility;
import it.cnr.it.giuseppe.SimulationVmUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import metaschedulerJgap.BestSolution;
import metaschedulerJgap.ConfigurationJGAPQos;
import metaschedulerJgap.JGAPMapping;
import metaschedulerJgap.MSPolicy;
import msApplicationIface.IMSApplication;
import msProvider.MSProvider;
import msProviderIface.IMSProvider;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.power.PowerHost;
import org.jgap.InvalidConfigurationException;




public class MetaTestSimulation {
	
        /** The cloudlet list. */
        private static List<Cloudlet> cloudletList;
        private static List<FederationPowerDatacenter> dcList;
        private static List<IMSProvider> providerList;
        private static DatacenterBroker broker;

        /** The vmlist. */
        private static List<Vm> vmList;
        
        private static void submitEntireApp(Application app, int brokerId){
        	//submit application to cloudsim
            submitApplication(app);
            
            //submit events cloudsim
            CloudSim.terminateSimulation(100);
            for(int i=0; i< dcList.size(); i++){
            	CloudSim.send(brokerId, dcList.get(i).getId(), 3, 10, dcList.get(i) );
            }
            // Starts the simulation
        	
        }
        
        private static void submitSingleAppNode(int datacenterId, Vm vm, double delay, int brokerId){
             CloudSim.send(brokerId, dcList.get(datacenterId).getId(), delay, CloudSimTags.VM_CREATE,  vm);
        }
        
        private static ConfigurationJGAPQos configMetascheduler(IMSApplication app){
        	 ConfigurationFitness fitConf = new ConfigurationFitness();
//             fitConf.addAggregationParams(Constant.RAM);
//             fitConf.addAggregationParams(Constant.BW);
             fitConf.addAggregationParams(Constant.STORE);
             fitConf.setApplication(app);
             fitConf.setProviders(providerList);
             List<MSPolicy> test = new ArrayList<>();
//             test.add(MakePolicy.makeCostPolicy(1));
//             test.add(MakePolicy.makePlacePolicy(2));
             test.add(MakePolicy.makeStoragePolicy(1));
             fitConf.setConstrains(test);
             
             ConfigurationJGAPQos conf = new ConfigurationJGAPQos();
             conf.setFitnessFunction(new CObjectiveFitness(fitConf));
             conf.setPopulationSize(Constant.POP_SIZE);
             conf.setEvolutionSize(Constant.EVOLUTION_SIZE);
             conf.application = app;
             conf.providers = providerList;
             try {
				conf.setConfiguration(app, providerList.size());
			} catch (InvalidConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             return conf;
        }

        /**
         * Creates main() to run this example.
         *
         * @param args the args
         */
        @SuppressWarnings("unused")
        public static void main(String[] args) {

                Log.printLine("Starting CloudSimExample1...");
        		try {
                    // First step: Initialize the CloudSim package. It should be called
                    // before creating any entities.
                    int num_user = 1; // number of cloud users
                    Calendar calendar = Calendar.getInstance();
                    boolean trace_flag = false; // mean trace events

                    // Initialize the CloudSim library
                    CloudSim.init(num_user, calendar, trace_flag);
                    
                    try{
                        
                		initDatacenter();
                		
//                		DatacenterUtility.printFederationDataCenter(dcList);
                		DatacenterUtility.printProviderList(providerList);
                		                		
                		
                        // Third step: Create Broker
                        broker = createBroker();
                        int brokerId = broker.getId();
                
                      //Creazione applicazione 
                        VmProvider.userId = brokerId;
                        Application app = ApplicationUtility.getApplication(brokerId,1);
                        List<Application> appList = new ArrayList<Application>();
                        appList.add(app);
                      //Conversione Applicazione in GAapplication
                        IMSApplication applicationMetaschduler = ApplicationUtility.applicationToMSApplication(app,"italia", "1000.02");
                        System.out.println(ApplicationUtility.toStringMSApplication(applicationMetaschduler));
                        
                        ConfigurationJGAPQos conf = configMetascheduler(applicationMetaschduler);
                        
                        JGAPMapping.init(conf);
                        BestSolution sol = JGAPMapping.startMapping();
                        HashMap<Integer, Integer> mappString = sol.getBest();
                        System.out.println("Fitness: " + sol.getFit());
                        for(int i=0; i<mappString.size(); i++){
                        	System.out.println("Node: " + i + " -> " + " Prov: "+  dcList.get(mappString.get(i)).getId());
                        }
                        
                        Double ris = new Double(sol.getFit());
                        if(ris <1 )
                        	System.out.println("SOLUZIONE NON VALIDA");
                        
                         
//                        CloudSim.send(brokerId, dcList.get(mappString.get(0)).getId(), 3, 10, dcList.get(0) );
//                        submitSingleAppNode(mappString.get(0), app.getAllVms().get(0), 4, brokerId);
//                        CloudSim.send(brokerId, dcList.get(mappString.get(0)).getId(), 5, 10, dcList.get(mappString.get(0)) );
//                        CloudSim.startSimulation();

//                        CloudSim.stopSimulation();
                        
                        /*
                        CloudSim.send(brokerId, dcList.get(0).getId(), 3, 10, dcList.get(0) );
                        //  Running Test entire app 
//                        submitEntireApp(app, brokerId);
                        
                        // Running test single VM
                        submitSingleAppNode(0, app.getAllVms().get(0), 4, brokerId);
                                                
                        CloudSim.send(brokerId, dcList.get(0).getId(), 5, 10, dcList.get(0) );
                        
                        CloudSim.startSimulation();

                        CloudSim.stopSimulation();

                        //Final step: Print results when simulation is over
                        List<Cloudlet> newList = broker.getCloudletReceivedList();
                        SimulationUtility.printCloudletList(newList);
                        
                        Log.printLine("CloudSimExample1 finished!");
                        */
                        } catch (Exception e) {
                        	e.printStackTrace();
                        	Log.printLine("Unwanted errors happen");
                        }
                    
                    
                    
                    
        		}catch (Exception e) {
                        e.printStackTrace();
                        Log.printLine("Unwanted errors happen");
                }
                
        }
        
        // We strongly encourage users to develop their own broker policies, to
        // submit vms and cloudlets according
        // to the specific rules of the simulated scenario
        /**
         * Creates the broker.
         *
         * @return the datacenter broker
         */
        private static DatacenterBroker createBroker() {
                DatacenterBroker broker = null;
                try {
                        broker = new DatacenterBroker("Broker");
                } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                }
                return broker;
        }
        
        private static void initDatacenter(){
        	dcList = new ArrayList<>();
        	providerList = new ArrayList<>();
        	FederationPowerDatacenter temp;
        	int bumberDatacenters = 3;
    		HashMap<String , Object> paramDatacenter = DatacenterUtility.getDatacenterParam();
            for(int i=0; i<bumberDatacenters; i++){
                paramDatacenter.put(Constant.PLACE, "italia");
                temp = DatacenterUtility.getDatacenter(paramDatacenter, 1, 1);
                dcList.add(temp);
                paramDatacenter.put(Constant.ID, temp.getId()+"");
                providerList.add(DatacenterUtility.getProvider(paramDatacenter, 1, 1));
                
            }
        }
        
        private static void submitApplication(Application application){
        	// Vm List
            vmList = new ArrayList<>();
            for(int i=0; i<application.getAllVms().size(); i++){
            	vmList.add(application.getAllVms().get(i));
            }
            broker.submitVmList(vmList);
            // add the cloudlet to the list
            cloudletList = new ArrayList<>();
            for( int i=0; i< application.getAllCloudlets().size(); i++){
            	cloudletList.add(application.getAllCloudlets().get(i));
            }
            
            // submit cloudlet list to the broker
            broker.submitCloudletList(cloudletList);
        	
        }
        
        
        
        
        
        private static void printAppInfo(Application app, int brokerId){
        	System.out.println("INFO:");
            System.out.println("  Vm size: " + app.getAllVms().size());
            System.out.println("     VmUser ID: " + app.getAllVms().get(0).getUserId());
            System.out.println("  Cloudlet size: " + app.getAllCloudlets().size());
            System.out.println("  uID: " + app.getAllVms().get(0).getUserId() + " brokerId: " + brokerId);
            System.out.println("  Cloudlet UID: " + app.getAllCloudlets().get(0).getUserId());
        }
}
