package edu.agile.service.responses;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import edu.agile.service.entities.Category;
import edu.agile.service.entities.Idea;
import edu.agile.service.entities.IdeaCategory;
import edu.agile.service.entities.IdeaVote;

public class IdeaInfoResponse extends StdSerializer<Idea> {

	private static final long serialVersionUID = 1L;

	public IdeaInfoResponse() {
		this(null);
	}

	public IdeaInfoResponse(Class<Idea> t) {
		super(t);
	}

	@Override
	public void serialize(Idea value, JsonGenerator jgen, SerializerProvider provider) throws IOException {

		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId());
		jgen.writeStringField("title", value.getTitle());
		jgen.writeStringField("description", value.getDescription());
		try {
			jgen.writeNumberField("sale_mode", value.getSaleMode());
			jgen.writeNumberField("price", value.getPrice());
		} catch (Exception e) {
			jgen.writeNumberField("sale_mode", null);
			jgen.writeNumberField("price", null);
		}

		Collection<IdeaCategory> categories = value.getIdeaCategoryCollection();

		jgen.writeFieldName("idCategories");
		jgen.writeStartArray(); // [
		for (IdeaCategory ic : categories) {
			jgen.writeNumber(ic.getCategoryId().getId());
		}

		Collection<IdeaVote> votes = value.getIdeaVoteCollection();

		jgen.writeEndArray(); // ]
		jgen.writeFieldName("votes");
		jgen.writeStartArray(); // [
		int countPositive = 0;
		int countNegative = 0;

		if (votes != null) {
			for (IdeaVote vote : votes) {
				if (vote.getVote() == 1) {
					countPositive++;
				} else if (vote.getVote() == -1) {
					countNegative++;
				}
			}
		}
		jgen.writeNumber(countPositive);
		jgen.writeNumber(countNegative);
		jgen.writeEndArray(); // ]
		jgen.writeEndObject();

	}
}
