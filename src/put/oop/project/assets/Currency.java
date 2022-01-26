package put.oop.project.assets;

import put.oop.project.Provider;

import java.util.List;
import java.util.Objects;

final public class Currency extends Asset {
    private List<String> legalTenderCountries;

    private Currency(String name, List<String> legalTenderCountries) {
        super(name, 1.0);
        this.legalTenderCountries = legalTenderCountries;
    }

    /** Creates a new random currency
     * @return created currency
     * @throws Exception
     */
    public static Currency create() throws Exception {
        String name = Provider.Currency.getName();
        List<String> countries = Provider.Currency.getCountries(name);
        Currency currency = new Currency(name, countries);

        return currency;
    }

    /**
     * @return string representation of the object
     */
    public String toString() {
        return Provider.Currency.getCurrencyTypeBy(getName()).toString();
    }

    /**
     * @return tender countries for this currency
     */
    public List<String> getLegalTenderCountries() {
        return legalTenderCountries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return getName().equals(currency.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}