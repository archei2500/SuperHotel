import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

public class ClientList {
	private DefaultTableModel model2;
	private JButton open_clients;
	private JButton add_client;
	private JButton edit_client;
	private JButton delete_client;
	private JToolBar toolBarClients;
	private JScrollPane scroll2;
	private JTable clients_table;
	private JComboBox<String> day;
	private JComboBox<String> month;
	private JComboBox<String> year;
	private JTextField enter_name;
	private JTextField enter_surname;
	private JTextField enter_number;
	private JButton filter2;
	private JLabel label1;
	
	private JFrame registration;
	private JLabel name_label;
	private JLabel lname_label;
	private JLabel age_label;
	private JLabel rooms_label;
	private JLabel rooms_label1;
	private JLabel date_label;
	private JLabel los_label;
	private JTextField name_reg;
	private JTextField lname_reg;
	private JTextField age_reg;
	private JComboBox<String> added_rooms;
	private JButton add_room;
	private JComboBox<String> day_reg;
	private JComboBox<String> month_reg;
	private JComboBox<String> year_reg;
	private JTextField los_reg;
	private JButton registrate;
	private List<Integer> array;
	
	private JFrame remove;
	private JLabel selection;
	private JComboBox<String> added_clients;
	private JButton delete_selected;
	private JButton delete_all;
	
	private JFrame editing;
	private JLabel selection1;
	private JComboBox<String> added_clients1;
	private JButton choice;
	private JLabel select_new_room;
	private JLabel select_room_todel;
	private JComboBox<String> new_room;
	private JComboBox<String> room_todel;
	private JButton choice1;
	private JButton choice2;
	private JButton save;
	private Client selected_client;
	private List<Integer> array1;
	private List<Integer> array2;
	
	public void create(JPanel clientList) {
		// Создание кнопок и прикрепление иконок
		open_clients = new JButton(new ImageIcon("./img/folder_red_open.png"));
		add_client = new JButton(new ImageIcon("./img/plus_orange.png"));
		edit_client = new JButton(new ImageIcon("./img/edit_icon.png"));
		delete_client = new JButton(new ImageIcon("./img/trash.png"));
		// Настройка подсказок для кнопок
		open_clients.setToolTipText("Обновить список клиентов");
		add_client.setToolTipText("Новый клиент");
		edit_client.setToolTipText("Отредактировать данные клиента");
		delete_client.setToolTipText("Удалить данные клиента");
		// Добавление кнопок на панель инструментов
		toolBarClients = new JToolBar("Панель инструментов");
		toolBarClients.add(open_clients);
		toolBarClients.add(add_client);
		toolBarClients.add(edit_client);
		toolBarClients.add(delete_client);
		// Размещение панели инструментов
		clientList.setLayout(new BorderLayout());
		clientList.add(toolBarClients, BorderLayout.NORTH);
		// Создание таблицы с данными
		String [] columns2 = {"Имя", "Фамилия", "Возраст", "Номера", "Дата заселения", "Срок проживания"};
		String [][] data2 = {{"-", "-", "-", "-", "-", "-"}};
		model2 = new DefaultTableModel(data2, columns2);
		clients_table = new JTable(model2);
		clients_table.setAutoCreateRowSorter(true);
		scroll2 = new JScrollPane(clients_table);
		// Размещение таблицы с данными
		clientList.add(scroll2, BorderLayout.CENTER);
		// Подготовка компонентов поиска
		String[] days = new String[32];
		days[0] = "день";
		for (int i = 1; i < days.length; i++) {
		     days[i] = Integer.toString(i); 
		}
		day = new JComboBox<String>(days);
		String[] months = new String[13];
		months[0] = "месяц";
		for (int i = 1; i < months.length; i++) {
		     months[i] = Integer.toString(i); 
		}
		month = new JComboBox<String>(months);
		year = new JComboBox<String>(new String[] {"год", "2018", "2019", "2020", "2021", "2022"});
		enter_name = new JTextField("Введите имя");
		enter_surname = new JTextField("Введите фамилию");
		enter_number = new JTextField("Номер");
		filter2 = new JButton("Поиск");
		// Добавление компонентов на панель
		JPanel filterPanel2 = new JPanel();
		label1 = new JLabel("Поиск по дате заселения: ");
		filterPanel2.add(label1);
		filterPanel2.add(day);
		filterPanel2.add(month);
		filterPanel2.add(year);
		filterPanel2.add(enter_name);
		filterPanel2.add(enter_surname);
		filterPanel2.add(enter_number);
		filterPanel2.add(filter2);
		// Размещение панели поиска внизу окна
		clientList.add(filterPanel2, BorderLayout.SOUTH);
		
		open_clients.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				TypedQuery<Client> q = DataBase.em.createQuery("select t from Client t", Client.class);
				List<Client> clients = q.getResultList();
				int rows = model2.getRowCount();
				for (int i = 0; i < rows; i++) {
					model2.removeRow(0); // Очистка таблицы
				}
				for (Client entity : clients) {
					String name_cell = entity.getName();
					String lastname_cell = entity.getLastName();
					String age_cell = Integer.toString(entity.getAge());
					//создание строки из номеров данного клиента
					List<RoomClient> room_numbers = entity.getRoomNumbers();
					String roomnums = new String();
					StringBuilder builder = new StringBuilder();
					for (RoomClient room : room_numbers) {
						builder.append(Integer.toString(room.getRoom().getRoomID()) + " ");
					}
					roomnums = builder.toString();
					String day = Integer.toString(entity.getDay());
					String month = Integer.toString(entity.getMonth());
					String year = Integer.toString(entity.getYear());
					String date = day + "." + month + "." + year;
					String length_of_stay = Integer.toString(entity.getLOS());
					// Запись данных в таблицу
					model2.addRow(new String[]{name_cell, lastname_cell, age_cell, roomnums, date, length_of_stay});
				}
			}
		} );
		add_client.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				register();
			}
		} );
		edit_client.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				editor();
			}
		} );
		delete_client.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				remover();
			}
		} );
		filter2.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				String cb1 = (String)day.getSelectedItem();
				String cb2 = (String)month.getSelectedItem();
				String cb3 = (String)year.getSelectedItem();
				String name_search = enter_name.getText();
				String lname_search = enter_surname.getText();
				String room_search = enter_number.getText();
				if (!((cb1.equals("день")) && (cb2.equals("месяц")) && (cb3.equals("год")) 
						&& (name_search.contains("Введите имя") || name_search.length() == 0) &&
						(lname_search.contains("Введите фамилию") || lname_search.length() == 0) &&
						(room_search.contains("Номер") || room_search.length() == 0))) {
					boolean can_search = false;
					TypedQuery<Client> q = DataBase.em.createQuery("select t from Client t", Client.class);
					List<Client> clients = q.getResultList();
					if (!((cb1.equals("день")) && (cb2.equals("месяц")) && (cb3.equals("год")))) {
						if (!((cb1.equals("день")) || (cb2.equals("месяц")) || (cb3.equals("год")))) { //если ввели полную дату
							int day1 = Integer.parseInt(cb1);
							int month1 = Integer.parseInt(cb2);
							int year1 = Integer.parseInt(cb3);
							boolean right_date = checkDate(day1, month1, year1);
							if (right_date) { //если такая дата может существовать
								Iterator<Client> clientIterator = clients.iterator();
								while (clientIterator.hasNext()) {
									Client nextClient = clientIterator.next();
									if (!(nextClient.getDay() == day1 && nextClient.getMonth() == month1 &&
											nextClient.getYear() == year1)) {
										clientIterator.remove();
									}
								}
								can_search = true;
							}
						}
						else if (!((cb2.equals("месяц")) || (cb3.equals("год")))) { //если ввели год и месяц
							int month1 = Integer.parseInt(cb2);
							int year1 = Integer.parseInt(cb3);
							Iterator<Client> clientIterator = clients.iterator();
							while (clientIterator.hasNext()) {
								Client nextClient = clientIterator.next();
								if (!(nextClient.getMonth() == month1 && nextClient.getYear() == year1)) {
									clientIterator.remove();
								}
							}
							can_search = true;
						}
						else if (!cb3.equals("год")) { //если поиск только по году
							int year1 = Integer.parseInt(cb3);
							Iterator<Client> clientIterator = clients.iterator();
							while (clientIterator.hasNext()) {
								Client nextClient = clientIterator.next();
								if (!(nextClient.getYear() == year1)) {
									clientIterator.remove();
								}
							}
							can_search = true;
						}
						else {
							JOptionPane.showMessageDialog(null, "Нет смысла искать по такой дате без уточнения!");
						}
					}
					if (!(name_search.contains("Введите имя") || name_search.length() == 0)) {
						boolean bool = isAlpha(name_search);
						if (bool) {
							name_search = format(name_search);
							Iterator<Client> clientIterator = clients.iterator();
							while (clientIterator.hasNext()) {
								Client nextClient = clientIterator.next();
								if (!nextClient.getName().equals(name_search)) {
									clientIterator.remove();
								}
							}
							can_search = true;
						}
						else {
							JOptionPane.showMessageDialog(null, "Человека не могут так звать!");
						}
					}
					if (!(lname_search.contains("Введите фамилию") || lname_search.length() == 0)) {
						boolean bool = isAlpha(lname_search);
						if (bool) {
							lname_search = format(lname_search);
							Iterator<Client> clientIterator = clients.iterator();
							while (clientIterator.hasNext()) {
								Client nextClient = clientIterator.next();
								if (!nextClient.getLastName().equals(lname_search)) {
									clientIterator.remove();
								}
							}
							can_search = true;
						}
						else {
							JOptionPane.showMessageDialog(null, "У человека не может быть такой фамилии!");
						}
					}
					if (!(room_search.contains("Номер") || room_search.length() == 0)) {
						int rm_srch = -1;
						try {
							rm_srch = checkValue(room_search);
						} catch (NotNumericInput nNumInp) {
							JOptionPane.showMessageDialog(null, nNumInp.getMessage());
						}
						if (rm_srch != -1 && rm_srch > 0) {
							Iterator<Client> clientIterator = clients.iterator();
							while (clientIterator.hasNext()) {
								boolean contains = false;
								Client nextClient = clientIterator.next();
								List<RoomClient> list = nextClient.getRoomNumbers();
								Iterator<RoomClient> roomIterator = list.iterator();
								while (!contains && roomIterator.hasNext()) {
									RoomClient nextRoom = roomIterator.next();
									if (nextRoom.getRoom().getRoomID() == rm_srch) {
										contains = true;
									}
								}
								if (!contains) {
									clientIterator.remove();
								}
							}
							can_search = true;
						}
						else {
							JOptionPane.showMessageDialog(null, "Номер не может быть отрицательным!");
						}
					}
					if (can_search) {
						int rows = model2.getRowCount();
						for (int i = 0; i < rows; i++) {
							model2.removeRow(0); // Очистка таблицы
						}
						for (Client entity : clients) {
							String name_cell = entity.getName();
							String lastname_cell = entity.getLastName();
							String age_cell = Integer.toString(entity.getAge());
							//создание строки из номеров данного клиента
							List<RoomClient> room_numbers = entity.getRoomNumbers();
							String roomnums = new String();
							StringBuilder builder = new StringBuilder();
							for (RoomClient room : room_numbers) {
								builder.append(Integer.toString(room.getRoom().getRoomID()) + " ");
							}
							roomnums = builder.toString();
							String day = Integer.toString(entity.getDay());
							String month = Integer.toString(entity.getMonth());
							String year = Integer.toString(entity.getYear());
							String date = day + "." + month + "." + year;
							String length_of_stay = Integer.toString(entity.getLOS());
							// Запись данных в таблицу
							model2.addRow(new String[]{name_cell, lastname_cell, age_cell, roomnums, date, length_of_stay});
						}
						if (model2.getRowCount() == 0) {
							JOptionPane.showMessageDialog(null, "К сожалению, по вашему запросу не удалось ничего найти :(");
						}
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Вы не настроили параметры поиска!");
				}
			}
		} );
	}
	private void register() {
		// Создание окна
		registration = new JFrame("Регистрация клиента");
		registration.setSize(700, 300);
		registration.setLocation(550, 350);
		registration.setLayout(new GridBagLayout());
		//Обработка компонентов
		name_label = new JLabel("Введите имя: ");
		lname_label = new JLabel("Введите фамилию: ");
		age_label = new JLabel("Введите возраст: ");
		rooms_label = new JLabel("Выберите номер. Для этого нажмите на кнопку добавления: ");
		rooms_label1 = new JLabel("(можно выбрать несколько)");
		date_label = new JLabel("Выберите дату заселения: ");
		los_label = new JLabel("Введите срок проживания: ");
		name_reg = new JTextField();
		lname_reg = new JTextField();
		age_reg = new JTextField();
		los_reg = new JTextField();
		TypedQuery<Room> q = DataBase.em.createQuery("select t from Room t", Room.class);
		List<Room> rooms = q.getResultList();
		List<String> added_rooms_list = new ArrayList<String>();
		added_rooms_list.add("Номер");
		for (Room entity : rooms) {
			added_rooms_list.add(Integer.toString(entity.getRoomID()));
		}
		added_rooms = new JComboBox<String>(added_rooms_list.toArray(new String[0]));
		String[] days = new String[31];
		for (int i = 0; i < days.length; i++) {
		     days[i] = Integer.toString(i + 1); 
		}
		day_reg = new JComboBox<String>(days);
		String[] months = new String[12];
		for (int i = 0; i < months.length; i++) {
		     months[i] = Integer.toString(i + 1); 
		}
		month_reg = new JComboBox<String>(months);
		year_reg = new JComboBox<String>(new String[] {"2018", "2019", "2020", "2021", "2022"});
		add_room = new JButton("Добавить");
		registrate = new JButton("Зарегистрировать");
		//размещение
		registration.add(name_label, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(name_reg, new GridBagConstraints(1, 0, 2, 1, 1, 1, GridBagConstraints.NORTH,  
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(lname_label, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(lname_reg, new GridBagConstraints(1, 1, 2, 1, 1, 1, GridBagConstraints.NORTH,  
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(age_label, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(age_reg, new GridBagConstraints(1, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(rooms_label, new GridBagConstraints(0, 3, 1, 1, 1, 1, GridBagConstraints.NORTH,  
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(added_rooms, new GridBagConstraints(1, 3, 1, 1, 1, 1, GridBagConstraints.NORTH,  
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(add_room, new GridBagConstraints(2, 3, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(rooms_label1, new GridBagConstraints(0, 4, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(date_label, new GridBagConstraints(0, 5, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(day_reg, new GridBagConstraints(1, 5, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(month_reg, new GridBagConstraints(2, 5, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(year_reg, new GridBagConstraints(3, 5, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(los_label, new GridBagConstraints(0, 6, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(los_reg, new GridBagConstraints(1, 6, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(registrate, new GridBagConstraints(0, 7, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		array = new ArrayList<Integer>();
		add_room.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent ev) {
				String cb = (String)added_rooms.getSelectedItem();
				int ind = added_rooms.getSelectedIndex();
				if (!cb.equals("Номер")) {
					array.add(Integer.parseInt(cb));
					added_rooms.removeItemAt(ind);
				}
				else {
					JOptionPane.showMessageDialog(null, "Вы не выбрали номер! Сначала выберите.");
				}
			}
		} );
		registrate.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent ev) {
				String val1 = name_reg.getText();
				String val2 = lname_reg.getText();
				int val3 = -1;
				int val4 = -1;
				String str1 = age_reg.getText();
				int day1 = Integer.parseInt((String)day_reg.getSelectedItem());
				int month1 = Integer.parseInt((String)month_reg.getSelectedItem());
				int year1 = Integer.parseInt((String)year_reg.getSelectedItem());
				// проверка даты на правильность
				boolean right_date = checkDate(day1, month1, year1);
				if (right_date) {
					String str2 = los_reg.getText();
					if ((val1.length() != 0) && (val2.length() != 0) && (str1.length() != 0) && (str2.length() != 0)) {
						try { 
							val3 = checkValue(str1);
						} 
						catch (NotNumericInput nNumInp) {
							JOptionPane.showMessageDialog(null, nNumInp.getMessage());
						}
						try { 
							val4 = checkValue(str2);
						} 
						catch (NotNumericInput nNumInp) {
							JOptionPane.showMessageDialog(null, nNumInp.getMessage());
						}
						if (val3 > 0 && val4 > 0) {
							DataBase.em.getTransaction().begin();
							Client cl = new Client();
							boolean bool1 = cl.setName(val1);
							boolean bool2 = cl.setLastName(val2);
							if (bool1 && bool2) {
								boolean bool3 = cl.setAge(val3);
								if (bool3) {
									cl.setLOS(val4);
									if (!array.isEmpty()) {
										int length = array.size(); //узнаём количество выбранных номеров
										TypedQuery<Client> q = DataBase.em.createQuery("select t from Client t", Client.class);
										List<Client> clients = q.getResultList();
										if (!clients.isEmpty()) { //если в базе уже есть клиенты
											boolean inters = false;
											Iterator<Integer> listIterator = array.iterator();
											while (listIterator.hasNext()) { //цикл по выбранным комнатам
												Integer nextRoom = listIterator.next();
												int interscounter = 0;
												for (Client entity : clients) {
													List<RoomClient> rooms = entity.getRoomNumbers();
													for (RoomClient ent : rooms) {
														if (ent.getRoom().getRoomID() == nextRoom) {
															inters = dateIntersection(day1, month1, year1, entity);
															if (inters) {
																interscounter = interscounter + 1;
															}
														}
													}
												}
												Room rm = DataBase.em.find(Room.class, nextRoom);
												if (interscounter > 0) {
													if (rm.getType().getCapacity() > interscounter) {
														Object[] options = {"Да","Нет, отказываюсь"};
														int n = JOptionPane.showOptionDialog(null, 
														"В этом номере уже кто-то живёт/жил в то время. Вы уверены?", 
														"confirmation", 
														JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
														if (n == 0) {
															RoomClient rc = new RoomClient();
															rc.setRoom(rm);
															cl.addRoomNumber(rc);
															added_rooms.addItem(Integer.toString(nextRoom));
															listIterator.remove();
														}
													}
													else {
														JOptionPane.showMessageDialog(null, "Вы не можете заселить клиента в номер " 
																+ Integer.toString(nextRoom) + ", потому что в нём на данный момент нет свободных мест!");
													}
												}
												else {
													RoomClient rc = new RoomClient();
													rc.setRoom(rm);
													cl.addRoomNumber(rc);
													added_rooms.addItem(Integer.toString(nextRoom));
													listIterator.remove();
												}
											}
										}
										else { //если это первый клиент (без проверок)
											Iterator<Integer> listIterator = array.iterator();
											while (listIterator.hasNext()) {
												Integer nextRoom = listIterator.next();
												Room rm = DataBase.em.find(Room.class, nextRoom);
												RoomClient rc = new RoomClient();
												rc.setRoom(rm);
												cl.addRoomNumber(rc);
												added_rooms.addItem(Integer.toString(nextRoom));
												listIterator.remove();
											}
										}
										if (array.size() != length) {
											cl.setDay(day1);
											cl.setMonth(month1);
											cl.setYear(year1);
											DataBase.em.persist(cl);
											JOptionPane.showMessageDialog(null, "Готово! Чтобы увидеть изменения в таблице, нажмите на панели кнопку с папкой.");
											List<RoomClient> rclist = cl.getRoomNumbers();
											for (RoomClient rc : rclist) {
												DataBase.em.persist(rc);
											}
										}
										if (array.size() != 0) {
											Iterator<Integer> listIterator = array.iterator();
											while (listIterator.hasNext()) {
												Integer nextRoom = listIterator.next();
												added_rooms.addItem(Integer.toString(nextRoom));
												listIterator.remove();
											}
										}
									}
									else {
										JOptionPane.showMessageDialog(null, "Вы не выбрали номер!");
									}
								}
								else {
									JOptionPane.showMessageDialog(null, "К сожалению, в таком возрасте заселение без сопровождения родителей недопустимо!");
								}
							}
							else {
								JOptionPane.showMessageDialog(null, "Человека не могут так звать!");
							}
							DataBase.em.getTransaction().commit();
						}
						else {
							JOptionPane.showMessageDialog(null, "Числа должны быть положительными!");
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "Вы заполнили не все поля!");
					}
				}
			}
		} );
		registration.setVisible(true);
		if (added_rooms.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "Сначала добавьте хотя бы один гостиничный номер!");
			registration.setVisible(false);
		}
	}
	private void editor() {
		// Создание окна
		editing = new JFrame("Редактирование");
		editing.setSize(500, 300);
		editing.setLocation(600, 350);
		editing.setLayout(new GridBagLayout());
		//Обработка компонентов
		selection1 = new JLabel("Выберите клиента и нажмите на кнопку: ");
		select_new_room = new JLabel("Выберите номера для добавления: ");
		select_room_todel = new JLabel("И/или выберите номера для удаления: ");
		DataBase.em.getTransaction().begin();
		TypedQuery<Client> q = DataBase.em.createQuery("select t from Client t", Client.class);
		List<Client> clients = q.getResultList();
		DataBase.em.getTransaction().commit();
		String[] added = new String[clients.size()];
		int i = 0;
		for (Client entity : clients) {
			added[i] = Integer.toString(entity.getID()) + " " + entity.getName() + " " + entity.getLastName();
			i = i + 1;
		}
		added_clients1 = new JComboBox<String>(added);
		choice = new JButton("Выбрать");
		new_room = new JComboBox<String>(new String[] {"Номер"});
		room_todel = new JComboBox<String>(new String[] {"Номер"});
		choice1 = new JButton("Добавить");
		choice2 = new JButton("Удалить");
		save = new JButton("Сохранить изменения");
		//размещение
		editing.add(selection1, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(added_clients1, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(choice, new GridBagConstraints(2, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(select_new_room, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(new_room, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(choice1, new GridBagConstraints(2, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(select_room_todel, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(room_todel, new GridBagConstraints(1, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(choice2, new GridBagConstraints(2, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(save, new GridBagConstraints(0, 3, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		array1 = new ArrayList<Integer>();
		array2 = new ArrayList<Integer>();
		choice.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				array1.clear();
				array2.clear();
				room_todel.removeAllItems();
				room_todel.addItem("Номер");
				new_room.removeAllItems();
				new_room.addItem("Номер");
				String cb = (String)added_clients1.getSelectedItem();
				int ind_space = cb.indexOf(' ');
				String id = cb.substring(0, ind_space);
				Client cl = DataBase.em.find(Client.class, Integer.parseInt(id));
				List<RoomClient> room_numbers = cl.getRoomNumbers();
				List<Integer> room_ids = new ArrayList<Integer>();
				for (RoomClient rc : room_numbers) {
					room_todel.addItem(Integer.toString(rc.getRoom().getRoomID()));
					room_ids.add(rc.getRoom().getRoomID());
				}
				TypedQuery<Room> q1 = DataBase.em.createQuery("select t from Room t", Room.class);
				List<Room> rooms = q1.getResultList();
				for (Room entity : rooms) {
					if (!room_ids.contains(entity.getRoomID())) {
						new_room.addItem(Integer.toString(entity.getRoomID()));
					}
				}
				selected_client = cl; 
			}
		} );
		choice1.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				String cb = (String)new_room.getSelectedItem();
				int ind = new_room.getSelectedIndex();
				if (!cb.equals("Номер")) {
					array1.add(Integer.parseInt(cb));
					new_room.removeItemAt(ind);
				}
				else {
					JOptionPane.showMessageDialog(null, "Вы не выбрали номер! Сначала выберите.");
				}
			}
		} );
		choice2.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				String cb = (String)room_todel.getSelectedItem();
				int ind = room_todel.getSelectedIndex();
				if (!cb.equals("Номер")) {
					array2.add(Integer.parseInt(cb));
					room_todel.removeItemAt(ind);
				}
				else {
					JOptionPane.showMessageDialog(null, "Вы не выбрали номер! Сначала выберите.");
				}
			}
		} );
		save.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				if (selected_client != null) {
					if (!(array1.isEmpty() && array2.isEmpty())) {
						Client cl = DataBase.em.find(Client.class, selected_client.getID());
						//если выбрали добавлять номера
						if (!array1.isEmpty()) {
							TypedQuery<Client> q = DataBase.em.createQuery("select t from Client t", Client.class);
							List<Client> clients = q.getResultList();
							clients.remove(selected_client);
							if (!clients.isEmpty()) { //если в базе уже есть клиенты
								boolean inters = false;
								Iterator<Integer> listIterator = array1.iterator();
								while (listIterator.hasNext()) {
									Integer nextRoom = listIterator.next();
									int interscounter = 0;
									for (Client entity : clients) {
										List<RoomClient> rooms = entity.getRoomNumbers();
										for (RoomClient ent : rooms) {
											if (ent.getRoom().getRoomID() == nextRoom) {
												inters = dateIntersection(cl.getDay(), 
														cl.getMonth(), cl.getYear(), entity);
												if (inters) {
													interscounter = interscounter + 1;
												}
											}
										}
									}
									Room rm = DataBase.em.find(Room.class, nextRoom);
									if (interscounter > 0) {
										if (rm.getType().getCapacity() > interscounter) {
											Object[] options = {"Да","Нет, отказываюсь"};
											int n = JOptionPane.showOptionDialog(null, 
											"В этом номере уже кто-то живёт/жил в то время. Вы уверены?", 
											"confirmation", 
											JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
											if (n == 0) {
												DataBase.em.getTransaction().begin();
												RoomClient rc = new RoomClient();
												rc.setRoom(rm);
												cl.addRoomNumber(rc);
												DataBase.em.persist(rc);
												listIterator.remove();
												DataBase.em.getTransaction().commit();
											}
										}
										else {
											JOptionPane.showMessageDialog(null, "Вы не можете заселить клиента в номер " 
													+ Integer.toString(nextRoom) + ", потому что в нём на данный момент нет свободных мест!");
										}
									}
									else {
										DataBase.em.getTransaction().begin();
										RoomClient rc = new RoomClient();
										rc.setRoom(rm);
										cl.addRoomNumber(rc);
										DataBase.em.persist(rc);
										listIterator.remove();
										DataBase.em.getTransaction().commit();
									}
								}
							}
							else { //если это единственный клиент (без проверок)
								Iterator<Integer> listIterator = array.iterator();
								DataBase.em.getTransaction().begin();
								while (listIterator.hasNext()) {
									Integer nextRoom = listIterator.next();
									Room rm = DataBase.em.find(Room.class, nextRoom);
									RoomClient rc = new RoomClient();
									rc.setRoom(rm);
									cl.addRoomNumber(rc);
									DataBase.em.persist(rc);
									listIterator.remove();
								}
								DataBase.em.getTransaction().commit();
							}
							if (array1.size() != 0) {
								Iterator<Integer> listIterator = array.iterator();
								while (listIterator.hasNext()) {
									listIterator.next();
									listIterator.remove();
								}
							}
						}
						//если выбрали удалять номера
						if (!array2.isEmpty()) {
							DataBase.em.getTransaction().begin();
							List<RoomClient> room_nums = cl.getRoomNumbers();
							Iterator<RoomClient> listIterator = room_nums.iterator();
							int i = 0;
							while (listIterator.hasNext()) {
								RoomClient nextRoom = listIterator.next();
								if (array2.contains(nextRoom.getRoom().getRoomID())) {
									Iterator<Integer> lstIterator = array2.iterator();
									while (lstIterator.hasNext()) {
										Integer numb = lstIterator.next();
										if (numb == nextRoom.getRoom().getRoomID()) {
											lstIterator.remove();
										}
									}
									DataBase.em.remove(cl.getRoomNumbers().get(i));
								}
								i = i + 1;
							}
							DataBase.em.getTransaction().commit();
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "Вы не выбрали ни одного номера!");
					}
					selected_client = null;
				}
				else {
					JOptionPane.showMessageDialog(null, "Выберите клиента!");
				}
			}
		} );
		editing.setVisible(true);
		if (added_clients1.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "Сначала добавьте хотя бы одного клиента!");
			editing.setVisible(false);
		}
	}
	private void remover() {
		// Создание окна
		remove = new JFrame("Удаление");
		remove.setSize(450, 150);
		remove.setLocation(600, 350);
		remove.setLayout(new GridBagLayout());
		//Обработка компонентов
		selection = new JLabel("Выберите клиента, которого желаете удалить: ");
		TypedQuery<Client> q = DataBase.em.createQuery("select t from Client t", Client.class);
		List<Client> clients = q.getResultList();
		String[] added = new String[clients.size()];
		int i = 0;
		for (Client entity : clients) {
			added[i] = Integer.toString(entity.getID()) + " " + entity.getName() + " " + entity.getLastName();
			i = i + 1;
		}
		added_clients = new JComboBox<String>(added);
		delete_selected = new JButton("Удалить данные выбранного клиента");
		delete_all = new JButton("Удалить всё");
		//размещение
		remove.add(selection, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		remove.add(added_clients, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		remove.add(delete_selected, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		remove.add(delete_all, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
						
		delete_selected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				String cb = (String)added_clients.getSelectedItem();
				int ind = added_clients.getSelectedIndex();
				DataBase.em.getTransaction().begin();
				int ind_space = cb.indexOf(' ');
				String id = cb.substring(0, ind_space);
				Client cl = DataBase.em.find(Client.class, Integer.parseInt(id));
				DataBase.em.remove(cl);
				DataBase.em.getTransaction().commit();
				added_clients.removeItemAt(ind);
				JOptionPane.showMessageDialog(null, "Готово! Чтобы увидеть изменения в таблице, нажмите на панели кнопку с папкой.");
			}
		});
		delete_all.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Object[] options = {"Да","Нет, отказываюсь"};
				int n = JOptionPane.showOptionDialog(null, 
				"Вы уверены, что хотите удалить всех клиентов?", 
				"confirmation", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (n == 0) {
					DataBase.em.getTransaction().begin();
					TypedQuery<Client> q = DataBase.em.createQuery("select t from Client t", Client.class);
					List<Client> clients = q.getResultList();
					for (Client entity : clients) {
						DataBase.em.remove(entity);
					}
					DataBase.em.getTransaction().commit();
				}
			}
		});
		remove.setVisible(true);
		if (added_clients.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "Сначала добавьте хотя бы одного клиента!");
			remove.setVisible(false);
		}
	}
	public static int checkValue (String str) throws NotNumericInput {
		int value = -1;
		try { 
			value = Integer.parseInt(str);
		} 
		catch (NumberFormatException e) { 
			value = -1; 
		}
		if (value == -1) throw new NotNumericInput();
		return value;
	}
	private boolean checkDate(int day, int month, int year) {
		boolean flag = true;
		if (day > 29 && month == 2 && year == 2020) {
			JOptionPane.showMessageDialog(null, "В феврале этого года нет такого дня!");
			flag = false;
		}
		else if (day > 28 && month == 2 && year == 2020) {
			JOptionPane.showMessageDialog(null, "В феврале этого года нет такого дня!");
			flag = false;
		}
		else {
			if (day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) {
				JOptionPane.showMessageDialog(null, "В этом месяце нет такого дня!");
				flag = false;
			}
		}
		return flag;
	}
	private boolean dateIntersection (int day, int month, int year, Client client) {
		boolean flag = false;
		int date1day;
		int date1month;
		int date1year;
		int date2day;
		int date2month;
		int date2year;
		
		date1day = client.getDay();
		date1month = client.getMonth();
		date1year = client.getYear();
		date2day = date1day + client.getLOS();
		int[] datelist = recDateSum(date2day, date1month, date1year);
		date2day = datelist[0];
		date2month = datelist[1];
		date2year = datelist[2];
		
		if (date1year == date2year) {
			if (year == date1year) {
				if (date1month == date2month) {
					if (month == date1month) {
						if (day >= date1day && day < date2day) {
							flag = true;
						}
					}
				}
				else {
					if (month > date1month && month < date2month) {
						flag = true;
					}
					else if (month == date1month && day >= date1day) {
						flag = true;
					}
					else if (month == date2month && day < date2day) {
						flag = true;
					}
				}
			}
		}
		else {
			if (year > date1year && year < date2year) {
				flag = true;
			}
			else if (year == date1year) {
				if (month == date1month && day >= date1day) {
					flag = true;
				}
				else if (month > date1month) {
					flag = true;
				}
			}
			else if (year == date2year) {
				if (month == date2month && day < date1day) {
					flag = true;
				}
				else if (month < date2month) {
					flag = true;
				}
			}
		}
		return flag;
	}
	private int[] recDateSum (int day, int month, int year) {
		int[] list = new int[3];
		if (day > 29 && month == 2 && year == 2020) {
			day = day - 29;
			month = month + 1;
			list = recDateSum(day, month, year);
		}
		else {
			if (day > 28 && month == 2) {
				day = day - 28;
				month = month + 1;
				list = recDateSum(day, month, year);
			}
			else if (day > 30 && (month == 4 || month == 6 || month == 9 || month == 11)) {
				day = day - 30;
				month = month + 1;
				list = recDateSum(day, month, year);
			}
			else if (day > 31 && month == 12) {
				day = day - 31;
				month = 1;
				year = year + 1;
				list = recDateSum(day, month, year);
			}
			else if (day > 31) {
				day = day - 31;
				month = month + 1;
				list = recDateSum(day, month, year);
			}
			else {
				list[0] = day;
				list[1] = month;
				list[2] = year;
			}
		}
		return list;
	}
	public static boolean isAlpha(String str) {
	    char[] chars = str.toCharArray();
	    for (char c : chars) {
	        if(!Character.isLetter(c)) {
	            return false;
	        }
	    }
	    return true;
	}
	private String format(String str) {
		String form = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
		return form;
	}
}
