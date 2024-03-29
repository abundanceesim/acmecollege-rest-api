package acmecollege.rest.resource;
/**
 * File:  CLubMembershipResource.java Course materials (23W) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 * 
 * 	Updated by: Group 12
 *  Abundance Esim 	
 *  Harshang Shah  
 *  Mei Zhi Li   
 *  Sergio  Machado  
 * 
 */

import static acmecollege.utility.MyConstants.CLUB_MEMBERSHIP_RESOURCE_NAME;
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
import acmecollege.entity.ClubMembership;

@Path(CLUB_MEMBERSHIP_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClubMembershipResource {
	private static final Logger LOG = LogManager.getLogger();

	@EJB
	protected ACMECollegeService service;

	@Inject
	protected SecurityContext sc;

	@GET
	public Response getMemberships() {
		LOG.debug("Retrieving all Memberships ...");
		List<ClubMembership> clubMembership = service.getAllRecords();
		Response response = Response.ok(clubMembership).build();
		return response;
	}

	@GET
	@RolesAllowed({ADMIN_ROLE, USER_ROLE})
	@Path(RESOURCE_PATH_ID_PATH)
	public Response getMembershipById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		LOG.debug("Retrieving specific clubMembership with id = {}", id);
		ClubMembership clubMembership = service.getClubMembershipById(id);
		if (clubMembership == null)
			return Response.status(Status.NOT_FOUND).build();
		return Response.ok(clubMembership).build();
	}

	@POST
	@RolesAllowed({ADMIN_ROLE})
	public Response addclubMembership(ClubMembership newClubMembership) {
		LOG.debug("Adding a new Club Membership = {}", newClubMembership);
		Response response = null;
		service.persistClubMembership(newClubMembership);
		response = Response.ok(newClubMembership).build();
		return response;
	}

	@DELETE
	@RolesAllowed({ADMIN_ROLE})
	@Path(RESOURCE_PATH_ID_PATH)
	public Response deleteClubMembership(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		LOG.debug("Deleting a Club Membership with id = {}", id);
		Response response = null;
		service.deleteClubMembershipById(id);
		response = Response.ok().build();
		return response;
	}
}