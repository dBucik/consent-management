package cz.muni.ics.cms.data.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class MapToJsonConverter implements AttributeConverter<Map<String, String>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, String> map) {
        return mapper.valueToTree(map).toString();
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String s) {
        try {
            return mapper.readValue(s, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
