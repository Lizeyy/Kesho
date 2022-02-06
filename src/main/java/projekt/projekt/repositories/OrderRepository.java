package projekt.projekt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projekt.projekt.entities.Orders;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query(value = "SELECT * FROM orders JOIN cart ON orders.cart_id = cart.id JOIN status ON orders.status_id = status.id WHERE cart.customer_id =?1 AND status.name !='Odebrane' ORDER BY date DESC", nativeQuery = true)
    List<Orders> findActive(long customerID);

    @Query(value = "SELECT * FROM orders JOIN cart ON orders.cart_id = cart.id WHERE cart.customer_id =?1 ORDER BY date DESC", nativeQuery = true)
    List<Orders> findAllCustomer(long customerID);

    @Query(value = "SELECT * FROM orders JOIN status ON orders.status_id = status.id WHERE status.id =?1 ORDER BY date DESC", nativeQuery = true)
    List<Orders> findStatus(long statusID);

    @Query(value = "SELECT * FROM orders ORDER BY date DESC", nativeQuery = true)
    List<Orders> findAllOrder();

    @Query(value = "SELECT * FROM orders JOIN cart ON orders.cart_id = cart.id JOIN cart_product ON cart_product.cart_id = cart.id WHERE cart_product.product.id =?1", nativeQuery = true)
    List<Orders> findProduct(long id);

    @Query(value = "SELECT * FROM orders JOIN cart ON orders.cart_id = cart.id JOIN cart_product ON cart_product.cart_id = cart.id WHERE cart_product.product_id =?1 AND orders.date >=?2 AND orders.date <=?3", nativeQuery = true)
    List<Orders> findProductSale(long id, String dateFrom, String dateTo);
}