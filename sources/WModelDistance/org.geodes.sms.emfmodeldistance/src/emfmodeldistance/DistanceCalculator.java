package emfmodeldistance;

import java.io.File;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * This is the class that MOMot uses to calculate the distance between two models.
 * @author Eugene Syriani
 *
 */
public abstract class DistanceCalculator {
	
	protected EObject targetModel;
	
	public DistanceCalculator(File targetModel) {
		try {
			this.targetModel = loadModel(targetModel.getAbsolutePath());
			//this.ecomp = EMFCompare.builder().build();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected String firstResourceName;
	public String getFirstResourceName() {
		return firstResourceName;
	}

	public String getSecondResourceName() {
		return secondResourceName;
	}

	protected String secondResourceName;
	
	protected String firstResourcePath;
	public String getFirstResourcePath() {
		return firstResourcePath;
	}
	
	protected String secondResourcePath;

	public String getSecondResourcePath() {
		return secondResourcePath;
	}
	
	protected static DistanceCalculator INSTANCE;
	
	public EObject loadModel(String uri) {
		// register XMI resource factory for .xmi extension
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("xmi", new XMIResourceFactoryImpl());
		// create resource set
		ResourceSet resourceSet = new ResourceSetImpl();
		// register FSA metamodel
		resourceSet.getPackageRegistry().put(getEPackageInstance().getNsURI(), getEPackageInstance());
		// create file URI, always use File.getAbsolutePath()
		URI fileUri = URI.createFileURI(new File(uri).getAbsolutePath());
		// load resource
		Resource resource = resourceSet.getResource(fileUri, true);
		// retrieve first EObject in the resource
		return resource.getContents().get(0);
	}
	
	protected boolean finished;
	public boolean isFinished() {
		return finished;
	}

	protected double distance;
	public double getDistance() {
		return distance;
	}

	public static double calculateFitness(EObject model) {
		return INSTANCE.calculateDistance(model);
	}

	public String getReportLine()
	{
		return "Distance between '" + firstResourceName + "' and '" + secondResourceName + "' is " + distance + "\n";
	}
	
	/**
	 * Returns the package corresponding to the metamodel.
	 * @return the EPackage of the models to compare
	 */
	protected abstract EPackage getEPackageInstance();
	
	/**
	 * Calculates the distance from model to target model.
	 * @param model the original model
	 * @return a value between 0 and 1, the lower the value the more similar the models are.
	 * 0 means they are an exact match.
	 */
	public abstract double calculateDistance(EObject model);
}
