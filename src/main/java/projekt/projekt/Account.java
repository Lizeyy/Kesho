package projekt.projekt;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import projekt.projekt.entities.Customer;
import projekt.projekt.entities.Employee;

@Component
@SessionScope
public class Account {
    private Customer customer;
    private Employee employee;

    public Account(){}
    public Account(Customer customer) {
        this.customer = customer;
    }
    public Account(Employee employee) {
        this.employee = employee;
    }

    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
