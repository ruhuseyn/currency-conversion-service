package com.example.currencyconversionservice;

import java.math.BigDecimal;

public record CurrencyConversion(
        Long id,
        String from,
        String to,
        BigDecimal quantity,
        BigDecimal conversionMultiple,
        BigDecimal totalCalculateAmount,
        String environment
) {

}
