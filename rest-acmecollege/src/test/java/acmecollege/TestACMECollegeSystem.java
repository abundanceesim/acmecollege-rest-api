/**
 * File:  TestACMECollegeSystem.java
 * Course materials (23W) CST 8277
 * Teddy Yap
 * (Original Author) Mike Norman
 *
 *  Updated by: Group 12
 *  Abundance Esim 	
 *  Harshang Shah  
 *  Mei Zhi Li   
 *  Sergio  Machado   
 *  
 */
package acmecollege;

import static acmecollege.utility.MyConstants.APPLICATION_API_VERSION;
import static acmecollege.utility.MyConstants.APPLICATION_CONTEXT_ROOT;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER;
import static acmecollege.utility.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static acmecollege.utility.MyConstants.DEFAULT_USER;
import static acmecollege.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static acmecollege.utility.MyConstants.STUDENT_RESOURCE_NAME;
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

import acmecollege.entity.Student;

@SuppressWarnings("unused")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestACMECollegeSystem {
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
    
    public int getSize(HttpAuthenticationFeature auth) throws JsonMappingException, JsonProcessingException{
    	Response response = webTarget
    			.register(auth)
    			.path(STUDENT_RESOURCE_NAME)
    			.request()
    			.get();
    	
    	List<Student> students = response.readEntity(new GenericType<List<Student>>(){});
    	return students.size();
    }

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
    public void test01_all_students_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(STUDENT_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<Student> students = response.readEntity(new GenericType<List<Student>>(){});
        assertThat(students, is(not(empty())));    }
    
    @Test
    public void test02_students_by_id_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(STUDENT_RESOURCE_NAME + "/" + ID)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        Student student = response.readEntity(new GenericType<Student>(){});
        assertThat(student, notNullValue());
        assertThat(student.getId(), equalTo(ID));
    }
    
    @Test
    public void test03_add_students_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	Student student = new Student();
    	int before = getSize(adminAuth);
    	student.setFirstName("Testing");
    	student.setLastName("name");
        Response response = webTarget
                .register(adminAuth)
                .path(STUDENT_RESOURCE_NAME)
                .request()
                .post(Entity.json(student));
        
        int after = getSize(adminAuth);
        assertThat(after, equalTo(before + 1));
    }
    
    @Test
    public void test04_add_students_with_userrole() throws JsonMappingException, JsonProcessingException {
    	Student student = new Student();
    	student.setFirstName("John");
    	student.setLastName("Smith");
        Response response = webTarget
                .register(userAuth)
                .path(STUDENT_RESOURCE_NAME)
                .request()
                .post(Entity.json(student));
        assertThat(response.getStatus(), is (403));
    }
    
    @Test
    public void test05_delete_students_with_adminrole() throws JsonMappingException, JsonProcessingException {
        //create new student
    	Student student = new Student();
        String firstName= "John";
        String lastName= "Doey";
        student.setFirstName(firstName);
        student.setLastName(lastName);
    	Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(STUDENT_RESOURCE_NAME)
            .request()
            .post(Entity.json(student));
        assertThat(response.getStatus(), is(200));
        //delete the new student
        Student student2 = response.readEntity(Student.class);
        assertThat(student2.getFirstName(), is(firstName));
        assertThat(student2.getLastName(), is(lastName));
		response = webTarget
				.register(adminAuth)
	            .path(STUDENT_RESOURCE_NAME)
				.path(Integer.toString(student2.getId()))
				.request()
				.delete();
		assertThat(response.getStatus(), is(200));
    }
    
    @Test
    public void test06_students_by_id_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(userAuth)
            .path(STUDENT_RESOURCE_NAME + "/" + ID)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        Student student = response.readEntity(new GenericType<Student>(){});
        assertThat(student, notNullValue());
        assertThat(student.getId(), equalTo(ID));
    }
    
    @Test
    public void test07_all_students_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(userAuth)
            .path(STUDENT_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(403));
    }
    
    @Test
    public void test08_delete_students_with_userrole() throws JsonMappingException, JsonProcessingException {
		Response response = webTarget
				.register(userAuth)
	            .path(STUDENT_RESOURCE_NAME + "/" + ID)
				.request()
				.delete();
		assertThat(response.getStatus(), is(403));
    }
    
}