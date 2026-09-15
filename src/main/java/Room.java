import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "super_hotel.rooms")
public class Room {
	@Id
	@Column(name = "roomID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int roomID;
	
	@Column(name = "status")
	private String status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type")
	private RoomType type;
	
	@OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
	private List<RoomClient> clients = new ArrayList<RoomClient>();
	
	public int getRoomID() {
		return roomID;
	}
	public boolean setRoomID(int new_id) {
		this.roomID = new_id;
		return true;
	}
	
	public String getStatus() {
		return status;
	}
	public boolean setStatus(String new_status) {
		boolean flag = true;
		if (new_status.equals("available") || new_status.equals("reserved")) {
			this.status = new_status;
		}
		else {
			flag = false;
		}	
		return flag;
	}
	
	public RoomType getType() { 
		return type; 
	}
	public void setType(RoomType new_type)
	{
		this.type = new_type;
		if (!new_type.getRoomNumbers().contains(this))
		{
			new_type.addRoomNumber(this);
		}
	}
	
	public void addClient(RoomClient client) {
		clients.add(client);
		if (client.getRoom() != this)
		{
			client.setRoom(this);
		}
	}
	public List<RoomClient> getClients() { 
		return clients; 
	}
	
	public Room() {}
}

