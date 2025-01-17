package io.mosip.kernel.idgenerator.tspid.constant;

/**
 * Property constant for TSPID generator.
 * 
 * @author Ritesh Sinha
 * @since 1.0.0
 *
 */
public enum TspIdPropertyConstant {

	ID_BASE("10");

	/**
	 * The property of TSPID generator.
	 */
	private String property;

	/**
	 * Getter for property.
	 * 
	 * @return the property.
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * Constructor for TspIdPropertyConstant.
	 * 
	 * @param property
	 *            the property.
	 */
	TspIdPropertyConstant(String property) {
		this.property = property;
	}

}
