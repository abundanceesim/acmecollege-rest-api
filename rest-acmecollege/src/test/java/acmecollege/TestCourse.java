/**
 * File:  TestCourse.java
 * Course materials (23W) CST 8277
 * 
 *  Created by: Group 12
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
import static acmecollege.utility.MyConstants.COURSE_RESOURCE_NAME;
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

import acmecollege.entity.Course;
import acmecollege.entity.Student;

@SuppressWarnings("unused")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestCourse {
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
    public void test01_all_courses_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(COURSE_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<Course> courses = response.readEntity(new GenericType<List<Course>>(){});
        assertThat(courses, is(not(empty())));
        
    }
    
    @Test
    public void test02_courses_by_id_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(COURSE_RESOURCE_NAME + "/" + ID)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        Course course = response.readEntity(new GenericType<Course>(){});
        assertThat(course, notNullValue());
        assertThat(course.getId(), equalTo(ID));
    }
    
    @Test
    public void test03_add_courses_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	Course course = new Course();
        String title= "Introduction to Philosophy";
        String semester= "Fall";
        String courseCode= "PHL2000";
        Integer year = 2022;
        Integer credit_unit=2;
        Byte online = 1;
        course.setCourseTitle(title);
        course.setSemester(semester);
        course.setCourseCode(courseCode);
        course.setYear(year);
        course.setCreditUnits(credit_unit);
        course.setOnline(online);
        Response response = webTarget
                //.register(userAuth)
                .register(adminAuth)
                .path(COURSE_RESOURCE_NAME)
                .request()
                .post(Entity.json(course));
        assertThat(response.getStatus(), is (200));
    }
    
    @Test
    public void test04_add_courses_with_userrole() throws JsonMappingException, JsonProcessingException {
    	Course course = new Course();
        String title= "Introduction to Psychology";
        String semester= "SPRING";
        String courseCode= "PSY1001";
        Integer year = 2023;
        Integer credit_unit=1;
        Byte online = 0;
        course.setCourseTitle(title);
        course.setSemester(semester);
        course.setCourseCode(courseCode);
        course.setYear(year);
        course.setCreditUnits(credit_unit);
        course.setOnline(online);
        Response response = webTarget
                .register(userAuth)
                .path(COURSE_RESOURCE_NAME)
                .request()
                .post(Entity.json(course));
        assertThat(response.getStatus(), is (403));
    }
    
    @Test
    public void test05_delete_courses_with_userrole() throws JsonMappingException, JsonProcessingException {
    	Response response = webTarget
				.register(userAuth)
	            .path(COURSE_RESOURCE_NAME + "/" + ID)
				.request()
				.delete();
		assertThat(response.getStatus(), is(403));
    }
    
    @Test
    public void test06_courses_by_id_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(userAuth)
            .path(COURSE_RESOURCE_NAME + "/" + ID)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        Course course = response.readEntity(new GenericType<Course>(){});
        assertThat(course, notNullValue());
        assertThat(course.getId(), equalTo(ID));
    }
    
    @Test
    public void test07_all_courses_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(userAuth)
            .path(COURSE_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(403));
    }
    
    @Test
    public void test08_delete_courses_with_adminrole() throws JsonMappingException, JsonProcessingException {
    	Course course = new Course();
        String title= "Mathematics for Third year";
        String semester= "AUTUMN";
        String courseCode= "MAT3339";
        Integer year = 2021;
        Integer credit_unit=3;
        Byte online = 1;
        course.setCourseTitle(title);
        course.setSemester(semester);
        course.setCourseCode(courseCode);
        course.setYear(year);
        course.setCreditUnits(credit_unit);
        course.setOnline(online);
    	Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(COURSE_RESOURCE_NAME)
            .request()
            .post(Entity.json(course));
        assertThat(response.getStatus(), is(200));
        
        Course course2 = response.readEntity(Course.class);
        assertThat(course2.getCourseTitle(), is(title));
        assertThat(course2.getCourseCode(), is(courseCode));
        assertThat(course2.getYear(), is(year));
        assertThat(course2.getSemester(), is(semester));
        assertThat(course2.getCreditUnits(), is(credit_unit));
        assertThat(course2.getOnline(), is(online));
		response = webTarget
				.register(adminAuth)
	            .path(COURSE_RESOURCE_NAME)
				.path(Integer.toString(course2.getId()))
				.request()
				.delete();
		assertThat(response.getStatus(), is(200));
		//retrieve the new course
		response = webTarget
				.register(adminAuth)
	            .path(COURSE_RESOURCE_NAME)
				.path(Integer.toString(course2.getId()))
				.request()
				.get();
		assertThat(response.getStatus(), is(404));
    }
    
}