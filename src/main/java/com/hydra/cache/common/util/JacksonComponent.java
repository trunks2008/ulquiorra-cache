package com.hydra.cache.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 * jackson
 * 序列化&反序列化
 *
 */
public class JacksonComponent {

    /**
     * 日期格式化
     */
    public static class DateSerializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {
            if (Objects.isNull(date)) {
                return;
            }
            jsonGenerator.writeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        }
    }

    /**
     * 解析日期字符串
     */
    public static class DateDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            if (StringUtils.isEmpty(jsonParser.getText())) {
                return null;
            }
            try {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonParser.getText());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }
    }

    /**
     * localDatetime日期格式化
     */
    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            if (Objects.isNull(localDateTime)) {
                return;
            }
            jsonGenerator.writeString(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }

    /**
     * localDateTime解析日期字符串
     */
    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            if (Objects.isNull(jsonParser) || StringUtils.isEmpty(jsonParser.getText())) {
                return null;
            }
            return LocalDateTime.parse(jsonParser.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    /**
     * localDate日期格式化
     */
    public static class LocalDateSerializer extends JsonSerializer<LocalDate> {
        @Override
        public void serialize(LocalDate localDateTime, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            if (Objects.isNull(localDateTime)) {
                return;
            }
            jsonGenerator.writeString(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
    }

    /**
     * localDate解析日期字符串
     */
    public static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            if (Objects.isNull(jsonParser) || StringUtils.isEmpty(jsonParser.getText())) {
                return null;
            }
            return LocalDate.parse(jsonParser.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }

    public static class LocalTimeSerializer extends JsonSerializer<LocalTime> {
        @Override
        public void serialize(LocalTime localDateTime, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            if(Objects.isNull(localDateTime)){
                return;
            }
            jsonGenerator.writeString(localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }
    }

    public static class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {
        @Override
        public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            if (Objects.isNull(jsonParser) || StringUtils.isEmpty(jsonParser.getText())) {
                return null;
            }
            return LocalTime.parse(jsonParser.getText(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
    }
}