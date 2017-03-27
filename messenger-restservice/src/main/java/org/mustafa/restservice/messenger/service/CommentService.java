package org.mustafa.restservice.messenger.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.mustafa.restservice.messenger.database.DatabaseClass;
import org.mustafa.restservice.messenger.exception.DataNotFoundException;
import org.mustafa.restservice.messenger.model.Comment;
import org.mustafa.restservice.messenger.model.ErrorMessage;
import org.mustafa.restservice.messenger.model.Message;

public class CommentService {
	
	private Map<Long, Message> messages = DatabaseClass.getMessages();
	//private Map<Long, Comment> comments1 = DatabaseClass.getComments(); 
	
	public CommentService(){
		//comments1.put(1L, new Comment(1, "Hello World", "Mustafa", "http://localhost:8080/messenger/webapi/messages/1"));
		//comments1.put(2L, new Comment(2, "Hello World Again", "Mustafa", "http://localhost:8080/messenger/webapi/messages/2"));
	}
	
	public List<Comment> getAllComments(long messageId) {
		Map<Long, Comment> comments = messages.get(messageId).getComments();
		return new ArrayList<Comment>(comments.values());
	}
	
	public Comment getComment(long messageId, long commentId) {
		Message message = messages.get(messageId);
		/*ErrorMessage errorMessage = new ErrorMessage("Not Found", 404, "http://google.com" );
		Response response = Response.status(Status.NOT_FOUND)
				.entity(errorMessage)
				.build();*/
		if(message == null){
			throw new DataNotFoundException("Message with message id "+messageId+" not found");
			//throw new WebApplicationException(response);
			//throw new NotFoundException(response);
		}
		Map<Long, Comment> comments = messages.get(messageId).getComments();
		Comment comment = comments.get(commentId);
		if(comment == null){
			throw new DataNotFoundException("Comment for message id "+messageId+" with comment id "+commentId+" not found");
			//throw new WebApplicationException(response);
			//throw new NotFoundException(response);
		}
		return comments.get(commentId);
	}
	
	public Comment addComment(long messageId, Comment comment) {
		Map<Long, Comment> comments = messages.get(messageId).getComments();
		comment.setId(comments.size() + 1);
		comment.setUri("http://localhost:8080/messenger/webapi/messages/"+messageId+"/comments/"+comment.getId());
		comment.setCreated(new Date());
		comments.put(comment.getId(), comment);
		return comment;
	}
	
	public Comment updateComment(long messageId, Comment comment) {
		Map<Long, Comment> comments = messages.get(messageId).getComments();
		if (comment.getId() <= 0) {
			return null;
		}
		comment.setCreated(new Date());
		comments.put(comment.getId(), comment);
		return comment;
	}
	
	public Comment removeComment(long messageId, long commentId) {
		Map<Long, Comment> comments = messages.get(messageId).getComments();
		return comments.remove(commentId);
	}
}
