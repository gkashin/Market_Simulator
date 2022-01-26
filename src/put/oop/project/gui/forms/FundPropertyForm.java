package put.oop.project.gui.forms;

import put.oop.project.assets.Asset;
import put.oop.project.trader.Fund;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

public class FundPropertyForm extends JFrame {
    private JLabel nameLabel;
    private JLabel fundNameLabel;
    private JLabel managerNameLabel;
    private JLabel fundInvestmentBudgetLabel;
    private JList assetList;
    private JPanel propertyPanel;

    public FundPropertyForm(Fund fund) {
        super("Fund Property Form");

        this.setContentPane(propertyPanel);
        this.pack();

        setupUI(fund);
    }

    private void setupUI(Fund fund) {
        setBounds(100, 100, 300, 400);
        fundNameLabel.setText(fund.getName());
        fundInvestmentBudgetLabel.setText("$" + fund.getInvestmentBudget());
        managerNameLabel.setText(fund.getManagerFullName());
        Map<Asset, Integer> assets = fund.getAssets();
        Set<Asset> assetSet = assets.keySet();
        List<String> stringAssets = assetSet.stream().map(asset -> asset.getName() + ": " + assets.get(asset)).collect(Collectors.toList());
        assetList.setListData(new Vector(stringAssets));
    }
}
