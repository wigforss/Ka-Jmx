package org.kasource.jmx.web.controller.rest;

public class JsonWrapper<T> {
    
    public JsonWrapper(T data) {
        this.data = data;
    }
    
    private T data;

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }

}
