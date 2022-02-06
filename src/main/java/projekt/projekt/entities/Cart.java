package projekt.projekt.entities;

import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="Cart")
public class Cart {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private boolean bought;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartProduct> productList = new ArrayList<>();

    public Cart(){}
    public Cart(Customer customer) {
        this.customer = customer;
        productList = new ArrayList<>();
    }

  //  public void addProduct(Product product){this.productList.add(product);}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    @Transactional
    public void addProduct(Product product, LocalDate date, int q){
        Hibernate.initialize(this);
        CartProduct cartProduct = new CartProduct(this, product, date, q);
        productList.add(cartProduct);
        product.getCartProducts().add(cartProduct);
    }
    @Transactional
    public void removeProduct(Product product){
        for(Iterator<CartProduct> iterator = productList.iterator(); iterator.hasNext();) {
            CartProduct cartProduct = iterator.next();
            if(cartProduct.getCart().equals(this) && cartProduct.getProduct().equals(product)){
                iterator.remove();
                cartProduct.getProduct().getCartProducts().remove(cartProduct);
                cartProduct.setProduct(null);
                cartProduct.setCart(null);
            }
        }
    }

    public String getStringId(){
        return id.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(bought, cart.bought);
    }
    @Override
    public int hashCode() {
        return Objects.hash(bought);
    }
//   public List<Product> getProductList() {
 //       return productList;
 //   }
 //   public void setProductList(List<Product> productList) {
 //      this.productList = productList;
 //  }
}
