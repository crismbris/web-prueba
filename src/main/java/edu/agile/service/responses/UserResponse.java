package edu.agile.service.responses;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import edu.agile.service.entities.User;

public class UserResponse extends StdSerializer<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserResponse() {
		this(null);
	}

	public UserResponse(Class<User> t) {
		super(t);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void serialize(User value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		if(value != null) {
			jgen.writeStartObject();
	        jgen.writeNumberField("id", value.getId());
	        jgen.writeStringField("email", value.getEmail());
	        jgen.writeStringField("username", value.getUsername());
	        jgen.writeEndObject();
		}else {
			jgen.writeStartObject();
	        jgen.writeStringField("message", "Username and/or password invalid.");
	        jgen.writeEndObject();
		}
	}

}
