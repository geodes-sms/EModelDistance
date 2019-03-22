package emodeldistance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

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
	protected Set<String> modifiableTypes;
	protected Set<String> otherTypes;
	protected Set<String> allTypes;
	
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
	 * Returns the modifiable object types of your metamodel.
	 * An object is modifiable if, when analyzing the rules,
	 * one of its attributes changes value.
	 * @return the set of class names for modifiable types
	 */
	public Set<String> getModifiableTypes() {
		return modifiableTypes;
	}
	
	/**
	 * Returns all other object types of your metamodel that is not
	 * a position, movable, or modifiable.
	 * @return the set of class names for other types
	 */
	public Set<String> getOtherTypes() {
		return otherTypes;
	}
	
	/**
	 * Returns all object types of your metamodel.
	 * @return the set of class names for other types
	 */
	public Set<String> getAllTypes() {
		if (allTypes == null) {
			allTypes = new HashSet<>();
			allTypes.addAll(movableTypes);
			allTypes.addAll(positionTypes);
			allTypes.addAll(modifiableTypes);
			allTypes.addAll(otherTypes);
			allTypes = Collections.unmodifiableSet(allTypes);
		}
		return allTypes;
	}

	/**
	 * Returns the unique identifier that characterizes a position or movable object.
	 * By default, it returns the object's ID as a String.
	 * @param object a position or movable object
	 * @return the identifier value, null if not found
	 * @see org.eclipse.emf.ecore.util.EcoreUtil#getID()
	 */
	public Object getId(EObject object) {
		return EcoreUtil.getID(object);
	}
	
	/**
	 * Returns the object in model that has the same ID as o.
	 * @param o the object to look for
	 * @param model an object in the model, ideally the root, but not necessarily
	 * @return the object corresponding to o in model
	 * @see #getId
	 */
	public EObject getObjectInModel(EObject o, EObject model)
	{
		Object oid = getId(o);
		if (oid == null)
			return null;
		String oclass = o.eClass().getName();
		model = getRoot(model);
		List<EObject> list;
		if (movableTypes.contains(oclass))
			list = getMovableObjects(model);
		else if (positionTypes.contains(oclass))
			list = getPositionObjects(model);
		else if (modifiableTypes.contains(oclass))
			list = getModifiableObjects(model);
		else if (otherTypes.contains(oclass))
			list = getOtherObjects(model);
		else return null;	// Shouldn't be reached
		for (EObject identical_to_o : list) {
			if (identical_to_o != null && oid.equals(getId(identical_to_o))) {
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
	 * Converts an object to double. If it is not parsable as a number, then it returns 0.0.
	 * @param value an object in number form
	 * @return the double version of value
	 */
	public double toDouble(Object value) {
		//TODO: cover all Ecore data types
		if (value instanceof Number) {
			return ((Number)value).doubleValue();
		}
		if (value instanceof String) {
			Scanner sc = new Scanner((String)value);
			if (sc.hasNextDouble()) {
				sc.close();
				return Double.parseDouble((String)value);
			}
		}
		return 0.0;
	}
	
	/**
	 * Returns all objects in the model.
	 * @param root is the root object of the model from which we can access all the objects in the model
	 * @return set of all objects
	 */
	public Set<EObject> getAllObjects(EObject root) {
		HashSet<EObject> collection = new HashSet<EObject>();
		List<EObject> list = getMovableObjects(root);
		if (list != null)
			collection.addAll(list);
		list = getModifiableObjects(root);
		if (list != null)
			collection.addAll(list);
		list = getPositionObjects(root);
		if (list != null)
			collection.addAll(list);
		list = getOtherObjects(root);
		if (list != null)
			collection.addAll(list);
		return collection;
	}
	
	/**
	 * Returns the id of all objects in the model.
	 * @param root is the root object of the model from which we can access all the objects in the model
	 * @return list of all ids
	 */
	public Set<Object> getAllObjectIds(EObject root) {
		return getAllObjects(root).stream().map(
				e -> getId(e)).collect(Collectors.toSet());
	}
	
	/**
	 * Predicate that tests if each EObject is of a type in typeSet.
	 * @param typeSet set of desired types
	 * @return the predicate 
	 */
	public Predicate<EObject> isInTypes(Set<String> typeSet) {
		return e -> typeSet.contains(e.eClass().getName());
	}

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
	 * Returns all modifiable objects in the model.
	 * @param root is the root object of the model from which we can access all the objects in the model
	 * @return list of all modifiable objects
	 */
	public abstract List<EObject> getModifiableObjects(EObject root);
	
	/**
	 * Returns all other objects in the model.
	 * @param root is the root object of the model from which we can access all the objects in the model
	 * @return list of all other objects
	 */
	public abstract List<EObject> getOtherObjects(EObject root);
	
	/**
	 * Returns all attribute values subject to modification for a given object.
	 * @param object the container of the attributes
	 * @return ordered list of attributes of all modifiable attributes
	 */
	public abstract List<Object> getModifiableAttributes(EObject object);
	
	/**
	 * Returns the root object in the model where o is defined.
	 * The root is the object that is not contained in any other object
	 * and contains o transitively.
	 * @param o an object of the model
	 * @return the root of o, which may be o itself.
	 */
	public abstract EObject getRoot(EObject o);
}