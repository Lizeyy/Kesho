package projekt.projekt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.projekt.entities.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}