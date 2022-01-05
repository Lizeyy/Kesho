package projekt.projekt.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Orders")
public class Orders {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private double cost;
    private String date;

    @OneToOne
    private Cart cart;
    @ManyToOne
    private Status status;
    @ManyToOne
    private Delivery delivery;
    @ManyToOne
    private Discount discount;

    protected Orders(){}
    public Orders(double cost, String date, Status status, Delivery  delivery, Discount discount, Cart cart) {
        this.cost = cost;
        this.date = date;
        this.status = status;
        this.delivery = delivery;
        this.discount = discount;
        this.cart = cart;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public Cart getCart() {
        return cart;
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public Delivery getDelivery() {
        return delivery;
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
    public Discount getDiscount() {
        return discount;
    }
    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
}
