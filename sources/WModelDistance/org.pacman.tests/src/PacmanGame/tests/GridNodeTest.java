/**
 */
package PacmanGame.tests;

import PacmanGame.GridNode;
import PacmanGame.PacmanGameFactory;

import junit.framework.TestCase;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Grid Node</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class GridNodeTest extends TestCase {

	/**
	 * The fixture for this Grid Node test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GridNode fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(GridNodeTest.class);
	}

	/**
	 * Constructs a new Grid Node test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GridNodeTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Grid Node test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(GridNode fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Grid Node test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GridNode getFixture() {
		return fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(PacmanGameFactory.eINSTANCE.createGridNode());
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

} //GridNodeTest
