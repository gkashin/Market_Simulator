package put.oop.project.trader;

import put.oop.project.assets.Asset;
import put.oop.project.Provider;

import java.util.ArrayList;
import java.util.List;

public class Fund extends Trader {
    private String managerFirstName;
    private String managerLastName;

    private Fund(String name, String managerFirstName, String managerLastName, Double investmentBudgetInDollars) {
        super(investmentBudgetInDollars);
        this.name = name;
        this.managerFirstName = managerFirstName;
        this.managerLastName = managerLastName;
    }

    /**
     * @return full name of the manager
     */
    public String getManagerFullName() {
        return managerFirstName + " " + managerLastName;
    }

    /**
     * @return string representation of the fund
     */
    public String toString() {
        return name + ": $" + investmentBudgetInDollars;
    }

    /** Create a fund with semi-random values
     * @return created fund
     * @throws Exception
     */
    public static Fund create() throws Exception {
        String name;
        name = Provider.Fund.getName();
        String managerFirstName = Provider.Fund.getManagerFirstName();
        String managerLastName = Provider.Fund.getManagerLastName();
        Double investmentBudgetInDollars = Provider.Fund.investmentBudgetInDollars();

        Fund fund = new Fund(name, managerFirstName, managerLastName, investmentBudgetInDollars);

        return fund;
    }

    /** Get random asset from the list
     * @return random asset from the list
     */
    public Asset getRandomAsset() {
        List<Asset> keys = new ArrayList<>(assets.keySet());
        return keys.get(Provider.randBetween(0, keys.size() - 1));
    }

    /** Get available amount of the asset
     * @param asset to count
     * @return amount of the asset
     */
    public Integer getAssetCount(Asset asset) { return assets.get(asset); }

    /** Sell the amount of the asset
     * @param asset to sell
     * @param amount to sell
     */
    public void sellAsset(Asset asset, Integer amount) {
        assets.put(asset, assets.get(asset) - amount);
        investmentBudgetInDollars = Provider.roundTo2Places(investmentBudgetInDollars + amount * getAssetCostInDollars(asset));
    }

    /** But the amount of the asset
     * @param asset to buy
     * @param amount to buy
     */
    public void buyAsset(Asset asset, Integer amount) {
        assets.putIfAbsent(asset, 0);
        assets.put(asset, assets.get(asset) + amount);
        investmentBudgetInDollars = Provider.roundTo2Places(investmentBudgetInDollars - amount * getAssetCostInDollars(asset));
    }
}
