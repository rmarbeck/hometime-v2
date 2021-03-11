package fr.hometime.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import models.RolexSerial;
import models.RolexYearFromSerialResult;
import models.RolexYearFromSerialResult.Years;


public class RolexSerialTest {
	
	@Test
	public void rolexSerialTester() {
		assertTrue("1", RolexSerial.of(Optional.of("12345")).isPresent());
		
		assertFalse("10", RolexSerial.of(Optional.of("r512345")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::isSingleYear).orElse(true));
		assertTrue("11", RolexSerial.of(Optional.of("r512345")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::getYears).map(Years::getYears).orElse(new ArrayList<Integer>()).contains(1987));
		assertTrue("12", RolexSerial.of(Optional.of("r512345")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::getYears).map(Years::getYears).orElse(new ArrayList<Integer>()).contains(1988));
		assertTrue("13", RolexSerial.of(Optional.of("r512345")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::getYears).map(Years::getYears).orElse(new ArrayList<Integer>()).size()==2);
		assertFalse("14", RolexSerial.of(Optional.of("r512345")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::isSingleYear).orElse(true));
		assertFalse("15", RolexSerial.of(Optional.of("r512345")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::isOpenRange).orElse(true));
		
		assertTrue("141", RolexSerial.tryToFindYears(Optional.of("r512345")).map(RolexYearFromSerialResult::getYears).map(Years::getYears).orElse(new ArrayList<Integer>()).contains(1987));
	
		assertTrue("14", RolexSerial.of(Optional.of("a512345")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::getYears).map(Years::getYears).orElse(new ArrayList<Integer>()).contains(1999));
		assertTrue("15", RolexSerial.of(Optional.of("a512345")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::getYears).map(Years::getYears).orElse(new ArrayList<Integer>()).contains(2000));
		
		assertTrue("14", RolexSerial.of(Optional.of("G512345")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::getYears).map(Years::getYears).orElse(new ArrayList<Integer>()).contains(2009));
		assertTrue("15", RolexSerial.of(Optional.of("G512345")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::getYears).map(Years::getYears).orElse(new ArrayList<Integer>()).contains(2010));

		assertTrue("21", RolexSerial.of(Optional.of("w512345")).flatMap(RolexSerial::getYears).flatMap(RolexYearFromSerialResult::getSingleYear).orElse(0) == 1995);
		assertTrue("22", RolexSerial.of(Optional.of("w512345")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::isSingleYear).orElse(false));
		
		System.err.println(RolexSerial.of(Optional.of("U879965")).flatMap(RolexSerial::getYears));
		
		assertTrue("24", RolexSerial.of(Optional.of("U879965")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::getYears).map(Years::getYears).orElse(new ArrayList<Integer>()).contains(1997));
		assertTrue("25", RolexSerial.of(Optional.of("U879965")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::getYears).map(Years::getYears).orElse(new ArrayList<Integer>()).contains(1998));
		
		//System.err.println(RolexSerial.of(Optional.of("OT23Q257")).flatMap(RolexSerial::getYears));
		
		assertTrue("31", RolexSerial.of(Optional.of("OT23Q257")).flatMap(RolexSerial::getYears).flatMap(RolexYearFromSerialResult::getStartingYearFromOpenRange).orElse(0) == 2010);
		assertFalse("32", RolexSerial.of(Optional.of("OT23Q257")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::isSingleYear).orElse(true));
		assertTrue("33", RolexSerial.of(Optional.of("OT23Q257")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::isOpenRange).orElse(false));
		
		assertFalse("41", RolexSerial.of((String) null).isPresent());
		assertFalse("42", RolexSerial.of(Optional.ofNullable(null)).isPresent());
		assertFalse("43", RolexSerial.of("").isPresent());
		
		assertTrue("51", RolexSerial.of(Optional.of("1000000")).flatMap(RolexSerial::getYears).flatMap(RolexYearFromSerialResult::getSingleYear).orElse(0) == 1964);
		assertTrue("52", RolexSerial.of(Optional.of("1000000")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::isSingleYear).orElse(false));
		
		//System.err.println(RolexSerial.of(Optional.of("999999")).flatMap(RolexSerial::getYears));
		assertTrue("54", RolexSerial.of(Optional.of("999999")).flatMap(RolexSerial::getYears).flatMap(RolexYearFromSerialResult::getFirstYearPossible).orElse(0) == 1954);
		assertTrue("55", RolexSerial.of(Optional.of("999999")).flatMap(RolexSerial::getYears).flatMap(RolexYearFromSerialResult::getSecondYearPossible).orElse(0) == 1964);
		assertTrue("56", RolexSerial.of(Optional.of("999999")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::hasTwoPossibleYears).orElse(false));
		
		
		assertTrue("61", RolexSerial.of(Optional.of("1900014")).flatMap(RolexSerial::getYears).flatMap(RolexYearFromSerialResult::getSingleYear).orElse(0) == 1969);
		assertTrue("62", RolexSerial.of(Optional.of("1900014")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::isSingleYear).orElse(false));
		
		assertTrue("64", RolexSerial.of(Optional.of("9863278")).flatMap(RolexSerial::getYears).flatMap(RolexYearFromSerialResult::getSingleYear).orElse(0) == 1986);
		assertTrue("65", RolexSerial.of(Optional.of("9863278")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::isSingleYear).orElse(false));
		
		assertTrue("67", RolexSerial.of(Optional.of("9863279")).flatMap(RolexSerial::getYears).flatMap(RolexYearFromSerialResult::getSingleYear).orElse(0) == 1987);
		assertTrue("68", RolexSerial.of(Optional.of("9863279")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::isSingleYear).orElse(false));
		
		assertTrue("69", RolexSerial.of(Optional.of("9863280")).flatMap(RolexSerial::getYears).flatMap(RolexYearFromSerialResult::getSingleYear).orElse(0) == 1987);
		assertTrue("699", RolexSerial.of(Optional.of("9863280")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::isSingleYear).orElse(false));
		
		assertTrue("70", RolexSerial.of(Optional.of("4683552")).flatMap(RolexSerial::getYears).flatMap(RolexYearFromSerialResult::getSingleYear).orElse(0) == 1976);
	
		assertTrue("54", RolexSerial.of(Optional.of("00001")).flatMap(RolexSerial::getYears).flatMap(RolexYearFromSerialResult::getFirstYearPossible).orElse(0) == 1926);
		assertTrue("55", RolexSerial.of(Optional.of("00001")).flatMap(RolexSerial::getYears).flatMap(RolexYearFromSerialResult::getSecondYearPossible).orElse(0) == 1954);
		assertTrue("56", RolexSerial.of(Optional.of("00001")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::hasTwoPossibleYears).orElse(false));
		
		assertTrue("57", RolexSerial.of(Optional.of("1")).flatMap(RolexSerial::getYears).flatMap(RolexYearFromSerialResult::getFirstYearPossible).orElse(0) == 1926);
		assertTrue("58", RolexSerial.of(Optional.of("1")).flatMap(RolexSerial::getYears).flatMap(RolexYearFromSerialResult::getSecondYearPossible).orElse(0) == 1954);
		assertTrue("59", RolexSerial.of(Optional.of("1")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::hasTwoPossibleYears).orElse(false));
		
	}
	
	@Test
	public void rolexSerialSideEffectsTester() {
		assertTrue("1", RolexSerial.of(Optional.of("5238376")).isPresent());
		assertTrue("2", RolexSerial.of(Optional.of("5238376")).flatMap(RolexSerial::getYears).flatMap(RolexYearFromSerialResult::getSingleYear).orElse(0) == 1978);
		assertTrue("3", RolexSerial.of(Optional.of("5238376")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::isSingleYear).orElse(false));

		
		assertTrue("10", RolexSerial.of(Optional.of("5238377")).isPresent());
		assertTrue("11", RolexSerial.of(Optional.of("5238377")).flatMap(RolexSerial::getYears).flatMap(RolexYearFromSerialResult::getSingleYear).orElse(0) == 1978);
		assertTrue("12", RolexSerial.of(Optional.of("5238377")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::isSingleYear).orElse(false));
		
		assertTrue("20", RolexSerial.of(Optional.of("5238375")).isPresent());
		assertTrue("21", RolexSerial.of(Optional.of("5238375")).flatMap(RolexSerial::getYears).flatMap(RolexYearFromSerialResult::getSingleYear).orElse(0) == 1977);
		assertTrue("22", RolexSerial.of(Optional.of("5238375")).flatMap(RolexSerial::getYears).map(RolexYearFromSerialResult::isSingleYear).orElse(false));
	}
	
	@Test
	public void rolexSerialYearTesterLeadingLetterSeries() {
		Arrays.asList(
					'r',
					'l',
					'e',
					'x',
					'n',
					'c',
					's',
					'w',
					't',
					'u',
					'a',
					'p',
					'k',
					'y',
					'f',
					'd',
					'z',
					'm',
					'v',
					'g').stream().forEach(car -> assertTrue("Testing starting by "+car , RolexSerialYearHelper.startsWithAKnownLetter(car+"123456") && RolexSerialYearHelper.startsWithAKnownLetter(Character.toUpperCase(car)+"123456")));
		
		
		assertFalse(RolexSerialYearHelper.startsWithAKnownLetter("a12345"));
		assertFalse(RolexSerialYearHelper.startsWithAKnownLetter("a1234567"));
		assertFalse(RolexSerialYearHelper.startsWithAKnownLetter("1a23456"));
		
		assertFalse(RolexSerialYearHelper.startsWithAKnownLetter("OT23Q257"));
	}
	
	@Test
	public void rolexSerialYearTesterRandomSeries() {
		assertTrue(RolexSerialYearHelper.isRandomNumber("OT23Q257"));
		assertTrue(RolexSerialYearHelper.isRandomNumber("ot23q257"));
		assertTrue(RolexSerialYearHelper.isRandomNumber("52335J78"));
		
		assertFalse(RolexSerialYearHelper.isRandomNumber("52335J784"));
		assertFalse(RolexSerialYearHelper.isRandomNumber("52335J7"));
		
		assertFalse(RolexSerialYearHelper.isRandomNumber("a123456"));
		assertFalse(RolexSerialYearHelper.isRandomNumber("12345678"));
	}
	
	@Test
	public void rolexSerialYearTesterNumericSeriesWithOverlapping() {
		assertTrue(RolexSerialYearHelper.isNumberOnlyWITHOverlapping("12345"));
		assertTrue(RolexSerialYearHelper.isNumberOnlyWITHOverlapping("123456"));
		assertTrue(RolexSerialYearHelper.isNumberOnlyWITHOverlapping("999999"));
		
		assertFalse(RolexSerialYearHelper.isNumberOnlyWITHOverlapping("1234567"));
		assertFalse(RolexSerialYearHelper.isNumberOnlyWITHOverlapping("1000000"));
		assertFalse(RolexSerialYearHelper.isNumberOnlyWITHOverlapping("9999999"));
	}
	
	@Test
	public void rolexSerialYearTesterNumericSeriesWithoutOverlapping() {
		assertTrue(RolexSerialYearHelper.isNumberOnlyWITHOUTOverlapping("1234567"));
		assertTrue(RolexSerialYearHelper.isNumberOnlyWITHOUTOverlapping("1000000"));
		assertTrue(RolexSerialYearHelper.isNumberOnlyWITHOUTOverlapping("9999999"));
		
		assertFalse(RolexSerialYearHelper.isNumberOnlyWITHOUTOverlapping("12345"));
		assertFalse(RolexSerialYearHelper.isNumberOnlyWITHOUTOverlapping("123456"));
		assertFalse(RolexSerialYearHelper.isNumberOnlyWITHOUTOverlapping("999999"));
	}
}
