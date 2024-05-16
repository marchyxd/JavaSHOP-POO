package model;

public abstract class Person{
	protected String name;
	
	public Person(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	// To string that return the name of client with all the characters upper
	public String toString() {
		return name.toUpperCase();
	}
	
	
	
}
