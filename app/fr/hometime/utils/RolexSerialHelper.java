package fr.hometime.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class RolexSerialHelper {
	private static int MIN_SERIAL_NUMBER_OF_CHARS_INCLUDED = 1;
	private static int MAX_SERIAL_NUMBER_OF_CHARS_INCLUDED = 8;
	private static int MIN_REFERENCE_NUMBER_OF_CHARS_INCLUDED = 4;
	private static int MAX_REFERENCE_NUMBER_OF_CHARS_INCLUDED = 11;
	private static int MAX_REFERENCE_NUMBER_OF_DIGITS_INCLUDED = 6;
	    
    public static boolean serialMandatoryTests(Optional<String> serialCandidate) {
    	return doTest(serialCandidate, RolexSerialHelper::buildSerialMandatoryTests);
    }
    
    public static boolean referenceMandatoryTests(Optional<String> referenceCandidate) {
    	return doTest(referenceCandidate, RolexSerialHelper::buildReferenceMandatoryTests);
    }
    
    public static boolean referenceOptionalTests(Optional<String> referenceCandidate) {
    	return doTest(referenceCandidate, RolexSerialHelper::buildReferenceOptionalTests);
    }
    
    private static boolean doTest(Optional<String> serialCandidate, Supplier<List<Predicate<Optional<String>>>> testSupplier) {
    	return testSupplier.get().stream().allMatch(tester -> tester.test(serialCandidate));
    }
    
    private static List<Predicate<Optional<String>>> buildSerialMandatoryTests() {
    	return Arrays.asList(
    				RolexSerialHelper::serialIsLongEnough,
    				RolexSerialHelper::serialIsNotTooLong,
    				RolexSerialHelper::doesNotContainSpecialChars);
    }
    
    private static List<Predicate<Optional<String>>> buildReferenceMandatoryTests() {
    	return Arrays.asList(
    				RolexSerialHelper::referenceIsLongEnough,
    				RolexSerialHelper::referenceDigitsAreNotTooLong,
    				RolexSerialHelper::referenceIsNotTooLong,
    				RolexSerialHelper::doesNotContainSpecialChars);
    }
    
    private static List<Predicate<Optional<String>>> buildReferenceOptionalTests() {
    	return Arrays.asList(
    				RolexSerialHelper::referenceStartsAsModelsWeKnow);
    }
    
    
    private static boolean serialIsLongEnough(Optional<String> serialCandidate) {
    	return serialCandidate.orElse("").length() >= MIN_SERIAL_NUMBER_OF_CHARS_INCLUDED;
    }
    
    private static boolean serialIsNotTooLong(Optional<String> serialCandidate) {
    	return serialCandidate.orElse("").length() <= MAX_SERIAL_NUMBER_OF_CHARS_INCLUDED;
    }
    
    private static boolean doesNotContainSpecialChars(Optional<String> candidate) {
    	return Pattern.matches("[a-zA-Z0-9]+", candidate.orElse(""));
    }
    
    private static boolean referenceIsLongEnough(Optional<String> serialCandidate) {
    	return serialCandidate.orElse("").length() >= MIN_REFERENCE_NUMBER_OF_CHARS_INCLUDED;
    }
    
    private static boolean referenceIsNotTooLong(Optional<String> serialCandidate) {
    	return serialCandidate.orElse("").length() <= MAX_REFERENCE_NUMBER_OF_CHARS_INCLUDED;
    }
    
    private static boolean referenceDigitsAreNotTooLong(Optional<String> serialCandidate) {
    	return serialCandidate.orElse("").replaceAll("[^0-9]", "").length() <= MAX_REFERENCE_NUMBER_OF_DIGITS_INCLUDED;
    }

    
    private static boolean referenceStartsAsModelsWeKnow(Optional<String> serialCandidate) {
    	return Arrays.asList(
    			"55", "140", "1140", "1240",// Sub no date
    			"16", "166", "1166", "1260", "1266",// Sub
    			"65", "167", "1167", "1267", // GMT Master
    			"66", "18", "180", "182", "183", "118", "128", "218", "228",// Day Date
    			"162", "1161", "1162", "126", "1662", "170", "178", "782", "77", "75", // Datejust
    			"62", "165", "1165", "122", "20", "22", "23", "25", "27", "28", "29", "3", "40", "41", "43", "45", "47", "50", "60", "71", "81", "82", "91",// Daytona & Chronograph
    			"63", "60", "216", "214", // Explorer
    			"10", "140", "142", "1142", "1143", "1242", "1243", // Oyster Perpetual
    			"15", "150", "115", // Oyster Perpetual
    			"170", // Oyster Quartz Datejust
    			"190", "191",// Oyster Quartz Day Date
    			"326", // Sky Dweller
    			"43", // Airking
    			"49", // Oyster
    			"126", "226", "686", "696", "268", // Yacht Master
    			"68", "682", // Midsize Oyster Perpetual Datejust
    			"67", "671", "672", "696", "279",// Ladies Oyster Perpetual
    			"65", "69", "691", "692", // Ladies Date
    			"505", "51", "52", "53", "42", "611", // Cellini
    			"24", "54", "610", "642", "805", "44", "46", "48", "49", // Bubbleback 
    			"77", // Others
    			"1265" // Very Likely Future Models 
    			).stream().anyMatch(value -> serialCandidate.orElse("#").startsWith(value));
    }
}
