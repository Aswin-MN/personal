package io.mosip.kernel.syncdata.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.kernel.syncdata.entity.Template;

/**
 * 
 * @author Neha
 * @since 1.0.0
 * 
 */
@Repository
public interface TemplateRepository extends BaseRepository<Template, String> {
	/**
	 * Method to find list of Template created , updated or deleted time is greater
	 * than lastUpdated timeStamp.
	 * 
	 * @param lastUpdated
	 *            timeStamp
	 * @return list of {@link Template}
	 */
	@Query("FROM Template WHERE createdDateTime > ?1 OR updatedDateTime > ?1  OR deletedDateTime > ?1")
	List<Template> findAllLatestCreatedUpdateDeleted(LocalDateTime lastUpdated);
}