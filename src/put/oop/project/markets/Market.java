package put.oop.project.markets;

import put.oop.project.assets.Index;
import put.oop.project.Provider;
import put.oop.project.World;
import put.oop.project.assets.Asset;
import put.oop.project.assets.Company;
import put.oop.project.assets.Share;
import put.oop.project.transaction.TradingOperationType;

import java.util.*;

final public class Market<T extends Asset> {
    private String name;
    private String country;
    private String city;
    private String address;
    private Map<TradingOperationType, Double> tradingOperationCosts;
    private List<Index> indices;
    private Map<T, Integer> assetsToCount;
    private Map<String, T> assetNamesToAsset;

    public Market(String name, String country, String city, String address,
                  Map<TradingOperationType, Double> tradingOperationCosts) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.address = address;
        this.tradingOperationCosts = tradingOperationCosts;

        indices = new ArrayList<>();
        assetsToCount = new HashMap<>();
        assetNamesToAsset = new HashMap<>();
    }

    /** Get random asset from this market
     * @return random asset of the market
     */
    public T getRandomAsset() {
        List<T> keys = new ArrayList<>(assetsToCount.keySet());
        return keys.get(Provider.randBetween(0, keys.size() - 1));
    }

    /** Get assetsToCount map
     * @return map from assets to their count
     */
    public Map<T, Integer> getAssetsToCount() {
        return assetsToCount;
    }

    /** Get number of asset available on the market
     * @param asset which count to return
     * @return amount of asset
     */
    public Integer getAssetCount(Asset asset) { return assetsToCount.get(asset); }

    /**
     * @return true if there are no assets on the market
     */
    public boolean assetsAreEmpty() {
        return assetsToCount.isEmpty();
    }

    /** Get all the assets available on the market
     * @return total number of assets available
     */
    public Integer getNumberOfAssets() {
        Integer amount = 0;
        for (T asset : assetsToCount.keySet()) {
            amount += assetsToCount.get(asset);
        }
        return amount;
    }

    /**
     * Update values of indices on the market
     */
    public void updateIndices() {
        indices.forEach(e -> e.updateValue());
    }

    /** Add new asset with specified amount
     * @param asset asset to add
     * @param amount amount of the asset to add
     */
    public void addAsset(T asset, Integer amount) {
        assetsToCount.put(asset, amount);
        assetNamesToAsset.put(asset.getName(), asset);
    }

    /** Add a new index to the market
     * @param index to add
     */
    public void addIndex(Index index) {
        indices.add(index);
    }

    /**
     * @return indices of the market
     */
    public List<Index> getIndices() {
        return indices;
    }

    /** Get prices over time for the asset
     * @param name of the asset
     * @return list of prices for the asset
     */
    public List<Double> getPricesForAsset(String name) {
        return assetNamesToAsset.get(name).getPricesOverTime();
    }

    /** Decrease the asset by amount
     * @param asset asset to decrease
     * @param amount amount to decrease
     */
    public void decreaseAsset(T asset, Integer amount) {
        companyTraded(asset, amount);
        assetsToCount.put(asset, assetsToCount.get(asset) - amount);
    }

    /** Increase the asset by amount
     * @param asset asset to increase
     * @param amount amount to increase
     */
    public void increaseAsset(T asset, Integer amount) {
        companyTraded(asset, amount);
        assetsToCount.put(asset, assetsToCount.getOrDefault(asset, 0) + amount);
        World.getInstance().assetsChanged();
    }

    /** If traded asset is of Share type - update company
     * @param asset asset traded
     * @param amount amount traded
     */
    private void companyTraded(T asset, Integer amount) {
        if (asset instanceof Share) {
            Company company = ((Share) asset).getCompany();
            company.commitTrading(amount);
        }
    }
}
