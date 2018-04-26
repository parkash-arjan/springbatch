package com.fibonacci.springbatch.flat_files;

import java.util.Date;

public class Student {
	private String id;
	private String name;
	private String email;
	private Date birthDate;

	public Student() {
	}

	public Student(String id, String name, String email, Date birthDate) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.birthDate = birthDate;
	}

	public Student(String id, String name, String email) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public String toString() {
		return String.format("Student [id=%s, name=%s, email=%s, birthDate=%s]", id, name, email, birthDate);
	}

}
