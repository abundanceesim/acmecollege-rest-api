/**
 * File:  ProfessorResource.java
 * Course materials (23W) CST 8277
 *
 * Created by: Group 12
 *  Abundance Esim 	
 *  Harshang Shah  
 *  Mei Zhi Li   
 *  Sergio  Machado  
 *
 */

package acmecollege.rest.resource;

import static acmecollege.utility.MyConstants.PROFESSOR_SUBRESOURCE_NAME;
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
import acmecollege.ejb.ProfessorService;
import acmecollege.entity.Professor;

@Path(PROFESSOR_SUBRESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProfessorResource {

	private static final Logger LOG = LogManager.getLogger();

	@EJB
	protected ProfessorService profService;

	@Inject
	protected SecurityContext secContx;

	@GET
	@RolesAllowed({ADMIN_ROLE})
	public Response getProfessors() {
		LOG.debug("Retrieving all professors ...");
		List<Professor> professors = profService.getAllProfessors();
		Response response = Response.ok(professors).build();
		return response;
	}

	@GET
	@RolesAllowed({ADMIN_ROLE, USER_ROLE})
	@Path(RESOURCE_PATH_ID_PATH)
	public Response getProfessorById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		LOG.debug("Retrieving specific professor with id = {}", id);
		Professor prof = profService.getProfessorId(id);
		if (prof == null)
			return Response.status(Status.NOT_FOUND).build();
		return Response.ok(prof).build();
	}

	@POST
	@RolesAllowed({ADMIN_ROLE})
	public Response addProfessor(Professor newProfessor) {
		LOG.debug("Adding a new professor = {}", newProfessor);
		Response response = null;
		profService.persistProfessor(newProfessor);
		response = Response.ok(newProfessor).build();
		return response;
	}

	@DELETE
	@RolesAllowed({ADMIN_ROLE})
	@Path(RESOURCE_PATH_ID_PATH)
	public Response deleteProfessor(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		LOG.debug("Deleting a professor with id = {}", id);
		Response response = null;
		profService.deleteProfessorById(id);
		response = Response.ok().build();
		return response;
	}
}
