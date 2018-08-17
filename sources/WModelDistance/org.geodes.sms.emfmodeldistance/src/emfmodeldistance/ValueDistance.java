package emfmodeldistance;

import java.io.File;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

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
		
		// Get all modifiable object of M1
		List<EObject> src_modifiableObjects =  util.getModifiableObjects(model);
		for (EObject src : src_modifiableObjects) {
			// Check if object in M1 is also in M2
			EObject tar = util.getObjectInModel(src, this.targetModel);
			if (tar != null) {
				// Get all attributes
				List<Object> src_attr = util.getModifiableAttributes(src),
						tar_attr = util.getModifiableAttributes(tar);
				for (int i = 0; i < src_attr.size(); i++) {
					// Compute attribute difference
					double src_value = util.toDouble(src_attr.get(i)),
						tar_value = util.toDouble(tar_attr.get(i));
					double denominator = (tar_value == 0) ? 1.0 : tar_value;
					distance_value += Math.abs((src_value - tar_value) / denominator);
					num_attributes++;
				}
			}
		}
		// distance_value = Sum(|M1.x-M2.x|/M2.x)/num_attributes
		if (num_attributes == 0)
			return 0.0;
		return distance_value / num_attributes;
	}
}