package io.mosip.kernel.masterdata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.constant.DeviceSpecificationErrorCode;
import io.mosip.kernel.masterdata.dto.DeviceSpecificationDto;
import io.mosip.kernel.masterdata.dto.RequestDto;
import io.mosip.kernel.masterdata.dto.postresponse.IdResponseDto;
import io.mosip.kernel.masterdata.entity.Device;
import io.mosip.kernel.masterdata.entity.DeviceSpecification;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.exception.RequestException;
import io.mosip.kernel.masterdata.repository.DeviceRepository;
import io.mosip.kernel.masterdata.repository.DeviceSpecificationRepository;
import io.mosip.kernel.masterdata.service.DeviceSpecificationService;
import io.mosip.kernel.masterdata.utils.EmptyCheckUtils;
import io.mosip.kernel.masterdata.utils.ExceptionUtils;
import io.mosip.kernel.masterdata.utils.MapperUtils;
import io.mosip.kernel.masterdata.utils.MetaDataUtils;

/**
 * Service class has methods to save and fetch DeviceSpecification Details
 * 
 * @author Megha Tanga
 * @author Uday
 * @since 1.0.0
 *
 */
/**
 * @author M1046571
 *
 */
@Service
public class DeviceSpecificationServiceImpl implements DeviceSpecificationService {

	@Autowired
	DeviceSpecificationRepository deviceSpecificationRepository;

	@Autowired
	DeviceRepository deviceRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.DeviceSpecificationService#
	 * findDeviceSpecificationByLangugeCode(java.lang.String)
	 */
	@Override
	public List<DeviceSpecificationDto> findDeviceSpecificationByLangugeCode(String languageCode) {
		List<DeviceSpecification> deviceSpecificationList = null;
		List<DeviceSpecificationDto> deviceSpecificationDtoList = null;
		try {
			deviceSpecificationList = deviceSpecificationRepository
					.findByLangCodeAndIsDeletedFalseOrIsDeletedIsNull(languageCode);
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(
					DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_DATA_FETCH_EXCEPTION.getErrorCode(),
					DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_DATA_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		if (deviceSpecificationList != null && !deviceSpecificationList.isEmpty()) {
			deviceSpecificationDtoList = MapperUtils.mapAll(deviceSpecificationList, DeviceSpecificationDto.class);
			return deviceSpecificationDtoList;
		} else {
			throw new DataNotFoundException(
					DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_NOT_FOUND_EXCEPTION.getErrorCode(),
					DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.DeviceSpecificationService#
	 * findDeviceSpecByLangCodeAndDevTypeCode(java.lang.String, java.lang.String)
	 */
	@Override
	public List<DeviceSpecificationDto> findDeviceSpecByLangCodeAndDevTypeCode(String languageCode,
			String deviceTypeCode) {
		List<DeviceSpecification> deviceSpecificationList = null;
		List<DeviceSpecificationDto> deviceSpecificationDtoList = null;
		try {
			deviceSpecificationList = deviceSpecificationRepository
					.findByLangCodeAndDeviceTypeCodeAndIsDeletedFalseOrIsDeletedIsNull(languageCode, deviceTypeCode);
		} catch (DataAccessException | DataAccessLayerException e) {
			throw new MasterDataServiceException(
					DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_DATA_FETCH_EXCEPTION.getErrorCode(),
					DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_DATA_FETCH_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		if (deviceSpecificationList != null && !deviceSpecificationList.isEmpty()) {
			deviceSpecificationDtoList = MapperUtils.mapAll(deviceSpecificationList, DeviceSpecificationDto.class);
			return deviceSpecificationDtoList;
		} else {
			throw new DataNotFoundException(
					DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_NOT_FOUND_EXCEPTION.getErrorCode(),
					DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.DeviceSpecificationService#
	 * createDeviceSpecification(io.mosip.kernel.masterdata.dto.RequestDto)
	 */
	@Override
	public IdResponseDto createDeviceSpecification(RequestDto<DeviceSpecificationDto> deviceSpecifications) {
		DeviceSpecification renDeviceSpecification = new DeviceSpecification();

		DeviceSpecification entity = MetaDataUtils.setCreateMetaData(deviceSpecifications.getRequest(),
				DeviceSpecification.class);
		try {
			renDeviceSpecification = deviceSpecificationRepository.create(entity);
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(
					DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_INSERT_EXCEPTION.getErrorCode(),
					DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_INSERT_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		IdResponseDto idResponseDto = new IdResponseDto();
		MapperUtils.map(renDeviceSpecification, idResponseDto);

		return idResponseDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.DeviceSpecificationService#
	 * updateDeviceSpecification(io.mosip.kernel.masterdata.dto.RequestDto)
	 */
	@Override
	public IdResponseDto updateDeviceSpecification(RequestDto<DeviceSpecificationDto> deviceSpecification) {
		IdResponseDto idResponseDto = new IdResponseDto();
		try {
			DeviceSpecification entity = deviceSpecificationRepository
					.findByIdAndIsDeletedFalseorIsDeletedIsNull(deviceSpecification.getRequest().getId());
			if (!EmptyCheckUtils.isNullEmpty(entity)) {
				MetaDataUtils.setUpdateMetaData(deviceSpecification.getRequest(), entity, false);
				deviceSpecificationRepository.update(entity);
				idResponseDto.setId(entity.getId());
			} else {
				throw new RequestException(
						DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_NOT_FOUND_EXCEPTION.getErrorCode(),
						DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_NOT_FOUND_EXCEPTION.getErrorMessage());
			}
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(
					DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_UPDATE_EXCEPTION.getErrorCode(),
					DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_UPDATE_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}
		return idResponseDto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.kernel.masterdata.service.DeviceSpecificationService#
	 * deleteDeviceSpecification(java.lang.String)
	 */
	@Override
	public IdResponseDto deleteDeviceSpecification(String id) {
		IdResponseDto idResponseDto = new IdResponseDto();
		try {
			DeviceSpecification deviceSpecification = deviceSpecificationRepository
					.findByIdAndIsDeletedFalseorIsDeletedIsNull(id);

			if (deviceSpecification != null) {
				List<Device> renDeviceList = deviceRepository
						.findDeviceByDeviceSpecIdAndIsDeletedFalseorIsDeletedIsNull(deviceSpecification.getId());
				if (renDeviceList.isEmpty()) {
					MetaDataUtils.setDeleteMetaData(deviceSpecification);
					deviceSpecificationRepository.update(deviceSpecification);
					idResponseDto.setId(deviceSpecification.getId());
				} else {
					throw new MasterDataServiceException(
							DeviceSpecificationErrorCode.DEVICE_DELETE_DEPENDENCY_EXCEPTION.getErrorCode(),
							DeviceSpecificationErrorCode.DEVICE_DELETE_DEPENDENCY_EXCEPTION.getErrorMessage());
				}

			} else {
				throw new RequestException(
						DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_NOT_FOUND_EXCEPTION.getErrorCode(),
						DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_NOT_FOUND_EXCEPTION.getErrorMessage());
			}
		} catch (DataAccessLayerException | DataAccessException e) {
			throw new MasterDataServiceException(
					DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_DELETE_EXCEPTION.getErrorCode(),
					DeviceSpecificationErrorCode.DEVICE_SPECIFICATION_DELETE_EXCEPTION.getErrorMessage()
							+ ExceptionUtils.parseException(e));
		}

		return idResponseDto;
	}

}
