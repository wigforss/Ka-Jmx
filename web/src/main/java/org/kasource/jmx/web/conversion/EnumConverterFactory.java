package org.kasource.jmx.web.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

@SuppressWarnings("rawtypes")
public class EnumConverterFactory implements ConverterFactory<String, Enum>{

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new EnumConverter(targetType);
        
    }
    

    private static class EnumConverter<T> implements Converter<String, T> {
        private Class<? extends Enum> targetClass;
        
        public EnumConverter(Class<? extends Enum> targetClass) {
            this.targetClass = targetClass;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T convert(String source) {
            return (T) Enum.valueOf(targetClass, source);
        }

        
        
       

       
    }

}
