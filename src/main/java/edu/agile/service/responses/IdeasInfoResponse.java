package edu.agile.service.responses;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import edu.agile.service.entities.Idea;
import edu.agile.service.entities.IdeaCategory;

public class IdeasInfoResponse extends StdSerializer<List> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IdeasInfoResponse() {
		this(null);
	}

	public IdeasInfoResponse(Class<List> t) {
		super(t);
	}

	@Override
	public void serialize(List listValue, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		// TODO Auto-generated method stub

		jgen.writeStartArray();
		for (Idea value : (List<Idea>) listValue) {

			jgen.writeStartObject();
			jgen.writeNumberField("id", value.getId());
			jgen.writeStringField("title", value.getTitle());
			jgen.writeStringField("description", value.getDescription());
			if (value.getSaleMode() != null) {
				jgen.writeNumberField("sale_mode", value.getSaleMode());
			} else {
				jgen.writeNullField("sale_mode");
			}
			jgen.writeNumberField("price", value.getPrice());

			Collection<IdeaCategory> categories = value.getIdeaCategoryCollection();

			jgen.writeFieldName("idCategories");
			jgen.writeStartArray(); // [
			for (IdeaCategory ic : categories) {
				if(ic.getCategoryId()!=null) {
					jgen.writeNumber(ic.getCategoryId().getId());
				}
			}
			jgen.writeEndArray(); // ]
			jgen.writeEndObject();
		}
		jgen.writeEndArray();
	}

}
