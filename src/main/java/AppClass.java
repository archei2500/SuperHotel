
public class AppClass {

	/*public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test_persistence");
		EntityManager em = emf.createEntityManager();
		
		System.out.println("Start a hibernate test");*/
		
		//entity search by id
		/*Client cl = em.find(Client.class, 9);
		Room rm = new Room();
		List<Room> rooms = cl.getRoomNumbers();
		if (!rooms.isEmpty()) {
			rm = rooms.get(0);
		}
		System.out.println("nono: " + rm.getStatus());*/
		/*Room rm = em.find(Room.class, 3);
		System.out.println("Found! Status = " + rm.getStatus() + ", RoomID = " + rm.getRoomID());*/
		
		//change database
		/*Room rm = em.find(Room.class, 3);
		System.out.println("Found! Status = " + rm.getStatus() + ", RoomID = " + rm.getRoomID());
		
		em.getTransaction().begin();
		
		rm.setStatus('r');
		em.getTransaction().commit();*/
		
		//add new entity
		/*em.getTransaction().begin();
		
		Client cl = new Client();
		cl.setName("Emmanuil");
		cl.setLastName("Viktorovich");
		cl.setAge(35);
		cl.setLOS(12);
		cl.setCID("12.12.2021");
		cl.setCOD("24.12.2021");
		em.persist(cl);
		
		RoomType rt = new RoomType();
		rt.setRoomClass("Economy");
		rt.setCapacity(1);
		rt.setCost(2500);
		em.persist(rt);
		
		Room rm = new Room();
		rm.setStatus("reserved");
		rm.setType(rt);
		rm.setClient(cl);
		em.persist(rm);
		
		em.getTransaction().commit();
		
		System.out.println("New room id is " + rm.getRoomID());
		System.out.println("New client name is " + cl.getName());
		System.out.println("New room type is " + rm.getType().getTypeID());
		int rerer = cl.getRoomNumbers().size();
		System.out.println(rerer);*/
		/*
		em.getTransaction().begin();
		
		Room rm = new Room();
		rm.setRoomID(254);
		rm.setStatus('f');
		rm.setType(4);
		
		em.persist(rm);
		em.getTransaction().commit();
		
		System.out.println("New room id is " + rm.getID());
		System.out.println("New room status is " + rm.getStatus());*/
		
		//delete entity
		/*em.getTransaction().begin();
		Room rm = em.find(Room.class, 3);
		em.remove(rm);
		em.getTransaction().commit();
		System.out.println("Removed an entity with ID = " + rm.getID());*/
		
		/*em.close();
		emf.close();
	}*/
}