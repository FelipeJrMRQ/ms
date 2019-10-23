package br.com.ms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.primefaces.json.JSONObject;

/**
 * Retorno a chave de validação criptografada para o sistema
 * @author admin
 *
 */
public class KeyValidator {

	public static String getKey() {
		try {
			URL url = new URL("https://myappkey.herokuapp.com");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(1500);
			connection.connect();
			InputStream content = connection.getInputStream();

			BufferedReader buff = new BufferedReader(new InputStreamReader(content, "UTF-8"));
			StringBuilder response = new StringBuilder();
			String texto;
			while ((texto = buff.readLine()) != null) {
				response.append(texto);
			}
			JSONObject json = new JSONObject(response.toString());
			return json.getString("key");
		} catch (IOException e) {
			return "Não foi possivel encontrar a chave";
		}
	}
}
