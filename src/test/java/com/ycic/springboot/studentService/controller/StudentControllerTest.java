package com.ycic.springboot.studentService.controller;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.nio.charset.Charset;

import java.util.Arrays;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.ycic.springboot.studentService.StudentServiceApplication;
import com.ycic.springboot.studentService.model.Course;
import com.ycic.springboot.studentService.service.StudentService;

@RunWith(SpringRunner.class)
@WebMvcTest(value=StudentController.class)
@WithMockUser
public class StudentControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private StudentService studentService;
	
	Course mockCourse = new Course("Course1", "Spring", "10Steps", 
			Arrays.asList("Learn Maven", "Import Project", "First Example","Second Example"));
	
	String exampleCourseJson = "{\"name\":\"Spring\",\"description\":\"10Steps\",\"steps\":[\"Learn Maven\",\"Import Project\",\"First Example\",\"Second Example\"]}";
	
	@Test
	public void testretrieveDetailsForCourse() throws Exception {
		Mockito.when(
			studentService.retrieveCourse(Mockito.anyString(), Mockito.anyString()))
			.thenReturn(mockCourse);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/students/Student1/courses/Course1")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		System.out.println(result.getResponse());
		String expected = "{id:Course1, name:Spring, description:10Steps}";
		
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void createStudentCourse() throws Exception {
		Course mockCourse = new Course("1", "Smallest Number", "1", Arrays
				.asList("1", "2", "3", "4"));
		
		// studentService.addCourse to response back with mockCourse
		Mockito.when(
			studentService.addCourse(Mockito.anyString(), Mockito.any(Course.class)))
			.thenReturn(mockCourse);
		
		// send course as body to /students/Student1/courses
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/students/Student1/courses/Course1")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		System.out.println(response);
		
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		assertEquals("http://localhost/students/Student1/courses/1", 
			response.getHeader(HttpHeaders.LOCATION));
	}
}
