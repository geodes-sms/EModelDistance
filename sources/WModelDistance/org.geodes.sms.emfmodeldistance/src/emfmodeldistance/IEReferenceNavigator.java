package emfmodeldistance;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * This interface provides functionality to navigate through the references of an EObject in an Ecore model.
 * @author Eugene Syriani
 *
 */
public interface IEReferenceNavigator {

	/**
	 * Accesses all objects that o references.
	 * @param o the source object
	 * @return the list of EObjects referred to by o
	 */
	public List<EObject> getNeighbors(EObject o);

}
