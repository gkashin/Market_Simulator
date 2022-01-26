package put.oop.project;

import put.oop.project.assets.Asset;

import java.util.HashMap;
import java.util.Map;

public class PriceRegulator {
    public final static Double minPrice = 0.01;
    private Map<Asset, Integer> amountBought;
    private Map<Asset, Integer> amountSold;
    private Map<Asset, Double> boughtToSoldRatio;

    public PriceRegulator() {
        amountBought = new HashMap<>();
        amountSold = new HashMap<>();
        boughtToSoldRatio = new HashMap<>();
    }

    /** Add the bought amount of the asset and update ratio
     * @param asset asset that has been bought
     * @param amount bought
     */
    public void addBoughtAsset(Asset asset, Integer amount) {
        amountBought.putIfAbsent(asset, 1);
        amountSold.putIfAbsent(asset, 1);
        amountBought.put(asset, amountBought.get(asset) + amount);
        updateRatio(asset);
    }

    /** Add the sold amount of the asset and update ratio
     * @param asset asset that has been sold
     * @param amount sold
     */
    public void addSoldAsset(Asset asset, Integer amount) {
        amountSold.putIfAbsent(asset, 1);
        amountBought.putIfAbsent(asset, 1);
        amountSold.put(asset, amountSold.get(asset) + amount);
        updateRatio(asset);
    }

    /** Update ratio for the asset
     * @param asset to update
     */
    private void updateRatio(Asset asset) {
        Double ratio = (double) amountBought.get(asset) / amountSold.get(asset);
        boughtToSoldRatio.put(asset, ratio);
        asset.updateCurrentPriceBy(ratio);
    }
}
