package org.kasource.jmx.web.util;

import org.springframework.web.bind.WebDataBinder;

public interface JmxValueParser {

    Object parse(String value, Class<?> clazz);
    
    void setDataBinder(WebDataBinder dataBinder);
}
