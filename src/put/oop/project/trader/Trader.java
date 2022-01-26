package put.oop.project.trader;

import put.oop.project.*;
import put.oop.project.assets.Asset;
import put.oop.project.assets.Commodity;
import put.oop.project.assets.CurrencyType;
import put.oop.project.assets.Share;
import put.oop.project.markets.ForexMarket;
import put.oop.project.markets.Market;
import put.oop.project.transaction.*;

import java.util.*;
import java.util.concurrent.Semaphore;

public class Trader implements Runnable {
    protected Map<Asset, Integer> assets;
    protected Double investmentBudgetInDollars;
    protected String name;
    private Semaphore semaphore;

    public Trader(Double investmentBudgetInDollars) {
        assets = new HashMap<>();
        this.investmentBudgetInDollars = investmentBudgetInDollars;
        semaphore = new Semaphore(1);
    }

    public void run() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                randomlyIncreaseBudget();
                TransactionType transactionType = generateTransactionType();
                if (semaphore.tryAcquire()) {
                    generateTransaction(transactionType);
                }
                semaphore.release();
                System.out.println("Transaction " + transactionType.toString());
            }
        }, 2 * 1000, 2 * 1000);
    }

    /** Get assets
     * @return assets of this trader
     */
    public Map<Asset, Integer> getAssets() {
        return assets;
    }

    /** Generate a market transaction of specified type
     * @param tradingOperationType type of trading operation
     * @return created market transaction
     */
    private MarketTransaction generateMarketTransaction(TradingOperationType tradingOperationType) {
        Market<? extends Asset> market = null;
        TradingOperation tradingOperation;
        Asset asset = null;
        Integer assetAmountActual = 0;
        if (tradingOperationType == TradingOperationType.BUY) {
            market = World.getInstance().getRandomMarket();
            if (market.assetsAreEmpty()) { return null; }
            asset = market.getRandomAsset();
            Integer assetToBuyAmountAvailable = market.getAssetCount(asset);
            assetAmountActual = getAmountOperated(tradingOperationType, asset, assetToBuyAmountAvailable);
        } else if (tradingOperationType == TradingOperationType.SELL) {
            if (assets.isEmpty()) { return null; }
            asset = getRandomAsset();
            market = getMarketOfAsset(asset);
            assetAmountActual = getAmountToSell(asset);
        }
        tradingOperation = new TradingOperation(tradingOperationType, asset, assetAmountActual);
        return new MarketTransaction(market, tradingOperation);
    }

    /** Get market of the asset
     * @param asset
     * @return market for the asset
     */
    private Market getMarketOfAsset(Asset asset) {
        if (asset instanceof Share) {
            return World.getInstance().getStockMarket();
        } else if (asset instanceof Commodity) {
            return World.getInstance().getCommodityMarket();
        }
        return null;
    }

    /** Generate a fund transaction of specified type
     * @param tradingOperationType
     * @return created fund transaction of specified type
     */
    private FundTransaction generateFundTransaction(TradingOperationType tradingOperationType) {
        Fund fund = World.getInstance().getRandomFund();
        if (fund == null) { return null; }
        Asset asset = null;
        TradingOperation tradingOperation;
        Integer assetAmountActual = 0;
        if (tradingOperationType == TradingOperationType.BUY) {
            if (fund.assets.isEmpty()) { return null; }
            asset = fund.getRandomAsset();
            Integer assetToBuyAmountAvailable = fund.getAssetCount(asset);
            assetAmountActual = getAmountOperated(tradingOperationType, asset, assetToBuyAmountAvailable);
        } else if (tradingOperationType == TradingOperationType.SELL) {
            if (assets.isEmpty()) { return null; }
            asset = getRandomAsset();
            assetAmountActual = getAmountToSell(asset);
        }
        tradingOperation = new TradingOperation(tradingOperationType, asset, assetAmountActual);
        return new FundTransaction(fund, tradingOperation);
    }

    /** Generate transaction of specified type
     * @param transactionType
     */
    public void generateTransaction(TransactionType transactionType) {
        TradingOperationType tradingOperationType = generateTradingOperationType();
        Asset asset = null;
        Integer amount = 0;
        if (transactionType == TransactionType.MARKET) {
            MarketTransaction marketTransaction = generateMarketTransaction(tradingOperationType);
            if (marketTransaction == null) { return; }
            amount = marketTransaction.commit();
            asset = marketTransaction.getTradingOperation().getAsset();
        } else if (transactionType == TransactionType.FUND) {
            FundTransaction fundTransaction = generateFundTransaction(tradingOperationType);
            if (fundTransaction == null) { return; }
            amount = fundTransaction.commit();
            asset = fundTransaction.getTradingOperation().getAsset();
        }

        changeInvestmentBudgetAndAssets(tradingOperationType, asset, amount);
    }

    /** Change investment budget of trader and assets of market
     * @param tradingOperationType
     * @param asset operated
     * @param amount operated
     */
    public void changeInvestmentBudgetAndAssets(TradingOperationType tradingOperationType, Asset asset, Integer amount) {
        Double assetCostInDollars = getAssetCostInDollars(asset);
        if (tradingOperationType == TradingOperationType.BUY) {
            World.getInstance().commitBuy(asset, amount);
            assets.put(asset, assets.getOrDefault(asset, 0) + amount);
            investmentBudgetInDollars = Provider.roundTo2Places(investmentBudgetInDollars - amount * assetCostInDollars);
        } else if (tradingOperationType == TradingOperationType.SELL) {
            World.getInstance().commitSell(asset, amount);
            assets.put(asset, assets.get(asset) - amount);
            if (assets.get(asset) == 0) { assets.remove(asset); }
            investmentBudgetInDollars = Provider.roundTo2Places(investmentBudgetInDollars + amount * assetCostInDollars);
        }
    }

    /** Randomly generate BUY or SELL operation
     * @return generated type of trading operation
     */
    private TradingOperationType generateTradingOperationType() {
        TradingOperationType tradingOperationType;
        if (World.getInstance().getBullToBearRatio() > 1) {
            if (Math.random() < 0.75) {
                tradingOperationType = TradingOperationType.BUY;
            } else {
                tradingOperationType = TradingOperationType.SELL;
            }
        } else {
            if (Math.random() < 0.75) {
                tradingOperationType = TradingOperationType.SELL;
            } else {
                tradingOperationType = TradingOperationType.BUY;
            }
        }
        return tradingOperationType;
    }

    /** Get random number of the asset to buy
     * @param assetCostInDollars
     * @param assetTotalCount total count of this asset
     * @return amount of asset to buy
     */
    private Integer getAmountToBuy(Double assetCostInDollars, Integer assetTotalCount) {
        Integer maxAmount = Math.min(assetTotalCount, (int)Math.floor(investmentBudgetInDollars / assetCostInDollars));
        return Provider.randBetween(0, maxAmount);
    }

    /** Get random amount of asset to sell
     * @param asset to sell
     * @return number of asset to sell
     */
    private Integer getAmountToSell(Asset asset) {
        if (assets.isEmpty()) { return 0; }
        return Provider.randBetween(0, assets.get(asset));
    }

    /** Get random amount of asset to operate
     * @param tradingOperationType
     * @param asset to operate
     * @param assetTotalCount
     * @return number of asset to operate
     */
    private Integer getAmountOperated(TradingOperationType tradingOperationType, Asset asset, Integer assetTotalCount) {
        Double assetCostInDollars = getAssetCostInDollars(asset);
        if (tradingOperationType == TradingOperationType.BUY) {
            return getAmountToBuy(assetCostInDollars, assetTotalCount);
        } else if (tradingOperationType == TradingOperationType.SELL) {
            return getAmountToSell(asset);
        }
        return 0;
    }

    /** Get name of the trader
     * @return name of the trader
     */
    public String getName() {
        return name;
    }

    /** Get investment budget in dollars
     * @return investment budget in dollars
     */
    public Double getInvestmentBudget() {
        return investmentBudgetInDollars;
    }

    /** Get cost of the asset in dollars
     * @param asset which cost to return
     * @return cost of the asset
     */
    public Double getAssetCostInDollars(Asset asset) {
        Double assetCost = asset.getCurrentPrice();
        Double exchangeRate = ForexMarket.getInstance().getExchangeRate(asset.getTradingCurrencyType(), CurrencyType.USD);
        return Provider.roundTo2Places(assetCost * exchangeRate);
    }

    /** Get random asset
     * @return asset
     */
    private Asset getRandomAsset() {
        List<Asset> keys = new ArrayList<>(assets.keySet());
        return keys.get(Provider.randBetween(0, keys.size() - 1));
    }

    /** Generate a random transaction type
     * @return MARKET or FUND transaction type
     */
    private TransactionType generateTransactionType() {
        return Math.random() > 0.5 ? TransactionType.MARKET : TransactionType.FUND;
    }

    /**
     * Randomly increase a budget of trader
     */
    private void randomlyIncreaseBudget() {
        if (Math.random() > 0.9) {
            Double amount = Provider.roundTo2Places(Math.random() * investmentBudgetInDollars);
            if (amount < 0) { amount *= -1; }
            investmentBudgetInDollars += amount;
            System.out.println("Trader " + name + " increased his budget by " + amount);
        }
    }
}
