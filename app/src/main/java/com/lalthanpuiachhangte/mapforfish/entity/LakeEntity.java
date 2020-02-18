package com.lalthanpuiachhangte.mapforfish.entity;

public class LakeEntity {

    String name;
    Double lat;
    Double lng;
    String district;
    String image;

    public LakeEntity() {
    }

    public LakeEntity(String name, Double lat, Double lng, String district, String image) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.district = district;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}