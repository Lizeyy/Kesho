package projekt.projekt.repositories;

import org.springframework.data.jpa.repository.Query;
import projekt.projekt.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Transactional
    @Query(value = "SELECT * FROM sale WHERE product_id =?1 AND date_from <= ?2 AND date_to >= ?2", nativeQuery = true)
    List<Sale> findActiveSale(long product_id, String date);

    @Transactional
    @Query(value = "SELECT * FROM sale WHERE product_id =?1 AND date_from > ?2", nativeQuery = true)
    List<Sale> findNextSale(long product_id, String date);

    @Transactional
    @Query(value = "SELECT * FROM sale WHERE product_id =?1 AND date_to < ?2", nativeQuery = true)
    List<Sale> findPrevSale(long product_id, String date);
}