package models;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import fr.hometime.utils.RolexSerialHelper;
import fr.hometime.utils.RolexSerialYearHelper;
import fr.hometime.utils.RolexYearFromSerialResolver;
import fr.hometime.utils.RolexYearFromSerialResolverDefault;

public class RolexSerial {
	private String strSerialNumber;
	private static List<RolexYearFromSerialResolver> resolvers = Arrays.asList(
			RolexYearFromSerialResolverDefault.of(doTest(RolexSerialYearHelper::startsWithAKnownLetter), doResolve(RolexSerialYearHelper::resolveStartingWithAKnownLetter)),
			RolexYearFromSerialResolverDefault.of(doTest(RolexSerialYearHelper::isRandomNumber), doResolve(RolexSerialYearHelper::resolveRandomNumber)),
			RolexYearFromSerialResolverDefault.of(doTest(RolexSerialYearHelper::isNumberOnlyWITHOverlapping), serialValue -> RolexYearFromSerialResolver.createSimpleYear(2000)),
			RolexYearFromSerialResolverDefault.of(doTest(RolexSerialYearHelper::isNumberOnlyWITHOUTOverlapping), serialValue -> RolexYearFromSerialResolver.createSimpleYear(2000)));
	
	private RolexSerial(String serial) {
		this.strSerialNumber = serial;
	}
	
	public static Optional<RolexSerial> of(Optional<String> serialCandidate) {
		if (RolexSerialHelper.serialMandatoryTests(serialCandidate))
			return Optional.of(new RolexSerial(serialCandidate.get()));
		return Optional.empty();
	}
	
	public static Optional<RolexSerial> of(String serialCandidate) {
		return of(Optional.ofNullable(serialCandidate));
	}
	
	public Optional<RolexYearFromSerialResult> getYears() {
		return 	this.resolvers.stream().filter(resolver -> resolver.doFilter(Optional.of(this))).findFirst().map(resolver -> resolver.doResolveIfMatches(Optional.of(this))).orElse(Optional.empty());
	}
	
	public static Predicate<RolexSerial> doTest(Predicate<String> serialTester) {
		return (instance) -> serialTester.test(instance.strSerialNumber);
	}
	
	public static Function<RolexSerial, RolexYearFromSerialResult> doResolve(Function<String, Optional<RolexYearFromSerialResult>> serialResolver) {
		return (instance) -> serialResolver.apply(instance.strSerialNumber).get();
	}
	
	public String getSerialAsString() {
		return strSerialNumber;
	}
}
