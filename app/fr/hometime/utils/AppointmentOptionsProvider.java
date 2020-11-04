package fr.hometime.utils;

import java.util.List;
import java.util.Optional;

import models.AppointmentOptionProxy;
import models.Price;

public interface AppointmentOptionsProvider extends DataProvider {
	public Optional<List<AppointmentOptionProxy>> retrieveAvailableOptions();
}
