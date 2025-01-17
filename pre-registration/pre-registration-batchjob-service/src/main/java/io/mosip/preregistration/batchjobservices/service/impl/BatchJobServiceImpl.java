package io.mosip.preregistration.batchjobservices.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.preregistration.batchjobservices.code.ErrorCode;
import io.mosip.preregistration.batchjobservices.code.ErrorMessage;
import io.mosip.preregistration.batchjobservices.entity.ApplicantDemographic;
import io.mosip.preregistration.batchjobservices.entity.ProcessedPreRegEntity;
import io.mosip.preregistration.batchjobservices.exceptions.NoPreIdAvailableException;
import io.mosip.preregistration.batchjobservices.repository.PreRegistrationDemographicRepository;
import io.mosip.preregistration.batchjobservices.repository.PreRegistrationProcessedPreIdRepository;
import io.mosip.preregistration.batchjobservices.service.BatchJobService;
import io.mosip.preregistration.core.common.dto.MainResponseDTO;
import io.mosip.preregistration.core.exception.TableNotAccessibleException;

/**
 * @author Kishan Rathore
 * @since 1.0.0
 *
 */

@Component
public class BatchJobServiceImpl implements BatchJobService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchJobServiceImpl.class);

	/** The Constant LOGDISPLAY. */
	private static final String LOGDISPLAY = "{} - {}";

	/** The Constant Status comments. */
	private static final String STATUS_COMMENTS = "Processed by registration processor";

	/** The Constant Status comments. */
	private static final String NEW_STATUS_COMMENTS = "Application consumed";
	/**
	 * The PreRegistration Processed PreId Repository.
	 */
	@Autowired
	@Qualifier("preRegProcessedRepository")
	private PreRegistrationProcessedPreIdRepository preRegListRepo;

	/**
	 * The PreRegistration applicant Demographic repository.
	 */
	@Autowired
	@Qualifier("preRegistrationDemographicRepository")
	private PreRegistrationDemographicRepository preRegistrationDemographicRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.preregistration.batchjobservices.service.BatchJobService#
	 * demographicConsumedStatus()
	 */
	@Override
	public MainResponseDTO<String> demographicConsumedStatus() {

		MainResponseDTO<String> response = new MainResponseDTO<>();

		List<ProcessedPreRegEntity> preRegList = new ArrayList<>();

		preRegList = preRegListRepo.findBystatusComments(STATUS_COMMENTS);

		if (!preRegList.isEmpty()) {
			preRegList.forEach(iterate -> {
				String status = iterate.getStatusCode();
				String preRegId = iterate.getPreRegistrationId();

				try {
					ApplicantDemographic demographicEntity = preRegistrationDemographicRepository
							.findBypreRegistrationId(preRegId);
					if (demographicEntity != null) {

						demographicEntity.setStatusCode(status);

						preRegistrationDemographicRepository.save(demographicEntity);

						iterate.setStatusComments(NEW_STATUS_COMMENTS);

						LOGGER.info(LOGDISPLAY, "Update the status successfully into Applicant demographic table");

					} else {
						throw new NoPreIdAvailableException(ErrorCode.PRG_PAM_BAT_001.toString(),
								ErrorMessage.NO_PRE_REGISTRATION_ID_FOUND_TO_UPDATE_CONSUMED_STATUS.toString());
					}

				} catch (DataAccessLayerException e) {
					throw new TableNotAccessibleException(ErrorCode.PRG_PAM_BAT_004.toString(),
							ErrorMessage.DEMOGRAPHIC_TABLE_NOT_ACCESSIBLE.toString(), e.getCause());
				}
			});

		} else {

			LOGGER.info("There are currently no Pre-Registration-Ids to update status to consumed");
			throw new NoPreIdAvailableException(ErrorCode.PRG_PAM_BAT_001.name(),
					ErrorMessage.NO_PRE_REGISTRATION_ID_FOUND_TO_UPDATE_CONSUMED_STATUS.name());
		}
		response.setResTime(getCurrentResponseTime());
		response.setStatus(true);
		response.setResponse("Demographic status to consumed updated successfully");
		return response;

	}

	public String getCurrentResponseTime() {
		return DateUtils.formatDate(new Date(System.currentTimeMillis()), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	}

}
