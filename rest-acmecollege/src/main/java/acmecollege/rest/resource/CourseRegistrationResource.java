package acmecollege.rest.resource;
/**
 * File:  CourseRegistrationResource.java
 *
 *  Created by: Group 12
 *  Abundance Esim 	
 *  Harshang Shah  
 *  Mei Zhi Li   
 *  Sergio  Machado    
 * 
 */

import static acmecollege.utility.MyConstants.COURSE_REGISTRATION_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_PATH;
import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.USER_ROLE;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
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
import acmecollege.ejb.ACMECollegeService;
import acmecollege.ejb.CourseRegistrationService;
import acmecollege.entity.CourseRegistration;

@Path(COURSE_REGISTRATION_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseRegistrationResource {
	private static final Logger LOG = LogManager.getLogger();

	@EJB
	protected ACMECollegeService service;
	
	@EJB
	protected CourseRegistrationService registrationService;

	@Inject
	protected SecurityContext sc;

	@GET
	@RolesAllowed({ADMIN_ROLE})
	public Response getRegistrations() {
		LOG.debug("Retrieving all Memberships ...");
		List<CourseRegistration> CourseRegistration = registrationService.getAllCourseRegistrations();
		Response response = Response.ok(CourseRegistration).build();
		return response;
	}

	@GET
	@RolesAllowed({ADMIN_ROLE, USER_ROLE})
	@Path(RESOURCE_PATH_ID_PATH)
	public Response getMembershipById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		LOG.debug("Retrieving specific CourseRegistration with id = {}", id);
		CourseRegistration CourseRegistration = registrationService.getCourseRegistrationById(id);
		if (CourseRegistration == null)
			return Response.status(Status.NOT_FOUND).build();
		return Response.ok(CourseRegistration).build();
	}

	@POST
	@RolesAllowed({ADMIN_ROLE})
	public Response addCourseRegistration(CourseRegistration newCourseRegistration) {
		LOG.debug("Adding a new Course Registration = {}", newCourseRegistration);
		Response response = null;
		registrationService.persistCourseRegistration(newCourseRegistration);
		response = Response.ok(newCourseRegistration).build();
		return response;
	}

	@DELETE
	@RolesAllowed({ADMIN_ROLE})
	@Path(RESOURCE_PATH_ID_PATH)
	public Response deleteCourseRegistration(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		LOG.debug("Deleting a Course Registration with id = {}", id);
		Response response = null;
		registrationService.deleteCourseRegistrationById(id);
		response = Response.ok().build();
		return response;
	}
}