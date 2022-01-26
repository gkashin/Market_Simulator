package put.oop.project.markets;

import put.oop.project.Provider;
import put.oop.project.assets.Currency;
import put.oop.project.assets.CurrencyType;

import java.util.*;

final public class ForexMarket {
    private static ForexMarket instance;

    private Map<CurrencyType, Map<CurrencyType, Double>> exchangeRates;
    private Map<CurrencyType, put.oop.project.assets.Currency> currencies;

    private ForexMarket() {
        currencies = new HashMap<>();
        exchangeRates = Provider.Currency.getExchangeRates();
    }

    public static ForexMarket getInstance() {
        if (instance == null) {
            instance = new ForexMarket();
        }
        return instance;
    }

    /**
     * @return number of currencies
     */
    public Integer getNumberOfCurrencies() {
        return currencies.size();
    }

    /** Get exchange rate from one currency to another
     * @param fromType type from which to convert
     * @param toType type to which to convert
     * @return
     */
    public Double getExchangeRate(CurrencyType fromType, CurrencyType toType) {
        return exchangeRates.get(fromType).get(toType);
    }

    /** Get currency by its type
     * @param type of the currency
     * @return currency corresponding to the type
     */
    public put.oop.project.assets.Currency getCurrency(CurrencyType type) {
        return currencies.get(type);
    }

    /** Add currency to the map by its type
     * @param currencyType type of the currency
     * @param currency itself
     */
    public void addCurrency(CurrencyType currencyType, put.oop.project.assets.Currency currency) {
        currencies.put(currencyType, currency);
    }

    /**
     * @return all the currency types stored
     */
    public Set<CurrencyType> getCurrencyTypes() {
        return currencies.keySet();
    }

    /**
     * @return all currencies stored
     */
    public List<put.oop.project.assets.Currency> getCurrencies() {
        return new ArrayList<Currency>(currencies.values());
    }
}
