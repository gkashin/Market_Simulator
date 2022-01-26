package put.oop.project.transaction;

public class Transaction {
    protected TradingOperation tradingOperation;

    public Transaction(TradingOperation tradingOperation) {
        this.tradingOperation = tradingOperation;
    }

    /**
     * Changes asset count
     */
    public void changeAssetCount() {}

    /**
     * @return trading operation
     */
    public TradingOperation getTradingOperation() {
        return tradingOperation;
    }

    /** Commit transaction
     * @return operated amount of asset
     */
    public Integer commit() {
        Integer assetAmountActual = tradingOperation.getAssetAmountActual();
        if (assetAmountActual == 0) { return 0; }
        changeAssetCount();
        return assetAmountActual;
    }
}
