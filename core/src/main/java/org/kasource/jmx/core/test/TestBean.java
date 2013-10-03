package org.kasource.jmx.core.test;

import java.util.Date;

import javax.management.Notification;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;
import org.springframework.stereotype.Component;


@ManagedResource(objectName="KaJMX:name=Test", description="Bean used for testing")
@Component
public class TestBean implements NotificationPublisherAware{
    private TestEnum testEnum;
    private Date started = new Date();
    private int[] intArray = {1,2,3,4};
    private short[] shortArray = {1,2,3,4};
    private byte[] byteArray = {1,2,3,4};
    private boolean boolenValue;
    private boolean[] booleanArray = {true,false,true,false};
    private NotificationPublisher notificationPublisher;
    /**
     * @return the testEnum
     */
    @ManagedAttribute(description = "Testar enum")
    public TestEnum getTestEnum() {
        return testEnum;
    }

    /**
     * @param testEnum the testEnum to set
     */
    @ManagedAttribute
    public void setTestEnum(TestEnum testEnum) {
        this.testEnum = testEnum;
    }

    /**
     * @return the started
     */
    @ManagedAttribute(description = "Date started")
    public Date getStarted() {
        return started;
    }

    /**
     * @param started the started to set
     */
    @ManagedAttribute
    public void setStarted(Date started) {
        this.started = started;
    }

    /**
     * @return the intArray
     */
    @ManagedAttribute(description = "Integer array test")
    public int[] getIntArray() {
        return intArray;
    }

    /**
     * @param intArray the intArray to set
     */
    @ManagedAttribute(description = "Integer array test")
    public void setIntArray(int[] intArray) {
        this.intArray = intArray;
    }

    /**
     * @return the shortArray
     */
    @ManagedAttribute(description = "Short array test")
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
    @ManagedAttribute(description = "Byte array test")
    public byte[] getByteArray() {
        return byteArray;
    }

    /**
     * @param byteArray the byteArray to set
     */
    @ManagedAttribute(description = "Byte array test")
    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    /**
     * @return the booleanArray
     */
    @ManagedAttribute(description = "Boolean array test")
    public boolean[] getBooleanArray() {
        return booleanArray;
    }

    /**
     * @param booleanArray the booleanArray to set
     */
    @ManagedAttribute(description = "Boolean array test")
    public void setBooleanArray(boolean[] booleanArray) {
        this.booleanArray = booleanArray;
    }

    @Override
    public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;        
    }
    
    @ManagedOperation
    public void publishNotification() {
        notificationPublisher.sendNotification(new Notification("test", this, 0));
    }

    /**
     * @return the boolenValue
     */
    @ManagedAttribute
    public boolean isBoolenValue() {
        return boolenValue;
    }

    /**
     * @param boolenValue the boolenValue to set
     */
    @ManagedAttribute
    public void setBoolenValue(boolean boolenValue) {
        this.boolenValue = boolenValue;
    }

}
