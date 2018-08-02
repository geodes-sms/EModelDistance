/**
 */
package PacmanGame.tests;

import PacmanGame.Pacman;
import PacmanGame.PacmanGameFactory;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Pacman</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class PacmanTest extends MoveableEntityTest {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(PacmanTest.class);
	}

	/**
	 * Constructs a new Pacman test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PacmanTest(String name) {
		super(name);
	}

	/**
	 * Returns the fixture for this Pacman test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected Pacman getFixture() {
		return (Pacman)fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(PacmanGameFactory.eINSTANCE.createPacman());
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

} //PacmanTest
