package com.example.currencyconversionservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public record CurrencyConversionController(CurrencyExcahngeProxy currencyExcahngeProxy) {

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity
    ) {

        HashMap<String,String > uriVariables = new HashMap<>();
        uriVariables.put("from",from);
        uriVariables.put("to",to);

        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity
                ("http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                        CurrencyConversion.class, uriVariables);
        CurrencyConversion currencyConversion = responseEntity.getBody();

        return new CurrencyConversion(currencyConversion.id(), from, to,
                quantity, currencyConversion.conversionMultiple(),
                quantity.multiply(currencyConversion.conversionMultiple()),
                currencyConversion.environment() + " rest template");
    }

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversionFeign(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity
    ) {

        CurrencyConversion currencyConversion = currencyExcahngeProxy.retrieveExchangeValue(from,to);

        return new CurrencyConversion(currencyConversion.id(), from, to,
                quantity, currencyConversion.conversionMultiple(),
                quantity.multiply(currencyConversion.conversionMultiple()),
                currencyConversion.environment() + " feign");
    }
}
