package projekt.projekt.entities;


import javax.persistence.*;

@Entity
@Table(name="Delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private double cost;

    protected Delivery(){}
    public Delivery(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
}
