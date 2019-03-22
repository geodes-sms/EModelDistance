package PacmanGame.search;

/**
 * Gives singleton access to {@link PacmanGameDistanceUtil}
 * @author Eugene Syriani
 *
 */
public final class PacmanGameDistanceUtilFactory {

	private static PacmanGameDistanceUtil INSTANCE;
	
	/**
	 * Returns the utility instance
	 * @return utility object
	 */
	public synchronized static PacmanGameDistanceUtil getInstance() {
		if(INSTANCE == null) {
	        synchronized (PacmanGameDistanceUtil.class) {
	            if(INSTANCE == null){
	            	INSTANCE = new PacmanGameDistanceUtil();
	            }
	        }
	    }
	    return INSTANCE;
	}
}
