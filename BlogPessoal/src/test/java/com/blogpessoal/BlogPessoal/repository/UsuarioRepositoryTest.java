package com.blogpessoal.BlogPessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.blogpessoal.BlogPessoal.model.Usuario;

//indica que a clase é do tipo springboot teste, o parametro passado é para que se a porta padrão estja
//ocupada vai procurar outra porta

@SuppressWarnings("unused")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository repository;
	
	@BeforeAll
	void start() {
		repository.deleteAll();
		repository.save(new Usuario(0L,"João Paz","https://th.bing.com/th/id/OIP.Tzs3s3tVrtDBHGTMkVzwtwHaHa?w=174&h=180&c=7&r=0&o=5&pid=1.7","João@gmail.com","12345678"));
		repository.save(new Usuario(0L,"João Marques","https://th.bing.com/th/id/OIP.Tzs3s3tVrtDBHGTMkVzwtwHaHa?w=174&h=180&c=7&r=0&o=5&pid=1.7","João2@gmail.com","12345678"));
		repository.save(new Usuario(0L,"João Neres","https://th.bing.com/th/id/OIP.Tzs3s3tVrtDBHGTMkVzwtwHaHa?w=174&h=180&c=7&r=0&o=5&pid=1.7","João3@gmail.com","12345678"));
		repository.save(new Usuario(0L,"João Lima","https://th.bing.com/th/id/OIP.Tzs3s3tVrtDBHGTMkVzwtwHaHa?w=174&h=180&c=7&r=0&o=5&pid=1.7","João4@gmail.com","12345678"));
	}
	
	@Test
	@DisplayName("retorna 1 usuário")
	public void deveRetornaUmUsuaio(){
		Optional<Usuario> usuario = repository.findByUsuario("joao@gmil.com");
		assertTrue(usuario.get().getUsuario().equals("joao@gmail.com"));
		
	}
	
	@Test
	@DisplayName("retorna 3 usuário")
	public void deveRetornaTresUsuaio(){
		List<Usuario> listaDeUsuarios = repository.findAllByNomeContainingIgnoreCase("paz");
		assertEquals(3,listaDeUsuarios.size());
		assertTrue(listaDeUsuarios.get(0).getNome().equals("João Paz"));
		assertTrue(listaDeUsuarios.get(0).getNome().equals("João marques"));
		assertTrue(listaDeUsuarios.get(0).getNome().equals("João neres"));
		
	}
	

}


