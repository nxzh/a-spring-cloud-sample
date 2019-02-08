package fun.nxzh.guilin.basket.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class JsonHelper {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  static {
    JavaTimeModule javaTimeModule = new JavaTimeModule();
    // Hack time module to allow 'Z' at the end of string (i.e. javascript json's)
    javaTimeModule.addDeserializer(
        LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
    MAPPER.registerModule(javaTimeModule);
    MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  public static <T> String parse(T obj) {
    try {
      if (obj == null) {
        return null;
      }
      return MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static <T> String parseInPrettyMode(T obj) {
    try {
      return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static ObjectMapper getMapper() {
    return MAPPER;
  }

  public static class StringToMapSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
      String val = value;
      if (value == null || value.isEmpty()) {
        val = "{}";
      }
      gen.writeObject(JsonHelper.getMapper().readValue(val, Map.class));
    }
  }
}
