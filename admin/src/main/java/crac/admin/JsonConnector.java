package crac.admin;

import java.nio.charset.Charset;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConnector<T>{

	protected HttpHeaders header;
	final Class<T> myClass;
	final Class<T[]> myClassArray;
	RestTemplate restTemplate;
	
	public JsonConnector(String name, String password, Class<T> myClass, Class<T[]> myClassArray){
		this.header = this.createHeaders(name, password);
		this.myClass = myClass;
		this.myClassArray = myClassArray;
		this.restTemplate = new RestTemplate();
	}

	public T[] index(String url) {		
		ResponseEntity<T[]> objects = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(header), myClassArray);
		return objects.getBody();
	}

	public T get(String url) {
		return null;
	}

	public void put(String url, T object) {

	}

	public void post(String url, T object) {
		ObjectMapper mapper = new ObjectMapper();
		HttpEntity<String> entity = null;
		try {
			entity = new HttpEntity<String>(mapper.writeValueAsString(object), header);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			System.out.println(mapper.writeValueAsString(object));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//this.restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
	}

	public void delete(String url, T object) {

	}
	
	protected HttpHeaders createHeaders(final String username, final String password ){
	    HttpHeaders headers =  new HttpHeaders(){
	          {
	             String auth = username + ":" + password;
	             byte[] encodedAuth = Base64.encodeBase64(
	                auth.getBytes(Charset.forName("US-ASCII")) );
	             String authHeader = "Basic " + new String( encodedAuth );
	             set( "Authorization", authHeader );
	          }
	       };
	       headers.add("Content-Type", "application/json");
	       headers.add("Accept", "application/json");

	       return headers;
	}



}
