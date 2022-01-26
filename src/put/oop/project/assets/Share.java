package put.oop.project.assets;

final public class Share extends Asset {
    private Company company;

    public Share(String name, Company company) {
        super(name, company.getCurrentPrice());
        this.company = company;
    }

    /** Get company for this share
     * @return company of the share
     */
    public Company getCompany() {
        return company;
    }

    /** Get current price of the chare
     * @return current price of the share
     */
    public Double getCurrentPrice() {
        return company.getCurrentPrice();
    }

    /** Set new current price fot the share
     * @param price price to be added
     */
    @Override
    public void setCurrentPrice(Double price) {
        super.setCurrentPrice(price);
        company.setCurrentPrice(price);
    }
}
