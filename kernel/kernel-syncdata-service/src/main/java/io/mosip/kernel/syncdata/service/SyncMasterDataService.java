package io.mosip.kernel.syncdata.service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import io.mosip.kernel.syncdata.dto.response.MasterDataResponseDto;

/**
 * Masterdata sync handler service
 * 
 * @author Abhishek Kumar
 * @since 1.0.0
 *
 */
public interface SyncMasterDataService {
	/**
	 * Method to get updated masterData
	 * 
	 * @param machineId
	 *            machine id
	 * @param lastUpdated
	 *            lastupdated timestamp if lastupdated timestamp is null fetch all
	 *            the masterdata
	 * @return {@link MasterDataResponseDto}
	 * @throws ExecutionException
	 *             - this method will throw execution exception
	 * @throws InterruptedException
	 *             - this method will throw interrupted exception
	 */
	MasterDataResponseDto syncData(String machineId, LocalDateTime lastUpdated)
			throws InterruptedException, ExecutionException;
}
