package cz.muni.ics.cms.data.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.ics.cms.data.model.ConsentAttribute;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.AttributeConverter;

public class ConsentAttributeSetConverter implements AttributeConverter<Set<ConsentAttribute>, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set<ConsentAttribute> set) {
        if (set == null) {
            return null;
        }
        try {
            set.remove(null);
        } catch (NullPointerException e) {
            //OK
        }
        List<ConsentAttribute> list = new ArrayList<>(set);
        list.sort((o1, o2) -> {
            // we are safe here as null elements have been previously removed from the set
            // we can now directly sort the list by name of the attributes
            // comparing values does not make sense as we will not get two attributes with the same name
            if (o1.getName() == null) {
                return o2.getName() == null ? 0 : 1;
            } else if (o2.getName() == null) {
                return -1;
            }

            return o1.getName().compareTo(o2.getName());
        });
        try {
            return MAPPER.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            //TODO: handle
            return null;
        }
    }

    @Override
    public Set<ConsentAttribute> convertToEntityAttribute(String dbData) {
        try {
            return MAPPER.readValue(dbData, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            //TODO: handle
            return new HashSet<>();
        }
    }

}
