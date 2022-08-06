package br.pi.gov.seduc.apiSpringMysql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pi.gov.seduc.apiSpringMysql.entities.Users;

public interface UsersRepository extends JpaRepository<Users, Long>  {

}
