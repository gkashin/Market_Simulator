package put.oop.project.assets;

import put.oop.project.Provider;
import put.oop.project.World;

import java.util.List;
import java.util.stream.Collectors;

final public class Index {
    private String name;
    private Double value;
    private List<Company> companies;

    private Index(String name, List<Company> companies) {
        this.name = name;
        this.companies = companies;
        this.value = Provider.roundTo2Places(companies.stream().map(e -> e.getCapitalization()).reduce(0.0, Double::sum));
    }

    /** Creates an index with semi-random values
     * @param type of the index
     * @param companyNames names of the companies for the CUSTOM type, null otherwise
     * @return created index
     * @throws Exception
     */
    public static Index create(IndexType type, List<String> companyNames) throws Exception {
        List<Company> companies = null;
        switch (type) {
            case TOP_FIVE:
                companies = World.getInstance().getTopCompanies(5);
                break;
            case TOP_TEN:
                companies = World.getInstance().getTopCompanies(10);
                break;
            case CUSTOM:
                companies = companyNames.stream().map(e -> World.getInstance().getCompany(e)).collect(Collectors.toList());
        }
        return new Index(Provider.Index.getName(), companies);
    }

    /**
     * @return name of the index
     */
    public String getName() {
        return name;
    }

    /**
     * @return value of the index
     */
    public Double getValue() {
        return value;
    }

    /**
     * @return companies the index consists of
     */
    public List<Company> getCompanies() {
        return companies;
    }

    /**
     * Update value of the index
     */
    public void updateValue() {
        value = Provider.roundTo2Places(companies.stream().map(e -> e.getCapitalization()).reduce(0.0, Double::sum));
    }

    /**
     * @return string representation of the index
     */
    public String toString() {
        return name + ": $" + value;
    }
}
