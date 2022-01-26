package put.oop.project.gui.forms;

import javax.swing.*;
import java.util.List;

public class CurrencyForm extends JFrame {
    private JPanel currencyPanel;
    private JLabel countriesLabel;

    public CurrencyForm(List<String> countries) {
        super("Currency Form");

        this.setContentPane(currencyPanel);
        this.pack();
        setBounds(400, 400, 250, 250);

        countriesLabel.setText(convertToString(countries));
    }

    private String convertToString(List<String> list) {
        return "<html>" + String.join("<br/>", list) + "</html>";
    }
}
