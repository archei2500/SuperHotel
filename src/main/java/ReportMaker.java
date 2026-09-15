import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ReportMaker {
	private DefaultTableModel model;
	private JScrollPane scroll;
	private JTable table;
	private JComboBox<String> month;
	private JComboBox<String> year;
	private JButton show_this;
	private JButton export_pdf;
	private JPanel num_of_clients;
	private JLabel label;
	private String english_label;
	
	public void create(JPanel report) {
		String str = "Число клиентов за _ _ года: _";
		num_of_clients = new JPanel();
		report.setLayout(new BorderLayout());
		report.add(num_of_clients, BorderLayout.NORTH);
		label = new JLabel(str);
		num_of_clients.add(label);
		// Создание таблицы с данными
		String [] columns = {"Номер", "Сколько дней был занят", "Сколько дней был свободен"};
		String [][] data = {{"-", "-", "-"}};
		model = new DefaultTableModel(data, columns);
		table = new JTable(model);
		scroll = new JScrollPane(table);
		// Размещение таблицы с данными
		report.add(scroll, BorderLayout.CENTER);
		// Подготовка компонентов выбора
		String[] months = new String[13];
		months[0] = "месяц";
		for (int i = 1; i < months.length; i++) {
		     months[i] = Integer.toString(i); 
		}
		month = new JComboBox<String>(months);
		year = new JComboBox<String>(new String[] {"год", "2018", "2019", "2020", "2021", "2022"});
		show_this = new JButton("Отчёт");
		export_pdf = new JButton(new ImageIcon("./img/pdf.png"));
		export_pdf.setToolTipText("Сохранить отчёт в PDF");
		// Добавление компонентов на панель
		JPanel choicePanel = new JPanel();
		choicePanel.add(month);
		choicePanel.add(year);
		choicePanel.add(show_this);
		choicePanel.add(export_pdf);
		// Размещение панели поиска внизу окна
		report.add(choicePanel, BorderLayout.SOUTH);
		
		english_label = new String();
		
		show_this.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				int counter = 0;
				int days_available = 0;
				int days_reserved = 0;
				String cb1 = (String)month.getSelectedItem();
				String cb2 = (String)year.getSelectedItem();
				if (!(cb1.equals("месяц")) && !(cb2.equals("год"))) {
					int month1 = Integer.parseInt(cb1);
					int year1 = Integer.parseInt(cb2);
					TypedQuery<Client> q = DataBase.em.createQuery("select t from Client t", Client.class);
					List<Client> clients = q.getResultList();
					for (Client entity : clients) {
						if (entity.getMonth() == month1 && entity.getYear() == year1) {
							counter = counter + 1;
						}
					}
					TypedQuery<Room> q1 = DataBase.em.createQuery("select t from Room t", Room.class);
					List<Room> rooms = q1.getResultList();
					int rows = model.getRowCount();
					for (int i = 0; i < rows; i++) {
						model.removeRow(0); // Очистка таблицы
					}
					for (Room entity : rooms) {
						int num_of_days = numOfDays(month1, year1);
						boolean[] day_vect = new boolean[num_of_days];
						for (int i = 0; i < num_of_days; i = i + 1) {
							day_vect[i] = false;
						}
						days_available = 0;
						days_reserved = 0;
						String id_cell = Integer.toString(entity.getRoomID());
						List<RoomClient> clientList = entity.getClients();
						for (RoomClient ent : clientList) {
							Client client = ent.getClient();
							boolean[] day_vect_2 = daysCounter(month1, year1, client);
							for (int i = 0; i < num_of_days; i = i + 1) {
								day_vect[i] = day_vect[i] || day_vect_2[i];
							}
						}
						for (int i = 0; i < num_of_days; i = i + 1) {
							if (day_vect[i]) {
								days_reserved = days_reserved + 1;
							}
						}
						days_available = num_of_days - days_reserved;
						// Запись данных в таблицу
						model.addRow(new String[]{id_cell, Integer.toString(days_reserved), Integer.toString(days_available)});
					}
					String month_name = numToStrDate(month1, 1);
					label.setText("Число клиентов за " + month_name + " " + Integer.toString(year1) + " года: " + Integer.toString(counter));
					month_name = numToStrDate(month1, 2);
					english_label = "Number of clients in " + month_name + " " + Integer.toString(year1) + ": " + Integer.toString(counter);
				}
				else {
					JOptionPane.showMessageDialog(null, "Вы не настроили параметры поиска!");
				}
			}
		} );
		export_pdf.addActionListener (new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				PDFReport obj = new PDFReport();
				try {
					obj.main(model, english_label);
					JOptionPane.showMessageDialog(null, "PDF-отчёт сгенерирован!");
				}
				catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		} );
	}
	private boolean[] daysCounter (int month, int year, Client client) {
		int num_of_days = numOfDays(month, year);
		boolean[] day_vect = new boolean[num_of_days];
		for (int i = 0; i < num_of_days; i = i + 1) {
			day_vect[i] = false;
		}
		int check_in_day = 0;
		int check_in_month = 0;
		int check_in_year = 0;
		int check_out_day = 0;
		int check_out_month = 0;
		int check_out_year = 0;
		int length_of_stay = 0;
		
		check_in_day = client.getDay();
		check_in_month = client.getMonth();
		check_in_year = client.getYear();
		length_of_stay = client.getLOS();
		check_out_day = check_in_day + length_of_stay;
		int[] check_out_date = recDateSum(check_out_day, check_in_month, check_in_year);
		check_out_day = check_out_date[0];
		check_out_month = check_out_date[1];
		check_out_year = check_out_date[2];
		
		if (year == check_in_year || year == check_out_year) {
			if (check_in_year == check_out_year) {
				if (month == check_in_month || month == check_out_month) {
					if (check_in_month == check_out_month) {
						for (int i = check_in_day; i < check_out_day; i = i + 1) {
							day_vect[i-1] = true;
						}
					}
					else if (month == check_in_month) {
						for (int i = check_in_day; i < (num_of_days + 1); i = i + 1) {
							day_vect[i-1] = true;
						}
					}
					else {
						if (check_out_day != 1) {
							for (int i = 0; i < (check_out_day - 1); i = i + 1) {
								day_vect[i] = true;
							}
						}
					}
				}
				else if (month > check_in_month && month < check_out_month) {
					for (int i = 0; i < num_of_days; i = i + 1) {
						day_vect[i] = true;
					}
				}
			}
			else if (year == check_in_year) {
				if (month == check_in_month) {
					for (int i = check_in_day; i < (num_of_days + 1); i = i + 1) {
						day_vect[i-1] = true;
					}
				}
				else if (month > check_in_month) {
					for (int i = 0; i < num_of_days; i = i + 1) {
						day_vect[i] = true;
					}
				}
			}
			else {
				if (check_out_day != 1) {
					if (month == check_out_month) {
						for (int i = 0; i < (check_out_day - 1); i = i + 1) {
							day_vect[i] = true;
						}
					}
					else if (month < check_out_month) {
						for (int i = 0; i < num_of_days; i = i + 1) {
							day_vect[i] = true;
						}
					}
				}
			}
		}
		else if (year > check_in_year && year < check_out_year) {
			for (int i = 0; i < num_of_days; i = i + 1) {
				day_vect[i] = true;
			}
		}
		
		return day_vect;
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
	private int numOfDays (int month, int year) {
		int num_of_days = 0;
		if (year == 2020 && month == 2) {
			num_of_days = 29;
		}
		else if (month == 2) {
			num_of_days = 28;
		}
		else if (month == 4 || month == 6 || month == 9 || month == 11) {
			num_of_days = 30;
		}
		else {
			num_of_days = 31;
		}
		return num_of_days;
	}
	private String numToStrDate(int month, int mode) {
		String month_name = new String();
		if (mode == 1) {
			switch(month) {
			case 1:
				month_name = "январь";
				break;
			case 2:
				month_name = "февраль";
				break;
			case 3:
				month_name = "март";
				break;
			case 4:
				month_name = "апрель";
				break;
			case 5:
				month_name = "май";
				break;
			case 6:
				month_name = "июнь";
				break;
			case 7:
				month_name = "июль";
				break;
			case 8:
				month_name = "август";
				break;
			case 9:
				month_name = "сентябрь";
				break;
			case 10:
				month_name = "октябрь";
				break;
			case 11:
				month_name = "ноябрь";
				break;
			case 12:
				month_name = "декабрь";
				break;
			}
		} else {
			switch(month) {
			case 1:
				month_name = "January";
				break;
			case 2:
				month_name = "February";
				break;
			case 3:
				month_name = "March";
				break;
			case 4:
				month_name = "April";
				break;
			case 5:
				month_name = "May";
				break;
			case 6:
				month_name = "June";
				break;
			case 7:
				month_name = "July";
				break;
			case 8:
				month_name = "August";
				break;
			case 9:
				month_name = "September";
				break;
			case 10:
				month_name = "October";
				break;
			case 11:
				month_name = "November";
				break;
			case 12:
				month_name = "December";
				break;
			}
		}
		
		return month_name;
	}
}