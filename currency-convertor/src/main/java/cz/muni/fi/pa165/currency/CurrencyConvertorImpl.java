package cz.muni.fi.pa165.currency;

import java.math.BigDecimal;
import java.util.Currency;


/**
 * This is base implementation of {@link CurrencyConvertor}.
 *
 * @author petr.adamek@embedit.cz
 */
public class CurrencyConvertorImpl implements CurrencyConvertor {

    private final ExchangeRateTable exchangeRateTable;
    //private final Logger logger = LoggerFactory.getLogger(CurrencyConvertorImpl.class);

    public CurrencyConvertorImpl(ExchangeRateTable exchangeRateTable) {
        this.exchangeRateTable = exchangeRateTable;
    }

    @Override
    public BigDecimal convert(Currency sourceCurrency, Currency targetCurrency, BigDecimal sourceAmount) {
        if (sourceCurrency == null || targetCurrency == null || sourceAmount == null) throw new IllegalArgumentException();

        BigDecimal rate;

        try {
            rate = exchangeRateTable.getExchangeRate(sourceCurrency,targetCurrency);
        } catch (ExternalServiceFailureException e) {
            throw new UnknownExchangeRateException("external service failure!!!");
        }

        if (rate == null) throw new UnknownExchangeRateException("unknown exchange rate");

        //TODO rounding
        return rate.multiply(sourceAmount);


        //throw new UnsupportedOperationException("Not implemented yet.");
    }

}
