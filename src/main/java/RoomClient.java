import javax.persistence.*;

@Entity
@Table(name = "super_hotel.rooms_clients")
public class RoomClient {
	@Id
	@Column(name = "IDroom_client")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int IDroom_client;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room")
	private Room room;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client")
	private Client client;
	
	public int getRoomClientID() {
		return IDroom_client;
	}
	public boolean setRoomClientID(int new_id) {
		this.IDroom_client = new_id;
		return true;
	}
	
	public Room getRoom() { 
		return room; 
	}
	public void setRoom(Room room)
	{
		this.room = room;
		if (!room.getClients().contains(this))
		{
			room.addClient(this);
		}
	}
	
	public Client getClient() { 
		return client; 
	}
	public void setClient(Client client)
	{
		this.client = client;
		if (!client.getRoomNumbers().contains(this))
		{
			client.addRoomNumber(this);
		}
	}
	
	public RoomClient() {}
}
