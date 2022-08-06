package br.pi.gov.seduc.apiSpringMysql.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.pi.gov.seduc.apiSpringMysql.dtos.UsersDto;
import br.pi.gov.seduc.apiSpringMysql.entities.Users;
import br.pi.gov.seduc.apiSpringMysql.services.UsersServices;

@RestController
@RequestMapping({"/users"})
public class UsersControllers {
	
	@Autowired
	private UsersServices service;
	
	@CrossOrigin
	@GetMapping
	public List<UsersDto> findAll(){
		 service.findAll();
		return service.findAll().stream()
				.map(user -> new UsersDto(user))
				.collect(Collectors.toList());
	}
	
	@CrossOrigin
	@GetMapping(path = {"/{id}"})
	public UsersDto findById(@PathVariable Long id){
		Users user = service.findById(id);
		UsersDto userdto = new UsersDto(user);
		return userdto;
	}
	
	@CrossOrigin
	@PostMapping
	public ResponseEntity<Users> create(@RequestBody Users users){
		users = service.create(users);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
		.buildAndExpand(users.getId()).toUri();
		return ResponseEntity.created(uri).body(users);
	}
	
	@CrossOrigin
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
	public ResponseEntity<Users> update(@RequestBody Users users, @PathVariable Long id){
		users = service.update(users, id);
		return ResponseEntity.noContent().build();
	}
	
	@CrossOrigin
	@DeleteMapping(value = {"/{id}"})
	public ResponseEntity<?> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}
