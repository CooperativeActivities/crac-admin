package crac.admin.controllers;

import java.nio.charset.Charset;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import crac.admin.models.CracUser;

@Controller
@RequestMapping("/world")
public class ConsumeWorld {

	@RequestMapping("/consume")
	public void send() {
		RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders header = this.createHeaders("user", "pass");
	    String url = "https://core.crac.at/crac-core/user/1";
		//ResponseEntity<CracUser> testUser = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(header), CracUser.class);
	    CracUser testUser = restTemplate.getForObject(url, CracUser.class);
		System.out.println(testUser.getName());
	}
	
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


	
}
