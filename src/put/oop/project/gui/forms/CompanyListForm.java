package put.oop.project.gui.forms;

import put.oop.project.World;
import put.oop.project.gui.utils.CheckboxListCellRenderer;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class CompanyListForm extends JFrame {
    private JList companyList;
    private JButton doneButton;
    private JPanel companyListPanel;

    public CompanyListForm() {
        super("Company List Form");
        this.setContentPane(companyListPanel);
        this.pack();

        setBounds(100, 100, 300, 400);

        List<String> companies = new ArrayList<>(World.getInstance().getCompanyNames());
        CheckboxListCellRenderer checkboxListCellRenderer = new CheckboxListCellRenderer();
        companyList.setCellRenderer(checkboxListCellRenderer);
        companyList.setListData(new Vector(companies));

        doneButton.addActionListener(e -> {
            List<String> companyNames = Arrays.stream(companyList.getSelectedIndices()).mapToObj(index -> companies.get(index)).collect(Collectors.toList());
            ControlPanel.companyNames = companyNames;
            dispose();
        });
    }
}
