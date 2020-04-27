package fr.hometime.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


import org.junit.Test;


public class StringHelperTest {

	@Test
	public void maskPartially() {
		assertThat(StringHelper.maskPartially("abcdefghijklmnopqrstuvwxyz"), equalTo("ab**********************yz"));
		assertThat(StringHelper.maskPartially(null), equalTo(null));
		assertThat(StringHelper.maskPartially("ab"), equalTo("ab"));
		assertThat(StringHelper.maskPartially("abcde"), equalTo("ab*de"));
		assertThat(StringHelper.maskPartially("ab%dfpz.1247412)à&\")'@cde"), equalTo("ab*********************de"));
		
		assertThat(StringHelper.maskPartially("abcdefghijklmnopqrstuvwxyz", 2), equalTo("ab**********************yz"));
		assertThat(StringHelper.maskPartially("abcdefghijklmnopqrstuvwxyz", 3), equalTo("abc********************xyz"));
		assertThat(StringHelper.maskPartially(null, 2), equalTo(null));
		assertThat(StringHelper.maskPartially("ab",-5), equalTo(null));
		assertThat(StringHelper.maskPartially("abcde",2), equalTo("ab*de"));
		assertThat(StringHelper.maskPartially("abcde",3), equalTo("abcde"));
	}
	
}
