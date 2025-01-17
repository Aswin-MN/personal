package io.mosip.kernel.idrepo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The Class IdRequestDTO.
 *
 * @author Manoj SP
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IdRequestDTO extends BaseIdRequestResponseDTO {
	
	/** The status. */
	private String status;
	
	/** The registration id. */
	private String registrationId;
	
	/** The request. */
	private RequestDTO request;
}
