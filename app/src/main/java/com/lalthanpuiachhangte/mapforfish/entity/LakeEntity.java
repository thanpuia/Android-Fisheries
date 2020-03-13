package com.lalthanpuiachhangte.mapforfish.entity;

public class LakeEntity {

    private int id;
    private String name;
    private Double lat;
    private Double lng;
    private String district;
    private String description;
    private String address;
    private String image;


    public LakeEntity() {
    }
    public LakeEntity( String name, Double lat, Double lng, String district, String description, String address, String image) {

        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.district = district;
        this.description = description;
        this.address = address;
        this.image = image;
    }
    public LakeEntity(int id, String name, Double lat, Double lng, String district, String description, String address, String image) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.district = district;
        this.description = description;
        this.address = address;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
