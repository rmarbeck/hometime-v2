package fr.hometime.utils;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Singleton;

import models.News;

@Singleton
public class MockStaticNewsProvider implements NewsProvider {
	private Optional<List<News>> news = Optional.empty();
	
	@Override
	public Optional<List<News>> retrieveNews() {
		if (!news.isPresent()) {
			news = Optional.of(Arrays.asList(
					(new News("Titre1", "Contenu 1", News.NewsType.ONE_PICTURE, Date.from(Instant.now())).addCategory("General").addPreviewUrl("/assets/images/demo-parallax.jpg").addPreviewAlt("toto").activate()),
					(new News("Titre2", "Contenu 2", News.NewsType.ONE_PICTURE, Date.from(Instant.now())).addCategory("General").addPreviewUrl("/assets/images/demo-parallax.jpg").addPreviewAlt("toto").setReadMoreUrl("blog-single.html").activate()),
					(new News("Titre Video", "Mocked contenu Video", News.NewsType.VIDEO, Date.from(Instant.now())).addCategory("General").addPreviewUrl("http://player.vimeo.com/video/87701971").activate())
					));
		}
		return news;
	}
}