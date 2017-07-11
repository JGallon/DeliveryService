package com.zucc.jjl1130.deliveryservice;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by JGallon on 2017/7/9.
 */
public class BeanOrder implements Serializable {
    private String orederID;
    private String user;
    private String courier;
    private String couriername;
    private String username;
    private int state;
    private String description;
    private String detail;
    private double endlng;
    private double endlat;
    private double startlng;
    private double startlat;
    private double pay;
    private Date createdate;
    private Date finishdate;
    private int flag;
    private double rate;
    private String comment;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOrederID() {
        return orederID;
    }

    public void setOrederID(String orederID) {
        this.orederID = orederID;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getCouriername() {
        return couriername;
    }

    public void setCouriername(String couriername) {
        this.couriername = couriername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getEndlng() {
        return endlng;
    }

    public void setEndlng(double endlng) {
        this.endlng = endlng;
    }

    public double getEndlat() {
        return endlat;
    }

    public void setEndlat(double endlat) {
        this.endlat = endlat;
    }

    public double getStartlng() {
        return startlng;
    }

    public void setStartlng(double startlng) {
        this.startlng = startlng;
    }

    public double getStartlat() {
        return startlat;
    }

    public void setStartlat(double startlat) {
        this.startlat = startlat;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getFinishdate() {
        return finishdate;
    }

    public void setFinishdate(Date finishdate) {
        this.finishdate = finishdate;
    }

}
