package com.gigup.config.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StringToBigDecimalConverter implements Converter<String, BigDecimal> {

    @Override
    public BigDecimal convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Remove any non-numeric characters except decimal point
            String cleaned = source.replaceAll("[^0-9.]", "");
            return new BigDecimal(cleaned);
        } catch (NumberFormatException e) {
            return null;
        }
    }
} 