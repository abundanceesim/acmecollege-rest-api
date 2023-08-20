/**
 * File:  TestProfessor.java
 * Course materials (23W) CST 8277
 * 
 *  Created by: Group 12
 *  Abundance Esim 	
 *  Harshang Shah  
 *  Mei Zhi Li   
 *  Sergio  Machado  
 */
package acmecollege;

import static acmecollege.utility.MyConstants.APPLICATION_API_VERSION;
import static acmecollege.utility.MyConstants.APPLICATION_CONTEXT_ROOT;
import static acmecollege.utility.MyConstants.COURSE_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static acmecollege.utility.MyConstants.DEFAULT_USER;
import static acmecollege.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static acmecollege.utility.MyConstants.PROFESSOR_SUBRESOURCE_NAME;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_PATH;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import acmecollege.entity.Professor;
import acmecollege.entity.Student;

@SuppressWarnings("unused")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestProfessor {
	 private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
	    private static final Logger logger = LogManager.getLogger(_thisClaz);

	    static final String HTTP_SCHEMA = "http";
	    static final String HOST = "localhost";
	    static final int PORT = 8080;

	    // Test fixture(s)
	    static URI uri;
	    static HttpAuthenticationFeature adminAuth;
	    static HttpAuthenticationFeature userAuth;
	    private final int ID = 1;

	    @BeforeAll
	    public static void oneTimeSetUp() throws Exception {
	        logger.debug("oneTimeSetUp");
	        uri = UriBuilder
	            .fromUri(APPLICATION_CONTEXT_ROOT + APPLICATION_API_VERSION)
	            .scheme(HTTP_SCHEMA)
	            .host(HOST)
	            .port(PORT)
	            .build();
	        adminAuth = HttpAuthenticationFeature.basic(DEFAULT_ADMIN_USER, DEFAULT_ADMIN_USER_PASSWORD);
	        userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER, DEFAULT_USER_PASSWORD);
	    }

	    protected WebTarget webTarget;
	    @BeforeEach
	    public void setUp() {
	        Client client = ClientBuilder.newClient(
	            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
	        webTarget = client.target(uri);
	    }

	    @Test
	    public void test01_all_professors_with_adminrole() throws JsonMappingException, JsonProcessingException {
	        Response response = webTarget
	            //.register(userAuth)
	            .register(adminAuth)
	            .path(PROFESSOR_SUBRESOURCE_NAME)
	            .request()
	            .get();
	        assertThat(response.getStatus(), is(200));
	        List<Professor> professors = response.readEntity(new GenericType<List<Professor>>(){});
	        assertThat(professors, is(not(empty())));
	    }
	    
	    @Test
	    public void test02_professors_by_id_adminrole() throws JsonMappingException, JsonProcessingException {
	        Response response = webTarget
	            //.register(userAuth)
	            .register(adminAuth)
	            .path(PROFESSOR_SUBRESOURCE_NAME + "/" + ID)
	            .request()
	            .get();
	        assertThat(response.getStatus(), is(200));
	        Professor professor = response.readEntity(new GenericType<Professor>(){});
	        assertThat(professor, notNullValue());
	        assertThat(professor.getId(), equalTo(ID));
	    }
	    
	    @Test
	    public void test03_add_professors_with_adminrole() throws JsonMappingException, JsonProcessingException {
	    	Professor professor = new Professor();
	    	professor.setFirstName("Rejaul");
	    	professor.setLastName("C");
	    	professor.setDepartment("Information and Communications Technology");
	    	professor.setHighestEducationalDegree(null);
	    	professor.setSpecialization(null);
	        Response response = webTarget
	                //.register(userAuth)
	                .register(adminAuth)
	                .path(PROFESSOR_SUBRESOURCE_NAME)
	                .request()
	                .post(Entity.json(professor));
	        assertThat(response.getStatus(), is (200));
	    }
	    
	    @Test
	    public void test04_add_professors_with_userrole() throws JsonMappingException, JsonProcessingException {
	    	Professor professor = new Professor();
	    	professor.setFirstName("Dr. Rama");
	    	professor.setLastName("T");
	    	professor.setDepartment("Information and Communications Technology");
	    	professor.setHighestEducationalDegree(null);
	    	professor.setSpecialization(null);
	        Response response = webTarget
	                .register(userAuth)
	                .path(PROFESSOR_SUBRESOURCE_NAME)
	                .request()
	                .post(Entity.json(professor));
	        assertThat(response.getStatus(), is (403));
	    }
	    
	    @Test
	    public void test05_delete_professors_with_userrole() throws JsonMappingException, JsonProcessingException {
	      	Response response = webTarget
					.register(userAuth)
		            .path(PROFESSOR_SUBRESOURCE_NAME + "/" + ID)
					.request()
					.delete();
			assertThat(response.getStatus(), is(403));
	    }
	    
	    @Test
	    public void test06_professors_by_id_userrole() throws JsonMappingException, JsonProcessingException {
	        Response response = webTarget
	            //.register(userAuth)
	            .register(userAuth)
	            .path(PROFESSOR_SUBRESOURCE_NAME + "/" + ID)
	            .request()
	            .get();
	        assertThat(response.getStatus(), is(200));
	        Professor professor = response.readEntity(new GenericType<Professor>(){});
	        assertThat(professor, notNullValue());
	        assertThat(professor.getId(), equalTo(ID));
	    }
	    
	    @Test
	    public void test07_all_professors_with_userrole() throws JsonMappingException, JsonProcessingException {
	        Response response = webTarget
	            //.register(userAuth)
	            .register(userAuth)
	            .path(PROFESSOR_SUBRESOURCE_NAME)
	            .request()
	            .get();
	        assertThat(response.getStatus(), is(403));
	    }
	    
	    @Test
	    public void test08_delete_professors_with_adminrole() throws JsonMappingException, JsonProcessingException {
	        //create new professor
	    	Professor professor = new Professor();
	        String firstName= "Jane";
	        String lastName= "Doe";
	        String department= "General Arts and Science";
	        professor.setFirstName(firstName);
	        professor.setLastName(lastName);
	        professor.setDepartment(department);
	    	Response response = webTarget
	            //.register(userAuth)
	            .register(adminAuth)
	            .path(PROFESSOR_SUBRESOURCE_NAME)
	            .request()
	            .post(Entity.json(professor));
	        assertThat(response.getStatus(), is(200));
	        //delete the new professor
	        Professor professor2 = response.readEntity(Professor.class);
	        assertThat(professor2.getFirstName(), is(firstName));
	        assertThat(professor2.getLastName(), is(lastName));
	        assertThat(professor2.getDepartment(), is(department));
			response = webTarget
					.register(adminAuth)
		            .path(PROFESSOR_SUBRESOURCE_NAME)
					.path(Integer.toString(professor2.getId()))
					.request()
					.delete();
			assertThat(response.getStatus(), is(200));
	    }
	    
	}