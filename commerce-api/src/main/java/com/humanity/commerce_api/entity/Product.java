package com.humanity.commerce_api.entity;

import com.humanity.commerce_api.enums.Gender;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    private String name;
    private String description;
    private String size;
    @Enumerated
    private Gender gender;
    private Double unit_price;
    private String category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Image> images;

    public Product() {
    }

    public Product(Product product) {
        this.product_id = product.getProduct_id();
        this.name = product.getName();
        this.description = product.getDescription();
        this.size = product.getSize();
        this.gender = product.getGender();
        this.unit_price = product.getUnit_price();
        this.category = product.getCategory();
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setUnit_price(Double unit_price) {
        this.unit_price = unit_price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSize() {
        return size;
    }

    public Gender getGender() {
        return gender;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public String getCategory() {
        return category;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_id=" + product_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", size='" + size + '\'' +
                ", gender=" + gender +
                ", unit_price=" + unit_price +
                ", category='" + category + '\'' +
                '}';
    }
}
