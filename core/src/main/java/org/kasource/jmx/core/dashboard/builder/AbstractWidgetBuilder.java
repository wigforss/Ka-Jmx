package org.kasource.jmx.core.dashboard.builder;

import java.util.regex.Pattern;

/**
 * Abstract base class for widgets
 * @author rikardwi
 **/
public abstract class AbstractWidgetBuilder {
    private static final Pattern ID_REG_EXP = Pattern.compile("\\s|:|@|\\$|%|&|/|\\+|,|\\(|\\)|\\{|\\}|\\[|\\]");
    private static final Pattern COLOR_REG_EXP = Pattern.compile("^#[A-F0-9]{6}",Pattern.CASE_INSENSITIVE);
    private String id;
    
    public AbstractWidgetBuilder(String id) {
        this.id = id;
    }
    
    protected void validateId() {
        if(id == null || id.trim().isEmpty()) {
            throw new IllegalStateException("A non empty id must be set");
        }
        if(ID_REG_EXP.matcher(id).find()) {
            throw new IllegalStateException("id " + id + " may not contain space, :, @, +, $, %, /, + or any brackets");
        }
    }

    protected void validateColor(String colorString) {
        if(colorString == null || colorString.trim().isEmpty()) {
            throw new IllegalStateException("A non empty color must be set");
        }
        /*
        if(COLOR_REG_EXP.matcher(colorString).matches()) {
            throw new IllegalStateException(colorString + " is not a valid color, # followed by 6 hex digits is expected.");
        }
        */
    }
    
    /**
     * @return the id
     */
    protected String getId() {
        return id;
    }
}
