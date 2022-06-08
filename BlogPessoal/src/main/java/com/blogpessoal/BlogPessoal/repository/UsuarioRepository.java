package com.blogpessoal.BlogPessoal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogpessoal.BlogPessoal.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	//Optional é pq ele pode retornar nulo
	public Optional<Usuario> findByUsuario(String usuario);
	

}
