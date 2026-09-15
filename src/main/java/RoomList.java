import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

public class RoomList {
	
	private DefaultTableModel model1;
	private JButton open_rooms;
	private JButton add_room;
	private JButton edit_room;
	private JButton delete_room;
	private JToolBar toolBarRooms;
	private JScrollPane scroll1;
	private JTable rooms_table;
	private JComboBox<String> occupancy;
	private JComboBox<String> category1;
	private JComboBox<String> capacity1;
	private JTextField price1;
	private JButton filter1;
	
	private JFrame registration1;
	private JLabel type_label;
	private JComboBox<String> type;
	private JButton add1;
	
	private JFrame editing1;
	private JLabel status_label1;
	private JLabel type_label1;
	private JComboBox<String> status1;
	private JComboBox<String> type1;
	private JButton save1;
	private JLabel select;
	private JComboBox<String> added_rooms;
	
	private JFrame remove1;
	private JLabel select1;
	private JComboBox<String> added_rooms1;
	private JButton delete_selected1;
	private JButton delete_all1;
	
	public void create(JPanel roomList) {
		// Создание кнопок и прикрепление иконок
		open_rooms = new JButton(new ImageIcon("./img/folder_red_open.png"));
		add_room = new JButton(new ImageIcon("./img/plus_orange.png"));
		edit_room = new JButton(new ImageIcon("./img/edit_icon.png"));
		delete_room = new JButton(new ImageIcon("./img/trash.png"));
		// Настройка подсказок для кнопок
		open_rooms.setToolTipText("Обновить список номеров");
		add_room.setToolTipText("Добавить гостиничный номер");
		edit_room.setToolTipText("Отредактировать характеристики номера");
		delete_room.setToolTipText("Удалить номер из базы");
		// Добавление кнопок на панель инструментов
		toolBarRooms = new JToolBar("Панель инструментов");
		toolBarRooms.add(open_rooms);
		toolBarRooms.add(add_room);
		toolBarRooms.add(edit_room);
		toolBarRooms.add(delete_room);
		// Размещение панели инструментов
		roomList.setLayout(new BorderLayout());
		roomList.add(toolBarRooms, BorderLayout.NORTH);
		// Создание таблицы с данными
		String [] columns1 = {"Номер", "Занятость", "Категория", "Вместимость", "Стоимость"};
		String [][] data1 = {{"-", "-", "-", "-", "-"}};
		model1 = new DefaultTableModel(data1, columns1);
		rooms_table = new JTable(model1);
		// Добавление пользователю возможности самому сортировать таблицу
		rooms_table.setAutoCreateRowSorter(true);
		scroll1 = new JScrollPane(rooms_table);
		// Размещение таблицы с данными
		roomList.add(scroll1, BorderLayout.CENTER);
		// Подготовка компонентов поиска
		occupancy = new JComboBox<String>(new String[] {"Занятость", "available", "reserved"});
		TypedQuery<RoomType> q = DataBase.em.createQuery("select t from RoomType t", RoomType.class); //имеющиеся категории
		List<RoomType> roomtypes = q.getResultList();
		List<String> added_types = new ArrayList<String>();
		added_types.add("Категория");
		for (RoomType entity : roomtypes) {
			if (!added_types.contains(entity.getRoomClass())) {
				added_types.add(entity.getRoomClass());
			}
		}
		category1 = new JComboBox<String>(added_types.toArray(new String[0]));
		capacity1 = new JComboBox<String>(new String[] {"Вместимость", "Single", "Double", "Triple", "Four persons", "Five persons"});
		price1 = new JTextField("Цена не выше");
		filter1 = new JButton("Поиск");
		// Добавление компонентов на панель
		JPanel filterPanel1 = new JPanel();
		filterPanel1.add(occupancy);
		filterPanel1.add(category1);
		filterPanel1.add(capacity1);
		filterPanel1.add(price1);
		filterPanel1.add(filter1);
		// Размещение панели поиска внизу окна
		roomList.add(filterPanel1, BorderLayout.SOUTH);
		
		open_rooms.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				TypedQuery<Room> q = DataBase.em.createQuery("select t from Room t", Room.class);
				List<Room> rooms = q.getResultList();
				int rows = model1.getRowCount();
				for (int i = 0; i < rows; i++) {
					model1.removeRow(0); // Очистка таблицы
				}
				for (Room entity : rooms) {
					String number_cell = Integer.toString(entity.getRoomID());
					String status_cell = entity.getStatus();
					String category_cell = entity.getType().getRoomClass();
					int capac = entity.getType().getCapacity();
					String capacity_cell = selectCapacity(capac);
					String cost_cell = Integer.toString(entity.getType().getCost());
					// Запись данных в таблицу
					model1.addRow(new String[]{number_cell, status_cell, category_cell, capacity_cell, cost_cell});
				}
				//Обновление списка имеющихся типов номеров на панели поиска
				changeComboBoxCategory();
			}
		} );
		add_room.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				new RoomList().register1();
			}
		} );
		edit_room.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				new RoomList().editor1();
			}
		} );
		delete_room.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				new RoomList().remover1();
			}
		} );
		filter1.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				String cb1 = (String)occupancy.getSelectedItem();
				String cb2 = (String)category1.getSelectedItem();
				String cb3 = (String)capacity1.getSelectedItem();
				String price_str = price1.getText();
				if (!((cb1.equals("Занятость")) && (cb2.equals("Категория")) && (cb3.equals("Вместимость")) 
						&& (price_str.contains("Цена не выше") || price_str.length() == 0))) {
					TypedQuery<Room> q = DataBase.em.createQuery("select t from Room t", Room.class);
					List<Room> rooms = q.getResultList();
					// Если были выбраны какие-то из этих параметров, из списка постепенно удаляются все номера,
					// данные в соответствующих полях которых не совпадают с выбранными
					if (!cb1.equals("Занятость")) {
						Iterator<Room> roomIterator = rooms.iterator();
						while (roomIterator.hasNext()) {
							Room nextRoom = roomIterator.next();
							if (!(nextRoom.getStatus().equals(cb1))) {
								roomIterator.remove();
							}
						}
					}
					if (!cb2.equals("Категория")) {
						Iterator<Room> roomIterator = rooms.iterator();
						while (roomIterator.hasNext()) {
							Room nextRoom = roomIterator.next();
							if (!(nextRoom.getType().getRoomClass().equals(cb2))) {
								roomIterator.remove();
							}
						}
					}
					if (!cb3.equals("Вместимость")) {
						int val1 = selectCapacity2(cb3);
						Iterator<Room> roomIterator = rooms.iterator();
						while (roomIterator.hasNext()) {
							Room nextRoom = roomIterator.next();
							if (nextRoom.getType().getCapacity() != val1) {
								roomIterator.remove();
							}
						}
					}
					if (!(price_str.contains("Цена не выше") || price_str.length() == 0)) {
						int val2 = -1;
						try { 
							val2 = checkPrice(price_str);
						} catch (NotNumericInput nNumInp) {
							JOptionPane.showMessageDialog(null, nNumInp.getMessage());
						}
						if (val2 != -1) {
							Iterator<Room> roomIterator = rooms.iterator();
							while (roomIterator.hasNext()) {
								Room nextRoom = roomIterator.next();
								if (nextRoom.getType().getCost() > val2) {
									roomIterator.remove();
								}
							}
						}
					}
					int rows = model1.getRowCount();
					for (int i = 0; i < rows; i++) {
						model1.removeRow(0); // Очистка таблицы
					}
					for (Room entity : rooms) {
						String number_cell = Integer.toString(entity.getRoomID());
						String status_cell = entity.getStatus();
						String category_cell = entity.getType().getRoomClass();
						int capac = entity.getType().getCapacity();
						String capacity_cell = selectCapacity(capac);
						String cost_cell = Integer.toString(entity.getType().getCost());
						// Запись данных в таблицу
						model1.addRow(new String[]{number_cell, status_cell, category_cell, capacity_cell, cost_cell});
					}
					if (model1.getRowCount() == 0) {
						JOptionPane.showMessageDialog(null, "К сожалению, по вашему запросу не удалось ничего найти :(");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Вы не настроили параметры поиска!");
				}
				
			}
		} );
	}
	public void register1() {
		// Создание окна
		registration1 = new JFrame("Добавление номера");
		registration1.setSize(500, 150);
		registration1.setLocation(600, 350);
		registration1.setLayout(new GridBagLayout());
		//Обработка компонентов
		type_label = new JLabel("Выберите тип номера из имеющихся: ");
		TypedQuery<RoomType> q = DataBase.em.createQuery("select t from RoomType t", RoomType.class);
		List<RoomType> roomtypes = q.getResultList();
		String[] added = new String[roomtypes.size()];
		int i = 0;
		for (RoomType entity : roomtypes) {
			added[i] = Integer.toString(entity.getTypeID()) + " " + entity.getRoomClass() + " with capacity " + Integer.toString(entity.getCapacity());
			i = i + 1;
		}
		type = new JComboBox<String>(added);
		add1 = new JButton("Добавить");
		//размещение
		registration1.add(type_label, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration1.add(type, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration1.add(add1, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		add1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				String cb = (String)type.getSelectedItem();
				DataBase.em.getTransaction().begin();
				Room rm = new Room();
				rm.setStatus("available");
				int ind_space = cb.indexOf(' ');
				String id = cb.substring(0, ind_space);
				RoomType entity = DataBase.em.find(RoomType.class, Integer.parseInt(id));
				rm.setType(entity);
				DataBase.em.persist(rm);
				DataBase.em.getTransaction().commit();
				JOptionPane.showMessageDialog(null, "Готово! Чтобы увидеть изменения в таблице, нажмите на панели кнопку с папкой.");
			}
		});
		registration1.setVisible(true);
		if (type.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "Сначала добавьте хотя бы один тип во вкладке Прейскурант цен!");
			registration1.setVisible(false);
		}
	}
	public void editor1() {
		// Создание окна
		editing1 = new JFrame("Редактирование");
		editing1.setSize(600, 300);
		editing1.setLocation(600, 350);
		editing1.setLayout(new GridBagLayout());
		//Обработка компонентов
		select = new JLabel("Выберите номер, который желаете отредактировать: ");
		status_label1 = new JLabel("Изменить занятость номера (свободен или занят): ");
		type_label1 = new JLabel("Изменить тип номера: ");
		status1 = new JComboBox<String>(new String[] {"без изменения", "available", "reserved"});
		DataBase.em.getTransaction().begin();
		TypedQuery<RoomType> q = DataBase.em.createQuery("select t from RoomType t", RoomType.class);
		//добавление в комбобокс типов номеров
		List<RoomType> roomtypes = q.getResultList();
		String[] added = new String[roomtypes.size() + 1];
		added[0] = "без изменения";
		int i = 1;
		for (RoomType entity : roomtypes) {
			added[i] = Integer.toString(entity.getTypeID()) + " " + entity.getRoomClass() + " with capacity " + Integer.toString(entity.getCapacity());
			i = i + 1;
		}
		//добавление в комбобокс имеющихся номеров
		TypedQuery<Room> q1 = DataBase.em.createQuery("select t from Room t", Room.class);
		List<Room> rooms = q1.getResultList();
		String[] added1 = new String[rooms.size()];
		i = 0;
		for (Room entity : rooms) {
			added1[i] = Integer.toString(entity.getRoomID());
			i = i + 1;
		}
		DataBase.em.getTransaction().commit();
		type1 = new JComboBox<String>(added);
		added_rooms = new JComboBox<String>(added1);
		save1 = new JButton("Сохранить изменения");
		//размещение
		editing1.add(select, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing1.add(added_rooms, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing1.add(status_label1, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing1.add(status1, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing1.add(type_label1, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing1.add(type1, new GridBagConstraints(1, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing1.add(save1, new GridBagConstraints(0, 3, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		save1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				String cb = (String)added_rooms.getSelectedItem();
				String cb1 = (String)status1.getSelectedItem();
				String cb2 = (String)type1.getSelectedItem();
				if (!(cb1.equals("без изменения")) && !(cb2.equals("без изменения"))) {
					DataBase.em.getTransaction().begin();
					Room entity = DataBase.em.find(Room.class, Integer.parseInt(cb));
					entity.setStatus(cb1);
					int ind_space = cb2.indexOf(' ');
					String id = cb2.substring(0, ind_space);
					RoomType roomtp = DataBase.em.find(RoomType.class, Integer.parseInt(id));
					if (!(entity.getType().equals(roomtp))) {
						entity.setType(roomtp);
					} else {
						JOptionPane.showMessageDialog(null, "Этот номер уже является номером данного типа!");
					}
					DataBase.em.getTransaction().commit();
					JOptionPane.showMessageDialog(null, "Готово! Чтобы увидеть изменения в таблице, нажмите на панели кнопку с папкой.");
				} else {
					if (!(cb1.equals("без изменения"))) {
						DataBase.em.getTransaction().begin();
						Room entity = DataBase.em.find(Room.class, Integer.parseInt(cb));
						entity.setStatus(cb1);
						DataBase.em.getTransaction().commit();
						JOptionPane.showMessageDialog(null, "Готово! Чтобы увидеть изменения в таблице, нажмите на панели кнопку с папкой.");
					} else if (!(cb2.equals("без изменения"))) {
						DataBase.em.getTransaction().begin();
						Room entity = DataBase.em.find(Room.class, Integer.parseInt(cb));
						int ind_space = cb2.indexOf(' ');
						String id = cb2.substring(0, ind_space);
						RoomType roomtp = DataBase.em.find(RoomType.class, Integer.parseInt(id));
						if (!(entity.getType().equals(roomtp))) {
							entity.setType(roomtp);
							JOptionPane.showMessageDialog(null, "Готово! Чтобы увидеть изменения в таблице, нажмите на панели кнопку с папкой.");
						} else {
							JOptionPane.showMessageDialog(null, "Этот номер уже является номером данного типа!");
						}
						DataBase.em.getTransaction().commit();
					} else {
						JOptionPane.showMessageDialog(null, "Вы ничего не выбрали!");
					}
				}
			}
		});
		editing1.setVisible(true);
		if (added_rooms.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "Сначала добавьте хотя бы один гостиничный номер!");
			editing1.setVisible(false);
		}
	}
	public void remover1() {
		// Создание окна
		remove1 = new JFrame("Удаление");
		remove1.setSize(450, 150);
		remove1.setLocation(600, 350);
		remove1.setLayout(new GridBagLayout());
		//Обработка компонентов
		select1 = new JLabel("Выберите номер, который желаете удалить: ");
		//добавление в комбобокс имеющихся номеров
		TypedQuery<Room> q1 = DataBase.em.createQuery("select t from Room t", Room.class);
		List<Room> rooms = q1.getResultList();
		String[] added1 = new String[rooms.size()];
		int i = 0;
		for (Room entity : rooms) {
			added1[i] = Integer.toString(entity.getRoomID());
			i = i + 1;
		}
		added_rooms1 = new JComboBox<String>(added1);
		delete_selected1 = new JButton("Удалить выбранный номер");
		delete_all1 = new JButton("Удалить всё");
		//размещение
		remove1.add(select1, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		remove1.add(added_rooms1, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		remove1.add(delete_selected1, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		remove1.add(delete_all1, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		//обработка события нажатия на кнопку удаления выбранного номера		
		delete_selected1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				String cb = (String)added_rooms1.getSelectedItem();
				int ind = added_rooms1.getSelectedIndex();
				DataBase.em.getTransaction().begin();
				Room rm = DataBase.em.find(Room.class, Integer.parseInt(cb));
				DataBase.em.remove(rm);
				added_rooms1.removeItemAt(ind);
				DataBase.em.getTransaction().commit();
				JOptionPane.showMessageDialog(null, "Готово! Чтобы увидеть изменения в таблице, нажмите на панели кнопку с папкой.");
			}
		});
		//обработка события нажатия на кнопку удаления всех номеров
		delete_all1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Object[] options = {"Да","Нет, отказываюсь"};
				int n = JOptionPane.showOptionDialog(null, 
				"Вы уверены, что хотите удалить все номера?", 
				"confirmation", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (n == 0) {
					DataBase.em.getTransaction().begin();
					TypedQuery<Room> q = DataBase.em.createQuery("select t from Room t", Room.class);
					List<Room> rooms = q.getResultList();
					for (Room entity : rooms) {
						DataBase.em.remove(entity);
					}
					DataBase.em.getTransaction().commit();
				}
			}
		});
		remove1.setVisible(true);
		if (added_rooms1.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "Сначала добавьте хотя бы один гостиничный номер!");
			remove1.setVisible(false);
		}
	}
	private String selectCapacity(int capac) {
		String str = new String(); 
		switch (capac) {
		case (1):
			str = "Single";
			break;
		case (2):
			str = "Double";
			break;
		case (3):
			str = "Triple";
			break;
		case (4):
			str = "Four persons";
			break;
		case (5):
			str = "Five persons";
			break;
		}
		return str;
	}
	private int selectCapacity2(String str) {
		int capac = 0; 
		if (str.equals("Single")) {
			capac = 1;
		}
		else if (str.equals("Double")) {
			capac = 2;
		}
		else if (str.equals("Triple")) {
			capac = 3;
		}
		else if (str.equals("Four persons")) {
			capac = 4;
		}
		else {
			capac = 5;
		}
		return capac;
	}
	private int checkPrice (String nPrice) throws NotNumericInput {
		int value = -1;
		try { 
			value = Integer.parseInt(nPrice);
		} 
		catch (NumberFormatException e) { 
			value = -1; 
		}
		if (value == -1) throw new NotNumericInput();
		return value;
	}
	//после нажатия кнопки open обновляет содержимое комбобокса с типами
	public void changeComboBoxCategory() {
		category1.removeAllItems();
		DataBase.em.getTransaction().begin();
		TypedQuery<RoomType> q = DataBase.em.createQuery("select t from RoomType t", RoomType.class);
		List<RoomType> roomtypes = q.getResultList();
		List<String> added_types = new ArrayList<String>();
		category1.addItem("Категория");
		for (RoomType entity : roomtypes) {
			if (!added_types.contains(entity.getRoomClass())) {
				added_types.add(entity.getRoomClass());
				category1.addItem(entity.getRoomClass());
			}
		}
		DataBase.em.getTransaction().commit();
	}
}
