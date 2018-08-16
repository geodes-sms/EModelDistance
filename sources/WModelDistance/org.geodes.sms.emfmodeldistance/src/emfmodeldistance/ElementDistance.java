package emfmodeldistance;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

/**
 * This class calculates the element distance between two models M1 and M2.
 * The element distance is the difference in the presence/absence of elements in M1 and M2.
 * The distance is the ratio, between 0 and 1, of the number of differences 
 * with respect to the total number of objects in M1 and M2.
 * @author Eugene Syriani
 *
 */
public abstract class ElementDistance extends DistanceCalculator {
	
	protected DistanceUtil util;
	
	/**
	 * Initializes the element distance calculator.
	 * @param targetModel the target model to compare with
	 */
	public ElementDistance(File targetModel) {
		super(targetModel);
	}
	
	@Override
	abstract protected EPackage getEPackageInstance();
	
	@Override
	public double calculate(EObject model) throws Exception {
		double distance_value = 0.0;
		
		Set<Object> src_Objects =  util.getAllObjectIds(model);
		Set<Object> tar_Objects = util.getAllObjectIds(this.targetModel);
		double total = src_Objects.size() + tar_Objects.size();
		
		// Clone target set
		Set<Object> clone = new HashSet<Object>(tar_Objects);	// shallow copy
		tar_Objects.removeAll(src_Objects);	// added objects
		src_Objects.removeAll(clone);		// removed objects
		
		distance_value = (tar_Objects.size() + src_Objects.size()) / total;
		return distance_value;
	}
}