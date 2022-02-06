package projekt.projekt.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@Table(name = "cart_product")
public class CartProduct {
    @EmbeddedId
    private CartProductId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cart")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("product")
    private Product product;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "quantity")
    private int quantity;

    public CartProduct(Cart cart, Product product, LocalDate date, int quantity){
        this.id = new CartProductId(cart.getId(), product.getId());
        this.cart = cart;
        this.product = product;
        this.date = date;
        this.quantity = quantity;
    }


    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartProduct that = (CartProduct) o;
        return Objects.equals(cart, that.cart) && Objects.equals(product, that.product);
    }
    @Override
    public int hashCode() {
        return Objects.hash(cart, product);
    }

}
