package com.safetyalert.jsonfilter;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

public class FirePersonFilter extends SimpleBeanPropertyFilter {
	
	private static final String[] properties = new String[] {"lastName","phone","age","fireStation","medicalRecord"};
	private static List<String> includeProperties = Arrays.asList(properties);
	
	@Override
	public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer)
			throws Exception {
		if (include(writer)) {
			if (includeProperties.contains(writer.getName())) {
				writer.serializeAsField(pojo, jgen, provider);
				return;
			}
//			int intValue = ((MyDtoWithFilter) pojo).getIntValue();
//			if (intValue >= 0) {
//				writer.serializeAsField(pojo, jgen, provider);
//			}
		} else if (!jgen.canOmitFields()) { // since 2.3
			writer.serializeAsOmittedField(pojo, jgen, provider);
		}
	}

	@Override
	protected boolean include(BeanPropertyWriter writer) {
		return true;
	}

	@Override
	protected boolean include(PropertyWriter writer) {
		return true;
	}
}