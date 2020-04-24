package models;

public class BrandProxy {
	public String id;
	
	public String display_name;
	
	public BrandProxy() {
		this("unknown", "unknown");
	}
	
	public BrandProxy(String id) {
		this(id, "unknown");
	}
	
	public BrandProxy(String id, String displayName) {
		this.id = id;
		this.display_name = displayName;
	}
}
