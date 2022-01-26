package put.oop.project.gui.forms;

import put.oop.project.assets.Company;
import put.oop.project.assets.Index;
import put.oop.project.World;

import javax.swing.*;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class IndexForm extends JFrame {
    private JLabel nameLabel;
    private JLabel valueLabel;
    private JList companyList;
    private JPanel indexPanel;

    public IndexForm(Index index) {
        super("Index Form");

        this.setContentPane(indexPanel);
        this.pack();
        setBounds(400, 400, 250, 400);

        setupUI(index);

        companyList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String companyName = (String)companyList.getSelectedValue();
                if (companyName == null) { return; }
                Company company = World.getInstance().getCompany(companyName);
                JFrame propertiesFormPanel = new CompanyPropertyForm(company);
                propertiesFormPanel.setVisible(true);
            }
        });
    }

    private void setupUI(Index index) {
        nameLabel.setText(index.getName());
        valueLabel.setText("$" + (index.getValue()));
        List<String> companyNames = index.getCompanies().stream().map(e -> e.getName()).collect(Collectors.toList());
        if (companyNames == null) { return; }
        companyList.setListData(new Vector(companyNames));
    }
}
