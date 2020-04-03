import com.google.inject.AbstractModule;

import fr.hometime.utils.I18nOrDBContentProvider;
import fr.hometime.utils.StaticBrandProvider;
import fr.hometime.utils.BrandProvider;
import fr.hometime.utils.ContentProvider;
import fr.hometime.utils.ExternalWSBrandProvider;

public class Module extends AbstractModule {

	@Override
	protected void configure() {
		bind(ContentProvider.class).to(I18nOrDBContentProvider.class);
		bind(BrandProvider.class).to(ExternalWSBrandProvider.class);
	}

}
