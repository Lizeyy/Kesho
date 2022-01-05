package projekt.projekt.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Discount")
public class Discount {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String code;
    private String dateFrom;
    private String dateTo;
    private int rebate;

    @ManyToMany(targetEntity = Producer.class)
    private List<Producer> producerList;

    protected Discount(){}
    public Discount(Long id, String code, String dateFrom, String dateTo, int rebate) {
        this.id = id;
        this.code = code;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.rebate = rebate;
        this.producerList = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getDateFrom() {
        return dateFrom;
    }
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }
    public String getDateTo() {
        return dateTo;
    }
    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }
    public int getRebate() {
        return rebate;
    }
    public void setRebate(int rebate) {
        this.rebate = rebate;
    }
    public List<Producer> getProducerList() {
        return producerList;
    }
    public void setProducerList(List<Producer> producerList) {
        this.producerList = producerList;
    }
}
