package com.blogpessoal.BlogPessoal.service;

import java.nio.charset.Charset;
import org.apache.commons.codec.binary.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.blogpessoal.BlogPessoal.model.UserLogin;
import com.blogpessoal.BlogPessoal.model.Usuario;
import com.blogpessoal.BlogPessoal.repository.UsuarioRepository;


@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	public Optional<Usuario> CadastrarUsuario(Usuario usuario) {

		if (repository.findByUsuario(usuario.getUsuario()).isPresent())
			return Optional.empty();

		usuario.setSenha(criptografarSenha(usuario.getSenha()));

		return Optional.of(repository.save(usuario));

	}

	public Optional<Usuario> atualizarUsuario(Usuario usuario) {

		if (repository.findById(usuario.getId()).isPresent()) {

			Optional<Usuario> buscaUsuario = repository.findByUsuario(usuario.getUsuario());

			if ((buscaUsuario.isPresent()) && (buscaUsuario.get().getId() != usuario.getId()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);

			usuario.setSenha(criptografarSenha(usuario.getSenha()));

			return Optional.ofNullable(repository.save(usuario));

		}

		return Optional.empty();

	}

	public Optional<UserLogin> autenticarUsuario(Optional<UserLogin> usuarioLogin) {

		Optional<Usuario> usuario = repository.findByUsuario(usuarioLogin.get().getUsuario());

		if (usuario.isPresent()) {

			if (compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {

				usuarioLogin.get().setId(usuario.get().getId());                                     
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setToken(gerarBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha()));
				usuarioLogin.get().setSenha(usuario.get().getSenha());

				return usuarioLogin;

			}
		}

		return Optional.empty();

	}

	private boolean compararSenhas(String senhaDigitada, String senhaBanco) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.matches(senhaDigitada, senhaBanco);

	}

	private String criptografarSenha(String senha) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.encode(senha);

	}

	/**
	 * Método Gerar Basic Token
	 * 
	 * A primeira linha, monta uma String (token) seguindo o padrão Basic, através
	 * da concatenação de caracteres que será codificada (Não criptografada) no
	 * formato Base64, através da Dependência Apache Commons Codec.
	 * 
	 * Essa String tem o formato padrão: <username>:<password> que não pode ser
	 * alterado
	 *
	 * Na segunda linha, faremos a codificação em Base 64 da String.
	 * 
	 * Observe que o vetor tokenBase64 é do tipo Byte para receber o resultado da
	 * codificação, porquê durante o processo é necessário trabalhar diretamente com
	 * os bits (0 e 1) da String
	 * 
	 * Base64.encodeBase64 -> aplica o algoritmo de codificação do Código Decimal
	 * para Base64, que foi gerado no próximo método. Para mais detalhes, veja
	 * Codificação 64 bits na Documentação.
	 * 
	 * Charset.forName("US-ASCII") -> Retorna o codigo ASCII (formato Decimal) de
	 * cada caractere da String. Para mais detalhes, veja a Tabela ASCII na
	 * Documentação.
	 *
	 * Na terceira linha, acrescenta a palavra Basic acompanhada de um espaço em
	 * branco (Obrigatório), além de converter o vetor de Bytes novamente em String
	 * e concatenar tudo em uma única String.
	 * 
	 * O espaço depois da palavra Basic é obrigatório. Caso não seja inserrido, o
	 * Token não será reconhecido.
	 */
	private String gerarBasicToken(String usuario, String senha) {

		String token = usuario + ":" + senha;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(tokenBase64);

	}

}
