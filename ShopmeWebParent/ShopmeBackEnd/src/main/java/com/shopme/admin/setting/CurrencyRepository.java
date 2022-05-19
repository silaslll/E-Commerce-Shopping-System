package com.shopme.admin.setting;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import com.shopme.common.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {
	public List<Currency> findAllByOrderByNameAsc();
	
}