package Petrinet.search;

import java.io.File;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import Petrinet.PetrinetPackage;
import emfmodeldistance.ElementDistance;

/**
 * This class is automatically generated for the Petrinet metamodel.
 * @author Eugene Syriani
 *
 */
public class PetrinetElementDistance extends ElementDistance {	
	/**
	 * Initializes the element distance calculator.
	 * @param targetModel the target model to compare with
	 */
	public PetrinetElementDistance(File targetModel) {
		super(targetModel);
		// Generated from metamodel
		util = PetrinetDistanceUtilFactory.getInstance();
	}
	
	@Override
	protected EPackage getEPackageInstance() {
		// Generated from metamodel
		return PetrinetPackage.eINSTANCE;
	}
	
	private static PetrinetElementDistance INSTANCE;
	
	public static double calculateFitness(EObject model) {
		return INSTANCE.calculateDistance(model);
	}
	
	public static void initWith(File modelFile) {
		INSTANCE = new PetrinetElementDistance(modelFile);
	}
}
