/**
 * File:  CourseResource.java 
 *
 * Created by: Group 12
 * 	Abundance Esim 	
 *  Harshang Shah  
 *  Mei Zhi Li   
 *  Sergio  Machado  
 * 
 */

package acmecollege.rest.resource;

import static acmecollege.utility.MyConstants.COURSE_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_PATH;
import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.USER_ROLE;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.security.enterprise.SecurityContext;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import acmecollege.ejb.CourseService;
import acmecollege.entity.Course;

@Path(COURSE_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource {
	private static final Logger LOG = LogManager.getLogger();

	@EJB
	protected CourseService service;

	@Inject
	protected SecurityContext sc;

	@GET
	@RolesAllowed({ADMIN_ROLE})
	public Response getCourses() {
		LOG.debug("Retrieving all courses ...");
		List<Course> courses = service.getAllCourses();
		Response response = Response.ok(courses).build();
		return response;
	}

	@GET
	@RolesAllowed({ADMIN_ROLE, USER_ROLE})
	@Path(RESOURCE_PATH_ID_PATH)
	public Response getCourseById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		LOG.debug("Retrieving specific course with id = {}", id);
		Course course = service.getCourseId(id);
		if (course == null)
			return Response.status(Status.NOT_FOUND).build();
		return Response.ok(course).build();
	}

	@POST
	@RolesAllowed({ADMIN_ROLE})
	public Response addCourse(Course course) {
		LOG.debug("Adding a new course = {}", course);
		Response response = null;
		service.persistCourse(course);
		response = Response.ok(course).build();
		return response;
	}

	@DELETE
	@RolesAllowed({ADMIN_ROLE})
	@Path(RESOURCE_PATH_ID_PATH)
	public Response deleteCourse(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		LOG.debug("Deleting a course with id = {}", id);
		Response response = null;
		service.deleteCourseById(id);
		response = Response.ok().build();
		return response;
	}
}