package com.blogpessoal.BlogPessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogpessoal.BlogPessoal.model.Postagem;


//define como um repositorio
@Repository
// agora o postagemrepository tem as class do Jpa
public interface PostagemRepository extends JpaRepository<Postagem, Long> {
	public List<Postagem> findAllByTituloContainingIgnoreCase (String titulo);
	
	
	

}
