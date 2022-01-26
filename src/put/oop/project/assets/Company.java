package put.oop.project.assets;

import put.oop.project.Provider;
import put.oop.project.World;

import java.util.*;

final public class Company implements Runnable {
    private String name;
    private Date ipoDate;
    private Double ipoShareValue;
    private Double openingPrice;
    private Double currentPrice;
    private Double minimalPrice;
    private Double maximalPrice;
    private Double profit;
    private Double revenue;
    private Double capital;
    private Double capitalization;
    private Double totalSales;
    private Integer tradingVolume;

    private Company(String name, Date ipoDate, Double ipoShareValue, Double openingPrice, Double currentPrice, Double minimalPrice, Double maximalPrice, Double revenue, Double profit, Double capital, Double capitalization) {
        this.name = name;
        this.ipoDate = ipoDate;
        this.ipoShareValue = ipoShareValue;
        this.revenue = revenue;
        this.profit = profit;
        this.capital = capital;
        this.currentPrice = currentPrice;
        this.openingPrice = openingPrice;
        this.minimalPrice = minimalPrice;
        this.maximalPrice = maximalPrice;
        this.capitalization = capitalization;
        this.totalSales = 0.0;
        this.tradingVolume = 0;
    }

    /** Create a new company with semi-random values
     * @return created company
     * @throws Exception
     */
    public static Company create() throws Exception {
        String name = Provider.Company.getName();

        Date ipoDate = Provider.Company.getIpoDate();
        Double ipoShareValue = Provider.Company.getIpoShareValue();
        Double openingPrice = Provider.Company.getOpeningPrice();
        Double currentPrice = openingPrice;
        Double minimalPrice = openingPrice;
        Double maximalPrice = openingPrice;
        Double revenue = Provider.Company.getRevenue();
        Double profit = Provider.Company.getProfit(revenue);
        Double capital = profit;
        Double capitalization = ipoShareValue;

        return new Company(name, ipoDate, ipoShareValue, openingPrice, currentPrice, minimalPrice, maximalPrice, revenue, profit, capital, capitalization);
    }

    /**
     * @return name of the company
     */
    public String getName() {
        return name;
    }

    /**
     * @return revenue of the company
     */
    public Double getRevenue() {
        return revenue;
    }

    /**
     * @return ipo date of the company
     */
    public Date getIpoDate() {
        return ipoDate;
    }

    /**
     * @return capital of the company
     */
    public Double getCapital() {
        return capital;
    }

    /**
     * @return profit of the company
     */
    public Double getProfit() {
        return profit;
    }

    /**
     * @return opening price of the company
     */
    public Double getOpeningPrice() {
        return openingPrice;
    }

    /**
     * @return ipo share value of the company
     */
    public Double getIpoShareValue() {
        return ipoShareValue;
    }

    /**
     * @return trading volume of the company
     */
    public Integer getTradingVolume() {
        return tradingVolume;
    }

    /**
     * @return current price of the company
     */
    public Double getCurrentPrice() {
        return currentPrice;
    }

    /**
     * @return minimal price of the company
     */
    public Double getMinimalPrice() {
        return minimalPrice;
    }

    /**
     * @return maximal price of the company
     */
    public Double getMaximalPrice() {
        return maximalPrice;
    }

    /**
     * @return total sales of the company
     */
    public Double getTotalSales() {
        return totalSales;
    }

    /**
     * @return capitalization of the company
     */
    public Double getCapitalization() {
        return capitalization;
    }


    /** Commit trading operation
     * @param amount number of share to be traded
     */
    public void commitTrading(Integer amount) {
        tradingVolume += amount;
        totalSales = Provider.roundTo2Places(totalSales + amount * currentPrice);
    }

    /** Set the price of the share
     * @param price price to be set
     */
    public void setCurrentPrice(Double price) {
        currentPrice = price;
        minimalPrice = Math.min(minimalPrice, price);
        maximalPrice = Math.max(maximalPrice, price);
    }

    /**
     * @return string representation of the company
     */
    public String toString() {
        return name + ": $" + currentPrice;
    }

    /**
     * Generate revenue and update profit and capitalization
     */
    private void generateRevenueAndProfit() {
        revenue = Provider.Company.getRevenue();
        profit = Provider.Company.getProfit(revenue);
        capital = Provider.roundTo2Places(capital + profit);
        capitalization = Provider.roundTo2Places(capitalization + profit);
        World.getInstance().getStockMarket().updateIndices();
    }

    /**
     * Increase total number of shares and tune current price
     */
    private void increaseTotalNumberOfShares() {
        Share share = getShare();
        Integer oldShareAmount = World.getInstance().getStockMarket().getAssetCount(share);
        Integer amount = getRandomShareAmount(share);
        World.getInstance().getStockMarket().increaseAsset(share, amount);
        // Decrease current price of share
        currentPrice = Provider.roundTo2Places(capitalization / (oldShareAmount + amount));
    }

    /** Buy out shares from the market
     * @param amount Amount of shares to buy out
     * @throws Exception
     */
    public void reclaimShares(Integer amount) throws Exception {
        Share share = getShare();
        Integer oldShareAmount = World.getInstance().getStockMarket().getAssetCount(share);
        if (amount > World.getInstance().getStockMarket().getAssetCount(share)) {
            throw new Exception("Specified amount of shares is greater than the total number of company's shares");
        }
        World.getInstance().getStockMarket().decreaseAsset(share, amount);
        // Increase current price of share
        currentPrice = Provider.roundTo2Places(capitalization / (oldShareAmount - amount));
    }

    /** Get random amount of the share
     * @param share to find
     * @return
     */
    private Integer getRandomShareAmount(Share share) {
        return Provider.randBetween(0, World.getInstance().getStockMarket().getAssetCount(share));
    }

    /** Creates and returns share
     * @return created share
     */
    private Share getShare() {
        String ticker = Provider.Company.getTickerByName(name);
        return new Share(ticker, this);
    }

    /**
     * Thread to:
     * 1. Randomly increase number of shares
     * 2. Generate revenue and profit
     */
    @Override
    public void run() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                randomlyIncreaseTotalNumberOfShares();
                generateRevenueAndProfit();
                System.out.println("Company " + getName() + " reported for the financial quarter");
            }
        }, 10 * 1000, 10 * 1000);
    }

    /**
     * Increase number of shares randomly
     */
    private void randomlyIncreaseTotalNumberOfShares() {
        if (Math.random() > 0.7) {
            increaseTotalNumberOfShares();
            System.out.println("Company " + getName() + " increased the total number of shares");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return name.equals(company.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}