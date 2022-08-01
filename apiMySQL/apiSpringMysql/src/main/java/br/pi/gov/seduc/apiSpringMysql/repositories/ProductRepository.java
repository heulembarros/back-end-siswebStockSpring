package br.pi.gov.seduc.apiSpringMysql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pi.gov.seduc.apiSpringMysql.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}