package crac.admin.daos;

import java.awt.List;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class CracUserDAO {
/*
	public Collection<CracUser> findUser(int id){
		RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders header = this.createHeaders("frontend", "frontendKey");
	    String url = "http://localhost:8080/user/"+id;
		ResponseEntity<CracUser> testUser = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(header), CracUser.class);
	    //CracUser testUser = restTemplate.getForObject(url, CracUser.class);
		
		ArrayList<CracUser> users = new ArrayList<CracUser>();
		users.add(testUser.getBody());
		return users;
	}
	
	public CracUser findSingleUser(Long id){
		RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders header = this.createHeaders("frontend", "frontendKey");
	    String url = "http://localhost:8080/user/"+id;
		ResponseEntity<CracUser> testUser = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(header), CracUser.class);
	    //CracUser testUser = restTemplate.getForObject(url, CracUser.class);
		return testUser.getBody();
	}
	*/
	private HttpHeaders createHeaders(final String username, final String password ){
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
	/*
	public void save(CracUser user){
		
	}
	
	public void delete(CracUser user){
		
	}
	*/
	
}
