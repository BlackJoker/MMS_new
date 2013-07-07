package de.team55.mms.data;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "UserRelation")

public class UserRelation {
	private ArrayList<String> relation = new ArrayList<String>();
	
	public UserRelation(){
		relation.add(null);
	}
	
	public UserRelation(ArrayList<String> relation){
		this.relation=relation;
	}

	public ArrayList<String> getRelation() {
		return relation;
	}

	public void setRelation(ArrayList<String> relation) {
		this.relation = relation;
	}

}
