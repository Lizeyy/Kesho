package projekt.projekt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.projekt.entities.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}