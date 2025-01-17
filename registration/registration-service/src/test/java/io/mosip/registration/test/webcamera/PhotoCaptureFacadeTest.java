package io.mosip.registration.test.webcamera;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryService;

import io.mosip.registration.device.webcam.PhotoCaptureFacade;
import io.mosip.registration.device.webcam.impl.LogitechPhotoProvider;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Webcam.class })
public class PhotoCaptureFacadeTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@InjectMocks
	PhotoCaptureFacade photoCaptureServiceImpl;
	
	@Mock
	LogitechPhotoProvider logitechPhotoProvider;
	
	@Mock
	WebcamDiscoveryService webcamDiscoveryService;
	
	@Test
	public void connectTest() {
		PowerMockito.mockStatic(Webcam.class);
		Webcam webcam = Mockito.mock(Webcam.class);
		List<Webcam> webcams = new ArrayList<>();
		when(webcam.getName()).thenReturn("Test-Cam");
		when(webcam.getViewSize()).thenReturn(new Dimension(640, 480));
		webcams.add(webcam);
		when(Webcam.getWebcams()).thenReturn(webcams);
		when(Webcam.getDiscoveryService()).thenReturn(webcamDiscoveryService);
		webcamDiscoveryService.stop();
		when(logitechPhotoProvider.connect(640, 480)).thenReturn(webcam);
		assertThat(photoCaptureServiceImpl.connect(640, 480), is(webcam));
	}
	
	@Test
	public void captureImageTest() {
		PowerMockito.mockStatic(Webcam.class);
		Webcam webcam = Mockito.mock(Webcam.class);
		when(webcam.getName()).thenReturn("Test-Cam");
		when(webcam.getViewSize()).thenReturn(new Dimension(640, 480));
		when(Webcam.getDefault()).thenReturn(webcam);
		BufferedImage image = null;		
		when(webcam.getImage()).thenReturn(image);
		when(logitechPhotoProvider.captureImage(webcam)).thenReturn(image);
		assertThat(photoCaptureServiceImpl.captureImage(webcam), is(image));
		photoCaptureServiceImpl.close(webcam);
	}
	
}
