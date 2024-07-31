package com.humanity.commerce_api.DTOs;

import java.util.List;

public class ProductDTO {

    private Long product_id;
    private String name;
    private String description;
    private String size;
    private String gender;
    private String unit_price;
    private String category;
    private List<String> imageURL;

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

    public String getGender() {
        return gender;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getImageURL() {
        return imageURL;
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

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageURL(List<String> imageURL) {
        this.imageURL = imageURL;
    }
}
