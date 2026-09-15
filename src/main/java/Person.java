import javax.persistence.*;

@MappedSuperclass
public class Person {
	@Column(name = "name")
	private String name;
	
	@Column(name = "last_name")
	private String last_name;
	
	@Column(name = "age")
	private int age;
	
	public String getName() {
		return name;
	}
	public boolean setName(String new_name) {
		boolean flag = true;
		if (!(new_name.isEmpty())) {
			if (isAlpha(new_name)) {
				new_name = format(new_name);
				this.name = new_name;
			}
			else {
				flag = false;
			}
		}
		else {
			flag = false;
		}
		return flag;
	}
	
	public String getLastName() {
		return last_name;
	}
	public boolean setLastName(String new_lname) {
		boolean flag = true;
		if (!(new_lname.isEmpty())) {
			if (isAlpha(new_lname)) {
				new_lname = format(new_lname);
				this.last_name = new_lname;
			}
			else {
				flag = false;
			}
		}
		else {
			flag = false;
		}
		return flag;
	}
	
	public int getAge() {
		return age;
	}
	public boolean setAge(int new_age) {
		if (new_age < 14) {
			return false;
		}
		else {
			this.age = new_age;
		}
		return true;
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