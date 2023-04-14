package com.employee.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.print.attribute.standard.Media;

//import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.employee.entity.Employee;
import com.employee.entity.ImageModel;
import com.employee.exception.ResourseNotFound;
import com.employee.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")


public class EmployeeController {
	@Autowired
	public EmployeeRepository repository;
	
	@GetMapping("employees")
	public List<Employee> getAllEmployees(){
		System.out.println(repository.findAll());
		return repository.findAll();
	}
	
	@PostMapping(value = {"employees"},consumes= {MediaType.MULTIPART_FORM_DATA_VALUE})
	public Employee saveEmployee(@RequestPart("employee") Employee employee,
			                      @RequestPart("imageFile")MultipartFile[] file) {
//		return repository.save(employee);
		
		try {
		   Set<ImageModel> images=uploadImage(file);
		   employee.setProductImages(images);
		   return repository.save(employee);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException {
		Set<ImageModel> imageModels=new HashSet<>();
		
		for(MultipartFile file: multipartFiles) {
			ImageModel imageModel=new ImageModel(
					file.getOriginalFilename(),
					file.getContentType(),
					file.getBytes()
					);
			imageModels.add(imageModel);
		}
		return imageModels;
	}
	
	@GetMapping("employees/{id}")
	
	public ResponseEntity<Employee> getEmployeeByID(@PathVariable int id){
		
		Employee employee = repository.findById(id).orElseThrow(() -> new ResourseNotFound("No Record found with this id:" + id));
		return ResponseEntity.ok(employee);
	}
	
	@PutMapping("employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable int id,@RequestBody Employee employee){
		Employee employee2 = repository.findById(id).orElseThrow(() -> new ResourseNotFound("no record found with this id:" + 	id));
		employee2.setUsername(employee.getUsername());
		employee2.setRole(employee.getRole());
		employee2.setEmail(employee.getEmail());
		employee2.setPhone(employee.getPhone());
		employee2.setPassword(employee.getPassword());
		Employee updateEmployee=repository.save(employee2);
		return ResponseEntity.ok(updateEmployee);
	}
	
	@DeleteMapping("employees/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable int id){
		Employee employee=repository.findById(id).orElseThrow(() -> new ResourseNotFound("no record found with this id:" + 	id));
		repository.delete(employee);
		Map<String, Boolean> response=new HashMap<>();
		response.put("delete", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}