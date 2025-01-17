package io.mosip.registration.processor.core.packet.dto.demographicinfo;
	
import java.util.List;

import lombok.Data;

/**
 * Instantiates a new individual demographic dedupe.
 */
@Data
public class IndividualDemographicDedupe {
	
	/** The name. */
	private List<JsonValue[]> name;

	/** The date of birth. */
	private JsonValue[] dateOfBirth;

	/** The gender. */
	private JsonValue[] gender;
	
	/** The pheonitic name. */
	private String pheoniticName;




}
