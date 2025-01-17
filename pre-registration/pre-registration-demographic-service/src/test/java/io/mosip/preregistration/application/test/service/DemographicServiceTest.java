package io.mosip.preregistration.application.test.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.idgenerator.spi.PridGenerator;
import io.mosip.kernel.core.jsonvalidator.exception.HttpRequestException;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.kernel.jsonvalidator.impl.JsonValidatorImpl;
import io.mosip.preregistration.application.code.RequestCodes;
import io.mosip.preregistration.application.dto.DeletePreRegistartionDTO;
import io.mosip.preregistration.application.dto.DemographicRequestDTO;
import io.mosip.preregistration.application.dto.PreRegistartionStatusDTO;
import io.mosip.preregistration.application.dto.PreRegistrationViewDTO;
import io.mosip.preregistration.application.entity.DemographicEntity;
import io.mosip.preregistration.application.errorcodes.ErrorCodes;
import io.mosip.preregistration.application.errorcodes.ErrorMessages;
import io.mosip.preregistration.application.exception.RecordNotFoundException;
import io.mosip.preregistration.application.exception.system.DateParseException;
import io.mosip.preregistration.application.exception.system.JsonValidationException;
import io.mosip.preregistration.application.exception.system.SystemUnsupportedEncodingException;
import io.mosip.preregistration.application.repository.DemographicRepository;
import io.mosip.preregistration.application.service.DemographicService;
import io.mosip.preregistration.application.service.util.DemographicServiceUtil;
import io.mosip.preregistration.core.common.dto.BookingRegistrationDTO;
import io.mosip.preregistration.core.common.dto.DemographicResponseDTO;
import io.mosip.preregistration.core.common.dto.DocumentDeleteDTO;
import io.mosip.preregistration.core.common.dto.MainListResponseDTO;
import io.mosip.preregistration.core.common.dto.MainRequestDTO;
import io.mosip.preregistration.core.exception.InvalidRequestParameterException;
import io.mosip.preregistration.core.exception.TableNotAccessibleException;

/**
 * Test class to test the PreRegistration Service methods
 * 
 * @author Rajath KR
 * @author Sanober Noor
 * @author Tapaswini Behera
 * @author Jagadishwari S
 * @author Ravi C Balaji
 * @since 1.0.0
 * 
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemographicServiceTest {

	/**
	 * Mocking the DemographicRepository bean
	 */
	@MockBean
	private DemographicRepository demographicRepository;

	/**
	 * Mocking the RestTemplateBuilder bean
	 */
	@MockBean
	RestTemplateBuilder restTemplateBuilder;

	/**
	 * Mocking the PridGenerator bean
	 */
	@MockBean
	private PridGenerator<String> pridGenerator;

	/**
	 * Mocking the JsonValidatorImpl bean
	 */
	@MockBean
	private JsonValidatorImpl jsonValidator;

	/**
	 * Autowired reference for $link{DemographicServiceUtil}
	 */
	@Autowired
	DemographicServiceUtil serviceUtil;

	JSONParser parser = new JSONParser();

	/**
	 * Autowired reference for $link{DemographicService}
	 */
	@Autowired
	private DemographicService preRegistrationService;

	List<DemographicEntity> userEntityDetails = new ArrayList<>();
	List<PreRegistrationViewDTO> responseViewList = new ArrayList<PreRegistrationViewDTO>();
	private PreRegistrationViewDTO preRegistrationViewDTO;
	private DemographicEntity preRegistrationEntity;
	private JSONObject jsonObject;
	private JSONObject jsonTestObject;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	File fileCr = null;
	File fileUp = null;
	MainRequestDTO<DemographicRequestDTO> demographicRequestDTO = null;
	DemographicRequestDTO createPreRegistrationDTO = null;
	DemographicResponseDTO demographicResponseDTO = null;
	boolean requestValidatorFlag = false;
	Map<String, String> requestMap = new HashMap<>();
	Map<String, String> requiredRequestMap = new HashMap<>();
	LocalDateTime times = null;
	BookingRegistrationDTO bookingRegistrationDTO;
	MainListResponseDTO<DemographicResponseDTO> responseDTO = null;

	@Value("${ver}")
	String versionUrl;

	@Value("${id}")
	String idUrl;

	private Map<String, String> reqDateRange = new HashMap<>();

	String fromDate = "";
	String toDate = "";

	/**
	 * @throws ParseException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws org.json.simple.parser.ParseException
	 * @throws URISyntaxException
	 */
	@Before
	public void setup() throws ParseException, FileNotFoundException, IOException,
			org.json.simple.parser.ParseException, URISyntaxException {

		preRegistrationEntity = new DemographicEntity();
		ClassLoader classLoader = getClass().getClassLoader();
		URI uri = new URI(
				classLoader.getResource("pre-registration-crby.json").getFile().trim().replaceAll("\\u0020", "%20"));
		fileCr = new File(uri.getPath());
		uri = new URI(
				classLoader.getResource("pre-registration-upby.json").getFile().trim().replaceAll("\\u0020", "%20"));
		fileUp = new File(uri.getPath());

		File file = new File(classLoader.getResource("pre-registration.json").getFile());
		jsonObject = (JSONObject) parser.parse(new FileReader(file));

		File fileTest = new File(classLoader.getResource("pre-registration-test.json").getFile());
		jsonTestObject = (JSONObject) parser.parse(new FileReader(fileTest));

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = dateFormat.parse("17/12/2018");
		long time = date.getTime();
		times = LocalDateTime.now();
		preRegistrationEntity.setCreateDateTime(times);
		preRegistrationEntity.setCreatedBy("9988905444");
		preRegistrationEntity.setStatusCode("Pending_Appointment");
		preRegistrationEntity.setUpdateDateTime(times);
		preRegistrationEntity.setApplicantDetailJson(jsonTestObject.toJSONString().getBytes());
		preRegistrationEntity.setPreRegistrationId("1234");
		userEntityDetails.add(preRegistrationEntity);

		logger.info("Entity " + preRegistrationEntity);

		preRegistrationViewDTO = new PreRegistrationViewDTO();
		preRegistrationViewDTO.setFullname(null);
		preRegistrationViewDTO.setStatusCode("Pending_Appointment");
		preRegistrationViewDTO.setPreId("1234");
		responseViewList.add(preRegistrationViewDTO);

		createPreRegistrationDTO = new DemographicRequestDTO();
		createPreRegistrationDTO.setDemographicDetails(jsonObject);
		createPreRegistrationDTO.setPreRegistrationId("1234");

		demographicRequestDTO = new MainRequestDTO<DemographicRequestDTO>();
		demographicRequestDTO.setId("mosip.pre-registration.demographic.create");
		demographicRequestDTO.setVer("1.0");
		demographicRequestDTO.setReqTime(new Timestamp(System.currentTimeMillis()));
		demographicRequestDTO.setRequest(createPreRegistrationDTO);

		bookingRegistrationDTO = new BookingRegistrationDTO();
		bookingRegistrationDTO.setRegDate("2018-12-10");
		bookingRegistrationDTO.setRegistrationCenterId("1");
		bookingRegistrationDTO.setSlotFromTime("09:00");
		bookingRegistrationDTO.setSlotToTime("09:13");

		requestMap.put("id", demographicRequestDTO.getId());
		requestMap.put("ver", demographicRequestDTO.getVer());
		requestMap.put("reqTime", demographicRequestDTO.getReqTime().toString());
		requestMap.put("request", demographicRequestDTO.getRequest().toString());

		fromDate = "2018-12-06 09:49:29";
		toDate = "2018-12-06 12:59:29";

		requestMap.put(RequestCodes.fromDate.toString(), fromDate);
		requestMap.put(RequestCodes.toDate.toString(), toDate);

		requiredRequestMap.put("id", idUrl);
		requiredRequestMap.put("ver", versionUrl);

		responseDTO = new MainListResponseDTO<DemographicResponseDTO>();
		responseDTO.setResTime(serviceUtil.getCurrentResponseTime());
		responseDTO.setStatus(Boolean.TRUE);
		responseDTO.setErr(null);

	}

	/**
	 * @throws Exception
	 */
	@Test
	public void successSaveImplTest() throws Exception {
		Mockito.when(pridGenerator.generateId()).thenReturn("67547447647457");
		Mockito.when(jsonValidator.validateJson(jsonObject.toString(), "mosip-prereg-identity-json-schema.json"))
				.thenReturn(null);
		Mockito.when(demographicRepository.save(Mockito.any())).thenReturn(preRegistrationEntity);
		demographicResponseDTO = new DemographicResponseDTO();
		demographicResponseDTO.setDemographicDetails(jsonObject);
		demographicResponseDTO.setPreRegistrationId("");
		demographicResponseDTO.setCreatedBy("9988905444");
		demographicResponseDTO.setCreatedDateTime(serviceUtil.getLocalDateString(times));
		demographicResponseDTO.setStatusCode("Pending_Appointment");
		createPreRegistrationDTO = new DemographicRequestDTO();
		createPreRegistrationDTO.setDemographicDetails(jsonObject);
		createPreRegistrationDTO.setPreRegistrationId("");
		createPreRegistrationDTO.setCreatedBy("9988905444");
		createPreRegistrationDTO.setCreatedDateTime(serviceUtil.getLocalDateString(times));
		demographicRequestDTO.setRequest(createPreRegistrationDTO);
		List<DemographicResponseDTO> listOfCreatePreRegistrationDTO = new ArrayList<>();
		listOfCreatePreRegistrationDTO.add(demographicResponseDTO);
		responseDTO.setResponse(listOfCreatePreRegistrationDTO);

		MainListResponseDTO<DemographicResponseDTO> actualRes = preRegistrationService
				.addPreRegistration(demographicRequestDTO);
		assertEquals(actualRes.getResponse().get(0).getStatusCode(), responseDTO.getResponse().get(0).getStatusCode());
	}

	/**
	 * @throws Exception
	 */
	@Test(expected = TableNotAccessibleException.class)
	public void saveFailureCheck() throws Exception {
		DataAccessLayerException exception = new DataAccessLayerException(ErrorCodes.PRG_PAM_APP_002.toString(),
				ErrorMessages.PRE_REGISTRATION_TABLE_NOT_ACCESSIBLE.toString(), null);
		Mockito.when(jsonValidator.validateJson(jsonObject.toString(), "mosip-prereg-identity-json-schema.json"))
				.thenReturn(null);
		Mockito.when(demographicRepository.save(Mockito.any())).thenThrow(exception);
		createPreRegistrationDTO = new DemographicRequestDTO();
		createPreRegistrationDTO.setDemographicDetails(jsonObject);
		createPreRegistrationDTO.setPreRegistrationId("");
		createPreRegistrationDTO.setCreatedBy("9988905444");
		createPreRegistrationDTO.setCreatedDateTime(serviceUtil.getLocalDateString(times));
		demographicRequestDTO.setRequest(createPreRegistrationDTO);
		preRegistrationService.addPreRegistration(demographicRequestDTO);
	}

	@Test
	public void successUpdateTest() throws Exception {
		Mockito.when(jsonValidator.validateJson(jsonTestObject.toString(), "mosip-prereg-identity-json-schema.json"))
				.thenReturn(null);
		Mockito.when(demographicRepository.findBypreRegistrationId("1234")).thenReturn(preRegistrationEntity);

		Mockito.when(demographicRepository.save(Mockito.any())).thenReturn(preRegistrationEntity);
		createPreRegistrationDTO = new DemographicRequestDTO();
		createPreRegistrationDTO.setDemographicDetails(jsonTestObject);
		createPreRegistrationDTO.setPreRegistrationId("1234");
		createPreRegistrationDTO.setCreatedBy("9988905444");
		createPreRegistrationDTO.setCreatedDateTime(serviceUtil.getLocalDateString(times));
		createPreRegistrationDTO.setUpdatedBy("9988905444");
		createPreRegistrationDTO.setUpdatedDateTime(serviceUtil.getLocalDateString(times));
		demographicRequestDTO.setRequest(createPreRegistrationDTO);
		MainListResponseDTO<DemographicResponseDTO> res = preRegistrationService
				.addPreRegistration(demographicRequestDTO);
		assertEquals("1234", res.getResponse().get(0).getPreRegistrationId());
	}

	@Test(expected = JsonValidationException.class)
	public void updateFailureCheck() throws Exception {
		HttpRequestException exception = new HttpRequestException(ErrorCodes.PRG_PAM_APP_007.name(),
				ErrorMessages.JSON_PARSING_FAILED.name());
		Mockito.when(jsonValidator.validateJson(jsonTestObject.toString(), "mosip-prereg-identity-json-schema.json"))
				.thenReturn(null);

		Mockito.when(demographicRepository.findBypreRegistrationId("1234")).thenReturn(preRegistrationEntity);

		Mockito.when(demographicRepository.save(Mockito.any())).thenThrow(exception);
		createPreRegistrationDTO = new DemographicRequestDTO();
		createPreRegistrationDTO.setDemographicDetails(jsonTestObject);
		createPreRegistrationDTO.setPreRegistrationId("1234");
		createPreRegistrationDTO.setUpdatedBy("9988905444");
		createPreRegistrationDTO.setUpdatedDateTime(serviceUtil.getLocalDateString(times));
		createPreRegistrationDTO.setCreatedBy("9988905444");
		createPreRegistrationDTO.setCreatedDateTime(serviceUtil.getLocalDateString(times));
		demographicRequestDTO.setRequest(createPreRegistrationDTO);
		preRegistrationService.addPreRegistration(demographicRequestDTO);
	}

	@Test(expected = InvalidRequestParameterException.class)
	public void createByDateFailureTest() throws Exception {
		InvalidRequestParameterException exception = new InvalidRequestParameterException(
				ErrorCodes.PRG_PAM_APP_012.toString(), ErrorMessages.MISSING_REQUEST_PARAMETER.toString());
		jsonObject = (JSONObject) parser.parse(new FileReader(fileCr));
		Mockito.when(jsonValidator.validateJson(jsonObject.toString(), "mosip-prereg-identity-json-schema.json"))
				.thenReturn(null);

		preRegistrationEntity.setCreateDateTime(null);
		preRegistrationEntity.setCreatedBy("");
		preRegistrationEntity.setPreRegistrationId("");
		Mockito.when(demographicRepository.save(preRegistrationEntity)).thenThrow(exception);
		demographicResponseDTO = new DemographicResponseDTO();
		demographicResponseDTO.setDemographicDetails(jsonObject);
		demographicResponseDTO.setPreRegistrationId("");
		demographicResponseDTO.setCreatedBy("9988905444");
		demographicResponseDTO.setCreatedDateTime(serviceUtil.getLocalDateString(times));
		demographicResponseDTO.setStatusCode("Pending_Appointment");
		createPreRegistrationDTO = new DemographicRequestDTO();
		createPreRegistrationDTO.setDemographicDetails(jsonObject);
		createPreRegistrationDTO.setPreRegistrationId("");
		createPreRegistrationDTO.setCreatedBy("");
		createPreRegistrationDTO.setCreatedDateTime(null);
		demographicRequestDTO.setRequest(createPreRegistrationDTO);
		List<DemographicResponseDTO> listOfCreatePreRegistrationDTO = new ArrayList<>();
		listOfCreatePreRegistrationDTO.add(demographicResponseDTO);
		responseDTO.setResponse(listOfCreatePreRegistrationDTO);
		MainListResponseDTO<DemographicResponseDTO> actualRes = preRegistrationService
				.addPreRegistration(demographicRequestDTO);
		assertEquals(actualRes.getResponse().get(0).getStatusCode(), responseDTO.getResponse().get(0).getStatusCode());

	}

	@Test(expected = InvalidRequestParameterException.class)
	public void updateByDateFailureTest() throws Exception {
		String prid = "1234";
		InvalidRequestParameterException exception = new InvalidRequestParameterException(
				ErrorCodes.PRG_PAM_APP_012.toString(), ErrorMessages.MISSING_REQUEST_PARAMETER.toString());
		jsonObject = (JSONObject) parser.parse(new FileReader(fileUp));
		Mockito.when(demographicRepository.findBypreRegistrationId(prid)).thenReturn(preRegistrationEntity);

		Mockito.when(jsonValidator.validateJson(jsonObject.toString(), "mosip-prereg-identity-json-schema.json"))
				.thenThrow(exception);
		MainListResponseDTO<DemographicResponseDTO> res = preRegistrationService
				.addPreRegistration(demographicRequestDTO);
		assertEquals(false, res.isStatus());
	}

	// @Test
	// public void getApplicationDetailsTest() throws
	// org.json.simple.parser.ParseException {
	//
	// RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
	// Mockito.when(restTemplateBuilder.build()).thenReturn(restTemplate);
	// String userId = "9988905444";
	// MainListResponseDTO<PreRegistrationViewDTO> response = new
	// MainListResponseDTO<>();
	// List<PreRegistrationViewDTO> viewList = new ArrayList<>();
	// PreRegistrationViewDTO viewDto = new PreRegistrationViewDTO();
	//
	// viewDto = new PreRegistrationViewDTO();
	// viewDto.setPreId("1234");
	// viewDto.setFullname("rupika");
	// viewDto.setStatusCode(preRegistrationEntity.getStatusCode());
	// viewDto.setBookingRegistrationDTO(bookingRegistrationDTO);
	//
	// viewList.add(viewDto);
	// response.setResponse(viewList);
	// response.setStatus(Boolean.TRUE);
	// BookingResponseDTO<BookingRegistrationDTO> bookingResultDto = new
	// BookingResponseDTO<>();
	//
	// ResponseEntity<BookingResponseDTO> res = new
	// ResponseEntity<>(bookingResultDto, HttpStatus.OK);
	//
	// Mockito.when(demographicRepository.findByCreatedBy(userId)).thenReturn(userEntityDetails);
	//
	// Mockito.when(restTemplate.exchange(Mockito.anyString(),
	// Mockito.eq(HttpMethod.GET), Mockito.any(),
	// Mockito.eq(BookingResponseDTO.class))).thenReturn(res);
	//
	// MainListResponseDTO<PreRegistrationViewDTO> actualRes =
	// preRegistrationService.getAllApplicationDetails(userId);
	// assertEquals(actualRes.isStatus(), response.isStatus());
	//
	// }

	@Test(expected = RecordNotFoundException.class)
	public void getApplicationDetailsFailureTest() {
		String userId = "12345";
		Mockito.when(demographicRepository.findByCreatedBy(Mockito.anyString(),Mockito.anyString()))
				.thenReturn(null);
		preRegistrationService.getAllApplicationDetails(userId);

	}

	@Test(expected = InvalidRequestParameterException.class)
	public void getApplicationDetailsInvalidRequestTest() {
		InvalidRequestParameterException exception = new InvalidRequestParameterException(
				ErrorCodes.PRG_PAM_APP_012.name(), ErrorMessages.MISSING_REQUEST_PARAMETER.name());
		Mockito.when(demographicRepository.findByCreatedBy("","")).thenThrow(exception);
		preRegistrationService.getAllApplicationDetails("");
	}

	@Test
	public void getApplicationStatusTest() {
		String preId = "1234";
		MainListResponseDTO<PreRegistartionStatusDTO> response = new MainListResponseDTO<>();
		List<PreRegistartionStatusDTO> statusList = new ArrayList<PreRegistartionStatusDTO>();
		PreRegistartionStatusDTO statusDto = new PreRegistartionStatusDTO();
		statusDto.setPreRegistartionId(preId);
		statusDto.setStatusCode("Pending_Appointment");
		statusList.add(statusDto);
		response.setResponse(statusList);

		Mockito.when(demographicRepository.findBypreRegistrationId(ArgumentMatchers.any()))
				.thenReturn(preRegistrationEntity);

		MainListResponseDTO<PreRegistartionStatusDTO> actualRes = preRegistrationService.getApplicationStatus(preId);
		assertEquals(response.getResponse().get(0).getStatusCode(), actualRes.getResponse().get(0).getStatusCode());

	}

	@Test(expected = RecordNotFoundException.class)
	public void getApplicationStatusFailure() {
		String preId = "1234";
		MainListResponseDTO<PreRegistartionStatusDTO> response = new MainListResponseDTO<>();
		List<PreRegistartionStatusDTO> statusList = new ArrayList<PreRegistartionStatusDTO>();
		PreRegistartionStatusDTO statusDto = new PreRegistartionStatusDTO();
		statusDto.setPreRegistartionId(preId);
		statusDto.setStatusCode("Pending_Appointment");
		statusList.add(statusDto);
		response.setResponse(statusList);

		Mockito.when(demographicRepository.findBypreRegistrationId(ArgumentMatchers.any())).thenReturn(null);

		MainListResponseDTO<PreRegistartionStatusDTO> actualRes = preRegistrationService.getApplicationStatus(preId);
		assertEquals(response.getResponse().get(0).getStatusCode(), actualRes.getResponse().get(0).getStatusCode());

	}

	@Test(expected = TableNotAccessibleException.class)
	public void getApplicationDetailsTransactionFailureCheck() throws Exception {
		String userId = "9988905444";
		DataAccessLayerException exception = new DataAccessLayerException(ErrorCodes.PRG_PAM_APP_002.toString(),
				ErrorMessages.PRE_REGISTRATION_TABLE_NOT_ACCESSIBLE.toString(), null);
		Mockito.when(demographicRepository.findByCreatedBy(Mockito.anyString(),Mockito.anyString())).thenThrow(exception);
		preRegistrationService.getAllApplicationDetails(userId);
	}

	@Test(expected = RecordNotFoundException.class)
	public void deleteRecordNotFoundTest() {
		RecordNotFoundException exception = new RecordNotFoundException(ErrorCodes.PRG_PAM_APP_005.name(),
				ErrorMessages.UNABLE_TO_FETCH_THE_PRE_REGISTRATION.name());
		RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
		Mockito.when(restTemplateBuilder.build()).thenReturn(restTemplate);
		String preRegId = "1234";

		DocumentDeleteDTO deleteDTO = new DocumentDeleteDTO();
		deleteDTO.setDocumnet_Id(String.valueOf("1"));
		List<DocumentDeleteDTO> deleteAllList = new ArrayList<>();
		deleteAllList.add(deleteDTO);

		MainListResponseDTO<DocumentDeleteDTO> delResponseDto = new MainListResponseDTO<>();
		delResponseDto.setStatus(Boolean.TRUE);
		delResponseDto.setErr(null);
		delResponseDto.setResponse(deleteAllList);
		delResponseDto.setResTime(serviceUtil.getCurrentResponseTime());

		ResponseEntity<MainListResponseDTO> res = new ResponseEntity<>(delResponseDto, HttpStatus.OK);
		Mockito.when(demographicRepository.findBypreRegistrationId(preRegId)).thenReturn(null);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.DELETE), Mockito.any(),
				Mockito.eq(MainListResponseDTO.class))).thenThrow(exception);

		preRegistrationService.deleteIndividual(preRegId);

	}

	@Test
	public void deleteIndividualSuccessTest() {
		RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
		Mockito.when(restTemplateBuilder.build()).thenReturn(restTemplate);
		String preRegId = "1234";
		preRegistrationEntity.setCreateDateTime(times);
		preRegistrationEntity.setCreatedBy("9988905444");
		preRegistrationEntity.setStatusCode("Pending_Appointment");
		preRegistrationEntity.setUpdateDateTime(times);
		preRegistrationEntity.setApplicantDetailJson(jsonTestObject.toJSONString().getBytes());
		preRegistrationEntity.setPreRegistrationId("1234");

		DocumentDeleteDTO deleteDTO = new DocumentDeleteDTO();
		deleteDTO.setDocumnet_Id(String.valueOf("1"));
		List<DocumentDeleteDTO> deleteAllList = new ArrayList<>();
		deleteAllList.add(deleteDTO);

		MainListResponseDTO<DocumentDeleteDTO> delResponseDto = new MainListResponseDTO<>();
		delResponseDto.setStatus(Boolean.TRUE);
		delResponseDto.setErr(null);
		delResponseDto.setResponse(deleteAllList);
		delResponseDto.setResTime(serviceUtil.getCurrentResponseTime());

		Mockito.when(demographicRepository.findBypreRegistrationId(preRegId)).thenReturn(preRegistrationEntity);

		ResponseEntity<MainListResponseDTO> res = new ResponseEntity<>(delResponseDto, HttpStatus.OK);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.DELETE), Mockito.any(),
				Mockito.eq(MainListResponseDTO.class))).thenReturn(res);

		Mockito.when(demographicRepository.deleteByPreRegistrationId(preRegistrationEntity.getPreRegistrationId()))
				.thenReturn(1);

		MainListResponseDTO<DeletePreRegistartionDTO> actualres = preRegistrationService.deleteIndividual(preRegId);

		assertEquals(true, actualres.isStatus());

	}

	@Test
	public void updateByPreIdTest() {
		Mockito.when(demographicRepository.findBypreRegistrationId("1234")).thenReturn(preRegistrationEntity);
		createPreRegistrationDTO = new DemographicRequestDTO();
		createPreRegistrationDTO.setDemographicDetails(jsonTestObject);
		createPreRegistrationDTO.setPreRegistrationId("1234");
		createPreRegistrationDTO.setUpdatedBy("9988905444");
		createPreRegistrationDTO.setUpdatedDateTime(serviceUtil.getLocalDateString(times));
		createPreRegistrationDTO.setCreatedBy("9988905444");
		createPreRegistrationDTO.setCreatedDateTime(serviceUtil.getLocalDateString(times));
		demographicRequestDTO.setRequest(createPreRegistrationDTO);
		preRegistrationService.addPreRegistration(demographicRequestDTO);
	}

	@Test(expected = RecordNotFoundException.class)
	public void RecordNotFoundExceptionTest() {
		Mockito.when(demographicRepository.findBypreRegistrationId("1234")).thenReturn(null);

		createPreRegistrationDTO = new DemographicRequestDTO();
		createPreRegistrationDTO.setDemographicDetails(jsonTestObject);
		createPreRegistrationDTO.setPreRegistrationId("1234");
		createPreRegistrationDTO.setUpdatedBy("9988905444");
		createPreRegistrationDTO.setUpdatedDateTime(serviceUtil.getLocalDateString(times));
		createPreRegistrationDTO.setCreatedBy("9988905444");
		createPreRegistrationDTO.setCreatedDateTime(serviceUtil.getLocalDateString(times));
		demographicRequestDTO.setRequest(createPreRegistrationDTO);

		preRegistrationService.addPreRegistration(demographicRequestDTO);
	}

	@Test
	public void getPreRegistrationTest() {
		Mockito.when(demographicRepository.findBypreRegistrationId("1234")).thenReturn(preRegistrationEntity);
		preRegistrationService.getDemographicData("1234");
	}

	@Test
	public void updatePreRegistrationStatusTest() {
		Mockito.when(demographicRepository.findBypreRegistrationId("1234")).thenReturn(preRegistrationEntity);
		preRegistrationService.updatePreRegistrationStatus("1234", "Booked");
	}

	@Test
	public void getApplicationByDateTest() {
		String fromDate = "2018-12-06 09:49:29";
		String toDate = "2018-12-06 12:59:29";
		MainListResponseDTO<String> response = new MainListResponseDTO<>();
		List<String> preIds = new ArrayList<>();
		List<DemographicEntity> details = new ArrayList<>();
		DemographicEntity entity = new DemographicEntity();
		entity.setPreRegistrationId("1234");
		details.add(entity);

		preIds.add("1234");
		response.setResponse(preIds);
		response.setStatus(true);

		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		Date myFromDate;
		try {
			myFromDate = DateUtils.parseToDate(URLDecoder.decode(fromDate, "UTF-8"), dateFormat);

			Date myToDate = DateUtils.parseToDate(URLDecoder.decode(toDate, "UTF-8"), dateFormat);

			Mockito.when(demographicRepository.findBycreateDateTimeBetween(
					DateUtils.parseDateToLocalDateTime(myFromDate), DateUtils.parseDateToLocalDateTime(myToDate)))
					.thenReturn(details);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (java.io.UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		MainListResponseDTO<String> actualRes = preRegistrationService.getPreRegistrationByDate(fromDate, toDate);
		assertEquals(actualRes.isStatus(), response.isStatus());

	}

//	@Test
//	public void getApplicationWithoutToDateTest() {
//		String fromDate = "2018-12-06 09:49:29";
//
//		MainListResponseDTO<String> response = new MainListResponseDTO<>();
//		List<String> preIds = new ArrayList<>();
//		List<DemographicEntity> details = new ArrayList<>();
//		DemographicEntity entity = new DemographicEntity();
//		entity.setPreRegistrationId("1234");
//		details.add(entity);
//
//		preIds.add("1234");
//		response.setResponse(preIds);
//		response.setStatus(Boolean.TRUE);
//
//		String dateFormat = "yyyy-MM-dd HH:mm:ss";
//		Date myFromDate;
//		try {
//			myFromDate = DateUtils.parseToDate(URLDecoder.decode(fromDate, "UTF-8"), dateFormat);
//			Mockito.when(demographicRepository
//					.findBycreateDateTimeBetween(DateUtils.parseDateToLocalDateTime(myFromDate), null))
//					.thenReturn(details);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (java.io.UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		MainListResponseDTO<String> actualRes = preRegistrationService.getPreRegistrationByDate(fromDate, null);
//		assertEquals(actualRes.isStatus(), response.isStatus());
//
//	}

	/**
	 * @throws Exception
	 */
	@Test(expected = SystemUnsupportedEncodingException.class)
	public void getBydateFailureCheck() throws Exception {
		SystemUnsupportedEncodingException exception = new SystemUnsupportedEncodingException(
				ErrorCodes.PRG_PAM_APP_009.name(), ErrorMessages.UNSUPPORTED_ENCODING_CHARSET.name());

		MainListResponseDTO<String> response = new MainListResponseDTO<>();
		List<String> preIds = new ArrayList<>();
		List<DemographicEntity> details = new ArrayList<>();
		DemographicEntity entity = new DemographicEntity();
		entity.setPreRegistrationId("1234");
		details.add(entity);
		preIds.add("1234");
		response.setResponse(preIds);

		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		Date myFromDate;
		Date myToDate;
		myFromDate = DateUtils.parseToDate(URLDecoder.decode(fromDate, "UTF-8"), dateFormat);

		myToDate = DateUtils.parseToDate(URLDecoder.decode(toDate, "UTF-8"), dateFormat);

		Mockito.when(demographicRepository.findBycreateDateTimeBetween(DateUtils.parseDateToLocalDateTime(myFromDate),
				DateUtils.parseDateToLocalDateTime(myToDate))).thenThrow(exception);
		preRegistrationService.getPreRegistrationByDate(fromDate, toDate);

	}

	/**
	 * @throws Exception
	 */
	@Test(expected = io.mosip.kernel.core.exception.ParseException.class)
	public void getBydateFailureParseCheck() throws Exception {
		DateParseException exception = new DateParseException(ErrorCodes.PRG_PAM_APP_011.name(),
				ErrorMessages.UNSUPPORTED_DATE_FORMAT.name());
		String fromDate = "2018-12-06 09:49:29";
		String toDate = "2018-12-06 12:59:29";
		MainListResponseDTO<String> response = new MainListResponseDTO<>();
		List<String> preIds = new ArrayList<>();
		List<DemographicEntity> details = new ArrayList<>();
		DemographicEntity entity = new DemographicEntity();
		entity.setPreRegistrationId("1234");
		details.add(entity);

		preIds.add("1234");
		response.setResponse(preIds);

		String dateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
		Date myFromDate;
		Date myToDate;

		myFromDate = DateUtils.parseToDate(URLDecoder.decode(fromDate, "UTF-0"), dateFormat);

		myToDate = DateUtils.parseToDate(URLDecoder.decode(toDate, "UTF-8"), dateFormat);

		Mockito.when(demographicRepository.findBycreateDateTimeBetween(DateUtils.parseDateToLocalDateTime(myFromDate),
				DateUtils.parseDateToLocalDateTime(myToDate))).thenThrow(exception);
		preRegistrationService.getPreRegistrationByDate(fromDate, toDate);

	}

}