package yh.recommend.entity;

import org.springframework.data.annotation.Transient;

public class House {
    private int houseId;
    private String picture;//房源图片
    private double price;//价格
    private int number;//总数量
    private int numbered;//已用数量
    private String information;//房源具体信息
    private String housename;//房源名字
    private String address;//地址
    private String label;//房源标签
    private int huId;//房源表-用户表外键
    private double score;//评分

    @Transient
    private String username;
    @Transient
    private String phone;




    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumbered() {
        return numbered;
    }

    public void setNumbered(int numbered) {
        this.numbered = numbered;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getName() {
        return housename;
    }

    public void setName(String housename) {
        this.housename = housename;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getHuId() {
        return huId;
    }

    public void setHuId(int huId) {
        this.huId = huId;
    }

    public String getHousename() {
        return housename;
    }

    public void setHousename(String housename) {
        this.housename = housename;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "House{" +
                "houseId=" + houseId +
                ", picture='" + picture + '\'' +
                ", price=" + price +
                ", number=" + number +
                ", numbered=" + numbered +
                ", information='" + information + '\'' +
                ", housename='" + housename + '\'' +
                ", address='" + address + '\'' +
                ", label='" + label + '\'' +
                ", huId=" + huId +
                ", score=" + score +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
