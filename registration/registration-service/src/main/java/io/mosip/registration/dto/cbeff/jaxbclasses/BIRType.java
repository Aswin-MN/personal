//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.06 at 02:49:01 PM IST 
//


package io.mosip.registration.dto.cbeff.jaxbclasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for BIRType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BIRType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Version" type="{http://docs.oasis-open.org/bias/ns/biaspatronformat-1.0/}VersionType" minOccurs="0"/>
 *         &lt;element name="CBEFFVersion" type="{http://docs.oasis-open.org/bias/ns/biaspatronformat-1.0/}VersionType" minOccurs="0"/>
 *         &lt;any processContents='skip' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="BIRInfo" type="{http://docs.oasis-open.org/bias/ns/biaspatronformat-1.0/}BIRInfoType"/>
 *         &lt;element name="BDBInfo" type="{http://docs.oasis-open.org/bias/ns/biaspatronformat-1.0/}BDBInfoType" minOccurs="0"/>
 *         &lt;element name="SBInfo" type="{http://docs.oasis-open.org/bias/ns/biaspatronformat-1.0/}SBInfoType" minOccurs="0"/>
 *         &lt;element name="BIR" type="{http://docs.oasis-open.org/bias/ns/biaspatronformat-1.0/}BIRType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="BDB" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="SB" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BIRType", propOrder = {
	"testFingerPrint",
	"testIris",
	"testFace",
    "version",
    "cbeffVersion",
    "birInfo",
    "bdbInfo",
    "sbInfo",
    "bir",
    "bdb",
    "sb"
})
@XmlRootElement(name="BIR")
public class BIRType {

    @XmlElement(name = "Version")
    protected VersionType version;
    @XmlElement(name = "CBEFFVersion")
    protected VersionType cbeffVersion;
    @XmlElement(name = "BIRInfo", required = true)
    protected BIRInfoType birInfo;
    @XmlElement(name = "BDBInfo")
    protected BDBInfoType bdbInfo;
    @XmlElement(name = "SBInfo")
    protected SBInfoType sbInfo;
    @XmlElement(name = "BIR")
    protected List<BIRType> bir;
    @XmlElement(name = "BDB")
    protected byte[] bdb;
    @XmlElement(name = "SB")
    protected byte[] sb;
    @XmlElement(name = "TestFingerPrint")
    protected TestBiometricType testFingerPrint;
    @XmlElement(name = "TestIris")
    protected TestBiometricType testIris;
    @XmlElement(name = "TestFace")
    protected TestBiometricType testFace;

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link VersionType }
     *     
     */
    public VersionType getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link VersionType }
     *     
     */
    public void setVersion(VersionType value) {
        this.version = value;
    }

    /**
     * Gets the value of the cbeffVersion property.
     * 
     * @return
     *     possible object is
     *     {@link VersionType }
     *     
     */
    public VersionType getCBEFFVersion() {
        return cbeffVersion;
    }

    /**
     * Sets the value of the cbeffVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link VersionType }
     *     
     */
    public void setCBEFFVersion(VersionType value) {
        this.cbeffVersion = value;
    }



    /**
     * Gets the value of the birInfo property.
     * 
     * @return
     *     possible object is
     *     {@link BIRInfoType }
     *     
     */
    public BIRInfoType getBIRInfo() {
        return birInfo;
    }

    /**
     * Sets the value of the birInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BIRInfoType }
     *     
     */
    public void setBIRInfo(BIRInfoType value) {
        this.birInfo = value;
    }

    /**
     * Gets the value of the bdbInfo property.
     * 
     * @return
     *     possible object is
     *     {@link BDBInfoType }
     *     
     */
    public BDBInfoType getBDBInfo() {
        return bdbInfo;
    }

    /**
     * Sets the value of the bdbInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BDBInfoType }
     *     
     */
    public void setBDBInfo(BDBInfoType value) {
        this.bdbInfo = value;
    }

    /**
     * Gets the value of the sbInfo property.
     * 
     * @return
     *     possible object is
     *     {@link SBInfoType }
     *     
     */
    public SBInfoType getSBInfo() {
        return sbInfo;
    }

    /**
     * Sets the value of the sbInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link SBInfoType }
     *     
     */
    public void setSBInfo(SBInfoType value) {
        this.sbInfo = value;
    }

    /**
     * Gets the value of the bir property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bir property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBIR().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BIRType }
     * 
     * 
     */
    public List<BIRType> getBIR() {
        if (bir == null) {
            bir = new ArrayList<BIRType>();
        }
        return this.bir;
    }

    /**
     * Gets the value of the bdb property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBDB() {
        return bdb;
    }

    /**
     * Sets the value of the bdb property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBDB(byte[] value) {
        this.bdb = value;
    }

    /**
     * Gets the value of the sb property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSB() {
        return sb;
    }

    public void setBir(List<BIRType> bir) {
		this.bir = bir;
	}

	/**
     * Sets the value of the sb property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSB(byte[] value) {
        this.sb = value;
    }
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bdb);
		result = prime * result + ((bdbInfo == null) ? 0 : bdbInfo.hashCode());
		result = prime * result + ((bir == null) ? 0 : bir.hashCode());
		result = prime * result + ((birInfo == null) ? 0 : birInfo.hashCode());
		result = prime * result + ((cbeffVersion == null) ? 0 : cbeffVersion.hashCode());
		result = prime * result + Arrays.hashCode(sb);
		result = prime * result + ((sbInfo == null) ? 0 : sbInfo.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BIRType other = (BIRType) obj;
		if (!Arrays.equals(bdb, other.bdb))
			return false;
		if (bdbInfo == null) {
			if (other.bdbInfo != null)
				return false;
		} else if (!bdbInfo.equals(other.bdbInfo))
			return false;
		if (bir == null) {
			if (other.bir != null)
				return false;
		} else if (!bir.equals(other.bir))
			return false;
		if (birInfo == null) {
			if (other.birInfo != null)
				return false;
		} else if (!birInfo.equals(other.birInfo))
			return false;
		if (cbeffVersion == null) {
			if (other.cbeffVersion != null)
				return false;
		} else if (!cbeffVersion.equals(other.cbeffVersion))
			return false;
		if (!Arrays.equals(sb, other.sb))
			return false;
		if (sbInfo == null) {
			if (other.sbInfo != null)
				return false;
		} else if (!sbInfo.equals(other.sbInfo))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	/**
	 * @return the testFingerPrint
	 */
	public TestBiometricType getTestFingerPrint() {
		return testFingerPrint;
	}

	/**
	 * @param testFingerPrint the testFingerPrint to set
	 */
	public void setTestFingerPrint(TestBiometricType testFingerPrint) {
		this.testFingerPrint = testFingerPrint;
	}

	/**
	 * @return the testIris
	 */
	public TestBiometricType getTestIris() {
		return testIris;
	}

	/**
	 * @param testIris the testIris to set
	 */
	public void setTestIris(TestBiometricType testIris) {
		this.testIris = testIris;
	}

	/**
	 * @return the testFace
	 */
	public TestBiometricType getTestFace() {
		return testFace;
	}

	/**
	 * @param testFace the testFace to set
	 */
	public void setTestFace(TestBiometricType testFace) {
		this.testFace = testFace;
	} 

}
