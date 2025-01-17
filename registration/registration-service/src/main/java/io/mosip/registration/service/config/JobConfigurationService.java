package io.mosip.registration.service.config;

import org.springframework.context.ApplicationContext;

import io.mosip.registration.dto.ResponseDTO;
import io.mosip.registration.entity.SyncJobDef;
import io.mosip.registration.exception.RegBaseCheckedException;

/**
 * service for configuring jobs
 * 
 * @author YASWANTH S
 * @since 1.0.0
 *
 */
public interface JobConfigurationService {

	/**
	 * To get the list of {@link SyncJobDef}
	 * 
	 */
	void initiateJobs();

	/**
	 * Start the jobs with sheduled Time
	 * 
	 * @param applicationContext
	 *            to get the beans
	 * @return responseDTO for start jobs
	 * @throws RegBaseCheckedException
	 */
	ResponseDTO startScheduler(ApplicationContext applicationContext);

	/**
	 * Stop the jobs manually
	 * 
	 * @return responseDTO for stop jobs
	 */
	ResponseDTO stopScheduler();

	/**
	 * To fetch the details of currently running job details
	 * 
	 * @return list of job names currently executing if success
	 */
	ResponseDTO getCurrentRunningJobDetails();

	/**
	 * execute the specified job
	 * 
	 * @param applicationContext
	 *            is a spring framework's application context used here to give
	 *            beans
	 * @param jobId
	 *            the job id
	 * @return responseDTO for execute job
	 */
	ResponseDTO executeJob(ApplicationContext applicationContext, String jobId);
	
	/**
	 * Get Last Completed Sync Jobs from Sync Control
	 * 
	 * @return responseDTO for last completedJobs
	 */
	ResponseDTO getLastCompletedSyncJobs();
	
	/**
	 * Get history of Sync Jobs from Sync Transaction
	 * 
	 * @return responseDTO for last transaction of each syncJob
	 */
	ResponseDTO getSyncJobsTransaction();

}
