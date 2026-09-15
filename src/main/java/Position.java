import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "super_hotel.positions")
public class Position {
	@Id
	@Column(name = "positionID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int positionID;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "type")
	private String type;
	
	@OneToMany(mappedBy = "position", cascade = CascadeType.REMOVE)
	private List<Worker> workers = new ArrayList<Worker>();
	
	public int getID() {
		return positionID;
	}
	public boolean setID(int id) {
		this.positionID = id;
		return true;
	}
	
	public String getName() {
		return name;
	}
	public boolean setName(String new_name) {
		boolean flag = true;
		if (!(new_name.isEmpty())) {
			if (isAlpha(new_name)) {
				this.name = new_name;
			}
			else {
				flag = false;
			}
		}
		else {
			flag = false;
		}
		return flag;
	}
	
	public String getType() {
		return type;
	}
	public boolean setType(String new_type) {
		boolean flag = true;
		if (new_type.equals("managerial") || new_type.equals("ordinary")) {
			this.type = new_type;
		} else {
			flag = false;
		}
		return flag;
	}
	
	public void addWorker(Worker worker) {
		workers.add(worker);
		if (worker.getPosition() != this)
		{
			worker.setPosition(this);
		}
	}
	public List<Worker> getWorkers() { 
		return workers; 
	}
	
	public boolean isAlpha(String str) {
	    char[] chars = str.toCharArray();
	    for (char c : chars) {
	        if(!(Character.isLetter(c) || c == ' ')) {
	            return false;
	        }
	    }
	    return true;
	}
	
	Position() {}
}
