package br.pi.gov.seduc.apiSpringMysql.security;


import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {
	
	private static final long serialVersionUID = 234234523523L;

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	@Value("${jwt.secret}")
	private String secretKey;

	private String chave;

	public String getaHashSenha(String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
		byte messageDigest[] = algorithm.digest(senha.getBytes("UTF-8"));

		StringBuilder hexString = new StringBuilder();
		for (byte b : messageDigest) {
			hexString.append(String.format("%02X", 0xFF & b));
		}
		String senhahex = hexString.toString();
		return senhahex;
	}

	// retrieve username from jwt token
	public String getUsernameFromToken(String token) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return getClaimFromToken(token, Claims::getSubject);
	}

	// retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// for retrieving any information from token we will need the secret key
	@SuppressWarnings("deprecation")
	private Claims getAllClaimsFromToken(String token) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		chave = getaHashSenha(secretKey);
		return Jwts.parser().setSigningKey(chave).parseClaimsJws(token).getBody();
	}

	// check if the token has expired
	private Boolean isTokenExpired(String token) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// generate token for user
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}
	
	// while creating the token -
	// 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
	// 2. Sign the JWT using the HS512 algorithm and secret key.
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		try {
			chave = getaHashSenha(secretKey);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS256, chave).compact();
	}

	// validate token
	public Boolean validateToken(String token, UserDetails userDetails) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}

