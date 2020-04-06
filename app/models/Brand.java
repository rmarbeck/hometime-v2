package models;

import java.util.Optional;

public class Brand {
	public Long id;
	public String internal_name;
	
	public String display_name;
	
	public String seo_name;

	public Optional<String> logo_url;
	
	public Optional<String> alt;
	
	public boolean supported = true;
	
	public Optional<String> description;
	
	public Optional<String> remarks;
	
	public int quartz_category = 0;
	
	private Brand() {
	}
	
	public Brand(Long id, String internalName) {
		this(id, internalName, internalName, internalName);
	}
	
	public Brand(Long id, String internalName, String displayName, String seoName) {
		this.id = id;
		this.internal_name = internalName;
		this.display_name = displayName;
		this.seo_name = seoName;
	}
	
	public String getInternalName() {
		return internal_name;
	}

	public void setDisplay_name(String displayName) {
		this.display_name = displayName;
	}

	public void setSeo_name(String seoName) {
		this.seo_name = seo_name;
	}

	public void setLogo_url(String logoUrl) {
		this.logo_url = Optional.of(logoUrl);
	}

	public void setSupported(boolean supported) {
		this.supported = supported;
	}

	public void setQuartz_category(int quartz_category) {
		this.quartz_category = quartz_category;
	}
}
