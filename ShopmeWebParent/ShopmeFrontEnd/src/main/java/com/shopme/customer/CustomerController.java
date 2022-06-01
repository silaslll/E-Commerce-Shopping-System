package com.shopme.customer;

import java.io.UnsupportedEncodingException;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.Utility;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;

@Controller
public class CustomerController {
	@Autowired private CustomerService customerService;
	@Autowired private SettingService settingService;
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		List<Country> listCountries = customerService.listAllCountries();
		
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "Customer Registration");
		model.addAttribute("customer", new Customer());
		
		return "register/register_form";
	}
	
	@PostMapping("/create_customer")
	public String createCustomer(Customer customer, Model model,
			HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		customerService.registerCustomer(customer);
		sendVerificationEmail(request, customer);
		
		model.addAttribute("pageTitle", "Registration Succeeded!");
		
		return "/register/register_success";
	}

	private void sendVerificationEmail(HttpServletRequest request, Customer customer) 
			throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		
		String toAddress = customer.getEmail();
		String subject = emailSettings.getCustomerVerifySubject();
		String content = emailSettings.getCustomerVerifyContent();
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		content = content.replace("[[name]]", customer.getFullName());
		
		String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
		
		content = content.replace("[[URL]]", verifyURL);
		
		helper.setText(content, true);
		
		mailSender.send(message);
		
		System.out.println("to Address: " + toAddress);
		System.out.println("Verify URL: " + verifyURL);
	}
	

	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code, Model model) {
		boolean verified = customerService.verify(code);

		return "register/" + (verified ? "verify_success" : "verify_fail");
	}
}