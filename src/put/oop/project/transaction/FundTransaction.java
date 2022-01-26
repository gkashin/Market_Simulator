package put.oop.project.transaction;

import put.oop.project.assets.Asset;
import put.oop.project.trader.Fund;

public class FundTransaction extends Transaction {
    private Fund fund;

    public FundTransaction(Fund fund, TradingOperation tradingOperation) {
        super(tradingOperation);
        this.fund = fund;
    }

    /**
     * Changes asset count of fund
     */
    @Override
    public void changeAssetCount() {
        TradingOperationType type = tradingOperation.getType();
        Asset asset = tradingOperation.getAsset();
        Integer amount = tradingOperation.getAssetAmountActual();
        if (type == TradingOperationType.BUY) {
            fund.sellAsset(asset, amount);
        } else if (type == TradingOperationType.SELL) {
            fund.buyAsset(asset, amount);
        }
    }
}
