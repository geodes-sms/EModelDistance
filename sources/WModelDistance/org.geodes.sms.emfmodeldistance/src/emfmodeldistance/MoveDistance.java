package emfmodeldistance;

import java.io.File;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

/**
 * This class calculates the move distance between two models.
 * The move distance of a movable object is the length of the shortest path from its position in a model
 * to its position in another model.
 * This class should be automatically generated.
 * @author Eugene Syriani
 *
 */
public abstract class MoveDistance extends DistanceCalculator {
	
	protected DistanceUtil util;
	
	/**
	 * Initializes the move distance calculator.
	 * @param targetModel the target model to compare with
	 */
	public MoveDistance(File targetModel) {
		super(targetModel);
	}
	
	@Override
	abstract protected EPackage getEPackageInstance();
	
	@Override
	protected double calculate(EObject model) throws Exception {
		double distance_moveable = 0.0;
		EcoreShortestPaths src_paths = new EcoreShortestPaths(util.getPositionObjects(model), util);
		src_paths.computeDistances();
		
		List<EObject> src_movableObjects =  util.getMovableObjects(model);
		
		for (EObject src_movable : src_movableObjects) {
			EObject src_position = util.getPosition(src_movable);
			// Check if src_position is in tar
			if (util.getObjectInModel(src_position, this.targetModel) != null) {
				// Check if src_movable is in tar
				EObject tar_movable = util.getObjectInModel(src_movable, this.targetModel);
				if (tar_movable != null) {
					// Get position of tar_movable
					EObject tar_position = util.getPosition(tar_movable);
					tar_position = util.getObjectInModel(tar_position, model);
					// Check if tar_position is in src
					if (tar_position != null) {
						// Compute the path from src_position to tar_position
						distance_moveable += src_paths.getDistance(src_position, tar_position);
						//System.out.println(util.getId(src_movable) + ": " + util.getId(src_position)
						//	+ "->" + util.getId(tar_position) + " = " + distance_moveable);
					}
				}
			}
		}
		return distance_moveable;
	}
}
