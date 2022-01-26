package put.oop.project.trader;

import put.oop.project.Provider;

import java.util.*;

public class Investor extends Trader {
    private UUID tradingId;

    private Investor(String firstName, String lastName, UUID tradingId, Double investmentBudgetInDollars) {
        super(investmentBudgetInDollars);
        this.name = firstName + " " + lastName;
        this.tradingId = tradingId;
    }

    /**
     * @return string representation of the investor
     */
    public String toString() {
        return name + ": $" + investmentBudgetInDollars;
    }

    /** Creates an investor with semi-random values
     * @return created investor
     */
    public static Investor create() {
        String firstName = Provider.Investor.getFirstName();
        String lastName = Provider.Investor.getLastName();
        UUID tradingId = Provider.Investor.getTradingId();
        Double investmentBudgetInDollars = Provider.Investor.investmentBudgetInDollars();
        return new Investor(firstName, lastName, tradingId, investmentBudgetInDollars);
    }
}
