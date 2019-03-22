package PacmanGame.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import PacmanGame.Food;
import PacmanGame.Ghost;
import PacmanGame.GridNode;
import PacmanGame.Pacman;
import PacmanGame.Scoreboard;
import PacmanGame.impl.GameImpl;
import emodeldistance.DistanceUtil;

/**
 * Class automatically generated from analyzing PacmanGame metamodel and the transformation rules.
 * @author Eugene Syriani
 *
 */
class PacmanGameDistanceUtil extends DistanceUtil {
	
	PacmanGameDistanceUtil() {
		movableTypes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("Pacman", "Ghost")));
		positionTypes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("GridNode")));
		modifiableTypes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("Scoreboard")));
		otherTypes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("Food")));
	}
	
	@Override
	public Object getId(EObject object) {
		/**
		 * If object has no attribute with setID(true),
		 * then you have to make up one unique on your own.
		 * Otherwise, just call super.
		 */
		if (object instanceof Pacman)
			return ((Pacman)object).getId();
		else if (object instanceof Ghost)
			return ((Ghost)object).getId();
		else if (object instanceof GridNode)
			return ((GridNode)object).getId();
		else if (object instanceof Scoreboard)
			// there is only one instance of them and has no explicit ID
			return true;
		else if (object instanceof Food) {
			// The identity of food is its position, since I can only have one per position
			return "f" + getId(((Food)object).getOn());
		}
		else
			return null;
	}

	@Override
	public EObject getPosition(EObject movable) {
		if (movable instanceof Pacman)
			return ((Pacman)movable).getOn();
		else if (movable instanceof Ghost)
			return ((Ghost)movable).getOn();
		else
			return null;
	}

	@Override
	public List<EObject> getPositionNeighbors(EObject position) {
		ArrayList<EObject> neighbors = new ArrayList<EObject>(); 
		if (position instanceof GridNode) {
			neighbors.add(((GridNode)position).getTop());
			neighbors.add(((GridNode)position).getBottom());
			neighbors.add(((GridNode)position).getLeft());
			neighbors.add(((GridNode)position).getRight());
		}
		return neighbors;
	}
	
	@Override
	public List<EObject> getMovableObjects(EObject root) {
		root = getRoot(root);
		return ((GameImpl)root).getEntites().stream().filter(isInTypes(getMovableTypes())).collect(Collectors.toList());
	}
	
	@Override
	public List<EObject> getPositionObjects(EObject root) {
		root = getRoot(root);
		return ((GameImpl)root).getGridnodes().stream().filter(isInTypes(getPositionTypes())).collect(Collectors.toList());
	}
	
	@Override
	public List<EObject> getModifiableObjects(EObject root) {
		root = getRoot(root);
		return Arrays.asList(((GameImpl)root).getScoreboard());
	}
	
	@Override
	public List<EObject> getOtherObjects(EObject root) {
		root = getRoot(root);
		return ((GameImpl)root).getEntites().stream().filter(isInTypes(getOtherTypes())).collect(Collectors.toList());
	}
	
	@Override
	public List<Object> getModifiableAttributes(EObject object) {
		if (object instanceof Scoreboard)
			return Arrays.asList(((Scoreboard)object).getScore());
		else
			return Collections.emptyList();
	}
	
	@Override
	public EObject getRoot(EObject o) {
		if (!(o instanceof GameImpl)) {
			o = EcoreUtil.getRootContainer(o);
		}
		return o;
	}
}
