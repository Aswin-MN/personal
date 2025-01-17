package io.mosip.registration.dao;

import java.util.List;

import io.mosip.registration.dto.mastersync.MasterDataResponseDto;
import io.mosip.registration.entity.SyncControl;
import io.mosip.registration.entity.mastersync.MasterBlacklistedWords;
import io.mosip.registration.entity.mastersync.MasterDocumentCategory;
import io.mosip.registration.entity.mastersync.MasterGender;
import io.mosip.registration.entity.mastersync.MasterLocation;
import io.mosip.registration.entity.mastersync.MasterReasonCategory;
import io.mosip.registration.entity.mastersync.MasterReasonList;

/**
 * The Interface MasterSyncDao.
 *
 * @author Sreekar Chukka
 * @since 1.0.0
 */
public interface MasterSyncDao {

	/**
	 * Gets the master sync status.
	 *
	 * @param synccontrol the synccontrol
	 * @return the master sync status
	 */
	public SyncControl syncJobDetails(String synccontrol);

	/**
	 * inserting master sync data into the database using entity.
	 *
	 * @param masterSyncDto the master sync dto
	 * @return the string
	 */
	public String save(MasterDataResponseDto masterSyncDto);

	/**
	 * Find location by lang code.
	 *
	 * @param hierarchyCode the hierarchy code
	 * @param langCode      the lang code
	 * @return the list
	 */
	List<MasterLocation> findLocationByLangCode(String hierarchyCode, String langCode);

	/**
	 * Find location by parent loc code.
	 *
	 * @param parentLocCode the parent loc code
	 * @return the list
	 */
	List<MasterLocation> findLocationByParentLocCode(String parentLocCode);

	/**
	 * Gets the all reason catogery.
	 *
	 * @return the all reason catogery
	 */
	List<MasterReasonCategory> getAllReasonCatogery();

	/**
	 * Gets the reason list.
	 *
	 * @param reasonCat the reason cat
	 * @return the reason list
	 */
	List<MasterReasonList> getReasonList(String langCode, List<String> reasonCat);
	
	/**
	 * Gets the black listed words.
	 *
	 * @param langCode the lang code
	 * @return the black listed words
	 */
	List<MasterBlacklistedWords> getBlackListedWords(String langCode);
	
	/**
	 * Gets the Document Categories.
	 *
	 * @param langCode the lang code
	 * @return the document categories
	 */
	List<MasterDocumentCategory> getDocumentCategories(String langCode);
	
	/**
	 * Gets the gender dtls.
	 *
	 * @param langCode the lang code
	 * @return the gender dtls
	 */
	List<MasterGender> getGenderDtls(String langCode);

}
