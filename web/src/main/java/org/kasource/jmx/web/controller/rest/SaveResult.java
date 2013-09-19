package org.kasource.jmx.web.controller.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveResult {

    private Map<String, Object> errors = new HashMap<String, Object>();
    private List<String> saved = new ArrayList<String>();
    
    /**
     * @return the errors
     */
    public Map<String, Object> getErrors() {
        return errors;
    }

    /**
     * @param errors the errors to set
     */
    public void setErrors(Map<String, Object> errors) {
        this.errors = errors;
    }

    /**
     * @return the saved
     */
    public List<String> getSaved() {
        return saved;
    }

    /**
     * @param saved the saved to set
     */
    public void setSaved(List<String> saved) {
        this.saved = saved;
    }
    
    
}
