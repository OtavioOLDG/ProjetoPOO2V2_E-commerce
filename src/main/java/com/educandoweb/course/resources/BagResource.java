package com.educandoweb.course.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.educandoweb.course.entities.Bag;
import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.Product;
import com.educandoweb.course.entities.User;
import com.educandoweb.course.repositories.UserRepository;
import com.educandoweb.course.resources.dto.BuyingBag;
import com.educandoweb.course.resources.dto.DeleteFromBag;
import com.educandoweb.course.services.BagService;
import com.educandoweb.course.services.ProductServices;
import com.educandoweb.course.services.UserServices;
import com.educandoweb.course.services.exceptions.NotAllowedEception;

@RestController
@RequestMapping(value ="/bag")
public class BagResource {
	
	@Autowired
	BagService service;
	
	@Autowired
	UserRepository repository;
	
	@Autowired
	UserServices userServices;
	
	@Autowired
	ProductServices productServices;
	
	@GetMapping
	public ResponseEntity<List<Order>> getOrders(JwtAuthenticationToken jwt) {
		var user = repository.findById(Long.parseLong(jwt.getName())).orElseThrow(() -> new RuntimeException("não achou"));
		//List<Order> orders = service.getBag(jwt);
		List<Order> orders = user.getOrders();
		
		return ResponseEntity.ok().body(orders);
	}
	
	@GetMapping(value ="/{id}")
	public ResponseEntity<Bag> findById(@PathVariable Long id, JwtAuthenticationToken jwt) {
		User user = userServices.findById(Long.parseLong(jwt.getName()));
		List<Bag> bags = userServices.getBags(user);
		Bag bag = service.findById(id);
		if(bags.contains(bag)) {
			return ResponseEntity.ok().body(bag);
		} else {
			throw new NotAllowedEception("Insuficient permission");
		}
	}
	
	@PostMapping(value = "/to_bag/{product_id}")
	public ResponseEntity<Void> test(@RequestBody BuyingBag buyingBag, @PathVariable Long product_id, JwtAuthenticationToken token){
		User user =  userServices.findById(Long.parseLong(token.getName()));
		Product product = productServices.findById(product_id);
		
		Bag bag = new Bag(buyingBag.quantity(), user, product, product.getPrice());
		service.save(bag);
		return null;
	}
	
	@GetMapping(value ="/user_bags")
	public ResponseEntity<List<Bag>> findBags(JwtAuthenticationToken jwt) {
		User user = userServices.findById(Long.parseLong(jwt.getName()));
		List<Bag> bags = userServices.getBags(user);
		return ResponseEntity.ok().body(bags);
	}
	
	@DeleteMapping
	public ResponseEntity<Void> deleteById(@RequestBody DeleteFromBag dBag, JwtAuthenticationToken jwt) {
		User user = userServices.findById(Long.parseLong(jwt.getName()));
		List<Bag> bags = userServices.getBags(user);
		Bag bag = service.findById(dBag.id());
		if(bags.contains(bag)) {
			service.delete(bag);
			return null;
		} else {
			throw new NotAllowedEception("Insuficient permission");
		}
	}
	
	@PostMapping
	public ResponseEntity<Void> buy(@RequestBody DeleteFromBag dBag, JwtAuthenticationToken jwt) {
		User user = userServices.findById(Long.parseLong(jwt.getName()));
		List<Bag> bags = userServices.getBags(user);
		Bag bag = service.findById(dBag.id());
		if(bags.contains(bag)) {
			service.delete(bag);
			return null;
		} else {
			throw new NotAllowedEception("Insuficient permission");
		}
	}
	
	@PostMapping
	public ResponseEntity<Void> test(@RequestBody DeleteFromBag dBag, JwtAuthenticationToken jwt) {
		User user = userServices.findById(Long.parseLong(jwt.getName()));
		List<Bag> bags = userServices.getBags(user);
		Bag bag = service.findById(dBag.id());
		if(bags.contains(bag)) {
			service.delete(bag);
			return null;
		} else {
			throw new NotAllowedEception("Insuficient permission");
		}
	}
	
	
	
	
}
