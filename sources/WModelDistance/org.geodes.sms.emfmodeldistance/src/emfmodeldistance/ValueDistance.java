package emfmodeldistance;

import java.io.File;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

/**
 * This class calculates the value distance between two models M1 and M2.
 * The value distance is the difference in attribute values between M1 and M2.
 * We assume that any attribute type can be encoded as a double.
 * Then the value distance of attribute x becomes |M2.x - M1.x|
 * @author Eugene Syriani
 *
 */
public abstract class ValueDistance extends DistanceCalculator {
	
	protected DistanceUtil util;
	
	/**
	 * Initializes the move distance calculator.
	 * @param targetModel the target model to compare with
	 */
	public ValueDistance(File targetModel) {
		super(targetModel);
	}
	
	@Override
	abstract protected EPackage getEPackageInstance();
	
	@Override
	public double calculate(EObject model) throws Exception {
		double distance_value = 0.0;
		
		
		
		return distance_value;
	}
}