package emodeldistance;

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
	
	/**
	 * Initializes the distance calculator
	 * @param targetModel the target model to compare an input model to
	 */
	public DistanceCalculator(File targetModel) {
		try {
			this.targetModel = loadModel(targetModel.getAbsolutePath());
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
	 * @param model the input model
	 * @return a positive number, the lower the value the more similar the models are.
	 * 0 means they are an exact match.
	 */
	public final double calculateDistance(EObject model) {
		try {
			// re-load resource to avoid multi-threading issues
			//reloadResource(first);
			synchronized (this.targetModel) {
				synchronized (model) {
					distance = calculate(model);
				}
			}
			finished = true;
			//System.out.println("Distance: " + distance);
			
		} catch (Exception e) {
			distance = 1.0;
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
		return distance;
	}
	
	/**
	 * Core distance calculation algorithm that is specific to the type of distance.
	 * @param model the input model
	 * @return the value of how distant model is from the target model
	 * @throws Exception if the computation fails
	 */
	protected abstract double calculate(EObject model) throws Exception;
}
