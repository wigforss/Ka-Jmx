package org.kasource.jmx.web.conversion;

import org.junit.Test;
import static org.junit.Assert.*;

public class EnumConverterFactoryTest {
    @Test
    public void test() {
        EnumConverterFactory factory = new EnumConverterFactory();
        assertEquals(EnumExample.ONE, factory.getConverter(EnumExample.class).convert("ONE"));
        
    }
}
