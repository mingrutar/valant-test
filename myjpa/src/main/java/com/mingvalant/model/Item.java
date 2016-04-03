package com.mingvalant.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Item {
	@Id 
	@GeneratedValue
	private Long id;
	
	private String label;
	private Date expiration;
	private String type;
	
	Item() {	
	}

	public Item(String label, Date expiration, String type) {
		this.label = label;
		this.expiration = expiration;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public Date getExpiration() {
		return expiration;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", label=" + label + ", expiration=" + expiration + ", type=" + type + "]";
	}
}
