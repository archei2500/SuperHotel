
public class NotNumericInput extends Exception {
	public NotNumericInput () {
		super ("Кажется, вы ввели некорректные данные там, где требовался ввод целого числа!");
	}
}