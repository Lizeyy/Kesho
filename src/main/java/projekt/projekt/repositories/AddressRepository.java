package projekt.projekt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.projekt.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}