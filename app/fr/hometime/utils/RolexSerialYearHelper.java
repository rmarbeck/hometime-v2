package fr.hometime.utils;

import static fr.hometime.utils.RolexYearFromSerialResolver.createOpenRangeFromSimpleYear;
import static fr.hometime.utils.RolexYearFromSerialResolver.createSimpleRange;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Pattern;

import models.RolexYearFromSerialResult;

public class RolexSerialYearHelper {
	static List<Integer> sevendDigitsSerialsStartingPoint = Arrays.asList(
								1000000,//1964
								1100000,//1965
								1200000,//1966
								1538435,//1967
								1752000,//1968
								1900000,//1969
								2241882,//1970
								2589295,//1971
								2890459,//1972
								3200268,//1973
								3567927,//1974
								3862196,//1975
								4115299,//1976
								5008000,//1977
								5238376,//1978
								5952834,//1979
								6434000,//1980
								6594000,//1981
								7129351,//1982
								7862000,//1983
								8338000,//1984
								8624000,//1985
								9290000,//1986
								9863279);//1987
	
	static List<Integer> fiveToSixDigitsSerialsStartingPointFirstPeriod = Arrays.asList(
								00001,//1926
								20190,//1927
								23969,//1928
								28290,//1929
								28600,//1930 - approx
								29000,//1931 - approx
								29312,//1932
								29933,//1933
								30823,//1934
								34336,//1935
								36856,//1936
								40920,//1937
								43793,//1938
								71224,//1939
								99775,//1940
								106047,//1941
								143509,//1942
								230878,//1943
								269561,//1944
								302459,//1945
								367946,//1946
								529163,//1947
								628840,//1948
								650000,//1949 - approx
								670000,//1950 - approx
								709249,//1951
								726639,//1952
								793930,//1953
								976195);//1954
	
	static List<Integer> fiveToSixDigitsSerialsStartingPointSecondPeriod = Arrays.asList(
								00001,//1954
								30634,//1955
								139400,//1956
								216821,//1957
								353343,//1958
								399000,//1959 - approx
								511687,//1960
								646900,//1961 - approx
								763663,//1962
								950000,//1963 - approx
								997436);//1964

	static Map<Integer, Integer> sevendDigitsNotOverlappingSerials = loadMap(sevendDigitsSerialsStartingPoint, 1964);
	static Map<Integer, Integer> fiveToSixDigitsSerialsStartingPointFirstPeriodSerials = loadMap(fiveToSixDigitsSerialsStartingPointFirstPeriod, 1926);
	static Map<Integer, Integer> fiveToSixDigitsSerialsStartingPointSecondPeriodSerials = loadMap(fiveToSixDigitsSerialsStartingPointSecondPeriod, 1954);
	
	private static Map<Integer, Integer> loadMap(List<Integer> listOfStartingPoints, Integer startingYear) {
		return listOfStartingPoints.stream().collect(HashMap::new, (hmap, value) -> hmap.put(value, getNextYear(hmap, startingYear)), HashMap::putAll);
	}
	
	private static Integer getNextYear(Map<Integer, Integer> currentMap, Integer startingYear) {
		return currentMap.values().stream().sorted().reduce(startingYear, (first, second) -> second + 1);
	}
	
	public static boolean startsWithAKnownLetter(String serial) {
		return Pattern.matches("(?i)[rlexncswtuapkyfdzmvg][0-9]{6}", serial);
	}
	
	public static boolean isRandomNumber(String serial) {
		return !Pattern.matches("[0-9]{8}", serial) && Pattern.matches("(?i)[a-z0-9]{8}", serial);
	}
	
	public static boolean isNumberOnlyWITHOverlapping(String serial) {
		return Pattern.matches("[0-9]{1,6}", serial);
	}
	
	public static boolean isNumberOnlyWITHOUTOverlapping(String serial) {
		return Pattern.matches("[1-9][0-9]{6}", serial);
	}
	
	public static Optional<RolexYearFromSerialResult> resolveRandomNumber(String serial) {
		return Optional.of(createOpenRangeFromSimpleYear(2010));
	}
	
	public static Optional<RolexYearFromSerialResult> resolveStartingWithAKnownLetter(String serial) {
		RolexYearFromSerialResult result = doResolveStartingWithAKnownLetter(serial);
		if (result == null)
			return Optional.empty();
		return Optional.of(result);
	}
	
	private static RolexYearFromSerialResult doResolveStartingWithAKnownLetter(String serial) {
		switch (Character.toLowerCase(serial.charAt(0))) {
		case 'r':
			return createSimpleRange(1987,1988);
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
			return createSimpleRange(1997, 1998);
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
	
	public static Optional<RolexYearFromSerialResult> resolveNumberOnlyWITHOUTOverlapping(String serial) {		
		return findYearInMap(sevendDigitsNotOverlappingSerials, serial);
	}
	
	public static Optional<RolexYearFromSerialResult> resolveNumberOnlyWITHOverlapping(String serial) {	
		Optional<RolexYearFromSerialResult> firstPeriodResolve = findYearInMap(fiveToSixDigitsSerialsStartingPointFirstPeriodSerials, serial);
		Optional<RolexYearFromSerialResult> secondPeriodResolve = findYearInMap(fiveToSixDigitsSerialsStartingPointSecondPeriodSerials, serial);
		
		return firstPeriodResolve.flatMap(result -> result.mergeWith(secondPeriodResolve));
	}
	
	private static Optional<RolexYearFromSerialResult> findYearInMap(Map<Integer, Integer> values, String serial) {
		return values.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).filter(entry -> Integer.valueOf(serial) >= entry.getKey()).map(Entry::getValue).reduce((first, second) -> second).map(year -> RolexYearFromSerialResult.newFromSingleYear(year));
	}
}
