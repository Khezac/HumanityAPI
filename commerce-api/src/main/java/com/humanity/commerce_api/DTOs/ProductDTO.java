package com.humanity.commerce_api.DTOs;

import com.humanity.commerce_api.entity.Image;
import com.humanity.commerce_api.enums.Gender;

import java.util.List;

public class ProductDTO {

    private Long product_id;
    private String name;
    private String description;
    private String size;
    private Gender gender;
    private Double unit_price;
    private String category;
    private List<ImageDTO> images;

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

    public List<ImageDTO> getImages() {
        return images;
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

    public void setImages(List<ImageDTO> images) {
        this.images = images;
    }
}
