package fr.hometime.utils;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Singleton;

import models.News;

@Singleton
public class StaticNewsProvider implements NewsProvider {
	private Optional<List<News>> news = Optional.empty();
	
	@Override
	public Optional<List<News>> retrieveNews() {
		if (!news.isPresent()) {
			news = Optional.of(Arrays.asList(
					(new News("Réouverture le 11 mai", "<b>Déconfinement</b> : nous réouvrons le 11 mai à 10h après près de 2 mois de fermeture suite à la situation sanitaire. Nous avons adapté nos locaux et pris des mesures pour tous nous protéger. A très bientôt !<br /><br />L'équipe Hometime.", News.NewsType.ONE_PICTURE, Date.from(Instant.ofEpochSecond(1589025893))).addCategory("General").addPreviewUrl("https://s3.eu-west-3.amazonaws.com/images.hometime.fr/news/working-1000.jpg").addPreviewAlt("Facebook").setReadMoreUrl("https://www.facebook.com/HometimeFr").activate()),
					(new News("Nouveau site Web", "Hometime.fr se modernise. Notre nouveau site est en ligne !", News.NewsType.ONE_PICTURE, Date.from(Instant.ofEpochSecond(1589025893))).addCategory("General").addPreviewUrl("https://s3.eu-west-3.amazonaws.com/images.hometime.fr/news/web-home-1000.jpg").addPreviewAlt("Homepage").setReadMoreUrl("https://www.hometime.fr/").activate())
					));
		}
		return news;
	}
}