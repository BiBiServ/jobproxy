package de.unibi.cebitec.bibiserv.jobproxy.model.task;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "task")
public class Task {

    @NotNull
    @Size(min = 1)
    @ApiModelProperty(required = true)
    protected String user;

    @Digits(integer = Integer.MAX_VALUE, fraction = 0)
    protected Integer cores;

    @Digits(integer = Integer.MAX_VALUE, fraction = 0)
    protected Integer memory;

    @Digits(integer = Integer.MAX_VALUE, fraction = 0)
    protected Integer cputime;

    protected String stdout;

    protected String stderr;

    protected String[] cmd;

    @Valid
    protected TContainer container;

    /**
     * Gets the value of the user property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUser(String value) {
        this.user = value;
    }

    /**
     * Gets the value of the cores property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCores() {
        return cores;
    }

    /**
     * Sets the value of the cores property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCores(Integer value) {
        this.cores = value;
    }

    /**
     * Gets the value of the memory property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMemory() {
        return memory;
    }

    /**
     * Sets the value of the memory property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMemory(Integer value) {
        this.memory = value;
    }

    /**
     * Gets the value of the cputime property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCputime() {
        return cputime;
    }

    /**
     * Sets the value of the cputime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCputime(Integer value) {
        this.cputime = value;
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

    /**
     * Gets the value of the cmd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String[] getCmd() {
        return cmd;
    }

    /**
     * Sets the value of the cmd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCmd(String[] value) {
        this.cmd = value;
    }

    /**
     * Gets the value of the container property.
     * 
     * @return
     *     possible object is
     *     {@link TContainer }
     *     
     */
    public TContainer getContainer() {
        return container;
    }

    /**
     * Sets the value of the container property.
     * 
     * @param value
     *     allowed object is
     *     {@link TContainer }
     *     
     */
    public void setContainer(TContainer value) {
        this.container = value;
    }
}