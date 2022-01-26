package put.oop.project.gui.forms;

import put.oop.project.assets.Asset;
import put.oop.project.trader.Investor;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

public class InvestorPropertyForm extends JFrame {
    private JPanel propertyPanel;
    private JLabel nameLabel;
    private JLabel investmentBudgetLabel;
    private JLabel investorNameLabel;
    private JLabel investorInvestmentBudgetLabel;
    private JLabel assetsLabel;
    private JList assetList;

    public InvestorPropertyForm(Investor investor) {
        super("Investor Property Form");

        this.setContentPane(propertyPanel);
        this.pack();

        setupUI(investor);
    }

    private void setupUI(Investor investor) {
        setBounds(100, 100, 300, 400);
        investorNameLabel.setText(investor.getName());
        investorInvestmentBudgetLabel.setText("$" + investor.getInvestmentBudget());
        Map<Asset, Integer> assets = investor.getAssets();
        Set<Asset> assetSet = assets.keySet();
        List<String> stringAssets = assetSet.stream().map(asset -> asset.getName() + ": " + assets.get(asset)).collect(Collectors.toList());
        assetList.setListData(new Vector(stringAssets));
    }
}
