package edu.agile.service.responses;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import edu.agile.service.entities.IdeaVote;

public class IdeaVoteResponse extends StdSerializer<IdeaVote> {

	public IdeaVoteResponse() {
        this(null);
    }
   
    public IdeaVoteResponse(Class<IdeaVote> t) {
        super(t);
    }

	@Override
	public void serialize(IdeaVote value, JsonGenerator jgen, SerializerProvider provider) throws IOException {

		jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeNumberField("user_id", value.getUserId().getId());
        jgen.writeNumberField("idea_id", value.getIdeaId().getId());
        jgen.writeNumberField("vote", value.getVote());
        jgen.writeEndObject();
		
	}

}
