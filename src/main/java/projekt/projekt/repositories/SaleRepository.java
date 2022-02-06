package projekt.projekt.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import projekt.projekt.entities.Sale;

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

    @Transactional
    @Query(value = "SELECT * FROM sale WHERE date_from <= ?1 AND date_to >= ?1", nativeQuery = true)
    List<Sale> findActiveSaleAll(String date);

    @Transactional
    @Query(value = "SELECT * FROM sale WHERE product_id =?1 AND date_from <= ?2 AND date_to >= ?2 AND id  != ?3", nativeQuery = true)
    List<Sale> findActiveSaleWithout(long product_id, String date, long sale_id);

    @Transactional
    @Query(value = "SELECT * FROM sale WHERE date_from <= ?1 AND date_to >= ?1 ORDER BY date_to", nativeQuery = true)
    Page<Sale> findSaleEnding(String date, Pageable pageable);
}