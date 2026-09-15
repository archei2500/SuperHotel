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

public class PositionList {
	private DefaultTableModel model5;
	private JButton open_positions;
	private JButton add_position;
	private JButton edit_position;
	private JButton delete_position;
	private JToolBar toolBarPositions;
	private JScrollPane scroll5;
	private JTable positions_table;
	
	private JFrame registration;
	private JLabel position_label;
	private JLabel type_label;
	private JTextField name_of_pos;
	private JComboBox<String> types;
	private JButton add;
	
	private JFrame remove;
	private JLabel selection;
	private JComboBox<String> added_positions;
	private JButton delete_selected;
	private JButton delete_all;
	
	private JFrame editing;
	private JLabel selection1;
	private JLabel new_name_label;
	private JComboBox<String> added_positions1;
	private JTextField new_name;
	private JButton save;
	
	public void create(JPanel positionList) {
		// Создание кнопок и прикрепление иконок
		open_positions = new JButton(new ImageIcon("./img/folder_red_open.png"));
		add_position = new JButton(new ImageIcon("./img/plus_orange.png"));
		edit_position = new JButton(new ImageIcon("./img/edit_icon.png"));
		delete_position = new JButton(new ImageIcon("./img/trash.png"));
		// Настройка подсказок для кнопок
		open_positions.setToolTipText("Обновить список должностей");
		add_position.setToolTipText("Добавить должность");
		edit_position.setToolTipText("Отредактировать название должности");
		delete_position.setToolTipText("Упразднить должность");
		// Добавление кнопок на панель инструментов
		toolBarPositions = new JToolBar("Панель инструментов");
		toolBarPositions.add(open_positions);
		toolBarPositions.add(add_position);
		toolBarPositions.add(edit_position);
		toolBarPositions.add(delete_position);
		// Размещение панели инструментов
		positionList.setLayout(new BorderLayout());
		positionList.add(toolBarPositions, BorderLayout.NORTH);
		// Создание таблицы с данными
		String [] columns5 = {"Должность", "Служащие"};
		String [][] data5 = {{"Director", "Rodion Raskolnikov"}};
		model5 = new DefaultTableModel(data5, columns5);
		positions_table = new JTable(model5);
		scroll5 = new JScrollPane(positions_table);
		// Размещение таблицы с данными
		positionList.add(scroll5, BorderLayout.CENTER);
		
		open_positions.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				TypedQuery<Position> q = DataBase.em.createQuery("select t from Position t", Position.class);
				List<Position> positions = q.getResultList();
				int rows = model5.getRowCount();
				for (int i = 0; i < rows; i++) {
					model5.removeRow(0); // Очистка таблицы
				}
				for (Position entity : positions) {
					String pos = entity.getName();
					//создание строки из служащих (на данной должности)
					List<Worker> workers = entity.getWorkers();
					String employees = new String();
					StringBuilder builder = new StringBuilder();
					int length = workers.size();
					for (int i = 0; i < length; i = i + 1) {
						Worker worker = workers.get(i);
						builder.append(Integer.toString(worker.getID()) + " " + worker.getName() + " " + worker.getLastName());
						if (i != (length-1)) {
							builder.append("; ");
						}
					}
					employees = builder.toString();
					// Запись данных в таблицу
					model5.addRow(new String[]{pos, employees});
				}
			}
		} );
		add_position.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				register();
			}
		} );
		delete_position.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				remover();
			}
		} );
		edit_position.addActionListener (new ActionListener() { //название поменять
			public void actionPerformed (ActionEvent event) {
				editor();
			}
		} );
	}
	private void register() {
		// Создание окна
		registration = new JFrame("Добавление должности");
		registration.setSize(500, 300);
		registration.setLocation(600, 350);
		registration.setLayout(new GridBagLayout());
		//Обработка компонентов
		position_label = new JLabel("Введите название должности: ");
		type_label = new JLabel("Выберите, руководящая или рядовая эта должность: ");
		name_of_pos = new JTextField();
		types = new JComboBox<String>(new String[] {"managerial", "ordinary"});
		add = new JButton("Добавить");
		//размещение
		registration.add(position_label, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(name_of_pos, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(type_label, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(types, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		registration.add(add, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
				
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				TypedQuery<Position> q = DataBase.em.createQuery("select t from Position t", Position.class);
				List<Position> positions = q.getResultList();
				boolean already_contained = false;
				String val = name_of_pos.getText();
				String val1 = (String)types.getSelectedItem();
				DataBase.em.getTransaction().begin();
				Position position = new Position();
			    boolean bool = position.setName(val);
			    if (bool) {
			    	Iterator<Position> posIterator = positions.iterator();
					while (!already_contained && posIterator.hasNext()) {
						Position nextPosition = posIterator.next();
						if (nextPosition.getName().equals(val)) {
							already_contained = true;
						}
					}
			    	if (!already_contained) {
			    		position.setType(val1);
			    		DataBase.em.persist(position);
			    		JOptionPane.showMessageDialog(null, "Готово! Чтобы увидеть изменения в таблице, нажмите на панели кнопку с папкой.");
			    	}
			    	else {
			    		JOptionPane.showMessageDialog(null, "Вы не можете добавить эту должность, поскольку она уже имеется!");
			    	}
			    }
			    else {
			    	JOptionPane.showMessageDialog(registration, "Вы ввели некорректные данные!");
			    }
			    DataBase.em.getTransaction().commit();
			}
		});
		registration.setVisible(true);
	}
	public void remover() {
		// Создание окна
		remove = new JFrame("Сокращение");
		remove.setSize(450, 150);
		remove.setLocation(600, 350);
		remove.setLayout(new GridBagLayout());
		//Обработка компонентов
		selection = new JLabel("Выберите должность, которую хотите сократить: ");
		TypedQuery<Position> q = DataBase.em.createQuery("select t from Position t", Position.class);
		List<Position> positions = q.getResultList();
		List<String> added = new ArrayList<String>();
		for (Position entity : positions) {
			if (!entity.getType().equals("managerial")) {
				added.add(Integer.toString(entity.getID()) + " " + entity.getName());
			}
		}
		added_positions = new JComboBox<String>(added.toArray(new String[0]));
		delete_selected = new JButton("Сократить выбранную должность");
		delete_all = new JButton("Удалить всё");
		//размещение
		remove.add(selection, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		remove.add(added_positions, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		remove.add(delete_selected, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		remove.add(delete_all, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));	
		
		delete_selected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				String cb = (String)added_positions.getSelectedItem();
				int ind = added_positions.getSelectedIndex();
				DataBase.em.getTransaction().begin();
				int ind_space = cb.indexOf(' ');
				String id = cb.substring(0, ind_space);
				Position del_pos = DataBase.em.find(Position.class, Integer.parseInt(id));
				DataBase.em.remove(del_pos);
				added_positions.removeItemAt(ind);
				DataBase.em.getTransaction().commit();
				JOptionPane.showMessageDialog(null, "Готово! Чтобы увидеть изменения в таблице, нажмите на панели кнопку с папкой.");
			}
		});
		delete_all.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Object[] options = {"Да","Нет, отказываюсь"};
				int n = JOptionPane.showOptionDialog(null, 
				"Вы уверены, что хотите сократить вообще все должности, включая директора и т.д.?", 
				"confirmation", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (n == 0) {
					DataBase.em.getTransaction().begin();
					TypedQuery<Position> q = DataBase.em.createQuery("select t from Position t", Position.class);
					List<Position> positions = q.getResultList();
					for (Position entity : positions) {
						DataBase.em.remove(entity);
					}
					DataBase.em.getTransaction().commit();
				}
			}
		});
		remove.setVisible(true);
		if (added_positions.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "Сначала добавьте хотя бы одну должность, помимо руководящих!");
			remove.setVisible(false);
		}
	}
	public void editor() {
		// Создание окна
		editing = new JFrame("Редактирование");
		editing.setSize(500, 200);
		editing.setLocation(600, 350);
		editing.setLayout(new GridBagLayout());
		//Обработка компонентов
		selection1 = new JLabel("Выберите должность для редактирования: ");
		new_name_label = new JLabel("Введите новое название должности: ");
		new_name = new JTextField();
		TypedQuery<Position> q = DataBase.em.createQuery("select t from Position t", Position.class);
		List<Position> positions = q.getResultList();
		String[] added = new String[positions.size()];
		int i = 0;
		for (Position entity : positions) {
			added[i] = Integer.toString(entity.getID()) + " " + entity.getName();
			i = i + 1;
		}
		added_positions1 = new JComboBox<String>(added);
		save = new JButton("Сохранить изменения");
		//размещение
		editing.add(selection1, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(added_positions1, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(new_name_label, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(new_name, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		editing.add(save, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.NORTH,  
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				String cb = (String)added_positions1.getSelectedItem();
				String text = new_name.getText();
				boolean bool = isAlpha(text);
				if (text.length() != 0 && bool) {
					DataBase.em.getTransaction().begin();
					int ind_space = cb.indexOf(' ');
					String id = cb.substring(0, ind_space);
					Position edit_pos = DataBase.em.find(Position.class, Integer.parseInt(id));
					edit_pos.setName(text);
					JOptionPane.showMessageDialog(null, "Готово! Чтобы увидеть изменения в таблице, нажмите на панели кнопку с папкой.");	  
					DataBase.em.getTransaction().commit();
				}
				else {
					JOptionPane.showMessageDialog(editing, "Вы ввели некорректные данные!");
				}
			}
		});
		editing.setVisible(true);
		if (added_positions1.getItemCount() == 0) {
			JOptionPane.showMessageDialog(null, "Сначала добавьте хотя бы одну должность!");
			editing.setVisible(false);
		}
	}
	public boolean isAlpha(String str) {
	    char[] chars = str.toCharArray();
	    for (char c : chars) {
	        if(!(Character.isLetter(c) || c == ' ')) {
	            return false;
	        }
	    }
	    return true;
	}
}
