import javax.persistence.*;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.List;

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

import org.apache.log4j.Logger;

public class PriceList {
	private static final Logger log = Logger.getLogger(PriceList.class);
	
	private DefaultTableModel model3;
	private JButton open_pricelist;
	private JButton add_type;
	private JButton edit_type;
	private JButton delete_type;
	private JToolBar toolBarTypes;
	private JScrollPane scroll3;
	private JTable prices_table;
	private JButton export_pdf_pricelist;
	
	private JFrame registration;
	private JLabel category_label;
	private JLabel capacity_label;
	private JLabel capac;
	private JLabel price_label;
	private JTextField category;
	private JComboBox<String> capacity;
	private JTextField price;
	private JButton add;
	
	private JFrame remove;
	private JLabel selection;
	private JComboBox<String> added_types;
	private JButton delete_selected;
	private JButton delete_all;
	
	private JFrame editing;
	private JLabel selection1;
	private JLabel new_price_label;
	private JComboBox<String> added_types1;
	private JTextField new_price;
	private JButton save;
	
	public void create(JPanel priceList, JFrame dataBase) {
		// Создание кнопок и прикрепление иконок
		open_pricelist = new JButton(new ImageIcon("./img/folder_red_open.png"));
		add_type = new JButton(new ImageIcon("./img/plus_orange.png"));
		edit_type = new JButton(new ImageIcon("./img/edit_icon.png"));
		delete_type = new JButton(new ImageIcon("./img/trash.png"));
		export_pdf_pricelist = new JButton(new ImageIcon("./img/savepdf.png"));
		// Настройка подсказок для кнопок
		open_pricelist.setToolTipText("Обновить прейскурант цен");
		add_type.setToolTipText("Добавить тип номера и цену");
		edit_type.setToolTipText("Изменить цену");
		delete_type.setToolTipText("Удалить тип номера с его ценой");
		export_pdf_pricelist.setToolTipText("Сохранить прейскурант в PDF");
		// Добавление кнопок на панель инструментов
		toolBarTypes = new JToolBar("Панель инструментов");
		toolBarTypes.add(open_pricelist);
		toolBarTypes.add(add_type);
		toolBarTypes.add(edit_type);
		toolBarTypes.add(delete_type);
		toolBarTypes.add(export_pdf_pricelist);
		// Размещение панели инструментов
		priceList.setLayout(new BorderLayout());
		priceList.add(toolBarTypes, BorderLayout.NORTH);
		// Создание таблицы с данными
		String [] columns3 = {"Категория", "Вместимость", "Стоимость", "Номера"};
		String [][] data3 = {{"-", "-", "-", "-"}}; 
		model3 = new DefaultTableModel(data3, columns3);
		prices_table = new JTable(model3);
		prices_table.setAutoCreateRowSorter(true);
		scroll3 = new JScrollPane(prices_table);
		// Размещение таблицы с данными
		priceList.add(scroll3, BorderLayout.CENTER);
		
		open_pricelist.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				TypedQuery<RoomType> q = DataBase.em.createQuery("select t from RoomType t", RoomType.class);
				List<RoomType> roomtypes = q.getResultList();
				int rows = model3.getRowCount();
				for (int i = 0; i < rows; i++) {
					model3.removeRow(0); // Очистка таблицы
				}
				log.info("Отображение полученных из базы данных в таблице");
				for (RoomType entity : roomtypes) {
					String numtype = entity.getRoomClass();
					String persons = Integer.toString(entity.getCapacity());
					String pr = Integer.toString(entity.getCost());
					//создание строки из номеров данного типа
					List<Room> room_numbers = entity.getRoomNumbers();
					String roomnums = new String();
					StringBuilder builder = new StringBuilder();
					for (Room room : room_numbers) {
						builder.append(Integer.toString(room.getRoomID()) + " ");
					}
					roomnums = builder.toString();
					// Запись данных в таблицу
					model3.addRow(new String[]{numtype, persons, pr, roomnums});
				}
			}
		} );
		add_type.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				register();
			}
		} );
		edit_type.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				editor();
			}
		} );
		delete_type.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				remover();
			}
		} );
		export_pdf_pricelist.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				PDFReportPriceList obj = new PDFReportPriceList();
				log.info("Попытка генерации отчёта");
				try {
					obj.main(model3);
					log.info("Отчёт сгенерирован");
					JOptionPane.showMessageDialog(null, "PDF-отчёт сгенерирован!");
				}
				catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		} );
	}
	public void register() {
		// Создание окна
		registration = new JFrame("Добавление типа");
		registration.setSize(500, 200);
		registration.setLocation(600, 350);
		registration.setLayout(new GridBagLayout());
		//Обработка компонентов
		category_label = new JLabel("Введите категорию: ");
		capacity_label = new JLabel("Выберите вместимость: ");
		capac = new JLabel("человек(-а)");
		price_label = new JLabel("Укажите стоимость (за сутки): ");
		category = new JTextField();
		capacity = new JComboBox<String>(new String[] {"1", "2", "3", "4", "5"});
		price = new JTextField();
		add = new JButton("Добавить");
		//размещение
		registration.add(category_label, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(category, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(capacity_label, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(capacity, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(capac, new GridBagConstraints(2, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(price_label, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(price, new GridBagConstraints(1, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(add, new GridBagConstraints(0, 3, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				String val1 = category.getText();
				String cb = (String)capacity.getSelectedItem();
				int val2 = Integer.parseInt(cb);
				try { 
					checkPrice(price);
				}
				catch(NullPointerException ex) {
					JOptionPane.showMessageDialog(registration, ex.toString());
				}
				catch(MyException myEx) {
					JOptionPane.showMessageDialog(null, myEx.getMessage());
				}
				catch(NotNumericInput nNumInp) {
					JOptionPane.showMessageDialog(null, nNumInp.getMessage());
				}
				String nPrice = price.getText();
				int val3;
				try { 
					val3 = Integer.parseInt(nPrice);
				} 
				catch (NumberFormatException e) { 
					val3 = -1; 
				}
				//Начало транзакции. заполнение полей объекта и добавление в БД
				DataBase.em.getTransaction().begin();
				RoomType rt = new RoomType();
			    boolean bool1 = rt.setRoomClass(val1); 
			    rt.setCapacity(val2);
			    boolean bool2 = rt.setCost(val3);
			    if (!(bool1) || !(bool2)) { //если категория или стоимость не установились
			    	if (!(bool1)) {
			    		JOptionPane.showMessageDialog(registration, "Вы ввели некорректные данные!");
			    	} else {
			    		JOptionPane.showMessageDialog(registration, "Цена слишком большая или слишком маленькая!");
			    	}
			    } else {
			    	boolean already_added = false;
			    	TypedQuery<RoomType> q = DataBase.em.createQuery("select t from RoomType t", RoomType.class);
					List<RoomType> roomtypes = q.getResultList();
			    	for (RoomType entity : roomtypes) {
			    		if (rt.getRoomClass().equals(entity.getRoomClass()) && rt.getCapacity() == entity.getCapacity()) {
			    			already_added = true;
			    		}
			    	}
			    	if (!already_added) {
			    		DataBase.em.persist(rt);
			    		JOptionPane.showMessageDialog(null, "Готово! Чтобы увидеть изменения в таблице, нажмите на панели кнопку с папкой.");
			    	} else {
			    		JOptionPane.showMessageDialog(editing, "Вы не можете добавить этот тип номеров, потому что он уже существует!");
			    	}
			    }
			    DataBase.em.getTransaction().commit();
			}
		});
		registration.setVisible(true);
	}
	public void editor() {
		// Создание окна
		editing = new JFrame("Редактирование");
		editing.setSize(500, 200);
		editing.setLocation(600, 350);
		editing.setLayout(new GridBagLayout());
		//Обработка компонентов
		selection1 = new JLabel("Выберите тип для редактирования: ");
		new_price_label = new JLabel("Введите новую стоимость: ");
		new_price = new JTextField();
		//заполнение combobox добавленными типами номеров
		TypedQuery<RoomType> q = DataBase.em.createQuery("select t from RoomType t", RoomType.class);
		List<RoomType> roomtypes = q.getResultList();
		String[] added = new String[roomtypes.size()];
		int i = 0;
		for (RoomType entity : roomtypes) {
			added[i] = Integer.toString(entity.getTypeID()) + " " + entity.getRoomClass() + " with capacity " + Integer.toString(entity.getCapacity());
			i = i + 1;
		}
		added_types1 = new JComboBox<String>(added);
		save = new JButton("Сохранить изменения");
		//размещение
		editing.add(selection1, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(added_types1, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(new_price_label, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(new_price, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(save, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		//добавление слушателя для кнопки сохранения
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				String cb = (String)added_types1.getSelectedItem();
				log.warn("Возможно возникновение исключительной ситуации");
				log.info("Проверка на успешный ввод новой стоимости");
				try { 
					checkPrice(new_price);
				}
				catch(NullPointerException ex) {
					JOptionPane.showMessageDialog(editing, ex.toString());
				}
				catch(MyException myEx) {
					JOptionPane.showMessageDialog(null, myEx.getMessage());
				}
				catch(NotNumericInput nNumInp) {
					JOptionPane.showMessageDialog(null, nNumInp.getMessage());
				}
				String nPrice = new_price.getText();
				int val;
				try { 
					val = Integer.parseInt(nPrice);
				} 
				catch (NumberFormatException e) { 
					val = -1; 
				}
				if (val != -1) {
					if (val > 0) {
						//Поиск выбранного типа по id в базе данных и установка новой стоимости
						DataBase.em.getTransaction().begin();
						int ind_space = cb.indexOf(' ');
						String id = cb.substring(0, ind_space);
						RoomType rt = DataBase.em.find(RoomType.class, Integer.parseInt(id));
						boolean bool = rt.setCost(val);
					    if (!(bool)) {
					    	JOptionPane.showMessageDialog(editing, "Цена слишком большая или слишком маленькая!");
					    } else {
					    	log.info("Стоимость успешно изменена");
					    	JOptionPane.showMessageDialog(null, "Готово! Чтобы увидеть изменения в таблице, нажмите на панели кнопку с папкой.");
					    }
						DataBase.em.getTransaction().commit();
					} else {
						JOptionPane.showMessageDialog(editing, "Цена не может быть отрицательной!");
					}
				}
			}
		});
		log.info("Открытие экранной формы");
		editing.setVisible(true);
		if (added_types1.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "Сначала добавьте хотя бы один тип!");
			editing.setVisible(false);
		}
	}
	public void remover() {
		// Создание окна
		remove = new JFrame("Удаление");
		remove.setSize(450, 150);
		remove.setLocation(600, 350);
		remove.setLayout(new GridBagLayout());
		//Обработка компонентов
		selection = new JLabel("Выберите тип, который желаете удалить: ");
		TypedQuery<RoomType> q = DataBase.em.createQuery("select t from RoomType t", RoomType.class);
		List<RoomType> roomtypes = q.getResultList();
		String[] added = new String[roomtypes.size()];
		int i = 0;
		for (RoomType entity : roomtypes) {
			added[i] = Integer.toString(entity.getTypeID()) + " " + entity.getRoomClass() + " with capacity " + Integer.toString(entity.getCapacity());
			i = i + 1;
		}
		added_types = new JComboBox<String>(added);
		delete_selected = new JButton("Удалить выбранный тип");
		delete_all = new JButton("Удалить всё");
		//размещение
		remove.add(selection, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		remove.add(added_types, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		remove.add(delete_selected, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		remove.add(delete_all, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		//добавление слушателя для кнопки удаления выбранного типа 
		delete_selected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				String cb = (String)added_types.getSelectedItem();
				int i = added_types.getSelectedIndex();
				//Поиск по id и удаление выбранного типа из БД
				DataBase.em.getTransaction().begin();
				int ind_space = cb.indexOf(' ');
				String id = cb.substring(0, ind_space);
				RoomType entity = DataBase.em.find(RoomType.class, Integer.parseInt(id));
				DataBase.em.remove(entity);
				added_types.removeItemAt(i);
				DataBase.em.getTransaction().commit();
				JOptionPane.showMessageDialog(null, "Готово! Чтобы увидеть изменения в таблице, нажмите на панели кнопку с папкой.");
			}
		});
		//добавление слушателя для кнопки удаления всех типов
		delete_all.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Object[] options = {"Да","Нет, отказываюсь"};
				int n = JOptionPane.showOptionDialog(null, 
				"Вы уверены, что хотите удалить все типы номеров?", 
				"confirmation", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (n == 0) {
					DataBase.em.getTransaction().begin();
					TypedQuery<RoomType> q = DataBase.em.createQuery("select t from RoomType t", RoomType.class);
					List<RoomType> roomtypes = q.getResultList();
					for (RoomType entity : roomtypes) {
						DataBase.em.remove(entity);
					}
					DataBase.em.getTransaction().commit();
				}
			}
		});
		remove.setVisible(true);
		if (added_types.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "Сначала добавьте хотя бы один тип!");
			remove.setVisible(false);
		}
	}
	//Проверка на правильный ввод цены в текстовое поле
	private void checkPrice (JTextField pr) throws MyException, NotNumericInput, NullPointerException {
		String nPrice = pr.getText();
		int value;
		try { 
			value = Integer.parseInt(nPrice);
		} 
		catch (NumberFormatException e) { 
			value = -1; 
		}
		if (nPrice.contains("Цена не выше")) throw new MyException();
		if (nPrice.length() == 0) throw new NullPointerException();
		if (value == -1) throw new NotNumericInput();
	}
}
