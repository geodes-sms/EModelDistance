package emfmodeldistance;

import java.io.File;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
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
		for (EObject src_modif : src_modifiableObjects) {
			// Check if object in M1 is also in M2
			EObject tar_modif = util.getObjectInModel(src_modif, this.targetModel);
			if (tar_modif != null) {
				// Get all attributes
				for (EAttribute attr : src_modif.eClass().getEAllAttributes()) {
					// Compute attribute difference
					double src_value = util.toDouble(src_modif.eGet((EAttribute)attr)),
						tar_value = util.toDouble(tar_modif.eGet((EAttribute)attr));
					distance_value = Math.abs(src_value - tar_value) / tar_value;
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