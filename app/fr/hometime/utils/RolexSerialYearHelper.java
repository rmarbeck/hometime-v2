package fr.hometime.utils;

import java.util.Optional;
import java.util.regex.Pattern;

import fr.hometime.utils.RolexYearFromSerialResolver.Result;
import static fr.hometime.utils.RolexYearFromSerialResolver.createSimpleYear;
import static fr.hometime.utils.RolexYearFromSerialResolver.createSimpleRange;
import static fr.hometime.utils.RolexYearFromSerialResolver.createTwoRanges;

public class RolexSerialYearHelper {
	public static boolean startsWithAKnownLetter(String serial) {
		return Pattern.matches("(?i)[rlexncswtuapkyfdzmvg][0-9]{6}", serial);
	}
	
	public static boolean isRandomNumber(String serial) {
		return !Pattern.matches("[0-9]{8}", serial) && Pattern.matches("(?i)[a-z0-9]{8}", serial);
	}
	
	public static boolean isNumberOnlyWITHOverlapping(String serial) {
		return Pattern.matches("[0-9]{4,6}", serial);
	}
	
	public static boolean isNumberOnlyWITHOUTOverlapping(String serial) {
		return Pattern.matches("[0-9]{7}", serial);
	}
	
	public static Optional<Result> resolveRandomNumber(String serial) {
		return Optional.of(createSimpleYear(2010));
	}
	
	public static Optional<Result> resolveStartingWithAKnownLetter(String serial) {
		Result result = doResolveStartingWithAKnownLetter(serial);
		if (result == null)
			return Optional.empty();
		return Optional.of(result);
	}
	
	private static Result doResolveStartingWithAKnownLetter(String serial) {
		switch (Character.toLowerCase(serial.charAt(0))) {
		case 'r':
			return createSimpleRange(1987,1989);
		case 'l':
			return createSimpleRange(1989,1990);
		case 'e':
			return createSimpleRange(1990,1991);
		case 'x':
			return createSimpleRange(1991, 1991);
		case 'n':
			return createSimpleRange(1991,1992);
		case 'c':
			return createSimpleRange(1992, 1993);
		case 's':
			return createSimpleRange(1993, 1995);
		case 'w':
			return createSimpleRange(1995, 1995);
		case 't':
			return createSimpleRange(1995, 1997);
		case 'u':
			return createSimpleRange(1997, 1999);
		case 'a':
			return createSimpleRange(1999, 2000);
		case 'p':
			return createSimpleRange(2000, 2001);
		case 'k':
			return createSimpleRange(2001, 2002);
		case 'y':
			return createSimpleRange(2002, 2003);
		case 'f':
			return createSimpleRange(2003, 2005);
		case 'd':
			return createSimpleRange(2005, 2006);
		case 'z':
			return createSimpleRange(2006, 2007);
		case 'm':
			return createSimpleRange(2007, 2008);
		case 'v':
			return createSimpleRange(2008, 2009);
		case 'g':
			return createSimpleRange(2009, 2010);
		default:
			return null;
		}
	}
}
