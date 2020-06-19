package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
public class GdriveLink {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	String inputLink;

	String generatedLink;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInputLink() {
		return inputLink;
	}

	public void setInputLink(String inputLink) {
		this.inputLink = inputLink;
	}

	public String getGeneratedLink() {
		return generatedLink;
	}

	public void setGeneratedLink(String generatedLink) {
		this.generatedLink = generatedLink;
	}

	@Override
	public String toString() {
		return "GdriveLink [id=" + id + ", inputLink=" + inputLink + ", generatedLink=" + generatedLink + "]";
	}

}
