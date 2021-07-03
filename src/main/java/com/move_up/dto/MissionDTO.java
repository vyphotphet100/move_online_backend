package com.move_up.dto;

import java.util.ArrayList;
import java.util.List;

public class MissionDTO extends BaseDTO {

    private Long id;
    private String name;
    private String description;
    private String shortDescription;
    private Integer numOfStar;
    private Integer numOfCoinGiftBox;
    private Integer numOfTimeGiftBox;
    private String type;
    private Integer time;
    private List<String> usernames = new ArrayList<String>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumOfStar() {
        return numOfStar;
    }

    public void setNumOfStar(Integer numOfStar) {
        this.numOfStar = numOfStar;
    }

    public Integer getNumOfCoinGiftBox() {
        return numOfCoinGiftBox;
    }

    public void setNumOfCoinGiftBox(Integer numOfCoinGiftBox) {
        this.numOfCoinGiftBox = numOfCoinGiftBox;
    }

    public Integer getNumOfTimeGiftBox() {
        return numOfTimeGiftBox;
    }

    public void setNumOfTimeGiftBox(Integer numOfTimeGiftBox) {
        this.numOfTimeGiftBox = numOfTimeGiftBox;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
