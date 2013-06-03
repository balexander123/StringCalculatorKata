package stringcalculator;

// http://osherove.com/tdd-kata-1/

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

	private String delimiter;

	public static final String LEFT_BRACKET = "[";
	public static final String RIGHT_BRACKET = "]";
	public static final String DELIMITER_MARKER = "//";
	public static final String COMMA = ",";
	public static final String NEGATIVE_ERROR_MSG = "Negative numbers not allowed: ";
	public static final int EXCLUDED_NUMBER = 1000;
	public static final String CALCULATOR_INPUT_REGEX = "(^//)(\\[.*\\])*(\n)([0-9])";
	public static final int REGEX_GROUP_NUMBER_TOKENS = 4;
	public static final int REGEX_GROUP_DELIMITERS = 2;

	public int add(String input) throws NegativeNotAllowedException {

		if (input.isEmpty())
			return 0;
				
		String tokens[] = numberTokens(input);

		checkNegatives(tokens);

		int sum = 0;
		for (String token : tokens) {
			if (token.length() == 0) {
				continue;
			}
			int num = Integer.parseInt(token);
			if (num < EXCLUDED_NUMBER)
				sum += num;
		}
		return sum;
	}
	
	String[] numberTokens(String input) {
		int numberTokenStart;
		
		String unifiedInput = findAndUnifyDelimiters(input);

		numberTokenStart = findNumberTokens(unifiedInput);
		
		return unifiedInput.substring(numberTokenStart).split(
				getDelimiter());
	}
	
	String findAndUnifyDelimiters(String input) {
		if (delimiterPresent(input)) {
			setDelimiter(extractDelimiter(input));
		} else
			setDelimiter(COMMA);

		String unifiedInput = new String(input);
		if (input.contains(RIGHT_BRACKET+LEFT_BRACKET))
			unifiedInput = unifyDelimiters(
					input,
					findMultiDelimiters(findTokens(input,
							CALCULATOR_INPUT_REGEX, REGEX_GROUP_DELIMITERS)));
		return unifiedInput;
	}
	
	public void checkNegatives(String tokens[]) throws NegativeNotAllowedException {
		List<String> errNums = allPositiveNumbers(tokens);
		if (!errNums.isEmpty()) {
			String errMsg = StringCalculator.NEGATIVE_ERROR_MSG;
			for (String errNum : errNums) {
				errMsg += errNum + ' ';
			}
			throw new NegativeNotAllowedException(errMsg);
		}
	}
	
	private String extractDelimiter(String input) {
		String delimiter = null;
		
		if (input.contains(LEFT_BRACKET))
			delimiter = input.substring(input.indexOf(LEFT_BRACKET)+1, input.indexOf(RIGHT_BRACKET));
		else if (input.contains(DELIMITER_MARKER))
			delimiter = input.substring(DELIMITER_MARKER.length(), DELIMITER_MARKER.length()+1);
		else
			delimiter = COMMA;
		
		return delimiter;
	}

	private int findNumberTokens(String input) {
		int numberTokenStart = 0;

		if (input.contains(LEFT_BRACKET))
			numberTokenStart = input.indexOf(findTokens(input,
					CALCULATOR_INPUT_REGEX, REGEX_GROUP_NUMBER_TOKENS));
		else if (input.contains(DELIMITER_MARKER))
			numberTokenStart = DELIMITER_MARKER.length() + 1;
		return numberTokenStart;
	}
	
	private String findTokens(String input, String regex, int regexGroup) {
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group(regexGroup);
		}
		return null;
	}
	
	public String[] findMultiDelimiters(String input) {
		String[] results = input.split("]");
		for (int i=0; i < results.length; i++) {
			results[i] = results[i].substring(1);
		}
		return results;
	}
	
	private List<String> allPositiveNumbers(String[] tokens) {
		List<String> errTokens = new ArrayList<String>();
		for (String token : tokens) {
			if (token.length() > 0) {
				int num = Integer.parseInt(token);
				if (num < 0) {
					errTokens.add(token);
				}
			}
		}
		return errTokens;
	}

	private boolean delimiterPresent(String input) {
		boolean hasDelimiter = false;
		hasDelimiter = input.contains(DELIMITER_MARKER);
		return hasDelimiter;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = "\\" + delimiter.substring(0,1);
	}

	public String unifyDelimiters(String input, String[] delimiters) {
		String result = new String();
		for (int i=1; i < delimiters.length; i++) {
			result = input.replace(delimiters[i], delimiters[0]);
		}
		return result;
	}
}
