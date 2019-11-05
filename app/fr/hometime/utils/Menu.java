package fr.hometime.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Defines a web menu as a tree of Menus
 */

public class Menu {
	private String label;
	private Optional<String> href;
	private Optional<String> cssClass;
	private Optional<String> img;
	
	private Optional<List<Menu>> subMenus;
	
	private Optional<Menu> parent;
	
	public Menu(String label) {
		this.label = label;
		href = Optional.empty();
		cssClass = Optional.empty();
		img = Optional.empty();
		subMenus = Optional.empty();
		parent = Optional.empty();
	}
	
	public Menu(String label, String href) {
		this(label);
		this.href= Optional.of(href);
	}
	
	public Menu(String label, String href, String cssClass, String img) {
		this(label, href);
		this.cssClass= Optional.of(cssClass);
		this.img= Optional.of(img);
	}
	
	private Menu(String label, Optional<String> href, Optional<String> cssClass, Optional<String> img) {
		this(label);
		href.ifPresent(v -> this.href = href);
		cssClass.ifPresent(v -> this.cssClass = cssClass);
		img.ifPresent(v -> this.img = img);
	}
	
	public static Menu of(String label, Optional<String> href, Optional<String> cssClass, Optional<String> img) {
		return new Menu(label, href, cssClass, img);
	}
	
	public Menu addSubMenu(Menu subMenu) {
		if (!this.subMenus.isPresent())
			this.subMenus = Optional.of(new ArrayList<Menu>());
		this.subMenus.get().add(subMenu);
		subMenu.parent = Optional.of(this);
		return subMenu;
	}
	
	public Menu addSubMenu(String label, Optional<String> href, Optional<String> cssClass, Optional<String> img) {
		return addSubMenu(new Menu(label, href, cssClass, img));
	}
	
	public Menu addSubMenus(List<Menu> subMenus) {
		if (!this.subMenus.isPresent()) {
			this.subMenus = Optional.of(subMenus);
		} else {
			this.subMenus.get().addAll(subMenus);
		}
		
		return this;
	}

	public String getLabel() {
		return label;
	}

	public Optional<String> getHref() {
		return href;
	}
	
	public boolean hasHref() {
		return hasValueDefined(href);
	}

	public Optional<String> getCssClass() {
		return cssClass;
	}
	
	public boolean hasCssClass() {
		return hasValueDefined(cssClass);
	}

	public Optional<String> getImg() {
		return img;
	}
	
	public boolean hasImg() {
		return hasValueDefined(img);
	}

	public Optional<List<Menu>> getSubMenus() {
		return subMenus;
	}

	public boolean hasSubMenus() {
		return this.subMenus.isPresent() && this.subMenus.get().size() != 0;
	}
	
	public Optional<Menu> getParent() {
		return parent;
	}
	
	public Menu getParentIfExists() {
		return parent.orElse(null);
	}
	
	public Menu getHighestParentIfExists() {
		Menu currentMenu = this;
		while(currentMenu.hasParent())
			currentMenu = currentMenu.getParentIfExists();
		return currentMenu;
	}


	public boolean hasParent() {
		return this.parent.isPresent();
	}
	
	private boolean hasValueDefined(Optional<String> value) {
		return value.isPresent();
	}
}
