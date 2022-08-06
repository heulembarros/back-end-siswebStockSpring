package br.pi.gov.seduc.apiSpringMysql.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.pi.gov.seduc.apiSpringMysql.entities.Users;
import br.pi.gov.seduc.apiSpringMysql.repositories.UsersRepository;

@Service
public class UsersServices {
	@Autowired
	private UsersRepository repository;
	
	@CrossOrigin
	@GetMapping
	public List<Users> findAll(){return repository.findAll();}
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private String senhaComHash;
	
			
	@CrossOrigin
	public Users findById(@PathVariable long id){
		Optional<Users> obj = repository.findById(id);
		return obj.get();
	} //findById end
	
	@CrossOrigin
	@PostMapping
	public Users create(@Validated @RequestBody Users user){
		this.senhaComHash = passwordEncoder.encode(user.getPassword());
		user.setPassword(this.senhaComHash);
	    return repository.save(user);
	} //post end
	
	@CrossOrigin
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
	public Users update(@RequestBody Users user, @PathVariable long id) {
		user.setId(id);
		return repository.save(user);		
	} // put method end
	
	@CrossOrigin
	public ResponseEntity<?> delete(Long id) {
		repository.deleteById(id);
		return null;
	} //delete end
}
