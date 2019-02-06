package com.qapitol.qa.rest.automation.test;

import static org.testng.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.qapitol.qa.rest.automation.pojos.PostStudentDetails;
import com.qapitol.qa.rest.automation.pojos.UpdateStudentDetails;
import com.qapitol.qa.rest.automation.restapi.SpringBootInterface;

public class SpringBootTest {

	SpringBootInterface spring;
	Client client;

	@BeforeMethod
	public void config() {
		List<Object> providers = new ArrayList<Object>();
		providers.add(new JacksonJsonProvider());

		spring = JAXRSClientFactory.create("http://localhost:9090/", SpringBootInterface.class, providers);
		client = WebClient.client(spring);
	}

	@Test(priority = 1)
	public void getAllStudents() throws Exception {
		Response list = spring.getAllStudents();

		String listOfStudents = readInputStreamAsString((InputStream) list.getEntity());

		JSONParser parser = new JSONParser();
		JSONArray json = (JSONArray) parser.parse(listOfStudents);

		System.out.println(json.toString());
		assertEquals(5, json.size());

	}

	@Test(priority = 2)
	public void getStudentById() throws Exception {
		Response student = spring.getStudentById("10001");

		String StudentById = readInputStreamAsString((InputStream) student.getEntity());
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(StudentById);

		System.out.println(json.toString());
		assertEquals("Jim-Al-Khalili", json.get("name"));

	}

	@Test(priority = 4)
	public void postStudentDetails() {

		PostStudentDetails post = new PostStudentDetails();

		post.setId(10003);
		post.setName("George Gamow");

		Response postDetails = spring.postStudentDetails(post);

		assertEquals(201, postDetails.getStatus());

	}

	@Test(priority = 3)
	public void deleteStudentById() {
		Response deleteStudent = spring.deleteStudentById("10003");

		assertEquals(200, deleteStudent.getStatus());

	}

	@Test(priority = 5)
	public void updateStudentDetails() {

		UpdateStudentDetails update = new UpdateStudentDetails();
		update.setId(10002);
		update.setName("Roger Federer");

		Response updateDetails = spring.updateStudentDetails("10002", update);

		assertEquals(204, updateDetails.getStatus());

	}

	public static String readInputStreamAsString(InputStream in) {
		try {
			BufferedInputStream bis = new BufferedInputStream(in);
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = bis.read();
			while (result != -1) {
				byte b = (byte) result;
				buf.write(b);
				result = bis.read();
			}
			return buf.toString();
		} catch (IOException ex) {
			return null;
		}
	}

}
