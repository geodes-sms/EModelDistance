package PacmanGame.search;

import java.io.File;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import PacmanGame.PacmanGamePackage;
import emfmodeldistance.ElementDistance;

/**
 * This class is automatically generated for the PacmanGame metamodel.
 * @author Eugene Syriani
 *
 */
public class PacmanGameElementDistance extends ElementDistance {	
	/**
	 * Initializes the element distance calculator.
	 * @param targetModel the target model to compare with
	 */
	public PacmanGameElementDistance(File targetModel) {
		super(targetModel);
		// Generated from metamodel
		util = PacmanGameDistanceUtilFactory.getInstance();
	}
	
	@Override
	protected EPackage getEPackageInstance() {
		// Generated from metamodel
		return PacmanGamePackage.eINSTANCE;
	}

	
	private static PacmanGameElementDistance INSTANCE;
	
	public static double calculateFitness(EObject model) {
		return INSTANCE.calculateDistance(model);
	}
	
	public static void initWith(File modelFile) {
		INSTANCE = new PacmanGameElementDistance(modelFile);
	}
}
