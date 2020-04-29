import java.util.List;

import com.google.inject.TypeLiteral;

import com.google.inject.AbstractModule;

import fr.hometime.utils.I18nOrDBContentProvider;
import fr.hometime.utils.InMemoryCached;
import fr.hometime.utils.JsonLocalFileProvider;
import fr.hometime.utils.JsonLocalFileProviderImpl;
import fr.hometime.utils.JsonWSProvider;
import fr.hometime.utils.JsonWSProviderImpl;
import fr.hometime.utils.NewsProvider;
import fr.hometime.utils.PriceProvider;
import fr.hometime.utils.StaticNewsProvider;
import models.Brand;
import models.Feedback;
import models.Price;
import fr.hometime.utils.BrandProvider;
import fr.hometime.utils.Cachable;
import fr.hometime.utils.CachedMultiSourceDataProvider;
import fr.hometime.utils.ContentProvider;
import fr.hometime.utils.FeedbackProvider;

public class Module extends AbstractModule {

	@Override
	protected void configure() {
		bind(ContentProvider.class).to(I18nOrDBContentProvider.class);
		bind(Cachable.class).to(InMemoryCached.class);
		bind(new TypeLiteral<Cachable<List<Brand>>>() {}).to(new TypeLiteral<InMemoryCached<List<Brand>>>() {});
		bind(new TypeLiteral<Cachable<List<Feedback>>>() {}).to(new TypeLiteral<InMemoryCached<List<Feedback>>>() {});
		bind(new TypeLiteral<Cachable<List<Price>>>() {}).to(new TypeLiteral<InMemoryCached<List<Price>>>() {});
		bind(PriceProvider.class).to(CachedMultiSourceDataProvider.class);
		bind(BrandProvider.class).to(CachedMultiSourceDataProvider.class);
		bind(FeedbackProvider.class).to(CachedMultiSourceDataProvider.class);
		bind(NewsProvider.class).to(StaticNewsProvider.class);
		bind(JsonWSProvider.class).to(JsonWSProviderImpl.class);
		bind(JsonLocalFileProvider.class).to(JsonLocalFileProviderImpl.class);
	}

}
