package projekt.projekt.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projekt.projekt.entities.Category;
import projekt.projekt.entities.Product;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Transactional
    List<Product> findByName(String name);

    @Transactional
    Page<Product> findByCategoryAndQuantityGreaterThan(Category category, Pageable pageable, int quantity);

    @Transactional
    Page<Product> findByCategoryAndQuantityGreaterThanOrderByName(Category category, Pageable pageable, int quantity);
    @Transactional
    Page<Product> findByCategoryAndQuantityGreaterThanOrderByNameDesc(Category category, Pageable pageable, int quantity);

    @Transactional
    Page<Product> findByCategoryAndQuantityGreaterThanOrderByPrice(Category category, Pageable pageable, int quantity);
    @Transactional
    Page<Product> findByCategoryAndQuantityGreaterThanOrderByPriceDesc(Category category, Pageable pageable, int quantity);

    @Transactional
    @Query(value = "SELECT * FROM product WHERE quantity > 1 AND quantity < 20 ORDER BY quantity", nativeQuery = true)
    Page<Product> findLastProduct(Pageable pageable);

    @Transactional
    @Query(value = "SELECT * FROM product JOIN cart_product ON product.id = cart_product.product_id WHERE cart_product.cart_id =?1", nativeQuery = true)
    List<Product> findCart(long cart);

}