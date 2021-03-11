package fr.hometime.utils;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import models.RolexSerial;
import models.RolexYearFromSerialResult;

public class RolexYearFromSerialResolverDefault implements RolexYearFromSerialResolver {
	private Predicate<RolexSerial> filter;
	private Function<RolexSerial, RolexYearFromSerialResult> resolver;
	
	private RolexYearFromSerialResolverDefault(Predicate<RolexSerial> filter, Function<RolexSerial, RolexYearFromSerialResult> resolver) {
		this.filter = filter;
		this.resolver = resolver;
	}
	
	public static RolexYearFromSerialResolver of(Predicate<RolexSerial> filter, Function<RolexSerial, RolexYearFromSerialResult> resolver) {
		return new RolexYearFromSerialResolverDefault(filter, resolver);
	}

	@Override
	public boolean doFilter(Optional<RolexSerial> serial) {
		return serial.filter(filter).isPresent();
	}

	@Override
	public Optional<RolexYearFromSerialResult> doResolve(Optional<RolexSerial> serial) {
		return serial.map(resolver);
	}
}
