import javax.persistence.*;

@Entity
@Table(name = "super_hotel.workers")
public class Worker extends Person {
	@Id
	@Column(name = "workerID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int workerID;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "position")
	private Position position;
	
	public int getID() {
		return workerID;
	}
	public boolean setID(int id) {
		this.workerID = id;
		return true;
	}
	
	public Position getPosition() { 
		return position; 
	}
	public void setPosition(Position position)
	{
		this.position = position;
		if (!position.getWorkers().contains(this))
		{
			position.addWorker(this);
		}
	}
	
	public Worker() {}
}
