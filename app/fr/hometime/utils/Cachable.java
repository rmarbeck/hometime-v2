package fr.hometime.utils;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Supplier;

public interface Cachable<T> {
	public Cachable<T> of(Supplier<T> supplier, long durationValue, ChronoUnit unitOfTime);
	public Cachable<T> of(Supplier<T> supplier);
	public Optional<T> get();
	public void flush();
	public boolean isValid();
}
