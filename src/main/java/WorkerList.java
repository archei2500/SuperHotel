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

public class WorkerList {
	private DefaultTableModel model4;
	private JButton open_workers;
	private JButton add_worker;
	private JButton edit_worker;
	private JButton delete_worker;
	private JToolBar toolBarWorkers;
	private JScrollPane scroll4;
	private JTable workers_table;
	private JComboBox<String> specialization;
	private JTextField enter_id;
	private JTextField enter_name1;
	private JTextField enter_surname1;
	private JButton filter3;
	
	private JFrame registration;
	private JLabel name_label;
	private JLabel lname_label;
	private JLabel age_label;
	private JLabel position_label;
	private JTextField name_reg;
	private JTextField lname_reg;
	private JTextField age_reg;
	private JComboBox<String> added_positions;
	private JButton registrate;
	
	private JFrame remove;
	private JLabel selection;
	private JComboBox<String> added_workers;
	private JButton delete_selected;
	private JButton delete_all;
	
	private JFrame editing;
	private JLabel selection1;
	private JLabel new_pos_label;
	private JComboBox<String> added_workers1;
	private JComboBox<String> added_positions1;
	private JButton choice;
	private JButton save;
	private Worker selected_worker;
	
	public void create(JPanel workerList) {
		// Создание кнопок и прикрепление иконок
		open_workers = new JButton(new ImageIcon("./img/folder_red_open.png"));
		add_worker = new JButton(new ImageIcon("./img/plus_orange.png"));
	    edit_worker = new JButton(new ImageIcon("./img/edit_icon.png"));
		delete_worker = new JButton(new ImageIcon("./img/trash.png"));
		// Настройка подсказок для кнопок
		open_workers.setToolTipText("Обновить список служащих");
		add_worker.setToolTipText("Добавить служащего");
		edit_worker.setToolTipText("Отредактировать данные служащего");
		delete_worker.setToolTipText("Удалить данные служащего");
		// Добавление кнопок на панель инструментов
		toolBarWorkers = new JToolBar("Панель инструментов");
		toolBarWorkers.add(open_workers);
		toolBarWorkers.add(add_worker);
		toolBarWorkers.add(edit_worker);
		toolBarWorkers.add(delete_worker);
		// Размещение панели инструментов
		workerList.setLayout(new BorderLayout());
		workerList.add(toolBarWorkers, BorderLayout.NORTH);
		// Создание таблицы с данными
		String [] columns4 = {"ID", "Имя", "Фамилия", "Должность"};
		String [][] data4 = {{"1", "Rodion", "Raskolnikov", "Director"}};
		model4 = new DefaultTableModel(data4, columns4);
		workers_table = new JTable(model4);
		workers_table.setAutoCreateRowSorter(true);
		scroll4 = new JScrollPane(workers_table);
		// Размещение таблицы с данными
		workerList.add(scroll4, BorderLayout.CENTER);
		// Подготовка компонентов поиска
		TypedQuery<Position> q = DataBase.em.createQuery("select t from Position t", Position.class);
		List<Position> positions = q.getResultList();
		List<String> added_pos_list = new ArrayList<String>();
		added_pos_list.add("Должность");
		for (Position entity : positions) {
			added_pos_list.add(entity.getName());
		}
		specialization = new JComboBox<String>(added_pos_list.toArray(new String[0]));
		enter_id = new JTextField("Введите id");
		enter_name1 = new JTextField("Введите имя");
		enter_surname1 = new JTextField("Введите фамилию");
		filter3 = new JButton("Поиск");
		// Добавление компонентов на панель
		JPanel filterPanel3 = new JPanel();
		filterPanel3.add(specialization);
		filterPanel3.add(enter_id);
		filterPanel3.add(enter_name1);
		filterPanel3.add(enter_surname1);
		filterPanel3.add(filter3);
		// Размещение панели поиска внизу окна
		workerList.add(filterPanel3, BorderLayout.SOUTH);
		
		open_workers.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				specialization.removeAllItems();
				specialization.addItem("Должность");
				TypedQuery<Position> q1 = DataBase.em.createQuery("select t from Position t", Position.class);
				List<Position> positions = q1.getResultList();
				for (Position entity : positions) {
					specialization.addItem(entity.getName());
				}
				TypedQuery<Worker> q = DataBase.em.createQuery("select t from Worker t", Worker.class);
				List<Worker> workers = q.getResultList();
				int rows = model4.getRowCount();
				for (int i = 0; i < rows; i++) {
					model4.removeRow(0); // Очистка таблицы
				}
				for (Worker entity : workers) {
					String id_cell = Integer.toString(entity.getID());
					String name_cell = entity.getName();
					String lastname_cell = entity.getLastName();
					String pos_cell = entity.getPosition().getName();
					// Запись данных в таблицу
					model4.addRow(new String[]{id_cell, name_cell, lastname_cell, pos_cell});
				}
			}
		} );
		add_worker.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				register();
			}
		} );
		delete_worker.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				remover();
			}
		} );
		edit_worker.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				editor();
			}
		} );
		filter3.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				String cb = (String)specialization.getSelectedItem();
				String id_search = enter_id.getText();
				String name_search = enter_name1.getText();
				String lname_search = enter_surname1.getText();
				if (!((cb.equals("Должность")) && (name_search.contains("Введите имя") || name_search.length() == 0) &&
						(lname_search.contains("Введите фамилию") || lname_search.length() == 0) &&
						(id_search.contains("Введите id") || id_search.length() == 0))) {
					boolean can_search = false;
					TypedQuery<Worker> q = DataBase.em.createQuery("select t from Worker t", Worker.class);
					List<Worker> workers = q.getResultList();
					if (!cb.equals("Должность")) {
						Iterator<Worker> workerIterator = workers.iterator();
						while (workerIterator.hasNext()) {
							Worker nextWorker = workerIterator.next();
							if (!nextWorker.getPosition().getName().equals(cb)) {
								workerIterator.remove();
							}
						}
						can_search = true;
					}
					if (!(name_search.contains("Введите имя") || name_search.length() == 0)) {
						boolean bool = isAlpha(name_search);
						if (bool) {
							name_search = format(name_search);
							Iterator<Worker> workerIterator = workers.iterator();
							while (workerIterator.hasNext()) {
								Worker nextWorker = workerIterator.next();
								if (!nextWorker.getName().equals(name_search)) {
									workerIterator.remove();
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
							Iterator<Worker> workerIterator = workers.iterator();
							while (workerIterator.hasNext()) {
								Worker nextWorker = workerIterator.next();
								if (!nextWorker.getLastName().equals(lname_search)) {
									workerIterator.remove();
								}
							}
							can_search = true;
						}
						else {
							JOptionPane.showMessageDialog(null, "У человека не может быть такой фамилии!");
						}
					}
					if (!(id_search.contains("Введите id") || id_search.length() == 0)) {
						int id_srch = -1;
						try {
							id_srch = checkValue(id_search);
						} catch (NotNumericInput nNumInp) {
							JOptionPane.showMessageDialog(null, nNumInp.getMessage());
						}
						if (id_srch != -1) {
							Iterator<Worker> workerIterator = workers.iterator();
							while (workerIterator.hasNext()) {
								Worker nextWorker = workerIterator.next();
								if (nextWorker.getID() != id_srch) {
									workerIterator.remove();
								}
							}
							can_search = true;
						}
					}
					if (can_search) {
						int rows = model4.getRowCount();
						for (int i = 0; i < rows; i++) {
							model4.removeRow(0); // Очистка таблицы
						}
						for (Worker entity : workers) {
							String id_cell = Integer.toString(entity.getID());
							String name_cell = entity.getName();
							String lastname_cell = entity.getLastName();
							String pos_cell = entity.getPosition().getName();
							// Запись данных в таблицу
							model4.addRow(new String[]{id_cell, name_cell, lastname_cell, pos_cell});
						}
						if (model4.getRowCount() == 0) {
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
		registration = new JFrame("Регистрация служащего");
		registration.setSize(500, 300);
		registration.setLocation(600, 350);
		registration.setLayout(new GridBagLayout());
		//Обработка компонентов
		name_label = new JLabel("Введите имя: ");
		lname_label = new JLabel("Введите фамилию: ");
		age_label = new JLabel("Введите возраст: ");
		position_label = new JLabel("Выберите должность: ");
		name_reg = new JTextField();
		lname_reg = new JTextField();
		age_reg = new JTextField();
		TypedQuery<Position> q = DataBase.em.createQuery("select t from Position t", Position.class);
		List<Position> positions = q.getResultList();
		List<String> added_pos_list = new ArrayList<String>();
		added_pos_list.add("Должность");
		for (Position entity : positions) {
			added_pos_list.add(Integer.toString(entity.getID()) + " " + entity.getName());
		}
		added_positions = new JComboBox<String>(added_pos_list.toArray(new String[0]));
		registrate = new JButton("Добавить");
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
		registration.add(position_label, new GridBagConstraints(0, 3, 1, 1, 1, 1, GridBagConstraints.NORTH,  
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(added_positions, new GridBagConstraints(1, 3, 1, 1, 1, 1, GridBagConstraints.NORTH,  
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(registrate, new GridBagConstraints(0, 4, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			
		registrate.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent ev) {
				String val1 = name_reg.getText();
				String val2 = lname_reg.getText();
				int val3 = -1;
				String str = age_reg.getText();
				String pos = (String)added_positions.getSelectedItem();
				if ((val1.length() != 0) && (val2.length() != 0) && (str.length() != 0) && (!pos.equals("Должность"))) {
					try { 
						val3 = checkValue(str);
					} 
					catch (NotNumericInput nNumInp) {
						JOptionPane.showMessageDialog(null, nNumInp.getMessage());
					}
					if (val3 >= 18) {
						DataBase.em.getTransaction().begin();
						Worker wk = new Worker();
						boolean bool1 = wk.setName(val1);
						boolean bool2 = wk.setLastName(val2);
						if (bool1 && bool2) {
							wk.setAge(val3);
							int ind_space = pos.indexOf(' ');
							String id = pos.substring(0, ind_space);
							Position selected_pos = DataBase.em.find(Position.class, Integer.parseInt(id));
							if (!(selected_pos.getType().equals("managerial") && selected_pos.getWorkers().size() == 1)) {
								wk.setPosition(selected_pos);
								DataBase.em.persist(wk);
								JOptionPane.showMessageDialog(null, "Готово! Чтобы увидеть изменения в таблице, нажмите на панели кнопку с папкой.");
							}
							else {
								JOptionPane.showMessageDialog(null, "Вы не можете добавить на эту должность более одного человека!");
							}
						}
						else {
							JOptionPane.showMessageDialog(null, "Человека не могут так звать!");
						}
						DataBase.em.getTransaction().commit();
					}
					else {
						JOptionPane.showMessageDialog(null, "Лицо должно достичь возраста 18 лет!");
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Вы заполнили не все поля!");
				}	
			}
		} );
		registration.setVisible(true);
		if (added_positions.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "Сначала добавьте хотя бы одну должность!");
			registration.setVisible(false);
		}
	}
	private void remover() {
		// Создание окна
		remove = new JFrame("Увольнение");
		remove.setSize(450, 150);
		remove.setLocation(600, 350);
		remove.setLayout(new GridBagLayout());
		//Обработка компонентов
		selection = new JLabel("Выберите служащего, которого желаете уволить: ");
		TypedQuery<Worker> q = DataBase.em.createQuery("select t from Worker t", Worker.class);
		List<Worker> workers = q.getResultList();
		List<String> added = new ArrayList<String>();
		for (Worker entity : workers) {
			added.add(Integer.toString(entity.getID()) + " " + entity.getName() + " " +  entity.getLastName());
		}
		added_workers = new JComboBox<String>(added.toArray(new String[0]));
		delete_selected = new JButton("Уволить выбранного служащего");
		delete_all = new JButton("Удалить всё");
		//размещение
		remove.add(selection, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		remove.add(added_workers, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		remove.add(delete_selected, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		remove.add(delete_all, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));	
				
		delete_selected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				String cb = (String)added_workers.getSelectedItem();
				int ind = added_workers.getSelectedIndex();
				DataBase.em.getTransaction().begin();
				int ind_space = cb.indexOf(' ');
				String id = cb.substring(0, ind_space);
				Worker del_wk = DataBase.em.find(Worker.class, Integer.parseInt(id));
				DataBase.em.remove(del_wk);
				added_workers.removeItemAt(ind);
				DataBase.em.getTransaction().commit();
				JOptionPane.showMessageDialog(null, "Готово! Чтобы увидеть изменения в таблице, нажмите на панели кнопку с папкой.");
			}
		});
		delete_all.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Object[] options = {"Да","Нет, отказываюсь"};
				int n = JOptionPane.showOptionDialog(null, 
				"Вы уверены, что хотите уволить вообще всех?", 
				"confirmation", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (n == 0) {
					DataBase.em.getTransaction().begin();
					TypedQuery<Worker> q = DataBase.em.createQuery("select t from Worker t", Worker.class);
					List<Worker> workers = q.getResultList();
					for (Worker entity : workers) {
						DataBase.em.remove(entity);
					}
					DataBase.em.getTransaction().commit();
				}
			}
		});
		remove.setVisible(true);
		if (added_workers.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "Сначала добавьте хотя бы одного служащего!");
			remove.setVisible(false);
		}
	}
	public void editor() {
		// Создание окна
		editing = new JFrame("Редактирование");
		editing.setSize(800, 200);
		editing.setLocation(500, 400);
		editing.setLayout(new GridBagLayout());
		//Обработка компонентов
		selection1 = new JLabel("Выберите служащего для редактирования: ");
		new_pos_label = new JLabel("Выберите, какую он(-а) теперь будет занимать должность: ");
		TypedQuery<Worker> q = DataBase.em.createQuery("select t from Worker t", Worker.class);
		List<Worker> workers = q.getResultList();
		List<String> added = new ArrayList<String>();
		for (Worker entity : workers) {
			added.add(Integer.toString(entity.getID()) + " " + entity.getName() + " " +  entity.getLastName());
		}
		added_workers1 = new JComboBox<String>(added.toArray(new String[0]));
		added_positions1 = new JComboBox<String>(new String[] {"без изменения"});
		choice = new JButton("Выбрать");
		save = new JButton("Сохранить изменения");
		//размещение
		editing.add(selection1, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(added_workers1, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(new_pos_label, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(added_positions1, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(choice, new GridBagConstraints(2, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(save, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		choice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				added_positions1.removeAllItems();
				added_positions1.addItem("без изменения");
				String cb = (String)added_workers1.getSelectedItem();
				int ind_space = cb.indexOf(' ');
				String id = cb.substring(0, ind_space);
				selected_worker = DataBase.em.find(Worker.class, Integer.parseInt(id));
				TypedQuery<Position> q1 = DataBase.em.createQuery("select t from Position t", Position.class);
				List<Position> positions = q1.getResultList();
				for (Position entity : positions) {
					if (entity != selected_worker.getPosition()) {
						added_positions1.addItem(Integer.toString(entity.getID()) + " " + entity.getName());
					}
				}
			}
		});
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (selected_worker != null) {
					String cb = (String)added_positions1.getSelectedItem();
					if (!cb.equals("без изменения")) {
						int ind_space = cb.indexOf(' ');
						String id = cb.substring(0, ind_space);
						Position selected_pos = DataBase.em.find(Position.class, Integer.parseInt(id));
						if (!(selected_pos.getType().equals("managerial") && selected_pos.getWorkers().size() == 1)) {
							DataBase.em.getTransaction().begin();
							selected_worker.setPosition(selected_pos);
							DataBase.em.getTransaction().commit();
							JOptionPane.showMessageDialog(null, "Готово! Чтобы увидеть изменения в таблице, нажмите на панели кнопку с папкой.");
						}
						else {
							JOptionPane.showMessageDialog(null, "Вы не можете добавить на эту должность более одного человека!");
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "Вы не выбрали должность, поэтому менять нечего.");
					}
					selected_worker = null;
				}
				else {
					JOptionPane.showMessageDialog(null, "Выберите служащего!");
				}
			}
		});
		editing.setVisible(true);
		if (added_workers1.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "Сначала добавьте хотя бы одного служащего!");
			editing.setVisible(false);
		}
	}
	private int checkValue (String str) throws NotNumericInput {
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
	private boolean isAlpha(String str) {
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
