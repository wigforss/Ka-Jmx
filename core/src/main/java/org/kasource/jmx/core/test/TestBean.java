package org.kasource.jmx.core.test;

import java.util.Date;

import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;


@ManagedResource(objectName="KaJMX:name=Test", description="Bean used for testing")
@Component
public class TestBean {
    private TestEnum testEnum;
    private Date started = new Date();
    private int[] intArray = {1,2,3,4};
    private short[] shortArray = {1,2,3,4};
    private byte[] byteArray = {1,2,3,4};
    private boolean[] booleanArray = {true,false,true,false};
    
    /**
     * @return the testEnum
     */
    @org.springframework.jmx.export.annotation.ManagedAttribute(description = "Testar enum")
    public TestEnum getTestEnum() {
        return testEnum;
    }

    /**
     * @param testEnum the testEnum to set
     */
    @org.springframework.jmx.export.annotation.ManagedAttribute
    public void setTestEnum(TestEnum testEnum) {
        this.testEnum = testEnum;
    }

    /**
     * @return the started
     */
    @org.springframework.jmx.export.annotation.ManagedAttribute(description = "Date started")
    public Date getStarted() {
        return started;
    }

    /**
     * @param started the started to set
     */
    @org.springframework.jmx.export.annotation.ManagedAttribute
    public void setStarted(Date started) {
        this.started = started;
    }

    /**
     * @return the intArray
     */
    @org.springframework.jmx.export.annotation.ManagedAttribute(description = "Integer array test")
    public int[] getIntArray() {
        return intArray;
    }

    /**
     * @param intArray the intArray to set
     */
    @org.springframework.jmx.export.annotation.ManagedAttribute(description = "Integer array test")
    public void setIntArray(int[] intArray) {
        this.intArray = intArray;
    }

    /**
     * @return the shortArray
     */
    @org.springframework.jmx.export.annotation.ManagedAttribute(description = "Short array test")
    public short[] getShortArray() {
        return shortArray;
    }

    /**
     * @param shortArray the shortArray to set
     */
    public void setShortArray(short[] shortArray) {
        this.shortArray = shortArray;
    }

    /**
     * @return the byteArray
     */
    @org.springframework.jmx.export.annotation.ManagedAttribute(description = "Byte array test")
    public byte[] getByteArray() {
        return byteArray;
    }

    /**
     * @param byteArray the byteArray to set
     */
    @org.springframework.jmx.export.annotation.ManagedAttribute(description = "Byte array test")
    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    /**
     * @return the booleanArray
     */
    @org.springframework.jmx.export.annotation.ManagedAttribute(description = "Boolean array test")
    public boolean[] getBooleanArray() {
        return booleanArray;
    }

    /**
     * @param booleanArray the booleanArray to set
     */
    @org.springframework.jmx.export.annotation.ManagedAttribute(description = "Boolean array test")
    public void setBooleanArray(boolean[] booleanArray) {
        this.booleanArray = booleanArray;
    }

}
