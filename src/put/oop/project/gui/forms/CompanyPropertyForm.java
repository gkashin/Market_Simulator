package put.oop.project.gui.forms;

import put.oop.project.assets.Company;
import put.oop.project.Provider;
import put.oop.project.World;
import put.oop.project.gui.GraphPanel;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class CompanyPropertyForm extends JFrame {
    private JPanel propertyPanel;
    private JLabel nameLabel;
    private JLabel currentPriceLabel;
    private JLabel openingPriceLabel;
    private JLabel minimalPriceLabel;
    private JLabel maximalPriceLabel;
    private JLabel profitLabel;
    private JLabel revenueLabel;
    private JLabel capitalLabel;
    private JLabel capitalizationLabel;
    private JLabel ipoShareValueLabel;
    private JLabel totalSalesLabel;
    private JLabel tradingVolumeLabel;
    private JLabel companyNameLabel;
    private JLabel companyCurrentPriceLabel;
    private JLabel companyOpeningPriceLabel;
    private JLabel companyMinimalPriceLabel;
    private JLabel companyMaximalPriceLabel;
    private JLabel companyProfitLabel;
    private JLabel companyRevenueLabel;
    private JLabel companyCapitalLabel;
    private JLabel companyCapitalizationLabel;
    private JLabel companyIpoShareValueLabel;
    private JLabel companyTotalSalesLabel;
    private JLabel companyTradingVolumeLabel;
    private JLabel ipoDateLabel;
    private JLabel companyIpoDateLabel;
    private JButton showPricesOverTimeButton;

    public CompanyPropertyForm(Company company) {
        super("Company Property Form");

        this.setContentPane(propertyPanel);
        this.pack();

        setupUI(company);
        showPricesOverTimeButton.addActionListener(e -> {
            String shareTicker = Provider.Company.getTickerByName(company.getName());
            List<Double> prices = World.getInstance().getStockMarket().getPricesForAsset(shareTicker);
            SwingUtilities.invokeLater(() -> GraphPanel.createAndShowGui(Arrays.asList(prices)));
        });
    }

    private void setupUI(Company company) {
        setBounds(100, 100, 300, 400);
        companyNameLabel.setText(company.getName());
        companyCurrentPriceLabel.setText("$" + company.getCurrentPrice());
        companyMaximalPriceLabel.setText("$" + company.getMaximalPrice());
        companyMinimalPriceLabel.setText("$" + company.getMinimalPrice());
        companyOpeningPriceLabel.setText("$" + company.getOpeningPrice());
        companyProfitLabel.setText("$" + company.getProfit());
        companyRevenueLabel.setText("$" + company.getRevenue());
        companyCapitalLabel.setText("$" + company.getCapital());
        companyCapitalizationLabel.setText("$" + company.getCapitalization());
        companyIpoShareValueLabel.setText("$" + company.getIpoShareValue());
        companyTotalSalesLabel.setText("$" + company.getTotalSales());
        companyTradingVolumeLabel.setText(String.valueOf(company.getTradingVolume()));
        companyIpoDateLabel.setText(String.valueOf(company.getIpoDate()));
    }
}
