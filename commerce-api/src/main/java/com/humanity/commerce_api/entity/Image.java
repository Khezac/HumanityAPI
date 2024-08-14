package com.humanity.commerce_api.entity;

import jakarta.persistence.*;

@Entity
public class Image {

    @Id
    private Long image_id;

    private String type;

    private String fileName;

    @Lob
    private byte[] bytes;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Image() {
    }

    public Long getImage_id() {
        return image_id;
    }

    public void setImage_id(Long image_id) {
        this.image_id = image_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
