package de.unibi.cebitec.bibiserv.jobproxy.model.task;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Describe a list of host,container mount values
 * 
 */
public class TMounts {

    @ApiModelProperty(required = true)
    protected TMounts.Mount mount;

    /**
     * Gets the value of the mount property.
     *
     * @return
     *     possible object is
     *     {@link TMounts.Mount }
     *
     */
    public TMounts.Mount getMount() {
        return mount;
    }

    /**
     * Sets the value of the mount property.
     *
     * @param value
     *     allowed object is
     *     {@link TMounts.Mount }
     *
     */
    public void setMount(TMounts.Mount value) {
        this.mount = value;
    }


    /**
     *
     * The following schema fragment specifies the expected content contained within this class.
     */
    public static class Mount {

        @Size(min=1)
        @NotNull
        @ApiModelProperty(required = true)
        protected String host;

        @Size(min=1)
        @NotNull
        @ApiModelProperty(required = true)
        protected String container;

        @Size(min=1)
        @NotNull
        @ApiModelProperty(required = true)
        private String mode;

        /**
         * Gets the value of the host property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getHost() {
            return host;
        }

        /**
         * Sets the value of the host property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setHost(String value) {
            this.host = value;
        }

        /**
         * Gets the value of the container property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getContainer() {
            return container;
        }

        /**
         * Sets the value of the container property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setContainer(String value) {
            this.container = value;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }
    }
}
