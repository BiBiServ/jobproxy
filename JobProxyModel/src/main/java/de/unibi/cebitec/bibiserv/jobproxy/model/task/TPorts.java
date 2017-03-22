package de.unibi.cebitec.bibiserv.jobproxy.model.task;

import io.swagger.annotations.ApiModelProperty;

/**
 * Describe a list of host,container port values
 */
public class TPorts {

    @ApiModelProperty(required = true)
    protected TPorts.Port port;

    /**
     * Gets the value of the port property.
     * 
     * @return
     *     possible object is
     *     {@link TPorts.Port }
     *     
     */
    public TPorts.Port getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     * @param value
     *     allowed object is
     *     {@link TPorts.Port }
     *     
     */
    public void setPort(TPorts.Port value) {
        this.port = value;
    }


    public static class Port {

        protected int host;
        protected int container;

        /**
         * Gets the value of the host property.
         * 
         */
        public int getHost() {
            return host;
        }

        /**
         * Sets the value of the host property.
         * 
         */
        public void setHost(int value) {
            this.host = value;
        }

        /**
         * Gets the value of the container property.
         * 
         */
        public int getContainer() {
            return container;
        }

        /**
         * Sets the value of the container property.
         * 
         */
        public void setContainer(int value) {
            this.container = value;
        }

    }
}