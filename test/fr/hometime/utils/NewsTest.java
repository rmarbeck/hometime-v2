package fr.hometime.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import org.junit.Test;

import models.News;


public class NewsTest {

	@Test
	public void buildingTest() {
		assertThat((new News("Nouveau site Web", "Hometime.fr se modernise. Notre nouveau site est en ligne !", News.NewsType.ONE_PICTURE, Date.from(Instant.ofEpochSecond(1589025893))).addCategory("General").addPreviewUrl("https://s3.eu-west-3.amazonaws.com/images.hometime.fr/news/web-home-1000.jpg").addPreviewAlt("Homepage").setReadMoreUrl("https://www.hometime.fr/").activate()).getType(), equalTo("General"));
	}
}
