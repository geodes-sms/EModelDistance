package PacmanGame.search;

import java.io.File;
import java.util.Stack;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import PacmanGame.PacmanGamePackage;

import emfmodeldistance.DistanceCalculator;

public class EMFCompareDistanceCalculator extends DistanceCalculator {
	
	private EMFCompare ecomp;
	private double matches;
	public double getMatches() {
		return matches;
	}

	public double getDifferences() {
		return differences;
	}

	public double getDistance() {
		return distance;
	}

	private double differences;
	private double distance;
	
	public EMFCompareDistanceCalculator(File targetModel) {
		super(targetModel);
		try {
		this.ecomp = EMFCompare.builder().build();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected EPackage getEPackageInstance() {
		return PacmanGamePackage.eINSTANCE;
	}
	
	@Override
	
	public  double calculateDistance(EObject model) {

		try {
			// re-load resource to avoid multi-threading issues
			//reloadResource(first);
			synchronized (this.targetModel) {
				synchronized (model) {
					IComparisonScope scope = new DefaultComparisonScope(this.targetModel, model, null);
					Comparison comp = ecomp.compare(scope);
					matches = 0;
					differences = 0;
					// Really simple, let's see how good it is
					// Decrease severity the lower you go
					for (Match m : comp.getMatches()) {
						++matches;
						double curWeight = 1.0;
						Stack<Stack<Match>> curMatches = new Stack();
						Stack<Match> curmStack = new Stack<Match>();
						curmStack.addAll(m.getSubmatches());
						curMatches.add(curmStack);
						while (!curMatches.isEmpty()) {
							Stack<Match> curStack = curMatches.peek();
							if (curStack.isEmpty()) {
								curMatches.pop();
								curWeight *= 2;
							} else {
								Match curMatch = curStack.pop();
								if (curMatch.getLeft() != null && curMatch.getRight() != null) {
									matches += curWeight;
								} else {
									differences += curWeight;
								}
			//					for (Diff diff : curMatch.getDifferences().size()) {
			//						// DifferenceKind kind = diff.getKind();
			//						differences += curWeight; 
			//					}
								differences += curWeight * curMatch.getDifferences().size();
								Stack<Match> subStack = new Stack<Match>();
								subStack.addAll(curMatch.getSubmatches());
								curMatches.push(subStack);
								curWeight *= 0.5;
							}
						}
					}
					distance = ((double) differences) / (matches + differences);
					distance = Math.pow(distance, 2); // ... to decrease smaller parts??
					finished = true;
				}
			}
			
		} catch (Exception e) {
			distance = 1.0;
			System.err.println("Error: "+e.getMessage());
			e.printStackTrace();
		}
		return distance;
	}
	
	public String getReportLine() {
		return "Distance between '" + firstResourceName + "' and '" + secondResourceName + "' is " + distance + " ("
				+ matches + "/" + differences + ")\n";
	}
}