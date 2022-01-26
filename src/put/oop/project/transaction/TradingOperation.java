package put.oop.project.transaction;

import put.oop.project.assets.Asset;

public class TradingOperation {
    private TradingOperationType type;
    private Asset asset;
    private Integer assetAmountActual;

    public TradingOperation(TradingOperationType type, Asset asset, Integer assetAmountActual) {
        this.type = type;
        this.asset = asset;
        this.assetAmountActual = assetAmountActual;
    }

    /**
     * @return type of trading operation
     */
    public TradingOperationType getType() {
        return type;
    }

    /**
     * @return asset of trading operation
     */
    public Asset getAsset() {
        return asset;
    }

    /**
     * @return amount of asset to be operated
     */
    public Integer getAssetAmountActual() {
        return assetAmountActual;
    }
}
