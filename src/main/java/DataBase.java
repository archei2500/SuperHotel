import javax.persistence.*;

import javax.swing.*;
import java.awt.event.*;

public class DataBase {
	//установление связи с БД
	public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("test_persistence");
	public static EntityManager em = emf.createEntityManager();
	
	private JFrame dataBase;
	JTabbedPane tabs;
	
	private JPanel roomList;
	private JPanel clientList;
	private JPanel priceList;
	private JPanel workerList;
	private JPanel positionList;
	private JPanel report;
	
	public void show() {
		// Создание окна
		dataBase = new JFrame("База данных гостиницы");
		dataBase.setSize(700, 400);
		dataBase.setLocation(550, 300);
		dataBase.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//Создание страницы с номерами
		roomList = new JPanel();
		RoomList page1 = new RoomList();
		page1.create(roomList);
		
		//Создание страницы с клиентами
		clientList = new JPanel();
		ClientList page2 = new ClientList();
		page2.create(clientList);
		
		// Создание страницы с прейскурантом цен
		priceList = new JPanel();
		PriceList page3 = new PriceList();
		page3.create(priceList, dataBase);
		
		//Создание страницы с работниками
		workerList = new JPanel();
		WorkerList page4 = new WorkerList();
		page4.create(workerList);
		
		//Создание страницы с должностями
		positionList = new JPanel();
		PositionList page5 = new PositionList();
		page5.create(positionList);
		
		//Создание конструктора отчётов
		report = new JPanel();
		ReportMaker page6 = new ReportMaker();
		page6.create(report);
		
		//Создание контейнера
		tabs = new JTabbedPane();
		tabs.setBounds(40,20,300,300);
	    tabs.add("Номера", roomList);
	    tabs.add("Клиенты", clientList);
	    tabs.add("Прейскурант цен", priceList);
	    tabs.add("Служащие", workerList);
	    tabs.add("Должности", positionList);
	    tabs.add("Конструктор отчётов", report);
	    dataBase.add(tabs);
	    
	    dataBase.addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent event)
			{
				Object[] variants = {"Да", "Нет"};
				int n = JOptionPane.showOptionDialog(event.getWindow(), "Вы точно уверены, что хотите закрыть окно?", "confirmation", 
						JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, variants, variants[0]);
				if (n == 0) {
					event.getWindow().setVisible(false);
					em.close();
					emf.close();
					System.exit(0);
				}
			}
			public void windowOpened(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {	}
			public void windowDeactivated(WindowEvent e) {}
		}	
		);
	   
	    dataBase.setVisible(true);
	}
	public static void main(String[] args) {
		// Создание и отображение экранной формы
		new DataBase().show();
	}
}
