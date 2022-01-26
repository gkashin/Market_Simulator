package put.oop.project;

import put.oop.project.assets.*;
import put.oop.project.assets.Currency;
import put.oop.project.markets.ForexMarket;
import put.oop.project.markets.Market;
import put.oop.project.trader.Fund;
import put.oop.project.trader.Investor;

import java.util.*;
import java.util.stream.Collectors;

public class World {
    private static World instance;
    final private ForexMarket forexMarket;
    final private Market<Share> stockMarket;
    final private Market<Commodity> commodityMarket;

    private PriceRegulator priceRegulator;

    private Double bullToBearRatio;

    private List<Investor> investors;
    private List<Fund> funds;
    private Map<String, Company> companies;

    private World() {
        stockMarket = new Market<>("Stock Market", "UK", "London", "10 Paternoster Square", null);
        commodityMarket = new Market<>("Commodity Market", "USA", "Washington", "Three Lafayette Centre 1155 Twenty-First Street NW", null);
        forexMarket = ForexMarket.getInstance();
        investors = new ArrayList<>();
        companies = new HashMap<>();
        funds = new ArrayList<>();
        bullToBearRatio = 0.9;
        priceRegulator = new PriceRegulator();
    }

    public static World getInstance() {
        if (instance == null) {
            instance = new World();
        }
        return instance;
    }

    /** Set bull to bear ratio. It only matters if it's lower or greater than 1
     * @param bullToBearRatio
     */
    public void setBullToBearRatio(Double bullToBearRatio) {
        this.bullToBearRatio = bullToBearRatio;
    }

    /** Get bull to bear ratio
     * @return bull to bear ratio
     */
    public Double getBullToBearRatio() {
        return bullToBearRatio;
    }

    /** Adds a new fund to the world
     * @throws Exception
     */
    public void addFund() throws Exception {
        Fund fund = Fund.create();
        funds.add(fund);
        addNewThreadWith(fund);
    }

    /**
     * Add a new investor to the world
     */
    public void addInvestor() {
        Investor investor = Investor.create();
        investors.add(investor);
        addNewThreadWith(investor);
    }

    /**
     * @return true if there are no companies in the world
     */
    public boolean companiesAreEmpty() {
        return companies.isEmpty();
    }

    /** Adds a new thread with some runnable
     * @param runnable
     */
    private void addNewThreadWith(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /** Adds asset to the market and trader according to the number of assets
     * @param assetType type of the asset
     * @throws Exception
     */
    public void addAsset(AssetType assetType) throws Exception {
        switch (assetType) {
            case COMMODITY:
                addCommodity();
                break;
            case SHARE:
                addCompany();
                break;
            case CURRENCY:
                addCurrency();
                break;
        }
        addTrader();
    }

    /** Adds a company to the market
     * @throws Exception
     */
    public void addCompany() throws Exception {
        Company company = Company.create();
        companies.put(company.getName(), company);
        addSharesFor(company);
        addNewThreadWith(company);
    }

    /** Add index of specified type. If type is CUSTOM, company names should be provided
     * @param type type of the index
     * @param companyNames name of the companies to be added to the index or null
     * @throws Exception
     */
    public void addIndex(IndexType type, List<String> companyNames) throws Exception {
        stockMarket.addIndex(Index.create(type, companyNames));
    }

    /** Get company names
     * @return set of company names
     */
    public Set<String> getCompanyNames() {
        return companies.keySet();
    }

    /** Get top companies of specified number to return
     * @param number of companies to return
     * @return list of companies
     * @throws Exception
     */
    public List<Company> getTopCompanies(Integer number) throws Exception {
        if (companies.size() < number) {
            throw new Exception("Couldn't create a new index. Not enough companies");
        }
        return companies.values().stream()
                .sorted((c1, c2) -> (c1.getCapitalization() - c2.getCapitalization()) > 0 ? 1 : -1)
                .collect(Collectors.toList())
                .subList(0, number);
    }

    /** Checks if the company exists or not
     * @param companyName
     * @return true if the company exists
     */
    public boolean isCompanyExists(String companyName) {
        if (companies == null) { return false; }
        return companies.containsKey(companyName);
    }

    /** Get number of shares for the company
     * @param companyName
     * @return number of shares of company
     */
    public Integer getNumberOfSharesFor(String companyName) {
        Share share = new Share(Provider.Company.getTickerByName(companyName), companies.get(companyName));
        return stockMarket.getAssetCount(share);
    }

    /** Adds shares for the company
     * @param company for which shares should be added
     */
    private void addSharesFor(Company company) {
        Share share = getShareFor(company);

        Integer numberOfShares = (int)(company.getIpoShareValue() / company.getOpeningPrice());
        stockMarket.addAsset(share, numberOfShares);
    }

    /** Force the company to buy out its shares
     * @param companyName
     * @param amount of shares to buy out
     * @throws Exception
     */
    public void forceBuyOutShares(String companyName, Integer amount) throws Exception {
        Company company = companies.get(companyName);
        company.reclaimShares(amount);
        System.out.println(companyName + " bought " + amount + " shares");
    }

    /** Create a share for the company
     * @param company
     * @return created share
     */
    private Share getShareFor(Company company) {
        String ticker = Provider.Company.getTickerByName(company.getName());
        return new Share(ticker, company);
    }

    /** Adds a new commodity on the market
     * @throws Exception
     */
    public void addCommodity() throws Exception {
        Integer numberOfPieces = Provider.randBetween(1, 100);
        commodityMarket.addAsset(Commodity.create(), numberOfPieces);
    }

    /** Adds a new currency of the market
     * @throws Exception
     */
    public void addCurrency() throws Exception {
        put.oop.project.assets.Currency currency = Currency.create();
        forexMarket.addCurrency(Provider.Currency.getCurrencyTypeBy(currency.getName()), currency);
    }

    /** Make buy transaction
     * @param asset to buy
     * @param amount to buy
     */
    public void commitBuy(Asset asset, Integer amount) {
        priceRegulator.addBoughtAsset(asset, amount);
    }

    /** Make sell transaction
     * @param asset to sell
     * @param amount to sell
     */
    public void commitSell(Asset asset, Integer amount) {
        priceRegulator.addSoldAsset(asset, amount);
    }

    /** Get random market from the world
     * @return random market
     */
    public Market getRandomMarket() {
        return Math.random() > 0.5 ? stockMarket : commodityMarket;
    }

    /** Get random fund from the world
     * @return random fund
     */
    public Fund getRandomFund() {
        if (funds.isEmpty()) { return null; }
        List<Fund> fundList = new ArrayList<>(funds);
        return fundList.get(Provider.randBetween(0, fundList.size() - 1));
    }

    /** Get stock market
     * @return stock market
     */
    public Market getStockMarket() {
        return stockMarket;
    }

    /** Get commodity market
     * @return commodity market
     */
    public Market getCommodityMarket() {
        return commodityMarket;
    }

    /** Get currency market
     * @return currency market
     */
    public ForexMarket getForexMarket() { return forexMarket; }

    /** Get the total number of assets of all markets
     * @return total number of assets
     */
    public Integer getNumberOfAssets() {
        Integer total = stockMarket.getNumberOfAssets();
        total += commodityMarket.getNumberOfAssets();
        total += forexMarket.getNumberOfCurrencies();
        return total;
    }

    /** Get all the investors
     * @return list of investors
     */
    public List<Investor> getInvestors() {
        return investors;
    }

    /** Get all the funds
     * @return list of funds
     */
    public List<Fund> getFunds() {
        return funds;
    }

    /** Get company by its name
     * @param name of the company
     * @return company
     */
    public Company getCompany(String name) {
        return companies.get(name);
    }

    /**
     * Notify the world that its assets have been changed
     */
    public void assetsChanged() {
        try {
            addTrader();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /** Get total number of traders in the world
     * @return total number of traders
     */
    private Integer getNumberOfTraders() {
        return funds.size() + investors.size();
    }

    /** Add a new trader to the world
     * @throws Exception
     */
    private void addTrader() throws Exception {
        Integer totalNumberOfAssets = getNumberOfAssets();
        Integer numberOfTraders = getNumberOfTraders();

        Double tradersToAssetsRatio = 0.01 / (numberOfTraders + 1);
        if (numberOfTraders < tradersToAssetsRatio * totalNumberOfAssets) {
            if (Math.random() > 0.7 && Provider.Fund.canAddFund()) {
                addFund();
            } else {
                addInvestor();
            }
        }
    }
}
