package projekt.projekt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.projekt.entities.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findBySubcategoryIsNull();
    List<Category> findBySubcategoryNotNull();
    List<Category> findByName(String name);
}