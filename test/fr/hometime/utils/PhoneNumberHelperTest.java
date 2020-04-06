package fr.hometime.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Test;


public class PhoneNumberHelperTest {

	@Test
	public void frenchMobileNumbers() {
		assertThat(PhoneNumberHelper.getFrenchPhoneNumberInInternationalFormat("0033620383295"), equalTo("+33620383295"));
		assertThat(PhoneNumberHelper.getFrenchPhoneNumberInInternationalFormat("+33620383295"), equalTo("+33620383295"));
		assertThat(PhoneNumberHelper.getFrenchPhoneNumberInInternationalFormat("+33 620383295"), equalTo("+33620383295"));
		assertThat(PhoneNumberHelper.getFrenchPhoneNumberInInternationalFormat("+33 6 20 38 32 95"), equalTo("+33620383295"));
		assertThat(PhoneNumberHelper.getFrenchPhoneNumberInInternationalFormat("06  20  383295"), equalTo("+33620383295"));
		assertThat(PhoneNumberHelper.tryToGetFrenchPhoneNumberInInternationalFormat("0033620383295"), not(equalTo(Optional.empty())));
	}
	
	@Test
	public void nonFrenchMobileNumbers() {
		assertThat(PhoneNumberHelper.isItAFrenchMobilePhoneNumber("0033420383295"), is(false));
		assertThat(PhoneNumberHelper.isItAFrenchMobilePhoneNumber("+33420383295"), is(false));
		assertThat(PhoneNumberHelper.isItAFrenchMobilePhoneNumber("0420383295"), is(false));
		assertThat(PhoneNumberHelper.isItAFrenchMobilePhoneNumber("04203832  95"), is(false));
		assertThat(PhoneNumberHelper.isItAFrenchMobilePhoneNumber("+32620383295"), is(false));
		assertThat(PhoneNumberHelper.isItAFrenchMobilePhoneNumber("0032620383295"), is(false));
		assertThat(PhoneNumberHelper.tryToGetFrenchPhoneNumberInInternationalFormat("0033420383295"), equalTo(Optional.empty()));
	}
	
	@Test
	public void checkValidity() {
		assertThat(PhoneNumberHelper.isItAValidPhoneNumber("0620383295"), is(true));
		assertThat(PhoneNumberHelper.isItAValidPhoneNumber("06  20  383295"), is(true));
		assertThat(PhoneNumberHelper.isItAValidPhoneNumber("0033420383295"), is(true));
		assertThat(PhoneNumberHelper.isItAValidPhoneNumber("+33420383295"), is(true));
		assertThat(PhoneNumberHelper.isItAValidPhoneNumber("0420383295"), is(true));
		assertThat(PhoneNumberHelper.isItAValidPhoneNumber("04203832  95"), is(true));
		assertThat(PhoneNumberHelper.isItAValidPhoneNumber("+32620383295"), is(true));
		assertThat(PhoneNumberHelper.isItAValidPhoneNumber("0032620383295"), is(true));
		assertThat(PhoneNumberHelper.isItAValidPhoneNumber("0032620383295"), is(true));
		assertThat(PhoneNumberHelper.isItAValidPhoneNumber("0032620383295"), is(true));
		
		assertThat(PhoneNumberHelper.isItAValidPhoneNumber("-0420383295"), is(false));
		assertThat(PhoneNumberHelper.isItAValidPhoneNumber("04203832 ss 95"), is(false));
		assertThat(PhoneNumberHelper.isItAValidPhoneNumber("+32620"), is(false));
		assertThat(PhoneNumberHelper.isItAValidPhoneNumber("00326"), is(false));
		assertThat(PhoneNumberHelper.isItAValidPhoneNumber("062050"), is(false));
		assertThat(PhoneNumberHelper.isItAValidPhoneNumber("062038329556"), is(false));
		
	}
	
}
