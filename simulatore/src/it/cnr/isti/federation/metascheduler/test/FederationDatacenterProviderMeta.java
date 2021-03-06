package it.cnr.isti.federation.metascheduler.test;


import it.cnr.isti.federation.metascheduler.test.FederationDatacenterProfileMeta.DatacenterParams;
import it.cnr.isti.federation.resources.FederationDatacenter;

import java.util.List;

import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicySimple;

public class FederationDatacenterProviderMeta 
{

	private static int DC_COUNTER = 0;
	
//	//added by Giuseppe
//	private static DatacenterCharacteristics datacenterCh;
	
	private static FederationDatacenter createFederationDatacenter(FederationDatacenterProfileMeta profile, List<PowerHost> hosts, List<Storage> storages)
	{
		// create the datacenter characteristics
		DatacenterCharacteristicsMS datacenterCh = new DatacenterCharacteristicsMS(profile.get(DatacenterParams.PLACE),
				profile.get(DatacenterParams.ARCHITECTURE),
				profile.get(DatacenterParams.OS),
				profile.get(DatacenterParams.VMM),
				hosts,
				Double.parseDouble(profile.get(DatacenterParams.TIME_ZONE)),
				Double.parseDouble(profile.get(DatacenterParams.COST_PER_SEC)),
				Double.parseDouble(profile.get(DatacenterParams.COST_PER_MEM)),
				Double.parseDouble(profile.get(DatacenterParams.COST_PER_STORAGE)),
				Double.parseDouble(profile.get(DatacenterParams.COST_PER_BW)));

		// creating vm allocation policy class
		VmAllocationPolicy vmAllocationPolicy = null;//new PowerVmAllocationPolicySimple(hosts);
		try
		{
			Class clazz = Class.forName(profile.get(DatacenterParams.VM_ALLOCATION_POLICY));
			vmAllocationPolicy = (VmAllocationPolicy)clazz.getDeclaredConstructor(List.class).newInstance(
					hosts
					);
		}
		catch (Exception e)
		{
			// TODO: log the error
			e.printStackTrace();
		}
		
		// creating the federation datacenter
		FederationDatacenter fc = null;
		try
		{
			fc = new FederationDatacenter("datacenter_"+DC_COUNTER++, datacenterCh, vmAllocationPolicy, storages, 
					Double.parseDouble(profile.get(DatacenterParams.SCHEDULING_INTERNAL)));
		}
		catch (Exception e)
		{
			// TODO: log the error
			e.printStackTrace();
		}
		
		return fc;
		
	}


	public static FederationDatacenter getDefault(List<PowerHost> hosts, List<Storage> storages)
	{
		return createFederationDatacenter(FederationDatacenterProfileMeta.getDefault(), hosts, storages);
	}
	
	public static FederationDatacenter get(FederationDatacenterProfileMeta profile, List<PowerHost> hosts, List<Storage> storages)
	{
		return createFederationDatacenter(profile, hosts, storages);
	}
}
