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
    private boolean active;

    protected Delivery(){}
    public Delivery(String name, double cost, boolean active) {
        this.name = name;
        this.cost = cost;
        this.active = active;
    }

    public String getParsed(){
        return String.format("%.2f", cost) + " zł";
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
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
}
