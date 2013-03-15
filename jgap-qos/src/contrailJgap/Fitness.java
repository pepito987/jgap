package contrailJgap;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

import cApplication.CApplication;
import cApplicationIface.ICApplication;
import cProviderIface.ICProvider;


public class Fitness extends FitnessFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected double evaluate(IChromosome arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	private static double judge(IChromosome ch){
		ICApplication[] app = (ICApplication[]) ch.getApplicationData();
		for(int i=0; i<ch.size(); i++){
			
			
		}
		return -1;
	}
	/* return >0  wrong
	 * return <0  ok
	 */
	private static double evaluateGene(CApplication app, ContrailGene gene){
		double good =0;
		double bad = 0;
		double temp;
		ICProvider[] prov = (ICProvider[])gene.getApplicationData();
		Integer index = (Integer) gene.getAllele();
		double price = prov[index].getBandwidth()  * prov[index].getRatePrice();
		if(app.getPlace().compareTo(prov[index].getPlace()) != 0)
			//penalizzazione vincolo localita`
			bad = 100;
		
		if((temp = app.getBadget() - price) <=0 )
			good += temp;
		else
			bad += Math.abs(temp);
		if((temp = app.getBandwidth() - prov[index].getBandwidth()) <=0)
			good += temp;
		else
			bad += Math.abs(temp);
		if( bad > 0)
			return bad;
		return good;
		
	}
}