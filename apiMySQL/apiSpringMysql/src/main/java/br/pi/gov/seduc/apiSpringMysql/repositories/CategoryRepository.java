package br.pi.gov.seduc.apiSpringMysql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pi.gov.seduc.apiSpringMysql.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}