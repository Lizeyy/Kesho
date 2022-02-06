package projekt.projekt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.projekt.entities.Customer;
import projekt.projekt.entities.Login;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByLogin(Login login);
    List<Customer> findByEmail(String email);
}