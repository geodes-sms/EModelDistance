package emfmodeldistance;

import java.io.File;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This class calculates the value distance between two models M1 and M2.
 * The value distance is the difference in attribute values between M1 and M2.
 * We assume that any attribute type can be encoded as a double.
 * Then the value distance of attribute x becomes |M2.x - M1.x| / M2.x.
 * In this case, M2 acts as the expected target.
 * @author Eugene Syriani
 *
 */
public abstract class ValueDistance extends DistanceCalculator {
	
	protected DistanceUtil util;
	
	/**
	 * Initializes the value distance calculator.
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
		int num_attributes = 0;
		
		// Get all elements reachable from the root object of M1
		for (EObject src : EcoreUtil.getAllContents(model, false)) {
			// Check if object in M1 is also in M2
			EObject tar = util.getObjectInModel(src, this.targetModel)
			if (tar != null) {
				// Get all attributes
				//TODO: should I do this._getAllAttributes(src) which returns a list of values (hopefully in the same order as for tar. This method converts to double. eAttrToDouble should be overriden in concrete util class.
				for (Iterator iter = src.eClass().getEAllAttributes().iterator(); iter.hasNext();) {
					// Compute attribute difference
					double src_value = util.eAttrToDouble(src.eGet((EAttribute)iter.next())),
						tar_value = util.eAttrToDouble(tar.eGet((EAttribute)iter.next()));
					distance_value = Math.abs(src_value - tar_value) / tar_value;
				}
			}
		// distance_value = Sum(|M1.x-M2.x|/M2.x)/num_attributes
		distance_value /= num_attributes
		return distance_value;
	}
}