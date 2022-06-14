package com.blogpessoal.BlogPessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.blogpessoal.BlogPessoal.model.UserLogin;
import com.blogpessoal.BlogPessoal.model.Usuario;
import com.blogpessoal.BlogPessoal.repository.UsuarioRepository;
import com.blogpessoal.BlogPessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//indica em ordem qual teste será executado
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UsuarioService usuarioService; 
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		usuarioRepository.deleteAll();
		
	}
	
	@Test
	@Order(1)
	@DisplayName("Cadastrar um Usuário")
	public void deveCriarUmUsuario() {
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(
				0L,"João Paz","https://th.bing.com/th/id/OIP.Tzs3s3tVrtDBHGTMkVzwtwHaHa?w=174&h=180&c=7&r=0&o=5&pid=1.7","João@gmail.com","12345678"));	
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar",HttpMethod.POST,requisicao,Usuario.class);
		assertEquals(HttpStatus.CREATED,resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(),resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getFoto(),resposta.getBody().getFoto());
		assertEquals(requisicao.getBody().getUsuario(),resposta.getBody().getUsuario());
	}
	
	@Test
	@Order(2)
	@DisplayName("Não deve duplicar Usuário")
	public void naoDeveDuplicarUmUsuario() {
		usuarioService.CadastrarUsuario(new Usuario(
				0L,"Maria Paz","https://th.bing.com/th/id/OIP.Tzs3s3tVrtDBHGTMkVzwtwHaHa?w=174&h=180&c=7&r=0&o=5&pid=1.7","João@gmail.com","12345678"));
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(
				0L,"João Paz","https://th.bing.com/th/id/OIP.Tzs3s3tVrtDBHGTMkVzwtwHaHa?w=174&h=180&c=7&r=0&o=5&pid=1.7","João@gmail.com","12345678"));
		ResponseEntity<Usuario> resposta = testRestTemplate
				.exchange("/usuarios/cadastrar",HttpMethod.POST,requisicao,Usuario.class);
		assertEquals(HttpStatus.BAD_REQUEST,resposta.getStatusCode());
	}
	
	@Test
	@Order(3)
	@DisplayName("deve atualizar um Usuário")
	public void DeveAtualizarUmUsuario() {
		
		Optional<Usuario> usuarioCadastrado = usuarioService.CadastrarUsuario(new Usuario(0L,"João Paz",
				"https://th.bing.com/th/id/OIP.Tzs3s3tVrtDBHGTMkVzwtwHaHa?w=174&h=180&c=7&r=0&o=5&pid=1.7","João@gmail.com","12345678"));
		
		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(),
				"João Paz","https://th.bing.com/th/id/OIP.Tzs3s3tVrtDBHGTMkVzwtwHaHa?w=174&h=180&c=7&r=0&o=5&pid=1.7","João@gmail.com","12345678");
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.withBasicAuth("root","root")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
		
		assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
		
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
		
	}
	
	

	@Test
	@Order(4)
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() {

		usuarioService.CadastrarUsuario(new Usuario(0L, 
			"Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123", "https://i.imgur.com/5M2p5Wb.jpg"));
		
		usuarioService.CadastrarUsuario(new Usuario(0L, 
			"Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123", "https://i.imgur.com/Sk5SjWE.jpg"));

		
		ResponseEntity<String> resposta = testRestTemplate
			.withBasicAuth("root", "root")
			.exchange("/usuarios/all", HttpMethod.GET, null, String.class);

		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());

	}



	@Test
	@Order(5)
	@DisplayName("Listar Um Usuário Específico")
	public void deveListarApenasUmUsuario() {
	
		Optional<Usuario> usuarioBusca = usuarioService.CadastrarUsuario(new Usuario(0L, 
				"Laura Santolia", "laura_santolia@email.com.br", "laura12345", "https://i.imgur.com/EcJG8kB.jpg"));
			
	
	
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/" + usuarioBusca.get().getId(), HttpMethod.GET, null, String.class);
		
	
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
	}

	@Test
	@Order(6)
	@DisplayName("Login do Usuário")
	public void deveAutenticarUsuario() {

	
		usuarioService.CadastrarUsuario(new Usuario(0L, 
			"Marisa Souza", "marisa_souza@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));

	
		HttpEntity<UserLogin> corpoRequisicao = new HttpEntity<UserLogin>(new UserLogin());

	
		ResponseEntity<UserLogin> corpoResposta = testRestTemplate
			.exchange("/usuarios/logar", HttpMethod.POST, corpoRequisicao, UserLogin.class);

	
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
}
	
}
