package projekt.projekt.entities;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="Cart")
public class Cart {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private int quantity;
    private String date;
    private boolean bought;

    @ManyToOne
    private Customer customer;
    @ManyToMany(targetEntity = Product.class)
    private List<Product> productList;

    protected Cart(){}
    public Cart(int quantity, String date, boolean bought, Customer customer) {
        this.quantity = quantity;
        this.date = date;
        this.bought = bought;
        this.customer = customer;
        productList = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public boolean isBought() {
        return bought;
    }
    public void setBought(boolean bought) {
        this.bought = bought;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public List<Product> getProductList() {
        return productList;
    }
    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
