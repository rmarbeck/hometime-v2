package fr.hometime.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import models.RolexSerial;
import models.RolexYearFromSerialResult;

public class RolexYearFromSerialResolverSevenDigitsMainResolver implements RolexYearFromSerialResolver {
	private List<RolexYearFromSerialResolver> resolvers;
	
	private RolexYearFromSerialResolverSevenDigitsMainResolver() {
		this.resolvers = new ArrayList<RolexYearFromSerialResolver>();
	}
	
	public RolexYearFromSerialResolverSevenDigitsMainResolver addResolver(RolexYearFromSerialResolver newResolver) {
		this.resolvers.add(newResolver);
		return this;
	}
	
	@Override
	public boolean doFilter(Optional<RolexSerial> serialToTest) {
		return serialToTest.map(serial -> Pattern.matches("[1-9][0-9]{6}", serial.getSerialAsString())).orElse(false);
	}
	
	@Override
	public Optional<RolexYearFromSerialResult> doResolveIfMatches(Optional<RolexSerial> serialToTest) {
		if (!doFilter(serialToTest))
			return Optional.empty();
		return null;//this.resolvers.parallelStream().map(resolver -> resolver.doResolveIfMatches(serialToTest)).filter(Optional::isPresent).reduce(Optional.empty(), accumulator);
	}
}
