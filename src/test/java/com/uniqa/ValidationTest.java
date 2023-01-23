package com.uniqa;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenInvalidInput_whenValidating_thenInvalid() throws IOException {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonSchema jsonSchema = factory.getSchema(
                ValidationTest.class.getResourceAsStream("/schema.json"));
        JsonNode jsonNode = mapper.readTree(
                ValidationTest.class.getResourceAsStream("/product_invalid.json"));
        Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
        assertThat(errors).isNotEmpty().asString().contains("price: must have a minimum value of 0");
    }

    @Test
    public void givenValidInput_whenValidating_thenValid() throws IOException {

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonSchema jsonSchema = factory.getSchema(ValidationTest.class.getResourceAsStream("/schema.json"));

        JsonNode jsonNode = mapper.readTree(ValidationTest.class.getResourceAsStream("/product_valid.json"));
        Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
        assertThat(errors).isEmpty();
    }
}