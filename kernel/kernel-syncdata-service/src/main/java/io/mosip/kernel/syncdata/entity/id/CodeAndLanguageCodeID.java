package io.mosip.kernel.syncdata.entity.id;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Abhishek Kumar
 * @since 1.0.0
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeAndLanguageCodeID implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7001663925687776491L;

	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "lang_code", nullable = false, length = 3)
	private String langCode;
}
