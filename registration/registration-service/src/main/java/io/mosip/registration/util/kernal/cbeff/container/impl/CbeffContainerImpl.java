/**
 * 
 */
package io.mosip.registration.util.kernal.cbeff.container.impl;

import java.util.ArrayList;
import java.util.List;

import io.mosip.registration.dto.cbeff.BIR;
import io.mosip.registration.dto.cbeff.jaxbclasses.BIRInfoType;
import io.mosip.registration.dto.cbeff.jaxbclasses.BIRType;
import io.mosip.registration.dto.cbeff.jaxbclasses.VersionType;
import io.mosip.registration.util.kernal.cbeff.abandoned.CbeffXSDValidator;
import io.mosip.registration.util.kernal.cbeff.common.CbeffValidator;
import io.mosip.registration.util.kernal.cbeff.container.CbeffContainerI;

/**
 * @author Ramadurai Pandian
 *
 */
public class CbeffContainerImpl extends CbeffContainerI<BIR, BIRType> {

	private BIRType birType;

	@Override
	public BIRType createBIRType(List<BIR> birList) {
		load();
		List<BIRType> birTypeList = new ArrayList<>();
		if (birList != null && birList.size() > 0) {
			for (BIR bir : birList) {
				birTypeList.add(bir.toBIRType(bir));
			}
		}
		birType.setBir(birTypeList);
		return birType;
	}

	private void load() {
		// Creating first version of Cbeff
		birType = new BIRType();
		// Initial Version
		VersionType versionType = new VersionType();
		versionType.setMajor(1);
		versionType.setMinor(1);
		VersionType cbeffVersion = new VersionType();
		cbeffVersion.setMajor(1);
		cbeffVersion.setMinor(1);
		birType.setVersion(versionType);
		birType.setCBEFFVersion(cbeffVersion);
		BIRInfoType birInfo = new BIRInfoType();
		birInfo.setIntegrity(false);
		birType.setBIRInfo(birInfo);
	}

	@Override
	public BIRType updateBIRType(List<BIR> birList, byte[] fileBytes) throws Exception {
		BIRType birType = CbeffValidator.getBIRFromXML(fileBytes);
		birType.getVersion().setMajor(birType.getVersion().getMajor() + 1);
		birType.getCBEFFVersion().setMajor(birType.getCBEFFVersion().getMajor() + 1);
		for (BIR bir : birList) {
			birType.getBIR().add(bir.toBIRType(bir));
		}
		return birType;
	}

	@Override
	public boolean validateXML(byte[] xmlBytes, byte[] xsdBytes) throws Exception {
		return CbeffXSDValidator.validateXML(xsdBytes, xmlBytes);
	}

}
