package it.cnr.isti.federation.metascheduler.resources.iface;

import it.cnr.isti.federation.metascheduler.resources.MSProviderComputing;
import it.cnr.isti.federation.metascheduler.resources.MSProviderNetwork;
import it.cnr.isti.federation.metascheduler.resources.MSProviderStorage;

import java.util.HashMap;



import org.jgap.IApplicationData;


public interface IMSProvider extends IApplicationData{

	/* disabled
	public void setCost(double cost);
	public void setPlace(String place);	
	public double getCost();
	public String getPlace(); 
	*/
	
	public void setID(int id);
	public int getID();

	//testing
	public void setCharacteristic(HashMap<String, Object> characteristic);
	public HashMap<String, Object> getCharacteristic();
		
	public void setNetwork(MSProviderNetwork network);
	public void setComputing(MSProviderComputing computing);
	public void setStorage(MSProviderStorage storage);
	
	public MSProviderComputing getComputing();
	public MSProviderStorage getStorage();
	public MSProviderNetwork getNetwork();
}
