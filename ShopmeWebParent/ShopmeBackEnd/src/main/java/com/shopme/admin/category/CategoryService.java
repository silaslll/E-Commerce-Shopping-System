package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;
import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository repo;
	
	
	//list Hierarchical Categories
	public List<Category> listAll() {
		List<Category> rootCategories = repo.findRootCategories();
		return listHierarchicalCategories(rootCategories);
	}
	
	private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
		List<Category> hierarchicalCategories = new ArrayList<>();
		
		for (Category rootCategory : rootCategories) {
			hierarchicalCategories.add(Category.copyFull(rootCategory));
			
			Set<Category> children = rootCategory.getChildren();
			
			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalCategories.add(Category.copyFull(subCategory, name));
				
				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
			}
		}
		
		return hierarchicalCategories;
	}
	
	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
			Category parent, int subLevel) {
		Set<Category> children = parent.getChildren();
		int newSubLevel = subLevel + 1;
		
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {				
				name += "--";
			}
			name += subCategory.getName();
		
			hierarchicalCategories.add(Category.copyFull(subCategory, name));
			
			listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel);
		}
		
	}
	
	public Category save(Category category) {
		return repo.save(category);
	}
	
	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<>();
		
		Iterable<Category> categoriesInDB = repo.findAll();
		
		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				categoriesUsedInForm.add(Category.copyIdAndName(category));
				
				Set<Category> children = category.getChildren();
				
				for (Category subCategory : children) {
					String name = "--" + subCategory.getName();
					categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
					
					listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
				}
			}
		}		
		
		return categoriesUsedInForm;
	}
	
	private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, 
			Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {				
				name += "--";
			}
			name += subCategory.getName();
			
			categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
			
			listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
		}		
	}
	
	public Category get(Integer id) throws CategoryNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CategoryNotFoundException("Could not find any category with ID " + id);
		}
	}
	
	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreatingNew = (id == null || id == 0);

		Category categoryByName = repo.findByName(name);

		if (isCreatingNew) {
			if (categoryByName != null) {
				return "DuplicateName";
			} else {
				Category categoryByAlias = repo.findByAlias(alias);
				if (categoryByAlias != null) {
					return "DuplicateAlias";	
				}
			}
		} else {
			if (categoryByName != null && categoryByName.getId() != id) {
				return "DuplicateName";
			}

			Category categoryByAlias = repo.findByAlias(alias);
			if (categoryByAlias != null && categoryByAlias.getId() != id) {
				return "DuplicateAlias";
			}

		}

		return "OK";
	}
}