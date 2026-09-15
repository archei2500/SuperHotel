import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "super_hotel.clients")
public class Client extends Person {
	@Id
	@Column(name = "clientID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int clientID;
	
	@OneToMany(mappedBy = "client", cascade = CascadeType.REMOVE)
	private List<RoomClient> room_numbers = new ArrayList<RoomClient>();
	
	@Column(name = "length_of_stay")
	private int length_of_stay;
	
	@Column(name = "day")
	private int day;
	
	@Column(name = "month")
	private int month;
	
	@Column(name = "year")
	private int year;
	
	public int getID() {
		return clientID;
	}
	public boolean setID(int id) {
		this.clientID = id;
		return true;
	}
	
	public void addRoomNumber(RoomClient room) {
		room_numbers.add(room);
		if (room.getClient() != this)
		{
			room.setClient(this);
		}
	}
	public List<RoomClient> getRoomNumbers() { 
		return room_numbers; 
	}
	
	public int getLOS() {
		return length_of_stay;
	}
	public boolean setLOS(int new_los) {
		boolean flag = true;
		if (new_los > 0) {
			this.length_of_stay = new_los;
		}
		else {
			flag = false;
		}
		return flag;
	}
	
	public int getDay() {
		return day;
	}
	public void setDay(int new_day) {
		this.day = new_day;
	}
	
	public int getMonth() {
		return month;
	}
	public void setMonth(int new_month) {
		this.month = new_month;
	}
	
	public int getYear() {
		return year;
	}
	public void setYear(int new_year) {
		this.year = new_year;
	}
	
	public Client() {}
}
