import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert; // Подключаем методы Assert
import org.junit.Before;
import org.junit.BeforeClass;

public class ClientListTest {
	@Test
	public void testcheckValue() throws NotNumericInput {
		Assert.assertEquals(5, ClientList.checkValue("5"));
	}
	
	@Test(expected=NotNumericInput.class)
	public void testNoncheckValue() throws NotNumericInput {
		Assert.assertEquals(-1, ClientList.checkValue("string"));
	}
	
	@Test
	public void testisAlpha() {
	   Assert.assertTrue(ClientList.isAlpha("test1"));
	}
	
	@Test
	public void testNonisAlpha() {
	   Assert.assertFalse(ClientList.isAlpha("7hour"));
	}
	
	@Test(expected = RuntimeException.class) // Проверяем на появление исключения
	public void testException() {
		throw new RuntimeException("Ошибка");
	}
	
	@BeforeClass // Фиксируем начало тестирования
	public static void allTestsStarted() {
		System.out.println("Начало тестирования");
	}
	
	@AfterClass // Фиксируем конец тестирования
	public static void allTestsFinished() {
		System.out.println("Конец тестирования");
	}
	
	@Before // Фиксируем запуск теста
	public void testStarted() {
		System.out.println("Запуск теста");
	}

	@After // Фиксируем завершение теста
	public void testFinished() {
		System.out.println("Завершение теста");
	}
	
	/*private int checkValue (String str) throws NotNumericInput {
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
	*/
}
