package katarina.products.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue
    private long id;

    @Column(columnDefinition = "char(10)", unique = true)
    @Size(min = 10, max = 10)
    private String code;

    private String name;

    @Positive
    private BigDecimal priceEur;

    @Transient
    private BigDecimal priceUsd;

    private String description;

    private boolean isAvailable;

    public Product() {
    }

    public Product(long id, String code, String name, BigDecimal priceEur, String description, boolean isAvailable) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.priceEur = priceEur;
        this.description = description;
        this.isAvailable = isAvailable;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPriceEur() {
        return priceEur;
    }

    public void setPriceEur(BigDecimal priceEur) {
        this.priceEur = priceEur;
    }

    public BigDecimal getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(BigDecimal priceUsd) {
        this.priceUsd = priceUsd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}