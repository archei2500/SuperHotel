import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "super_hotel.room_types")
public class RoomType {
	@Id
	@Column(name = "typeID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int typeID;
	
	@Column(name = "room_class")
	private String room_class;
	
	@Column(name = "capacity")
	private int capacity;
	
	@Column(name = "cost_per_night")
	private int cost_per_night;
	
	@OneToMany(mappedBy = "type", cascade = CascadeType.REMOVE)
	private List<Room> room_numbers = new ArrayList<Room>();
	
	public int getTypeID() {
		return typeID;
	}
	public boolean setTypeID(int new_id) {
		this.typeID = new_id;
		return true;
	}
	
	public String getRoomClass() {
		return room_class;
	}
	public boolean setRoomClass(String new_class) {
		boolean flag = true;
		if (!(new_class.isEmpty())) {
			if (isAlpha(new_class)) {
				this.room_class = new_class;
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
	
	public int getCapacity() {
		return capacity;
	}
	public boolean setCapacity(int new_capacity) {
		boolean flag = true;
		if (new_capacity < 6 && new_capacity > 0) {
			this.capacity = new_capacity;
		}
		else {
			flag = false;
		}
		return flag;
	}
	
	public int getCost() {
		return cost_per_night;
	}
	public boolean setCost(int new_cost) {
		boolean flag = true;
		if (new_cost < 20001 && new_cost > 999) {
			this.cost_per_night = new_cost;
		}
		else {
			flag = false;
		}
		return flag;
	}
	
	public void addRoomNumber(Room room)
	{
		room_numbers.add(room);
		if (room.getType() != this)
		{
			room.setType(this);
		}
	}
	public List<Room> getRoomNumbers() { return room_numbers; }
	
	public boolean isAlpha(String str) {
	    char[] chars = str.toCharArray();
	    for (char c : chars) {
	        if(!(Character.isLetter(c) || c == ' ')) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public RoomType() {}
}