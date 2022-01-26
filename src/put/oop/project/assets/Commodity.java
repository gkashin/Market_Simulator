package put.oop.project.assets;

import put.oop.project.Provider;
import put.oop.project.trader.TradingUnit;

final public class Commodity extends Asset {
    private TradingUnit tradingUnit;
    private CurrencyType tradingCurrencyType;
    private Double currentPrice;
    private Double minimalPrice;
    private Double maximalPrice;

    private Commodity(String name, TradingUnit tradingUnit, CurrencyType tradingCurrencyType, Double currentPrice, Double minimalPrice, Double maximalPrice) {
        super(name, currentPrice);
        this.tradingUnit = tradingUnit;
        this.tradingCurrencyType = tradingCurrencyType;
        this.currentPrice = currentPrice;
        this.minimalPrice = minimalPrice;
        this.maximalPrice = maximalPrice;
    }

    /** Create a new commodity with semi-random values
     * @return created commodity
     * @throws Exception
     */
    public static Commodity create() throws Exception {
        CurrencyType tradingCurrencyType = Provider.Commodity.getCurrency();
        String name = Provider.Commodity.getName();
        TradingUnit tradingUnit = Provider.Commodity.getTradingUnit(name);
        Double currentPrice = Provider.Commodity.getCurrentPrice();
        Double minimalPrice = currentPrice;
        Double maximalPrice = currentPrice;

        Commodity commodity = new Commodity(name, tradingUnit, tradingCurrencyType, currentPrice, minimalPrice, maximalPrice);

        return commodity;
    }

    /**
     * @return trading currency type of commodity
     */
    public CurrencyType getTradingCurrencyType() {
        return tradingCurrencyType;
    }

    /**
     * @return current price of commodity
     */
    public Double getCurrentPrice() {
        return currentPrice;
    }

    /** Updates current, minimal and maximal prices
     * @param price price to be added
     */
    @Override
    public void setCurrentPrice(Double price) {
        super.setCurrentPrice(price);
        currentPrice = price;
        minimalPrice = Math.min(minimalPrice, price);
        maximalPrice = Math.max(maximalPrice, price);
    }

    /**
     * @return string representation of the object
     */
    public String toString() {
        return getName() + ": " + Provider.Currency.getSignBy(tradingCurrencyType) + currentPrice;
    }
}
