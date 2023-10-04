package katarina.products.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue
    private long id;
    private String code;
    private String name;
    private BigDecimal priceEur;
    @Transient
    @JsonInclude // TODO: remove if ProductDTO is added
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