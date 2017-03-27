package org.mustafa.restservice.messenger.resources;

import java.net.URI;
//import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.mustafa.restservice.messenger.model.Message;
import org.mustafa.restservice.messenger.service.MessageService;

@Path("/messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(value = {MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public class MessageResource {
	
	MessageService messageService = new MessageService();

	@GET
	public List<Message> getMessages(@QueryParam("year") int year, @QueryParam("start") int start, @QueryParam("size") int size){
		if(start>=0 && size > 0) {
			return messageService.getAllMessagesPaginated(start, size);
		}
		if(year > 0) {
			return messageService.getAllMessagesForYear(year);
		}
		return messageService.getAllMessages();
	}
	
	@GET
	@Path("/{messageId}")
	public Message getMessage(@PathParam("messageId") long messageId, @Context UriInfo uriInfo){
		Message message = messageService.getMessage(messageId); 
		message.addLink(getUriForSelf(uriInfo, message), "self");
		message.addLink(getUriForProfile(uriInfo, message), "profile");
		message.addLink(getUriForComments(uriInfo, message), "comments");
		return message;
		
	}

	private String getUriForComments(UriInfo uriInfo, Message message) {
		return uriInfo.getBaseUriBuilder()
				.path(MessageResource.class)
				.path(MessageResource.class, "getCommentResource")
				.path(CommentResource.class)
				.resolveTemplate("messageId", message.getId())
				.build()
				.toString();
	}

	private String getUriForSelf(UriInfo uriInfo, Message message) {
		return uriInfo.getBaseUriBuilder()
					.path(MessageResource.class)
					.path(Long.toString(message.getId()))
					.build()
					.toString();
	}
	
	private String getUriForProfile(UriInfo uriInfo, Message message) {
		return uriInfo.getBaseUriBuilder()
					.path(ProfileResource.class)
					.path(message.getAuthor())
					.build()
					.toString();
	}
	
	@POST
	public Response addMessage(Message message, @Context UriInfo uriInfo){ //throws URISyntaxException{
		Message newMessage = messageService.addMessage(message);
		String newId = String.valueOf(newMessage.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		/*return Response.created(new URI("/messenger/webapi/messages/"+newMessage.getId()))
					.entity(newMessage)
					.build();*/
		return Response.created(uri)
				.entity(newMessage)
				.build();
		//return messageService.addMessage(message);
	}
	
	@PUT
	@Path("/{messageId}")
	public Message updateMessage(@PathParam("messageId") long messageId, Message message){
		message.setId(messageId);
		return messageService.updateMessage(message);
	}
	
	@DELETE
	@Path("/{messageId}")
	public Message deleteMessage(@PathParam("messageId") long messageId){
		return messageService.removeMessage(messageId);
	}
	
	@Path("/{messageId}/comments")
	public CommentResource getCommentResource(){
		
		return new CommentResource();
	}
}
