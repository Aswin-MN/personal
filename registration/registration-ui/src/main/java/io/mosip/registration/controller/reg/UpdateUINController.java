package io.mosip.registration.controller.reg;

import static io.mosip.registration.constants.LoggerConstants.LOG_REG_UIN_UPDATE;
import static io.mosip.registration.constants.RegistrationConstants.APPLICATION_ID;
import static io.mosip.registration.constants.RegistrationConstants.APPLICATION_NAME;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import io.mosip.kernel.core.idvalidator.exception.InvalidIDException;
import io.mosip.kernel.core.idvalidator.spi.IdValidator;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.util.StringUtils;
import io.mosip.registration.config.AppConfig;
import io.mosip.registration.constants.RegistrationConstants;
import io.mosip.registration.constants.RegistrationUIConstants;
import io.mosip.registration.controller.BaseController;
import io.mosip.registration.dto.ErrorResponseDTO;
import io.mosip.registration.dto.ResponseDTO;
import io.mosip.registration.dto.SelectionListDTO;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

@Controller
public class UpdateUINController extends BaseController implements Initializable {

	private static final Logger LOGGER = AppConfig.getLogger(UpdateUINController.class);

	@Autowired
	private RegistrationController registrationController;

	@FXML
	private TextField uinId;
	@FXML
	private CheckBox name;
	@FXML
	private CheckBox age;
	@FXML
	private CheckBox gender;
	@FXML
	private CheckBox address;
	@FXML
	private CheckBox contactDetails;
	@FXML
	private CheckBox biometricException;
	@FXML
	private CheckBox biometricIris;
	@FXML
	private CheckBox biometricFingerprint;
	@FXML
	private CheckBox cnieNumber;
	@FXML
	private CheckBox parentOrGuardianDetails;
	@FXML
	private Label toggleLabel1;
	@FXML
	private Label toggleLabel2;
	private SimpleBooleanProperty switchedOn;
	private boolean isChild;

	@Autowired
	@Qualifier(value = "uinValidator")
	private IdValidator<String> uinValidatorImpl;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		switchedOn = new SimpleBooleanProperty(false);
		isChild = switchedOn.get();
		toggleFunction();
	}

	/**
	 * Toggle functionality to give individual is adult or child.
	 */
	private void toggleFunction() {
		try {
			LOGGER.debug(LOG_REG_UIN_UPDATE, APPLICATION_NAME,APPLICATION_ID,
					"Entering into toggle function for toggle label 1 and toggle level 2");

			// TODO : remove this stub afterwards
			uinId.setText("426789089018");

			toggleLabel1.setId(RegistrationConstants.FIRST_TOGGLE_LABEL);
			toggleLabel2.setId(RegistrationConstants.SECOND_TOGGLE_LABEL);
			switchedOn.addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						toggleLabel1.setId(RegistrationConstants.SECOND_TOGGLE_LABEL);
						toggleLabel2.setId(RegistrationConstants.FIRST_TOGGLE_LABEL);
						isChild = newValue;

					} else {
						toggleLabel1.setId(RegistrationConstants.FIRST_TOGGLE_LABEL);
						toggleLabel2.setId(RegistrationConstants.SECOND_TOGGLE_LABEL);
						isChild = newValue;

					}
				}
			});

			toggleLabel1.setOnMouseClicked((event) -> {
				switchedOn.set(!switchedOn.get());
			});
			toggleLabel2.setOnMouseClicked((event) -> {
				switchedOn.set(!switchedOn.get());
			});
			LOGGER.debug(LOG_REG_UIN_UPDATE, APPLICATION_NAME,APPLICATION_ID,
					"Exiting the toggle function for toggle label 1 and toggle level 2");
		} catch (RuntimeException runtimeException) {
			LOGGER.error(LOG_REG_UIN_UPDATE, APPLICATION_NAME,APPLICATION_ID, runtimeException.getMessage());
		}
	}

	@FXML
	public void submitUINUpdate(ActionEvent event) {
		LOGGER.debug(LOG_REG_UIN_UPDATE, APPLICATION_NAME, APPLICATION_ID, "Updating UIN details");
		try {

			if (StringUtils.isEmpty(uinId.getText())) {
				generateAlert(RegistrationConstants.ERROR, RegistrationUIConstants.UPDATE_UIN_ENTER_UIN_ALERT);
			} else {

				if (uinValidatorImpl.validateId(uinId.getText())) {

					SelectionListDTO selectionListDTO = new SelectionListDTO();

					if (name.isSelected()) {
						selectionListDTO.setName(true);
					}
					if (age.isSelected()) {
						selectionListDTO.setAge(true);
					}
					if (gender.isSelected()) {
						selectionListDTO.setGender(true);
					}
					if (address.isSelected()) {
						selectionListDTO.setAddress(true);
					}
					if (contactDetails.isSelected()) {
						selectionListDTO.setContactDetails(true);
					}
					if (biometricException.isSelected()) {
						selectionListDTO.setBiometricException(true);
					}
					if (biometricIris.isSelected()) {
						selectionListDTO.setBiometricIris(true);
					}
					if (biometricFingerprint.isSelected()) {
						selectionListDTO.setBiometricFingerprint(true);
					}
					if (cnieNumber.isSelected()) {
						selectionListDTO.setCnieNumber(true);
					}
					if (parentOrGuardianDetails.isSelected()) {
						selectionListDTO.setParentOrGuardianDetails(true);
					}
					selectionListDTO.setChild(isChild);
					selectionListDTO.setUinId(uinId.getText());

					if (name.isSelected() || age.isSelected() || gender.isSelected() || address.isSelected()
							|| contactDetails.isSelected() || biometricException.isSelected()
							|| biometricIris.isSelected() || biometricFingerprint.isSelected()
							|| cnieNumber.isSelected() || parentOrGuardianDetails.isSelected()) {
						registrationController.init(selectionListDTO);

						Parent createRoot = BaseController.load(
								getClass().getResource(RegistrationConstants.CREATE_PACKET_PAGE),
								applicationContext.getApplicationLanguageBundle());

						if (!validateScreenAuthorization(createRoot.getId())) {
							generateAlert(RegistrationConstants.ERROR,
									RegistrationUIConstants.AUTHORIZATION_ERROR);
						} else {
							StringBuilder errorMessage = new StringBuilder();
							ResponseDTO responseDTO;
							responseDTO = validateSyncStatus();
							List<ErrorResponseDTO> errorResponseDTOs = responseDTO.getErrorResponseDTOs();
							if (errorResponseDTOs != null && !errorResponseDTOs.isEmpty()) {
								for (ErrorResponseDTO errorResponseDTO : errorResponseDTOs) {
									errorMessage.append(errorResponseDTO.getMessage() + " - "
											+ errorResponseDTO.getCode() + "\n\n");
								}
								generateAlert(RegistrationConstants.ERROR, errorMessage.toString().trim());

							} else {
								getScene(createRoot).setRoot(createRoot);
							}
						}
					} else {
						generateAlert(RegistrationConstants.ERROR, RegistrationUIConstants.UPDATE_UIN_SELECTION_ALERT);
					}
				}
			}
		} catch (InvalidIDException invalidIdException) {
			LOGGER.error(LOG_REG_UIN_UPDATE, APPLICATION_NAME, APPLICATION_ID, invalidIdException.getMessage());

			generateAlert(RegistrationConstants.ERROR, RegistrationUIConstants.UPDATE_UIN_VALIDATION_ALERT);
		} catch (IOException ioException) {
			LOGGER.error(LOG_REG_UIN_UPDATE, APPLICATION_NAME, APPLICATION_ID, ioException.getMessage());

			generateAlert(RegistrationConstants.ERROR, RegistrationUIConstants.UNABLE_LOAD_REG_PAGE);
		}
	}
}
