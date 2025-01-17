package io.mosip.authentication.core.spi.indauth.match;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.mosip.authentication.core.dto.indauth.AuthUsageDataBit;
import io.mosip.authentication.core.dto.indauth.IdentityDTO;
import io.mosip.authentication.core.dto.indauth.IdentityInfoDTO;
import io.mosip.authentication.core.dto.indauth.LanguageType;

/**
 * Base interface for the match type.
 *
 * @author Loganathan Sekar
 */
public interface MatchType {

	/**
	 * The Category Enum
	 */
	public enum Category {

		/** Demo category */
		DEMO("demo"), 
		/**  OTP category */
		OTP("otp"), 
 		/** Bio category */
		BIO("bio");

		/** The type. */
		String type;

		/**
		 * Instantiates a Category.
		 *
		 * @param type the type
		 */
		private Category(String type) {
			this.type = type;
		}

		/**
		 * Gets the type.
		 *
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * Get the category for the type.
		 *
		 * @param type the type
		 * @return Optional of category
		 */
		public static Optional<Category> getCategory(String type) {
			return Stream.of(values()).filter(t -> t.getType().equals(type)).findAny();
		}

	}

	/**
	 * Gets the IDMapping
	 *
	 * @return ID Mapping
	 */
	public IdMapping getIdMapping();

	/**
	 * Gets the allowed matching strategy for the MatchingStrategyType value
	 *
	 * @param matchStrategyType 
	 * @return the allowed matching strategy
	 */
	Optional<MatchingStrategy> getAllowedMatchingStrategy(MatchingStrategyType matchStrategyType);

	/**
	 * Get the Identity Info Function
	 *
	 * @return 
	 */
	public Function<IdentityDTO, Map<String,List<IdentityInfoDTO>>> getIdentityInfoFunction();
	
	/**
	 * Get the IdentityInfoDTO list out of the identity block for this MatchType
	 *
	 * @param identity the IdentityDTO
	 * @return the list of IdentityInfoDTO
	 */
	public default List<IdentityInfoDTO> getIdentityInfoList(IdentityDTO identity) {
		return getIdentityInfoFunction().apply(identity)
				.values()
				.stream()
				.filter(Objects::nonNull)
				.flatMap(List::stream)
				.collect(Collectors.toList());
	}

	/**
	 * Get the Language Type
	 *
	 * @return the LanguageType
	 */
	public default LanguageType getLanguageType() {
		return LanguageType.PRIMARY_LANG;
	}

	/**
	 * Gets the used bit
	 *
	 * @return the used bit
	 */
	public AuthUsageDataBit getUsedBit();

	/**
	 * Gets the matched bit
	 *
	 * @return the matched bit
	 */
	public AuthUsageDataBit getMatchedBit();

	/**
	 * Gets the Entity info mapper function
	 *
	 * @return the Entity info mapper function
	 */
	public Function<Map<String, String>, Map<String, String>> getEntityInfoMapper();

	/**
	 * Get the category of this MatchType
	 *
	 * @return the category
	 */
	public Category getCategory();

}
