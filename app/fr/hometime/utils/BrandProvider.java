package fr.hometime.utils;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import models.Brand;

public interface BrandProvider {
	public Optional<List<Brand>> retrieveBrands();
	
	public default Optional<Brand> getBrandById(long id) {
		return Hidden.getBrandByFilter(brand -> (brand.id==id), retrieveBrands());
	}
	
	public default Optional<Brand> getBrandBySeoName(String seoName) {
		return Hidden.getBrandByFilter(brand -> (brand.seo_name.equals(seoName)), retrieveBrands());
	}
	
	public default Optional<Brand> getBrandByInternalName(String internalName) {
		return Hidden.getBrandByFilter(brand -> (brand.internal_name.equals(internalName)), retrieveBrands());
	}
	
	class Hidden {
        private static Optional<Brand> getBrandByFilter(Predicate<Brand> tester, Optional<List<Brand>> brands) {
        	return brands.map(list -> list.stream().filter(tester).findFirst()).orElseGet(() -> Optional.empty());
        }
    }
}
