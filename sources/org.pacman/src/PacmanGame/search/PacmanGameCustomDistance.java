package PacmanGame.search;

import java.io.File;
import java.util.Iterator;
import java.util.ArrayList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import PacmanGame.GridNode;
import PacmanGame.PacmanGamePackage;
import PacmanGame.PositionableEntity;
import PacmanGame.impl.FoodImpl;
import PacmanGame.impl.GameImpl;
import PacmanGame.impl.MoveableEntityImpl;
import emodeldistance.DistanceCalculator;

public class PacmanGameCustomDistance extends DistanceCalculator {
	
	private static PacmanGameCustomDistance INSTANCE;
	
	public static double calculateFitness(EObject model) {
		return INSTANCE.calculateDistance(model);
	}
	
	public static void initWith(File modelFile) {
		INSTANCE = new PacmanGameCustomDistance(modelFile);
	}
	
	public PacmanGameCustomDistance(File targetModel) {
		super(targetModel);
	}
	
	@Override
	protected EPackage getEPackageInstance() {
		return PacmanGamePackage.eINSTANCE;
	}
	
	@Override
	protected double calculate(EObject model) throws Exception {
		double diff_food = 0.0, diff_score = 0.0, diff_moveable = 0.0, distance_moveable = 0.0;
		GameImpl src = (GameImpl)model;
		GameImpl tar = (GameImpl)this.targetModel;
		long src_food = src.getEntites().stream().filter(e -> e instanceof FoodImpl).count(),
				tar_food = tar.getEntites().stream().filter(e -> e instanceof FoodImpl).count();
		long max_food = Math.max(src_food, tar_food);
		if (max_food > 0) {
			diff_food = Math.abs(src_food - tar_food) / max_food;
		}
		/*diff_score = Math.abs(src.getScoreboard().getScore() - tar.getScoreboard().getScore());
		if (diff_score > 0)
		{
			diff_score = 1 / diff_score;	// to get a number in [0,1]
		}*/
		// Diff and distance pacmans
		int entityCount = 0;
		int gridCount = tar.getGridnodes().size();
		for (Iterator<PositionableEntity> iter = tar.getEntites().stream().filter(e -> e instanceof MoveableEntityImpl).iterator(); iter.hasNext(); ) {
			PositionableEntity tar_entity = iter.next();
			String id = tar_entity.getId();
			ArrayList<PositionableEntity> entities = new ArrayList<PositionableEntity>();
			for (PositionableEntity e : src.getEntites())
			{
				if ((e instanceof MoveableEntityImpl) && e.getId().equals(id))
				{
					entities.add(e);
				}
			}
			if (entities.size() == 1) {
				distance_moveable += 1.0 * this.manhattanDistance(entities.get(0), tar_entity) / gridCount;
				//System.out.println("Id: " + id + " ; Dist: " + distance_moveable + " ; Grid: " + gridCount);
			}
			else {
				diff_moveable++;	// this pacman or ghost is missing
			}
			entityCount++;
		}
		if (entityCount > 0) {
			diff_moveable /= entityCount;
		}
		//distance = (diff_food + diff_score + diff_moveable + distance_moveable) / 4;
		return (diff_food + diff_moveable + distance_moveable) / 3;
		//System.out.println("Food: " + diff_food + " ; Entities: " + diff_moveable + " ; Dist: " + distance_moveable + " ; Distance: " + distance);
	}

	public int manhattanDistance(PositionableEntity source, GridNode target) throws Exception
	{
		int src_x = getX(source.getOn()),
			src_y = getY(source.getOn()),
			tar_x = getX(target),
			tar_y = getY(target);
		return Math.abs(tar_x-src_x) + Math.abs(tar_y-src_y);
	}
	public int manhattanDistance(PositionableEntity source, PositionableEntity target) throws Exception
	{
		int src_x = getX(source.getOn()),
			src_y = getY(source.getOn()),
			tar_x = getX(target.getOn()),
			tar_y = getY(target.getOn());
		//int d = Math.abs(tar_x-src_x) + Math.abs(tar_y-src_y);
		//System.out.println(source.getId() + " on " + source.getOn().getId() + " should be on " + target.getOn().getId() + " distance=" + d);
		return Math.abs(tar_x-src_x) + Math.abs(tar_y-src_y);
	}
	
	public int getX(GridNode node) throws Exception
	{
		return _getHalfString(node.getId(), true);
	}
	
	public int getY(GridNode node) throws Exception
	{
		return _getHalfString(node.getId(), false);
	}
	
	private int _getHalfString(String s, boolean first) throws Exception
	{
		int size = s.length();
		if (size % 2 != 0)
			throw new Exception("Incorrect ID format for grid node " + s);
		String x;
		if (first)
			x = s.substring(0, size/2);
		else
			x = s.substring(size/2, size);
		try
		{
			return Integer.parseInt(x);
		}
		catch (Exception e)
		{
			throw new Exception("ID is not a number for grid node " + s); 
		}
	}
}

