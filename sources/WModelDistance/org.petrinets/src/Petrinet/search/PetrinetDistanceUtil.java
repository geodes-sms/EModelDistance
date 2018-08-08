package Petrinet.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import Petrinet.Place;
import Petrinet.Transition;
import Petrinet.impl.PetriNetImpl;
import emfmodeldistance.DistanceUtil;

/**
 * Class automatically generated from analyzing Petrinet metamodel and the transformation rules.
 * @author Eugene Syriani
 *
 */
class PetrinetDistanceUtil extends DistanceUtil {
	
	PetrinetDistanceUtil() {
		movableTypes = Collections.emptySet();
		positionTypes = Collections.emptySet();
		modifiableTypes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("Place")));
	}
	
	@Override
	public Object getId(EObject object) {
		/**
		 * If object has no attribute with setID(true),
		 * then you have to make up one unique on your own.
		 * Otherwise, just call super.
		 */
		if (object instanceof Place)
			return ((Place)object).getName();
		else if (object instanceof Transition)
			return ((Transition)object).getName();
		else
			return null;
	}

	@Override
	public EObject getPosition(EObject movable) {
		return null;
	}

	@Override
	public List<EObject> getPositionNeighbors(EObject position) {
		return Collections.emptyList();
	}
	
	@Override
	public List<EObject> getMovableObjects(EObject root) {
		return Collections.emptyList();
	}
	
	@Override
	public List<EObject> getPositionObjects(EObject root) {
		return Collections.emptyList();
	}
	
	@Override
	public List<EObject> getModifiableObjects(EObject root) {
		root = getRoot(root);
		return ((PetriNetImpl)root).getPlaces().stream().collect(Collectors.toList());
	}
	
	@Override
	public List<EObject> getOtherObjects(EObject root) {
		root = getRoot(root);
		return ((PetriNetImpl)root).getTransitions().stream().filter(
				e -> getOtherTypes().contains(e.eClass().getName())).collect(Collectors.toList());
	}
	
	@Override
	public List<Object> getModifiableAttributes(EObject object) {
		if (object instanceof Place)
			return Arrays.asList(((Place)object).getTokens());
		else
			return Collections.emptyList();
	}
	
	@Override
	public EObject getRoot(EObject o) {
		if (!(o instanceof PetriNetImpl)) {
			o = EcoreUtil.getRootContainer(o);
		}
		return o;
	}
}
