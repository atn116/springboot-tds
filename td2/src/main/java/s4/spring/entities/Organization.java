package s4.spring.td2.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Organization {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String name;
	private String domain;
	private String aliases;
	private String ville;
	@OneToMany(mappedBy="org",cascade=CascadeType.ALL)
	private List<Groupe> groupes;
	/*@OneToMany(mappedBy="organization",cascade=CascadeType.ALL)
	private List<User> users;*/
	
	public Organization() {
		groupes=new ArrayList<>();
		
	}
	
	public void addGroupe(Groupe grp) {
		groupes.add(grp);
		grp.setOrg(this);
	}
	 
	public List<Groupe> getGroupes() {
		return groupes;
	}
	
	public void setGroupes(List<Groupe> groupes) {
		this.groupes = groupes;
	}

	/*public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<User> getUsers() {
		return users;
	}*/
	
	
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getAliases() {
		return aliases;
	}
	public void setAliases(String aliases) {
		this.aliases = aliases;
	}
	
}
