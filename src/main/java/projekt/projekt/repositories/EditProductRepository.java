package projekt.projekt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projekt.projekt.entities.EditProduct;

import java.util.List;

public interface EditProductRepository extends JpaRepository<EditProduct, Long> {
    @Query(value = "SELECT * FROM edit_product WHERE date <?1 AND product_id =?2 ORDER BY date", nativeQuery = true)
    List<EditProduct> findDate(String date, long id);
}