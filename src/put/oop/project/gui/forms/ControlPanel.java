package put.oop.project.gui.forms;

import put.oop.project.*;
import put.oop.project.assets.*;
import put.oop.project.assets.Currency;
import put.oop.project.gui.GraphPanel;
import put.oop.project.gui.utils.TextFieldDocumentListener;
import put.oop.project.trader.Fund;
import put.oop.project.trader.Investor;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

public class ControlPanel extends JFrame {
    public static List<String> companyNames;
    private JPanel controlPanel;
    private JTextField bullBearRatioTextField;
    private JButton setBullBearRatioButton;
    private JLabel bullBearRatioLabel;
    private JLabel bullBearLabel;
    private JButton addInvestorButton;
    private JButton addFundButton;
    private JButton addCompanyButton;
    private JButton addCurrencyButton;
    private JButton addCommodityButton;
    private JLabel investorsLabel;
    private JLabel fundsLabel;
    private JTextField sharesToBuyOutTextField;
    private JTextField companyNameTextField;
    private JButton forceButton;
    private JLabel availableNumberOfSharesLabel;
    private JList investorsList;
    private JLabel forceCompanyLabel;
    private JList fundsList;
    private JScrollPane investorsScrollPane;
    private JList companiesList;
    private JList currenciesList;
    private JList commoditiesList;
    private JButton addIndexButton;
    private JList indexList;
    private JRadioButton top5RadioButton;
    private JRadioButton top10RadioButton;
    private JRadioButton customButton;
    private ButtonGroup buttonGroup;

    public ControlPanel(String title) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(controlPanel);
        this.pack();

        setBounds(400, 400, 800, 800);

        bullBearRatioLabel.setText(String.valueOf(World.getInstance().getBullToBearRatio()));
        companyNameTextField.getDocument().addDocumentListener((TextFieldDocumentListener) e -> availableNumberOfSharesLabel());

        setupTimer();
        setupRadioButtons();
        addButtonActions();
        setupLists();
    }

    public static void main(String[] args) {
        JFrame jFrame = new ControlPanel("Market Simulator");
        jFrame.setVisible(true);
    }

    private void updateUI() {
        updateInvestors();
        updateCommodities();
        updateCompanies();
        updateFunds();
        updateCurrencies();
        updateIndices();
        availableNumberOfSharesLabel();
    }

    private void updateInvestors() {
        List<Investor> investors = World.getInstance().getInvestors();
        List<String> investorStrings = investors.stream().map(investor -> investor.toString()).collect(Collectors.toList());
        if (investorStrings == null) { return; }
        investorsList.setListData(new Vector(investorStrings));
    }

    private void updateFunds() {
        List<Fund> funds = World.getInstance().getFunds();
        List<String> fundStrings = funds.stream().map(fund -> fund.toString()).collect(Collectors.toList());
        if (fundStrings == null) { return; }
        fundsList.setListData(new Vector(fundStrings));
    }

    private void updateCommodities() {
        Map<Commodity, Integer> commodities = World.getInstance().getCommodityMarket().getAssetsToCount();
        List<String> commodityStrings = commodities.keySet().stream().map(commodity -> commodity.toString()).collect(Collectors.toList());
        if (commodityStrings == null) { return; }
        commoditiesList.setListData(new Vector(commodityStrings));
    }

    private void updateCurrencies() {
        List<Currency> currencies = World.getInstance().getForexMarket().getCurrencies();
        List<String> currencyStrings = currencies.stream().map(currency -> currency.toString()).collect(Collectors.toList());
        if (currencyStrings == null) { return; }
        currenciesList.setListData(new Vector(currencyStrings));
    }

    private void updateCompanies() {
        Map<Share, Integer> shares = World.getInstance().getStockMarket().getAssetsToCount();
        List<Company> companies = shares.keySet().stream().map(share -> share.getCompany()).collect(Collectors.toList());
        List<String> companyStrings = companies.stream().map(company -> company.toString()).collect(Collectors.toList());
        if (companyStrings == null) { return; }
        companiesList.setListData(new Vector(companyStrings));
    }

    private void updateIndices() {
        List<Index> indices = World.getInstance().getStockMarket().getIndices();
        List<String> indicesNames = indices.stream().map(index -> index.toString()).collect(Collectors.toList());
        if (indicesNames == null) { return; }
        indexList.setListData(new Vector(indicesNames));
    }

    private void availableNumberOfSharesLabel() {
        String companyName = companyNameTextField.getText();
        if (World.getInstance().isCompanyExists(companyName)) {
            Integer totalAmount = World.getInstance().getNumberOfSharesFor(companyName);
            availableNumberOfSharesLabel.setText("from 0 to " + totalAmount);
        }
    }

    private void showMessageDialog(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    private void setupTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateUI();
//                System.out.println("UI Updated");
            }
        }, 1 * 1000, 1 * 1000);
    }

    private void handleCompanyBuyOut(String companyName) {
        if (!World.getInstance().isCompanyExists(companyName)) {
            showMessageDialog("The company " + companyName +  " doesn't exist. Try a different one");
            return;
        }
        Integer numberOfSharesClaimed = 0;
        try {
            numberOfSharesClaimed = Integer.parseInt(sharesToBuyOutTextField.getText());
        } catch (Exception ex) {
            showMessageDialog("Amount of shares must be an integer number.");
            return;
        }
        if (numberOfSharesClaimed < 0) {
            showMessageDialog("Amount of shares can't be less than zero.");
            return;
        }
        Integer totalNumberOfShares = World.getInstance().getNumberOfSharesFor(companyName);
        if (numberOfSharesClaimed > totalNumberOfShares) {
            showMessageDialog("The claimed number of shares exceeds the total number.");
            return;
        }
        try {
            World.getInstance().forceBuyOutShares(companyName, numberOfSharesClaimed);
        } catch (Exception ex) {
            showMessageDialog(ex.getMessage());
            return;
        }
        clearTextFields();
        availableNumberOfSharesLabel.setText("Available number");
    }

    private void clearTextFields() {
        companyNameTextField.setText("");
        sharesToBuyOutTextField.setText("");
    }

    private void addButtonActions() {
        setBullBearRatioButton.addActionListener(e -> {
            Double bullToBearRatio = null;
            try {
                bullToBearRatio = Double.parseDouble(bullBearRatioTextField.getText());
            } catch (NumberFormatException ex) {
                showMessageDialog("You must provide a number");
            }
            World.getInstance().setBullToBearRatio(bullToBearRatio);
            bullBearRatioLabel.setText(String.valueOf(bullToBearRatio));
            bullBearRatioTextField.setText("");
        });

        addCompanyButton.addActionListener(e -> {
            try {
                World.getInstance().addAsset(AssetType.SHARE);
            } catch (Exception ex) {
                showMessageDialog(ex.getMessage());
            }
            updateCompanies();
        });

        addCurrencyButton.addActionListener(e -> {
            try {
                World.getInstance().addAsset(AssetType.CURRENCY);
            } catch (Exception ex) {
                showMessageDialog(ex.getMessage());
            }
            updateCurrencies();
        });

        addCommodityButton.addActionListener(e -> {
            try {
                World.getInstance().addAsset(AssetType.COMMODITY);
            } catch (Exception ex) {
                showMessageDialog(ex.getMessage());
            }
            updateCommodities();
        });

        forceButton.addActionListener(e -> {
            String companyName = companyNameTextField.getText();
            handleCompanyBuyOut(companyName);
        });

        addIndexButtonListener();
    }

    private void setupLists() {
        investorsList.setVisibleRowCount(10);
        investorsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Integer index = investorsList.getSelectedIndex();
                if (index < 0) { return; }
                Investor investor = World.getInstance().getInvestors().get(index);
                JFrame propertiesFormPanel = new InvestorPropertyForm(investor);
                propertiesFormPanel.setVisible(true);
            }
        });

        fundsList.setVisibleRowCount(10);
        fundsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Integer index = fundsList.getSelectedIndex();
                if (index < 0) { return; }
                Fund fund = World.getInstance().getFunds().get(index);
                JFrame propertiesFormPanel = new FundPropertyForm(fund);
                propertiesFormPanel.setVisible(true);
            }
        });

        companiesList.setVisibleRowCount(10);
        companiesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String companyString = (String)companiesList.getSelectedValue();
                String companyName = getNameOf(companyString);
                if (companyName == null) { return; }
                Company company = World.getInstance().getCompany(companyName);
                JFrame propertiesFormPanel = new CompanyPropertyForm(company);
                propertiesFormPanel.setVisible(true);
            }
        });

        commoditiesList.setVisibleRowCount(10);
        commoditiesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String commodityString = (String) commoditiesList.getSelectedValue();
                String commodityName = getNameOf(commodityString);
                if (commodityName == null) { return; }
                List<Double> prices = World.getInstance().getCommodityMarket().getPricesForAsset(commodityName);
                SwingUtilities.invokeLater(() -> GraphPanel.createAndShowGui(Arrays.asList(prices)));
            }
        });

        indexList.setVisibleRowCount(10);
        indexList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Integer i = indexList.getSelectedIndex();
                if (i < 0) { return; }
                Index index = (Index) World.getInstance().getStockMarket().getIndices().get(i);
                JFrame indexForm = new IndexForm(index);
                indexForm.setVisible(true);
            }
        });

        currenciesList.setVisibleRowCount(10);
        currenciesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String currencyName = (String) currenciesList.getSelectedValue();
                if (currencyName == null) { return; }
                CurrencyType type = Provider.Currency.getCurrencyType(currencyName);
                Currency currency = World.getInstance().getForexMarket().getCurrency(type);
                List<String> countries = currency.getLegalTenderCountries();
                JFrame currencyFormForm = new CurrencyForm(countries);
                currencyFormForm.setVisible(true);
            }
        });
    }

    private void setupRadioButtons() {
        buttonGroup = new ButtonGroup();
        buttonGroup.add(top5RadioButton);
        buttonGroup.add(top10RadioButton);
        buttonGroup.add(customButton);
    }

    private String getNameOf(String str) {
        if (str == null) { return null; }
        Integer index = str.indexOf(":");
        if (index == null) { return null; }
        return str.substring(0, index);
    }

    private void addIndexButtonListener() {
        addIndexButton.addActionListener(e -> {
            if (buttonGroup.getSelection() == null) {
                showMessageDialog("Please choose the index type");
            }
            if (top5RadioButton.isSelected()) {
                addIndex(IndexType.TOP_FIVE);
            } else if (top10RadioButton.isSelected()) {
                addIndex(IndexType.TOP_TEN);
            } else if (customButton.isSelected()) {
                if (World.getInstance().companiesAreEmpty()) {
                    showMessageDialog("There are no companies to create an index");
                    return;
                }
                JFrame companyListForm = new CompanyListForm();
                companyListForm.setVisible(true);
                companyListForm.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowDeactivated(WindowEvent e) {
                        super.windowDeactivated(e);
                        addIndex(IndexType.CUSTOM);
                    }
                });
            }
        });
    }

    private void addIndex(IndexType type) {
        try {
            World.getInstance().addIndex(type, companyNames);
        } catch (Exception ex) {
            showMessageDialog(ex.getMessage());
        }
    }
}

