package projekt.projekt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projekt.projekt.entities.CartProduct;
import projekt.projekt.entities.CartProductId;

import java.util.List;

public interface CartProductRepository extends JpaRepository<CartProduct, CartProductId> {

    @Query(value = "SELECT * FROM cart_product JOIN cart ON cart.id = cart_product.cart_id WHERE cart.bought = false AND cart.customer_id =?1", nativeQuery = true)
    List<CartProduct> findActive(long customer);

    @Query(value = "SELECT * FROM cart_product JOIN cart ON cart.id = cart_product.cart_id WHERE cart.customer_id =?1", nativeQuery = true)
    List<CartProduct> findCustomer(long customer);

    @Query(value = "SELECT * FROM cart_product WHERE cart_id =?1 AND product_id =?2 LIMIT 1", nativeQuery = true)
    CartProduct findCartProduct(long cart_id, long product_id);
}