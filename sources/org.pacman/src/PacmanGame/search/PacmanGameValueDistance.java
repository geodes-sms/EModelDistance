package PacmanGame.search;

import java.io.File;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import PacmanGame.PacmanGamePackage;
import emodeldistance.ValueDistance;

/**
 * This class is automatically generated for the PacmanGame metamodel.
 * @author Eugene Syriani
 *
 */
public class PacmanGameValueDistance extends ValueDistance {	
	/**
	 * Initializes the move distance calculator.
	 * @param targetModel the target model to compare with
	 */
	public PacmanGameValueDistance(File targetModel) {
		super(targetModel);
		// Generated from metamodel
		util = PacmanGameDistanceUtilFactory.getInstance();
	}
	
	@Override
	protected EPackage getEPackageInstance() {
		// Generated from metamodel
		return PacmanGamePackage.eINSTANCE;
	}

	
	private static PacmanGameValueDistance INSTANCE;
	
	public static double calculateFitness(EObject model) {
		return INSTANCE.calculateDistance(model);
	}
	
	public static void initWith(File modelFile) {
		INSTANCE = new PacmanGameValueDistance(modelFile);
	}
}