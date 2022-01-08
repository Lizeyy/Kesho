package projekt.projekt.entities;

import javax.persistence.*;

@Entity
@Table(name="Sale")
public class Sale {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String dateFrom;
    private String dateTo;
    private Double price;

    @ManyToOne
    private Product product;

    public Sale(){}
    public Sale(String dateFrom, String dateTo, Double price, Product product) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.price = price;
        this.product = product;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDateFrom() {
        return dateFrom;
    }
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }
    public String getDateTo() {
        return dateTo;
    }
    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
}
