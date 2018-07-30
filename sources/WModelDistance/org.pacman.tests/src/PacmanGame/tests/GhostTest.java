/**
 */
package PacmanGame.tests;

import PacmanGame.Ghost;
import PacmanGame.PacmanGameFactory;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Ghost</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class GhostTest extends MoveableEntityTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(GhostTest.class);
	}

	/**
	 * Constructs a new Ghost test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GhostTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Ghost test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected Ghost getFixture() {
		return (Ghost)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(PacmanGameFactory.eINSTANCE.createGhost());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

} //GhostTest
