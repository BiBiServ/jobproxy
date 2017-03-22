package de.unibi.cebitec.bibiserv.jobproxy.model.task;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


/**
 * Container description
 *
 */
public class TContainer {

    @Size(min = 1)
    @NotNull
    @ApiModelProperty(required = true)
    protected String image;

    protected List<TPorts> ports;

    protected List<TMounts> mounts;

    /**
     * Gets the value of the image property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the value of the image property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImage(String value) {
        this.image = value;
    }

    /**
     * Gets the value of the ports property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ports property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPorts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TPorts }
     * 
     * 
     */
    public List<TPorts> getPorts() {
        if (ports == null) {
            ports = new ArrayList<TPorts>();
        }
        return this.ports;
    }

    /**
     * Gets the value of the mounts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mounts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMounts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TMounts }
     * 
     * 
     */
    public List<TMounts> getMounts() {
        if (mounts == null) {
            mounts = new ArrayList<TMounts>();
        }
        return this.mounts;
    }

}
