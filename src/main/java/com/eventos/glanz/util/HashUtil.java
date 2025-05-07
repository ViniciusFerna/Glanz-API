package com.eventos.glanz.util;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;

public class HashUtil {
	public static String hash(String palavra) {
		String salt = "projetinhosenaifellas";
		
		palavra = salt + palavra;
		
		String hash = Hashing.sha256().hashString(palavra, StandardCharsets.UTF_8).toString();
		
		return hash;
		
	}

}
