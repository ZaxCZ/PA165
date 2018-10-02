package cz.muni.fi.pa165.currency;

import org.junit.Test;
import org.mockito.internal.matchers.Null;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class CurrencyConvertorImplTest {

    private         ExchangeRateTable mockExchangeRateTable = mock(ExchangeRateTable.class);
    private         CurrencyConvertorImpl testedConvertor = new CurrencyConvertorImpl(mockExchangeRateTable);

    private Currency eur = Currency.getInstance("EUR");
    private Currency czk = Currency.getInstance("CZK");
    private Currency usd = Currency.getInstance("USD");
    private Currency gel = Currency.getInstance("GEL");

    @Test
    public void testConvert() throws ExternalServiceFailureException {


        when(mockExchangeRateTable.getExchangeRate(czk, usd)).thenReturn(new BigDecimal("0.50"));
        when(mockExchangeRateTable.getExchangeRate(usd, czk)).thenReturn(new BigDecimal("2"));

        when(mockExchangeRateTable.getExchangeRate(eur, czk)).thenReturn(new BigDecimal("25.2929"));
        when(mockExchangeRateTable.getExchangeRate(czk, eur)).thenReturn(new BigDecimal("0.1499"));

        when(mockExchangeRateTable.getExchangeRate(usd, eur)).thenReturn(new BigDecimal("-2.000001"));
        when(mockExchangeRateTable.getExchangeRate(eur, usd)).thenReturn(new BigDecimal("0"));



        assertThat(testedConvertor.convert(usd, czk, new BigDecimal("1"))).isEqualTo("2");
        assertThat(testedConvertor.convert(usd, czk, new BigDecimal("5"))).isEqualTo(new BigDecimal("10"));
        assertThat(testedConvertor.convert(usd, czk, new BigDecimal("1.86"))).isEqualTo(new BigDecimal("3.72"));
        assertThat(testedConvertor.convert(usd, czk, new BigDecimal("1.999"))).isEqualTo(new BigDecimal("4"));
        assertThat(testedConvertor.convert(usd, czk, new BigDecimal("0.009"))).isEqualTo(new BigDecimal("0"));
        assertThat(testedConvertor.convert(usd, czk, new BigDecimal("0"))).isEqualTo(new BigDecimal("0"));
        assertThat(testedConvertor.convert(usd, czk, new BigDecimal("-5"))).isEqualTo(new BigDecimal("-10"));


        assertThat(testedConvertor.convert(czk, usd, new BigDecimal("1"))).isEqualTo(new BigDecimal("0.50"));

/*
        assertThat(testedConvertor.convert(czk, eur, new BigDecimal("10")).isEqualTo(new BigDecimal("26545")));
        assertThat(testedConvertor.convert(eur, czk, new BigDecimal("137.41")).isEqualTo(new BigDecimal("26545")));

        // Don't forget to test border values and proper rounding.
        fail("Test is not implemented yet.");
*/

    }

    @Test
    public void testConvertWithNullSourceCurrency() {
        //expect
        Throwable thrown = catchThrowable(() -> { testedConvertor.convert(null, czk, new BigDecimal("1"));});

        //when
        assertThat(thrown).hasMessageContaining("IllegalArgumentException");
    }

    @Test
    public void testConvertWithNullTargetCurrency() {
        //expect
        Throwable thrown = catchThrowable(() -> { testedConvertor.convert(czk, null, new BigDecimal("1"));});

        //when
        assertThat(thrown).hasMessageContaining("IllegalArgumentException");
    }

    @Test
    public void testConvertWithNullSourceAmount() {
        //expect
        Throwable thrown = catchThrowable(() -> { testedConvertor.convert(czk, usd, null);});

        //when
        assertThat(thrown).hasMessageContaining("IllegalArgumentException");
    }

    @Test
    public void testConvertWithUnknownCurrency() throws ExternalServiceFailureException {
        when(mockExchangeRateTable.getExchangeRate(gel, czk)).thenReturn(null);

        //expect
        Throwable thrown = catchThrowable(() -> { testedConvertor.convert(gel, czk,  new BigDecimal("1"));});

        //when
        assertThat(thrown).hasMessageContaining("UnknownExchangeRateException");
    }

    @Test
    public void testConvertWithExternalServiceFailure() throws ExternalServiceFailureException {
        when(mockExchangeRateTable.getExchangeRate(gel, usd)).thenThrow(ExternalServiceFailureException.class);


        //expect
        Throwable thrown = catchThrowable(() -> { testedConvertor.convert(gel, usd,  new BigDecimal("1"));});

        //when
        assertThat(thrown).hasMessageContaining("UnknownExchangeRateException");    }

}
