package projekt.projekt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projekt.projekt.entities.Cart;
import projekt.projekt.entities.Customer;

import javax.transaction.Transactional;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Transactional
    @Query(value = "SELECT * FROM cart JOIN cart_product ON cart.id = cart_product.cart_id WHERE cart.customer_id =?1 AND cart_product.product_id =?2 AND cart.bought = false",  nativeQuery = true)
    List<Cart> findCustomerCartProductActive(long customer_id, long product_id);

    List<Cart> findByCustomerAndBought(Customer customer, boolean bought);
}