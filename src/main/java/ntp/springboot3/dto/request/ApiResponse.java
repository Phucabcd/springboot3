package ntp.springboot3.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) //json bo qua cac du lieu null
public class ApiResponse <T> {
     @Builder.Default
     int code = 1000;

     String message;
     T result;
}
