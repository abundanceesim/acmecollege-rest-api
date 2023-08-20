/**
 * File:  MembershipCardResource.java
 *
 * Created by:  Group 12
 *  Abundance Esim 	
 *  Harshang Shah  
 *  Mei Zhi Li   
 *  Sergio  Machado   
 * 
 */
package acmecollege.rest.resource;

import static acmecollege.utility.MyConstants.MEMBERSHIP_CARD_RESOURCE_NAME;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static acmecollege.utility.MyConstants.RESOURCE_PATH_ID_PATH;
import static acmecollege.utility.MyConstants.ADMIN_ROLE;
import static acmecollege.utility.MyConstants.USER_ROLE;
import static acmecollege.utility.MyConstants.castObject;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.soteria.WrappingCallerPrincipal;

import acmecollege.entity.SecurityUser;
//import acmecollege.entity.User;

import acmecollege.ejb.MembershipCardService;
import acmecollege.entity.MembershipCard;

@Path(MEMBERSHIP_CARD_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MembershipCardResource {
	private static final Logger LOG = LogManager.getLogger();

	private static final String DETAILED_FORBIDDEN_MSG = """
	        {
	          "detailedMsg " :  "User trying to access resource it does not own, given id does not match Principal id"
	        }
	    """;
	
	@EJB
	protected MembershipCardService service;

	@Inject
	protected SecurityContext sc;
	
    @Context
    protected ServletContext servletContext;

	@GET
	@RolesAllowed({ADMIN_ROLE})
	public Response getAllCards() {
		LOG.debug("Retrieving all membership cards ...");
		List<MembershipCard> cards = service.getAllCards();
		Response response = Response.ok(cards).build();
		return response;
	}
	
	@GET
	@RolesAllowed({ADMIN_ROLE, USER_ROLE})
	@Path(RESOURCE_PATH_ID_PATH)
	public Response getMembershipCardId(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		Response response = Response.status(FORBIDDEN).entity(DETAILED_FORBIDDEN_MSG).build();
        WrappingCallerPrincipal wCallerPrincipal = castObject( WrappingCallerPrincipal.class, sc.getCallerPrincipal());
        if (wCallerPrincipal != null) {
            SecurityUser sUser = castObject(SecurityUser.class, wCallerPrincipal.getWrapped());
            if (sUser.getId() == id) {
            	MembershipCard mc = service.getMembershipCardId(id);
                response = Response.ok(mc).build();
                servletContext.log("Retrieved membership card for user = " + sUser.toString());
            }
        }
        return response;
	}

	@POST
	@RolesAllowed({ADMIN_ROLE})
	public Response persistMembershipCard(MembershipCard newCard) {
		LOG.debug("Adding a new membership card = {}", newCard);
		Response response = null;
		service.persistMembershipCard(newCard);
		response = Response.ok(newCard).build();
		return response;
	}

	@DELETE
	@RolesAllowed({ADMIN_ROLE})
	@Path(RESOURCE_PATH_ID_PATH)
	public Response deleteMembershipCardById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		LOG.debug("Deleting a membership card with id = {}", id);
		Response response = null;
		service.deleteMembershipCardById(id);
		response = Response.ok().build();
		return response;
	}
}
