package Petrinet.search;

/**
 * Gives singleton access to {@link PetrinetDistanceUtil}
 * @author Eugene Syriani
 *
 */
public final class PetrinetDistanceUtilFactory {

	private static PetrinetDistanceUtil INSTANCE;
	
	/**
	 * Returns the utility instance
	 * @return utility object
	 */
	public synchronized static PetrinetDistanceUtil getInstance() {
		if(INSTANCE == null) {
	        synchronized (PetrinetDistanceUtil.class) {
	            if(INSTANCE == null) {
	            	INSTANCE = new PetrinetDistanceUtil();
	            }
	        }
	    }
	    return INSTANCE;
	}
}
