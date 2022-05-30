package com.blogpessoal.BlogPessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogpessoal.BlogPessoal.model.Postagem;
import com.blogpessoal.BlogPessoal.repository.PostagemRepository;
//indica que é uma classe controladora da api(onde fica os endpoint)
@RestController
//ciar um endpoint
@RequestMapping("/postagem")
//Permite que requisiÇões de outras portas sejam aceitas na minha aplicação.
@CrossOrigin("*")
public class PostagemController {
	
	//Autowired funciona como injeção de dependencia, Transferindo a resposabilididade para o repository
	@Autowired
	private PostagemRepository repository;
	
	//tras tudo que quem dentro da minha postagem que fica na model
	//ou seja, tudo que tem dentro de postagem, por isso usar o "list",
	//GetMapping indica o verbo que pode ser utilizado no endpoint
	
	@GetMapping
	public ResponseEntity<List<Postagem>> GetAll(){
		return ResponseEntity.ok(repository.findAll());
			
	}
	
	@GetMapping("/{id}")
	//@PathVariable permite que façamos a solicitação por URL
	public ResponseEntity<Postagem> BuscaId(@PathVariable Long id){
		return repository.findById(id)
				//
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());		
	}
	
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> BuscaTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(repository.findAllByTituloContainingIgnoreCase(titulo));
		
	}
	
	
	//postmapping insere dados no banco,
	// requestBody informa o formato do "commo são os campos que serão inseridos".
	@PostMapping
	public ResponseEntity<Postagem> publicarPostagem(@RequestBody Postagem postagem){
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(postagem));
		
	}
	
	@PutMapping
	public ResponseEntity<Postagem> atualizaPostagem(@RequestBody Postagem postagem){
		return ResponseEntity.status(HttpStatus.OK).body(repository.save(postagem));
		
	}
	
	@DeleteMapping("/{id}")
	public void deletePostagem(@PathVariable Long id) {
		repository.deleteById(id);
	}
	
	
	
}
