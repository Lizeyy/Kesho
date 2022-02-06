package projekt.projekt.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CartProductId implements Serializable {
    private long cart;
    private long product;

    private CartProductId() {}
    public CartProductId(long cart, long product){
        this.cart = cart;
        this.product = product;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        CartProductId that = (CartProductId) o;
        return Objects.equals(cart, that.cart) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode(){
        return Objects.hash(cart, product);
    }
}
