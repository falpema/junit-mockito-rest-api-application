import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import com.rest.app.Calculator;

public class CalculatorTest {
	Calculator calculator;

	@Test
	public void testMultiply() {
		calculator = new Calculator();
		assertEquals(20, calculator.multiply(4, 5));
	}

	@Test
	public void testDivide() {
		calculator = new Calculator();
		assertEquals(2, calculator.divide(4, 0));
	}

}
