package io.mosip.registration.device.webcam.impl;

import static io.mosip.registration.constants.RegistrationConstants.APPLICATION_ID;
import static io.mosip.registration.constants.RegistrationConstants.APPLICATION_NAME;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.List;

import org.springframework.stereotype.Component;

import com.github.sarxos.webcam.Webcam;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.registration.config.AppConfig;
import io.mosip.registration.device.webcam.PhotoProvider;

/**
 * class to access the webcam and its functionalities.
 *
 * @author Himaja Dhanyamraju
 */
@Component
public class LogitechPhotoProvider extends PhotoProvider {
	/**
	 * Instance of {@link MosipLogger}
	 */
	private static final Logger LOGGER = AppConfig.getLogger(LogitechPhotoProvider.class);

	@Override
	public Webcam connect(int width, int height) {
		LOGGER.debug("REGISTRATION - WEBCAMDEVICE", APPLICATION_NAME, APPLICATION_ID, "connecting to webcam");
		List<Webcam> webcams = Webcam.getWebcams();
		Webcam webcam;
		if (!webcams.isEmpty()) {
			if (webcams.get(0).getName().toLowerCase().contains("integrated")) {
				if (webcams.size() > 1) {
					webcam = webcams.get(1);
				} else {
					return null;
				}
			} else {
				webcam = webcams.get(0);
			}
			Dimension requiredDimension = new Dimension(width, height);
			webcam.setViewSize(requiredDimension);
			webcam.open();
			Webcam.getDiscoveryService().stop();
			return webcam;
		}
		return null;
	}

	@Override
	public BufferedImage captureImage(Webcam webcam) {
		LOGGER.debug("REGISTRATION - WEBCAMDEVICE", APPLICATION_NAME, APPLICATION_ID,
				"capturing the image from webcam");
		return webcam.getImage();
	}

	@Override
	public void close(Webcam webcam) {
		LOGGER.debug("REGISTRATION - WEBCAMDEVICE", APPLICATION_NAME, APPLICATION_ID, "closing the webcam");
		webcam.close();
	}
}
