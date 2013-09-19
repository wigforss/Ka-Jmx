package org.kasource.jmx.core.dashboard.builder;

import java.util.regex.Pattern;

public abstract class AbstractWidgetBuilder {
    private static final Pattern ID_REG_EXP = Pattern.compile("\\s|:|@|\\$|%|&|/|\\+|,|\\(|\\)|\\{|\\}|\\[|\\]");
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

    /**
     * @return the id
     */
    protected String getId() {
        return id;
    }
}
