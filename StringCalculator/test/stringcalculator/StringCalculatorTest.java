package stringcalculator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class StringCalculatorTest {

	private StringCalculator stringCalculator;

	@Rule
	public ExpectedException negativeException = ExpectedException.none();

	@Before
	public void setup() {
		stringCalculator = new StringCalculator();
	}

	@Test
	public void returnsZeroForEmptyString() throws NegativeNotAllowedException {
		assertEquals(0, stringCalculator.add(""));
	}

	@Test
	public void returnsSingleLiteral() throws NegativeNotAllowedException {
		assertEquals(5, stringCalculator.add("5"));
	}

	@Test
	public void returnsTheSumOfNumbersSeparatedByCommaDelimiter() throws NegativeNotAllowedException {
		assertEquals(3, stringCalculator.add("1,2"));
		assertEquals(6, stringCalculator.add("1,2,3"));
	}

	@Test
	public void returnsTheSumWithDifferentDelimiters() throws NegativeNotAllowedException {
		assertEquals(3, stringCalculator.add("//;1;2"));
		assertEquals(6, stringCalculator.add("//!1!2!3"));
	}

	@Test
	public void throwsNegativesNotAllowedException() throws NegativeNotAllowedException {
		negativeException.expect(NegativeNotAllowedException.class);
		stringCalculator.add("-1,1");
	}
	
	@Test
	public void negativeValuesReturnedOnException() {
		try {
			stringCalculator.add("-1,-2,1");
		}
		catch (NegativeNotAllowedException e) {
			System.err.println(e.getMessage());
			assertEquals(true, e.getMessage().contains(StringCalculator.NEGATIVE_ERROR_MSG));
			assertEquals(true, e.getMessage().contains("-1"));
			assertEquals(true, e.getMessage().contains("-2"));
		}
	}
	
	@Test
	public void ignoreValuesOver1000() throws NegativeNotAllowedException {
		assertEquals(2, stringCalculator.add("2,1001"));
	}
	
	@Test
	public void supportsMultiCharacterDelimiters() throws NegativeNotAllowedException {
		assertEquals(6, stringCalculator.add("//[***]\n1***2***3"));
	}
	
	@Test
	public void supportsMultipleDelimiters() throws NegativeNotAllowedException {
		assertEquals(6, stringCalculator.add("//[*][%]\n1*2%3"));
	}
	
	@Test
	public void findsArrayOfDelimiters() {
		String[] delimiters = stringCalculator.findMultiDelimiters("[*][%]");
		assertEquals(2, delimiters.length);
		for (String dim : delimiters) {
			System.out.println(dim);
		}
		assertEquals("*", delimiters[0]);
		assertEquals("%", delimiters[1]);
	}
	
	@Test
	public void unifiesDelimiters() {
		String[] delimiters = {"*","%"};
		String result = stringCalculator.unifyDelimiters("1*2%3",delimiters);
		System.out.println(result);
		assertEquals(0, "1*2*3".compareTo(result));
	}
}
