package com.ntdev.library.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.ntdev.library.dto.response.ApiResponse;
import com.ntdev.library.dto.response.BookResponse;
import com.ntdev.library.dto.response.UserResponse;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ApiResponseSerializer extends StdSerializer<ApiResponse<?>> {

    public ApiResponseSerializer() {
        this(null);
    }

    public ApiResponseSerializer(Class<ApiResponse<?>> t) {
        super(t);
    }

    @Override
    public void serialize(ApiResponse<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
        gen.writeStringField("timestamp", formatter.format(value.getTimestamp()));
        gen.writeNumberField("status", value.getStatus());
        gen.writeBooleanField("success", value.isSuccess());

        if (value.isSuccess()) {
            gen.writeStringField("message", value.getMessage());
        } else {
            gen.writeStringField("error", value.getMessage());
        }

        // Nếu có lỗi, ghi lại thông tin lỗi
        if (value.getDetails() != null) {
            if (value.getDetails() instanceof Map<?, ?>) {
                gen.writeObjectField("details", value.getDetails()); // Serialize Map as JSON
            } else {
                gen.writeStringField("details", value.getDetails().toString()); // For non-map details
            }
        }

        // Nếu có đường dẫn, ghi lại đường dẫn
        if (value.getPath() != null) {
            gen.writeStringField("path", value.getPath());
        }

        // Xác định key cho data
        String key = "data";
        Object data = value.getData();

        if (data != null) {
            if (data instanceof List<?>) {
                List<?> list = (List<?>) data;
                if (!list.isEmpty()) {
                    Object firstItem = list.get(0);
                    if (firstItem instanceof UserResponse) {
                        key = "users";
                    } else if (firstItem instanceof BookResponse) {
                        key = "books";
                    }
                }
            } else if (data instanceof UserResponse) {
                key = "user";
            } else if (data instanceof BookResponse) {
                key = "book";
            }
        }
        if (data != null) {
            gen.writeObjectField(key, data);
        }

        gen.writeEndObject();
    }

}
