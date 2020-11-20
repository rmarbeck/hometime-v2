package fr.hometime.utils;

import java.util.List;
import java.util.Optional;

import models.LiveConfig;

public interface LiveConfigProvider extends DataProvider {
	public Optional<List<LiveConfig>> retrieveLiveConfigs();
}
