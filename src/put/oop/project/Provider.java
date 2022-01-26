package put.oop.project;

import put.oop.project.assets.CurrencyType;
import put.oop.project.trader.TradingUnit;

import java.util.*;

public class Provider {
    public static class Company {
        private static Set<String> names = new HashSet<>(Arrays.asList("Apple", "Microsoft", "Google", "Meta", "Amazon", "Netflix", "Airbnb", "Tesla", "Yandex", "Accenture", "Nvidia", "Nike", "JPMorgan Chase", "Berkshire Hathaway", "Walmart"));
        private static Map<String, String> namesToTicker = new HashMap<>() {{
            put("Apple", "AAPL");
            put("Microsoft", "MSFT");
            put("Google", "GOOGL");
            put("Meta", "META");
            put("Amazon", "AMZN");
            put("Airbnb", "ABNB");
            put("Tesla", "TSLA");
            put("Yandex", "YNDX");
            put("Accenture", "ACN");
            put("Nvidia", "NVDA");
            put("Nike", "NKE");
            put("JPMorgan Chase", "JPM");
            put("Berkshire Hathaway", "BRK.A");
            put("Walmart", "WMT");
            put("Netflix", "NFLX");
        }};

        /** Get ticker of the company with name provided
         * @param name of the company
         * @return ticker of the company
         */
        public static String getTickerByName(String name) {
            return namesToTicker.get(name);
        }

        /** Get random name for the company
         * @return random name
         * @throws Exception
         */
        public static String getName() throws Exception {
            String companyName = "";
            Iterator<String> it = names.iterator();
            if (it.hasNext()) {
                companyName = it.next();
            } else {
                throw new Exception("Couldn't create a new company. There are no companies left");
            }
            names.remove(companyName);
            return companyName;
        }

        /** Get ipo date for the company
         * @return current date
         */
        public static Date getIpoDate() {
            return new Date();
        }

        /** Get random ipo share value for the company
         * @return ipo share value
         */
        public static Double getIpoShareValue() {
            return roundTo2Places(Math.random() * 10000);
        }

        /** Get random opening price of the company share
         * @return opening price of the company share
         */
        public static Double getOpeningPrice() {
            return roundTo2Places(Math.random() * 100);
        }


        /** Get random profit of the company depending on revenue
         * @param revenue from with profit will be calculated
         * @return profit of the company
         */
        public static Double getProfit(Double revenue) {
            Double randDouble = getRandomSignedNumber();
            return roundTo2Places(revenue * randDouble);
        }

        /** Get random revenue
         * @return revenue of the company
         */
        public static Double getRevenue() {
            return roundTo2Places(Math.random() * 1000);
        }
    }

    public static class Currency {
        private static Map<CurrencyType, Map<CurrencyType, Double>> exchangeRates = new HashMap<>() {{
            put(CurrencyType.USD, new HashMap<>() {{
                put(CurrencyType.EUR, 0.88);
                put(CurrencyType.RUB, 75.91);
                put(CurrencyType.USD, 1.0);
                put(CurrencyType.PLN, 4.03);
                put(CurrencyType.GBP, 0.74);
                put(CurrencyType.CHF, 0.92);
                put(CurrencyType.JPY, 114.0);
            }});
            put(CurrencyType.EUR, new HashMap<>() {{
                put(CurrencyType.USD, 1.13);
                put(CurrencyType.RUB, 85.79);
                put(CurrencyType.EUR, 1.0);
                put(CurrencyType.PLN, 4.56);
                put(CurrencyType.GBP, 0.84);
                put(CurrencyType.CHF, 1.04);
                put(CurrencyType.JPY, 129.0);
            }});
            put(CurrencyType.RUB, new HashMap<>() {{
                put(CurrencyType.EUR, 0.012);
                put(CurrencyType.USD, 0.013);
                put(CurrencyType.RUB, 1.0);
                put(CurrencyType.PLN, 0.053);
                put(CurrencyType.GBP, 0.01);
                put(CurrencyType.CHF, 0.01);
                put(CurrencyType.JPY, 1.0);
            }});
            put(CurrencyType.PLN, new HashMap<>() {{
                put(CurrencyType.EUR, 0.22);
                put(CurrencyType.RUB, 18.83);
                put(CurrencyType.PLN, 1.0);
                put(CurrencyType.USD, 0.25);
                put(CurrencyType.GBP, 0.18);
                put(CurrencyType.CHF, 0.23);
                put(CurrencyType.JPY, 28.02);
            }});
            put(CurrencyType.GBP, new HashMap<>() {{
                put(CurrencyType.EUR, 1.19);
                put(CurrencyType.RUB, 106.45);
                put(CurrencyType.PLN, 18.65);
                put(CurrencyType.USD, 1.35);
                put(CurrencyType.GBP, 1.0);
                put(CurrencyType.CHF, 1.24);
                put(CurrencyType.JPY, 154.0);
            }});
            put(CurrencyType.CHF, new HashMap<>() {{
                put(CurrencyType.EUR, 0.96);
                put(CurrencyType.RUB, 85.98);
                put(CurrencyType.PLN, 4.42);
                put(CurrencyType.USD, 1.09);
                put(CurrencyType.GBP, 0.81);
                put(CurrencyType.CHF, 1.0);
                put(CurrencyType.JPY, 124.0);
            }});
            put(CurrencyType.JPY, new HashMap<>() {{
                put(CurrencyType.EUR, 0.01);
                put(CurrencyType.RUB, 0.69);
                put(CurrencyType.PLN, 0.036);
                put(CurrencyType.USD, 0.01);
                put(CurrencyType.GBP, 0.01);
                put(CurrencyType.CHF, 0.01);
                put(CurrencyType.JPY, 1.0);
            }});
        }};
        private static Set<String> names = new HashSet<>(Arrays.asList("euro", "zloty", "dollar", "ruble", "yen", "pound", "franc"));
        private static Map<String, CurrencyType> nameToCurrencyType = new HashMap<>() {{
            put("euro", CurrencyType.EUR);
            put("zloty", CurrencyType.PLN);
            put("dollar", CurrencyType.USD);
            put("ruble", CurrencyType.RUB);
            put("yen", CurrencyType.JPY);
            put("pound", CurrencyType.GBP);
            put("franc", CurrencyType.CHF);
        }};
        private static Map<String, CurrencyType> stringTypeToName = new HashMap<>() {{
            put("EUR", CurrencyType.EUR);
            put("PLN", CurrencyType.PLN);
            put("USD", CurrencyType.USD);
            put("RUB", CurrencyType.RUB);
            put("JPY", CurrencyType.JPY);
            put("GBP", CurrencyType.GBP);
            put("CHF", CurrencyType.CHF);
        }};
        private static Map<CurrencyType, String> typeToSign = new HashMap<>() {{
            put(CurrencyType.EUR, "€");
            put(CurrencyType.PLN, "zł");
            put(CurrencyType.USD, "$");
            put(CurrencyType.RUB, "₽");
            put(CurrencyType.JPY, "¥");
            put(CurrencyType.GBP, "£");
            put(CurrencyType.CHF, "₣");
        }};
        private static Map<String, List<String>> currencyToCountries = new HashMap<String, List<String>>() {{
            put("euro", Arrays.asList("Germany", "France", "Spain", "Italy"));
            put("zloty", Arrays.asList("Poland"));
            put("dollar", Arrays.asList("USA"));
            put("ruble", Arrays.asList("Russia"));
            put("yen", Arrays.asList("Japan"));
            put("pound", Arrays.asList("Great Britain"));
            put("franc", Arrays.asList("Switzerland"));
        }};

        /** Get name of the currency
         * @return random name of the currency
         * @throws Exception
         */
        public static String getName() throws Exception {
            String currencyName = "";
            Iterator<String> it = names.iterator();
            if (it.hasNext()) {
                currencyName = it.next();
            } else {
                throw new Exception("Couldn't create a new currency. There are no currencies left");
            }
            names.remove(currencyName);
            return currencyName;
        }

        /** Get currency type by name
         * @param name of the currency
         * @return type of the currency
         */
        public static CurrencyType getCurrencyTypeBy(String name) {
            return nameToCurrencyType.get(name);
        }

        /** Get currency type by its string representation
         * @param stringType string representation of CurrencyType
         * @return currency type
         */
        public static CurrencyType getCurrencyType(String stringType) {
            return stringTypeToName.get(stringType);
        }

        /** Get sign representation of the currency
         * @param type of the currency
         * @return sign representation of the currency
         */
        public static String getSignBy(CurrencyType type) {
            return typeToSign.get(type);
        }

        /** Get countries where this currency is legal
         * @param name of the currency
         * @return list of legal tender countries
         */
        public static List<String> getCountries(String name) {
            return currencyToCountries.get(name);
        }

        /** Get exchange rates for currencies
         * @return exchange rates
         */
        public static Map<CurrencyType, Map<CurrencyType, Double>> getExchangeRates() {
            return exchangeRates;
        }
    }

    public static class Commodity {
        private static Set<String> names = new HashSet<>(Arrays.asList("Gold", "Coffee", "Wheat", "Corn", "Cocoa", "Platinum", "Palm Oil", "Sugar", "Cotton", "Ethanol"));
        private static Map<String, TradingUnit> commodityToTradingUnit = new HashMap<>() {{
            put("Gold", TradingUnit.OUNCE);
            put("Coffee", TradingUnit.KG);
            put("Wheat", TradingUnit.BU);
            put("Corn", TradingUnit.TONS);
            put("Cocoa", TradingUnit.TONS);
            put("Platinum", TradingUnit.OUNCE);
            put("Palm Oil", TradingUnit.KG);
            put("Sugar", TradingUnit.LB);
            put("Cotton", TradingUnit.LB);
            put("Ethanol", TradingUnit.GAL);
        }};

        /** Get random name of the commodity
         * @return name of the commodity
         * @throws Exception
         */
        public static String getName() throws Exception {
            String commodityName = "";
            Iterator<String> it = names.iterator();
            if (it.hasNext()) {
                commodityName = it.next();
            } else {
                throw new Exception("Couldn't create a new commodity. There are no commodities left");
            }
            names.remove(commodityName);
            return commodityName;
        }

        /** Get trading unit of the commodity
         * @param name of the commodity
         * @return trading unit of the commodity
         */
        public static TradingUnit getTradingUnit(String name) {
            return commodityToTradingUnit.get(name);
        }

        /** Get random current price for the commodity
         * @return current price for the commodity
         */
        public static Double getCurrentPrice() {
            return roundTo2Places(Math.random() * 100);
        }

        /** Get random currency of the commodity
         * @return currency of the commodity
         * @throws Exception
         */
        public static CurrencyType getCurrency() throws Exception {
            Set<CurrencyType> currencyTypes = World.getInstance().getForexMarket().getCurrencyTypes();
            if (currencyTypes.isEmpty()) {
                throw new Exception("Couldn't create a new commodity. There are no currencies");
            }
            List<CurrencyType> currencyTypeList = new ArrayList<>(currencyTypes);
            return currencyTypeList.get(Provider.randBetween(0, currencyTypeList.size() - 1));
        }
    }

    public static class Investor {
        private static List<String> firstNames = new ArrayList<>(Arrays.asList("John", "Warren", "Philip", "Benjamin", "Bill", "John", "Carl", "Peter", "George", "Michael", "Shark", "Dragon's", "The Wolf of", "Duck", "Big"));
        private static List<String> lastNames = new ArrayList<>(Arrays.asList("Bogle", "Buffett", "Fisher", "Graham", "Gross", "Templeton", "Icahn", "Lynch", "Soros", "Steinhardt", "Tank", "Den", "Wallstreet", "Tales", "Short"));

        /** Get first name for investor
         * @return first name of investor
         */
        public static String getFirstName() {
            return firstNames.get(randBetween(0, firstNames.size() - 1));
        }

        /** Get last name for the investor
         * @return last name of investor
         */
        public static String getLastName() {
            return lastNames.get(randBetween(0, lastNames.size() - 1));
        }

        /** Get UUID of investor
         * @return trading UUID for investor
         */
        public static UUID getTradingId() {
            return UUID.randomUUID();
        }

        /** Get random investment budget in dollars
         * @return investment budget in dollars
         */
        public static Double investmentBudgetInDollars() {
            return roundTo2Places(Math.random() * 100000);
        }
    }

    public static class Fund {
        private static Set<String> names = new HashSet<>(Arrays.asList("Baillie Gifford American", "Fundsmith Equity", "Rathbone Global Opportunities", "AlphaMark Fund", "GoodHaven Fund", "Pacific Funds High Income", "Palsori Inversiones", "Pantheon International", "Katana Capital", "Jacob Forward ETF"));
        private static List<String> firstNames = new ArrayList<>(Arrays.asList("Kaushik", "James", "Terry", "Tom", "Steven", "Michael", "Larry", "Robertson", "Jacob", "Kerschner"));
        private static List<String> lastNames = new ArrayList<>(Arrays.asList("Patel", "Thomson", "Smith", "Slater", "Hay", "Simon", "Pitkowsky", "Boyd", "Chervitz", "Childs"));

        /** Get random name for the fund
         * @return name of the fund
         * @throws Exception
         */
        public static String getName() throws Exception {
            String fundName = "";
            Iterator<String> it = names.iterator();
            if (it.hasNext()) {
                fundName = it.next();
            } else {
                throw new Exception("Couldn't create a new fund. There are no funds left");
            }
            names.remove(fundName);
            return fundName;
        }

        /** Check if there are free funds or not
         * @return true if fund can be added
         */
        public static Boolean canAddFund() {
            return !names.isEmpty();
        }

        /** Get first name for the manager
         * @return first name of manager
         */
        public static String getManagerFirstName() {
            return firstNames.get(randBetween(0, firstNames.size() - 1));
        }

        /** Get last name for the manager
         * @return last name of manager
         */
        public static String getManagerLastName() {
            return lastNames.get(randBetween(0, lastNames.size() - 1));
        }

        /** Get random investment budget in dollars of fund
         * @return investment budget in dollars of fund
         */
        public static Double investmentBudgetInDollars() {
            return roundTo2Places(Math.random() * 1000000);
        }
    }

    /** Get random integer between start and end
     * @param start number
     * @param end number
     * @return random integer value
     */
    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

    /** Round number by 2 places
     * @param number to round
     * @return rounded number
     */
    public static Double roundTo2Places(Double number) {
        return Math.round(number * 100) / 100.0;
    }

    /** Get random positive or negative number
     * @return random signed number
     */
    public static Double getRandomSignedNumber() {
        Double randDouble = Math.random();
        int sign = 1;
        if (randDouble > 0.5) {
            sign = -1;
        }
        return randDouble * sign;
    }

    public static class Index {
        private static Set<String> names = new HashSet<>(Arrays.asList("Invesco QQQ Trust ETF", "Shelton NASDAQ-100 Index Direct", "Fidelity ZERO Large Cap Index", "The Dow Jones Industrial Average", "The Standard & Poor's 500 Index", "The Nasdaq 100 Index", "The New York Stock Exchange Composite Index", "The Wilshire 5000 Total Market Index", "The Russell 2000 Index", "Vanguard S&P 500 ETF"));

        /** Get random name for the index
         * @return name of the index
         * @throws Exception
         */
        public static String getName() throws Exception {
            String indexName = "";
            Iterator<String> it = names.iterator();
            if (it.hasNext()) {
                indexName = it.next();
            } else {
                throw new Exception("Couldn't create a new index. There are no indexes left");
            }
            names.remove(indexName);
            return indexName;
        }
    }
}
