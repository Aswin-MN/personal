package io.mosip.authentication.service.integration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.mosip.authentication.core.constant.IdAuthenticationErrorConstants;
import io.mosip.authentication.core.exception.IdAuthenticationBusinessException;
import io.mosip.authentication.core.logger.IdaLogger;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.pdfgenerator.spi.PDFGenerator;
import io.mosip.kernel.core.templatemanager.spi.TemplateManager;
import io.mosip.kernel.core.templatemanager.spi.TemplateManagerBuilder;


/**
 * The Class IdTemplateManager.
 *
 * @author Dinesh Karuppiah.T
 */

@Component
public class IdTemplateManager {

	/** PDF Generator */
	private PDFGenerator pdfGenerator;

	/**  Class path. */
	private static final String CLASSPATH = "classpath";

	/**  UTF type. */
	private static final String ENCODE_TYPE = "UTF-8";

	/**  Template path. */
	private static final String TEMPLATES = "templates/";

	/** The logger. */
	private static Logger logger = IdaLogger.getLogger(IdTemplateManager.class);
	
	/** The template manager builder. */
	@Autowired
	private TemplateManagerBuilder templateManagerBuilder;
	
	/** The template manager. */
	private TemplateManager templateManager;

	
	/**
	 * Id template manager post construct.
	 */
	@PostConstruct
	public void idTemplateManagerPostConstruct() {
		templateManager = templateManagerBuilder.encodingType(ENCODE_TYPE)
				.enableCache(false).resourceLoader(CLASSPATH).build();
	}

	/**
	 * To apply Template for PDF Generation.
	 *
	 * @param templateName - template name for pdf format
	 * @param values       - list of contents
	 * @return the string
	 * @throws IdAuthenticationBusinessException the id authentication business exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String applyTemplate(String templateName, Map<String, Object> values)
			throws IdAuthenticationBusinessException, IOException {

		Objects.requireNonNull(templateName);
		Objects.requireNonNull(values);
		StringWriter writer = new StringWriter();
		boolean isTemplateAvail = false;
		isTemplateAvail = templateManager.merge(TEMPLATES + templateName, writer, values);
		if (isTemplateAvail) {
			return writer.toString();
		} else {
			throw new IdAuthenticationBusinessException(IdAuthenticationErrorConstants.MISSING_TEMPLATE_CONFIG);
		}
	}

	/**
	 * Generate PDF for e-KYC.
	 *
	 * @param templateName the template name
	 * @param values the values
	 * @return the output stream
	 * @throws IdAuthenticationBusinessException the id authentication business exception
	 */
	public OutputStream generatePDF(String templateName, Map<String, Object> values)
			throws IdAuthenticationBusinessException {
		try {
			String template = applyTemplate(templateName, values);
			Objects.requireNonNull(template);
			return pdfGenerator.generate(new ByteArrayInputStream(template.getBytes()));
		} catch (IOException e) {
			logger.error("NA", "Inside generatePDF >>>>>", e.getMessage(), e.getMessage());
			throw new IdAuthenticationBusinessException(IdAuthenticationErrorConstants.PDF_NOT_GENERATED, e);
		}

	}

}
