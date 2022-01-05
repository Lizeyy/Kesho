package projekt.projekt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.projekt.entities.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}