package projekt.projekt.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import projekt.projekt.entities.Category;
import projekt.projekt.entities.Product;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Transactional
    List<Product> findByName(String name);

    @Transactional
    Page<Product> findByCategory(Category category, Pageable pageable);
}