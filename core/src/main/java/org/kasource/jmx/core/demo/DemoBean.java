package org.kasource.jmx.core.demo;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@ManagedResource(objectName="KaJMX:name=Demo", description="Bean used for dashboard demonstrations, attribute changes on this bean will affect the Demo Dashboard")
@Component
public class DemoBean {
    private int integer1 = 1;
    private int integer2 = 10;
    private int integer3 = 20;
    private int integer4 = 30;
    private int integer5 = 40;
    private int integer6 = 50;
    
    private boolean boolean1 = false;
    private boolean boolean2 = true;
    private boolean boolean3 = false;
    private boolean boolean4 = true;
    private boolean boolean5 = false;
    private boolean boolean6 = true;
    
    private DemoState enum1;
    private DemoState enum2 = DemoState.ONE;
    private DemoState enum3 = DemoState.TWO;
    private DemoState enum4 = DemoState.THREE;
    private DemoState enum5;
    private DemoState enum6 = DemoState.ONE;
    
    private float float1 = 1.0f;
    private float float2 = 20.0f;
    private float float3 = 30.0f;
    private float float4 = 40.0f;
    private float float5 = 50.0f;
    private float float6 = 60.0f;
    
    private double double1 = 1.0d;
    private double double2 = 20.0d;
    private double double3 = 30.0d;
    
    @ManagedOperation(description="Set random values on the integer attributes")
    public void randomizeIntegers() {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                for(int i=0; i < 800; i++) {
                   int integerToSet = (int) Math.round(Math.random()*60);
                   int value = (int) Math.round(Math.random()*100);
                   switch(integerToSet) {
                   case 1:
                       integer1 = value;
                       break;
                   case 2:
                       integer2 = value;
                       break;
                   case 3:
                       integer3 = value;
                       break;
                   case 4:
                       integer4 = value;
                       break;
                   case 5:
                       integer5 = value;
                       break;
                   case 6:
                       integer6 = value;
                       break;
                   }
                }
                long wait = (int) Math.round(Math.random()*1000);
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                   
                }
            }
        }).start();
    }
    
    @ManagedOperation(description="Set random values on the float attributes")
    public void randomizeFloats() {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                for(int i=0; i < 800; i++) {
                   int floatToSet = (int) Math.round(Math.random()*60);
                   float value = Double.valueOf(Math.random()*100.0).floatValue();
                   switch(floatToSet) {
                   case 1:
                       float1 = value;
                       break;
                   case 2:
                       float2 = value;
                       break;
                   case 3:
                       float3 = value;
                       break;
                   case 4:
                       float4 = value;
                       break;
                   case 5:
                       float5 = value;
                       break;
                   case 6:
                       float6 = value;
                       break;
                   }
                }
                long wait = (int) Math.round(Math.random()*1000);
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                   
                }
            }
        }).start();
    }
    
    
    /**
     * @return the integer1
     */
	@ManagedAttribute
    public int getInteger1() {
        return integer1;
    }
    /**
     * @param integer1 the integer1 to set
     */
    @ManagedAttribute
    public void setInteger1(int integer1) {
        this.integer1 = integer1;
    }
    /**
     * @return the integer2
     */
	@ManagedAttribute
    public int getInteger2() {
        return integer2;
    }
    /**
     * @param integer2 the integer2 to set
     */
	@ManagedAttribute
    public void setInteger2(int integer2) {
        this.integer2 = integer2;
    }
    /**
     * @return the integer3
     */
	@ManagedAttribute
    public int getInteger3() {
        return integer3;
    }
    /**
     * @param integer3 the integer3 to set
     */
	@ManagedAttribute
    public void setInteger3(int integer3) {
        this.integer3 = integer3;
    }
    /**
     * @return the integer4
     */
	@ManagedAttribute
    public int getInteger4() {
        return integer4;
    }
    /**
     * @param integer4 the integer4 to set
     */
	@ManagedAttribute
    public void setInteger4(int integer4) {
        this.integer4 = integer4;
    }
    /**
     * @return the integer5
     */
	@ManagedAttribute
    public int getInteger5() {
        return integer5;
    }
    /**
     * @param integer5 the integer5 to set
     */
	@ManagedAttribute
    public void setInteger5(int integer5) {
        this.integer5 = integer5;
    }
    /**
     * @return the integer6
     */
	@ManagedAttribute
    public int getInteger6() {
        return integer6;
    }
    /**
     * @param integer6 the integer6 to set
     */
	@ManagedAttribute
    public void setInteger6(int integer6) {
        this.integer6 = integer6;
    }
    /**
     * @return the boolean1
     */
	@ManagedAttribute
    public boolean isBoolean1() {
        return boolean1;
    }
    /**
     * @param boolean1 the boolean1 to set
     */
	@ManagedAttribute
    public void setBoolean1(boolean boolean1) {
        this.boolean1 = boolean1;
    }
    /**
     * @return the boolean2
     */
	@ManagedAttribute
    public boolean isBoolean2() {
        return boolean2;
    }
    /**
     * @param boolean2 the boolean2 to set
     */
	@ManagedAttribute
    public void setBoolean2(boolean boolean2) {
        this.boolean2 = boolean2;
    }
    /**
     * @return the boolean3
     */
	@ManagedAttribute
    public boolean isBoolean3() {
        return boolean3;
    }
    /**
     * @param boolean3 the boolean3 to set
     */
	@ManagedAttribute
    public void setBoolean3(boolean boolean3) {
        this.boolean3 = boolean3;
    }
    /**
     * @return the boolean4
     */
	@ManagedAttribute
    public boolean isBoolean4() {
        return boolean4;
    }
    /**
     * @param boolean4 the boolean4 to set
     */
	@ManagedAttribute
    public void setBoolean4(boolean boolean4) {
        this.boolean4 = boolean4;
    }
    /**
     * @return the boolean5
     */
	@ManagedAttribute
    public boolean isBoolean5() {
        return boolean5;
    }
    /**
     * @param boolean5 the boolean5 to set
     */
	@ManagedAttribute
    public void setBoolean5(boolean boolean5) {
        this.boolean5 = boolean5;
    }
    /**
     * @return the boolean6
     */
	@ManagedAttribute
    public boolean isBoolean6() {
        return boolean6;
    }
    /**
     * @param boolean6 the boolean6 to set
     */
	@ManagedAttribute
    public void setBoolean6(boolean boolean6) {
        this.boolean6 = boolean6;
    }
    /**
     * @return the enum1
     */
	@ManagedAttribute
    public DemoState getEnum1() {
        return enum1;
    }
    /**
     * @param enum1 the enum1 to set
     */
	@ManagedAttribute
    public void setEnum1(DemoState enum1) {
        this.enum1 = enum1;
    }
    /**
     * @return the enum2
     */
	@ManagedAttribute
    public DemoState getEnum2() {
        return enum2;
    }
    /**
     * @param enum2 the enum2 to set
     */
	@ManagedAttribute
    public void setEnum2(DemoState enum2) {
        this.enum2 = enum2;
    }
    /**
     * @return the enum3
     */
	@ManagedAttribute
    public DemoState getEnum3() {
        return enum3;
    }
    /**
     * @param enum3 the enum3 to set
     */
	@ManagedAttribute
    public void setEnum3(DemoState enum3) {
        this.enum3 = enum3;
    }
    /**
     * @return the enum4
     */
	@ManagedAttribute
    public DemoState getEnum4() {
        return enum4;
    }
    /**
     * @param enum4 the enum4 to set
     */
	@ManagedAttribute
    public void setEnum4(DemoState enum4) {
        this.enum4 = enum4;
    }
    /**
     * @return the enum5
     */
	@ManagedAttribute
    public DemoState getEnum5() {
        return enum5;
    }
    /**
     * @param enum5 the enum5 to set
     */
	@ManagedAttribute
    public void setEnum5(DemoState enum5) {
        this.enum5 = enum5;
    }
    /**
     * @return the enum6
     */
	@ManagedAttribute
    public DemoState getEnum6() {
        return enum6;
    }
    /**
     * @param enum6 the enum6 to set
     */
	@ManagedAttribute
    public void setEnum6(DemoState enum6) {
        this.enum6 = enum6;
    }
    /**
     * @return the float1
     */
	@ManagedAttribute
    public float getFloat1() {
        return float1;
    }
    /**
     * @param float1 the float1 to set
     */
	@ManagedAttribute
    public void setFloat1(float float1) {
        this.float1 = float1;
    }
    /**
     * @return the float2
     */
	@ManagedAttribute
    public float getFloat2() {
        return float2;
    }
    /**
     * @param float2 the float2 to set
     */
	@ManagedAttribute
    public void setFloat2(float float2) {
        this.float2 = float2;
    }
    /**
     * @return the float3
     */
	@ManagedAttribute
    public float getFloat3() {
        return float3;
    }
    /**
     * @param float3 the float3 to set
     */
	@ManagedAttribute
    public void setFloat3(float float3) {
        this.float3 = float3;
    }
    /**
     * @return the float4
     */
	@ManagedAttribute
    public float getFloat4() {
        return float4;
    }
    /**
     * @param float4 the float4 to set
     */
	@ManagedAttribute
    public void setFloat4(float float4) {
        this.float4 = float4;
    }
    /**
     * @return the float5
     */
	@ManagedAttribute
    public float getFloat5() {
        return float5;
    }
    /**
     * @param float5 the float5 to set
     */
	@ManagedAttribute
    public void setFloat5(float float5) {
        this.float5 = float5;
    }
    /**
     * @return the float6
     */
	@ManagedAttribute
    public float getFloat6() {
        return float6;
    }
    /**
     * @param float6 the float6 to set
     */
	@ManagedAttribute
    public void setFloat6(float float6) {
        this.float6 = float6;
    }


    /**
     * @return the double1
     */
	@ManagedAttribute
    public double getDouble1() {
        return double1;
    }


    /**
     * @param double1 the double1 to set
     */
    @ManagedAttribute
    public void setDouble1(double double1) {
        this.double1 = double1;
    }


    /**
     * @return the double2
     */
    @ManagedAttribute
    public double getDouble2() {
        return double2;
    }


    /**
     * @param double2 the double2 to set
     */
    @ManagedAttribute
    public void setDouble2(double double2) {
        this.double2 = double2;
    }


    /**
     * @return the double3
     */
    @ManagedAttribute
    public double getDouble3() {
        return double3;
    }


    /**
     * @param double3 the double3 to set
     */
    @ManagedAttribute
    public void setDouble3(double double3) {
        this.double3 = double3;
    }
}
