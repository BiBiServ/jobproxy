package de.unibi.cebitec.bibiserv.jobproxy.model.state;


import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "state")
public class State {

    @NotNull
    @ApiModelProperty(required = true)
    protected String id;
    @NotNull
    @ApiModelProperty(required = true)
    protected String code;
    protected String description;
    protected String stdout;
    protected String stderr;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the stdout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStdout() {
        return stdout;
    }

    /**
     * Sets the value of the stdout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStdout(String value) {
        this.stdout = value;
    }

    /**
     * Gets the value of the stderr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStderr() {
        return stderr;
    }

    /**
     * Sets the value of the stderr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStderr(String value) {
        this.stderr = value;
    }

}
