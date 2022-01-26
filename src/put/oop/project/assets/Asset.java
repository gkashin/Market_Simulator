package put.oop.project.assets;

import put.oop.project.PriceRegulator;
import put.oop.project.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

abstract public class Asset {
    private String name;
    protected List<Double> pricesOverTime;

    protected Asset(String name, Double currentPrice) {
        this.name = name;
        this.pricesOverTime = new ArrayList<>();
        this.pricesOverTime.add(currentPrice);
    }

    /**
     * @return name of the asset
     */
    public String getName() {
        return name;
    }

    /**
     * @return current price of the asset
     */
    public Double getCurrentPrice() { return 0.0; }

    /** Add price to the prices list
     * @param price price to be added
     */
    public void setCurrentPrice(Double price) {
        pricesOverTime.add(price);
    }

    /**
     * @return prices over time
     */
    public List<Double> getPricesOverTime() { return pricesOverTime; }

    /** Update current price of the asset by some ratio
     * @param ratio ratio for the price
     */
    public void updateCurrentPriceBy(Double ratio) {
        Double newCurrentPrice;
        if (ratio < 1) {
            // Price cannot be lower than minPrice
            newCurrentPrice = Math.max(PriceRegulator.minPrice, Provider.roundTo2Places(getCurrentPrice() * (1 - ratio)));
        } else if (ratio > 1) {
            newCurrentPrice = Provider.roundTo2Places(getCurrentPrice() * (1 + (ratio / 100)));
        } else {
            return;
        }
        setCurrentPrice(newCurrentPrice);
    }

    /**
     * @return trading currency type of the asset (USD by default)
     */
    public CurrencyType getTradingCurrencyType() {
        return CurrencyType.USD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return name.equals(asset.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}