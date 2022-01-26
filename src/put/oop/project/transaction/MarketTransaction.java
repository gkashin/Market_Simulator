package put.oop.project.transaction;

import put.oop.project.assets.Asset;
import put.oop.project.markets.Market;

public class MarketTransaction extends Transaction {
    private Market market;

    public MarketTransaction(Market market, TradingOperation tradingOperation) {
        super(tradingOperation);
        this.market = market;
    }

    /**
     * Changes asset count of market
     */
    @Override
    public void changeAssetCount() {
        TradingOperationType type = tradingOperation.getType();
        Asset asset = tradingOperation.getAsset();
        Integer amount = tradingOperation.getAssetAmountActual();
        if (type == TradingOperationType.BUY) {
            market.decreaseAsset(asset, amount);
        } else if (type == TradingOperationType.SELL) {
            market.increaseAsset(asset, amount);
        }
    }
}
