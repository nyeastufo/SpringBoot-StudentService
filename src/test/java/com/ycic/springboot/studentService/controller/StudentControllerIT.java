package com.ycic.springboot.studentService.controller;

import java.net.URI;
import java.nio.charset.Charset;

import java.util.Arrays;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.context.junit4.SpringRunner;

import com.ycic.springboot.studentService.StudentServiceApplication;
import com.ycic.springboot.studentService.model.Course;

@SuppressWarnings("deprecation")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StudentServiceApplication.class, 
		webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerIT {
	@LocalServerPort
	private int port;
	
	TestRestTemplate restTemplate = new TestRestTemplate();
	
	HttpHeaders headers = new HttpHeaders();
	
	@Before
	public void before() {
		headers.add("Authorization",  createHttpAuthenticationHeaderValue(
				"user1", "secret1"));
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	}
	
	private String createHttpAuthenticationHeaderValue(String userId, String password) {
		String auth = userId + ":" + password;
		
		byte[] encodedAuth = Base64.encode(auth.getBytes(Charset.forName("US-ASCII")));
		return null;
	}

	@Test
	public void testRetrieveStudentCourse() throws JSONException {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/students/Student1/courses/Course1"), 
				HttpMethod.GET, entity, String.class);
		
		String expected = "{id:Course1, name:Spring, description:10Steps}";
		
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	};
	
	@Test
	public void addCourse() {
		Course course =  new Course("Course1", "Spring", "10Steps", 
				Arrays.asList("Learn Maven", "Import Project", "First Example", "Second Example"));
		
		HttpEntity<Course> entity = new HttpEntity<Course>(course, headers);
		
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/students/Student1/courses"), 
				HttpMethod.POST,
				entity, String.class);
	}

}
