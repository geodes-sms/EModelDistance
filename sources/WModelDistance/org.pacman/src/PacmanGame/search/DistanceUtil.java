package PacmanGame.search;

import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

/**
 * This class defines the interface of the utility functions to compute the distance
 * between two ECore models. This is a singleton class.
 * @author Eugene Syriani
 */
public abstract class DistanceUtil implements IEReferenceNavigator {
	
	// Singleton object
	protected static DistanceUtil INSTANCE;
	
	protected Set<String> movableTypes;
	protected Set<String> positionTypes;
	
	/**
	 * Returns the movable object types of your metamodel.
	 * An object is movable if, when analyzing the rules,
	 * it has a reference to a position object and the rules modify that reference.
	 * Note that it could also be that a position object references a movable object.
	 * @return the set of class names for movable types
	 */
	public Set<String> getMovableTypes() {
		return movableTypes;
	}

	/**
	 * Returns the position object types of your metamodel.
	 * An object is a position if, when analyzing the rules,
	 * it is what movable objects are always linked to.
	 * Note that it could also be that a position object references a movable object.
	 * @return the set of class names for position types
	 */
	public Set<String> getPositionTypes() {
		return positionTypes;
	}
	
	/**
	 * Returns the object in model that has the same ID as movable (see {@link #getId}).
	 * @param o the object to look for
	 * @param model an object in the model, ideally the root, but not necessarily
	 * @return the object corresponding to o in model
	 */
	public EObject getObjectInModel(EObject o, EObject model)
	{
		model = getRoot(model);
		List<EObject> list;
		if (movableTypes.contains(o.eClass().getName()))
			list = getMovableObjects(model);
		else if (positionTypes.contains(o.eClass().getName()))
			list = getPositionObjects(model);
		else return null;
		for (EObject identical_to_o : list) {
			if (getId(o).equals(getId(identical_to_o))) {
				return identical_to_o;
			}
		}
		return null;
	}
	
	@Override
	public List<EObject> getNeighbors(EObject o) {
		return getPositionNeighbors(o);
	}

	/**
	 * Returns the unique identifier that characterizes a position or movable object.
	 * @param object a position or movable object
	 * @return the identifier value
	 */
	public abstract Object getId(EObject object);

	/**
	 * Returns the position of a movable object.
	 * @param movable a movable object
	 * @return the position object on which movable is placed
	 */
	public abstract EObject getPosition(EObject movable);

	/**
	 * Accesses all neighboring position objects.
	 * @param position the current position object
	 * @return the list of the other position objects position is linked to
	 */
	public abstract List<EObject> getPositionNeighbors(EObject position);
	
	/**
	 * Returns all movable objects in the model.
	 * @param root is the root object of the model from which we can access all the objects in the model
	 * @return list of all movable objects
	 */
	public abstract List<EObject> getMovableObjects(EObject root);
	
	/**
	 * Returns all position objects in the model.
	 * @param root is the root object of the model from which we can access all the objects in the model
	 * @return list of all position objects
	 */
	public abstract List<EObject> getPositionObjects(EObject root);
	
	/**
	 * Returns the root object in the model where o is defined.
	 * The root is the object that is not contained in any other object
	 * and contains o transitively.
	 * @param o an object of the model
	 * @return the root of o, which may be o itself.
	 */
	public abstract EObject getRoot(EObject o);
}