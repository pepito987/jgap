package it.cnr.isti.federation.metascheduler;

public class Constant {
	public static final String BW = "bw_amount";
	public static final String STORE = "storage_mb";
	public static final String RAM = "ram_amount_mb";
	public static final String MIPS = "mips";
	
	public static final String BUDGET = "budget";
	public static final String VM_INSTANCES= "vm_instances";
	
	public static final String COST_SEC = "cost_per_sec";
	public static final String COST_MEM = "cost_per_mem";
	public static final String COST_STORAGE = "cost_per_storage";
	public static final String COST_BW = "cost_per_bw";

	public static final String PLACE = "place";
	public static final String ID = "ID";
	
	public static final String[] aggregationParam = {"ram_amount_mb", "bw_amount", "storage_mb"};
	
	public static final String DATACENTER_RAM_INC = "ram_increment";
	public static final String DATACENTER_SIZE = "datacenter_size";
	public static final String DATACENTER_NUMEBER = "datacenter_number";
	public static final String DATACENTER_PLACES = "datacenter_places";
	public static final String APPLICATION_PLACES = "application_places";
	public static final String APPLICATION_BUDGET ="application_budget";
	public static final String APPLICATION_CLOUDLETS = "application_cloudlets";
	
	
	
	//MAPPING JGAP
	public static final int POP_SIZE = 250;
//	public static final int APP_SIZE = 2;
	//public static final int PROV_SIZE = 6;
	//public static final int TARGET = 5;
	public static final int EVOLUTION_SIZE=1;
	
	public static final char ASCENDENT_TYPE = 'A';
	public static final char DESCENDENT_TYPE = 'D';
	public static final char EQUAL_TYPE = 'E';
	
	public static final char GLOBAL_CONSTRAIN ='G';
	public static final char LOCAL_CONSTRAIN = 'L';
	
}
