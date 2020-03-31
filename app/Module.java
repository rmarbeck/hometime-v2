import com.google.inject.AbstractModule;

import fr.hometime.utils.I18nOrDBContentProvider;
import fr.hometime.utils.ContentProvider;

public class Module extends AbstractModule {

	@Override
	protected void configure() {
		bind(ContentProvider.class).to(I18nOrDBContentProvider.class);
	}

}
