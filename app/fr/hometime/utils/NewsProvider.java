package fr.hometime.utils;

import java.util.List;
import java.util.Optional;

import models.News;

public interface NewsProvider extends DataProvider {
	public Optional<List<News>> retrieveNews();
}
