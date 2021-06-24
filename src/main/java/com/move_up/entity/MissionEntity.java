package com.move_up.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "mission")
public class MissionEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "short_description")
	private String shortDescription;
	
	@Column(name = "num_of_star")
	private Integer numOfStar;
	
	@Column(name = "num_of_coin_gift_box")
	private Integer numOfCoinGiftBox;
	
	@Column(name = "num_of_time_gift_box")
	private Integer numOfTimeGiftBox;
	
	@Column(name = "type")
	private String type;
	
	@ManyToMany(mappedBy = "missions")
    private List<UserEntity> users = new ArrayList<UserEntity>();

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

	public List<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	
}
