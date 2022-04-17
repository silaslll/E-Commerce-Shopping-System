package com.shopme.admin.user.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.repository.query.Param;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.user.UserService;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.admin.user.export.UserExcelExporter;
import com.shopme.admin.user.export.UserPdfExporter;
import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.UserService;


@Controller
public class UserController {						//This class is for handling methods

	@Autowired
	private UserService service;
	

	@GetMapping("/users")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "firstName", "asc", null);
	}
	
	
	/*URI parameter (Path Param) is basically used to identify a 
	 * specific resource or resources whereas Query Parameter is 
	 * used to sort/filter those resources.
	 * */
	
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword
			) {
		System.out.println("Sort Field: " + sortField);
		System.out.println("Sort Order: " + sortDir);

		Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		
		List<User> listUsers = page.getContent();
		
		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortField", sortField);              //in order to show the direction of arrow icon
		model.addAttribute("sortDir", sortDir);					 //in order to show the direction of arrow icon
		model.addAttribute("reverseSortDir", reverseSortDir);	 //in order to show the direction of arrow icon
		model.addAttribute("keyword", keyword);

		
		return "users/users";		

	}
	
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();
		
		User user = new User();
		user.setEnabled(true);
		
		//addAttribute will add the value to the name ${user} in the html file  
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		
		return "users/user_form";
	}
	
	@PostMapping("/users/save")                 //post request is supported in user_form.html
	public String saveUser(User user, RedirectAttributes redirectAttributes ,   //show the successful message
			@RequestParam("image") MultipartFile multipartFile) throws IOException { 
		if (!multipartFile.isEmpty()) {                                         //meaning form has an upload file
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = service.save(user);
			
			String uploadDir = "user-photos/" + savedUser.getId();
			
			FileUploadUtil.cleanDir(uploadDir);                            //delete previous image before saving the image
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
		} else {
			if (user.getPhotos().isEmpty()) user.setPhotos(null);
			service.save(user);
		}
			
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully."); //（Attribute name ,  content)
		
		return "redirect:/users";
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id,    //map the value from url
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			User user = service.get(id);                            //edit the user with specified Id, it's different than creating user since we declare a new user when creating users 
			List<Role> listRoles = service.listRoles();
			
			model.addAttribute("user", user);                                 //add to the th:object="${user}" in the user_form.html
			model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
			model.addAttribute("listRoles", listRoles);
			
			return "users/user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());         //in order to set massage when redirecting
			return "redirect:/users";
		}
	}
	
	
	//delete user
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);;
			redirectAttributes.addFlashAttribute("message", 
					"The user ID " + id + " has been deleted successfully");
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/users";
	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The user ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		
		return "redirect:/users";
	}
	
	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();

		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();

		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);
	}	
	
}

