package com.exam.serializer;

import com.exam.dto.RoleDTO;
import com.exam.dto.UserDTO;
import com.exam.util.AuthorityUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserDTOSerializer extends StdSerializer<UserDTO> {

    public UserDTOSerializer() {
        this(null);
    }

    public UserDTOSerializer(Class<UserDTO> t) {
        super(t);
    }

    @Override
    public void serialize(UserDTO user, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("userId", user.getUserId());
        gen.writeStringField("userName", user.getUserName());
        gen.writeStringField("name", user.getName());
        gen.writeStringField("email", user.getEmail());
        gen.writeStringField("gender", user.getGender().getValue());
        gen.writeStringField("phoneNumber", user.getPhoneNumber());
        gen.writeStringField("userImage", user.getUserImage());
        gen.writeFieldName("roles");
        gen.writeStartArray();
        for(RoleDTO roleDTO: user.getRoles()){
            gen.writeStartObject();
            gen.writeNumberField("roleId", roleDTO.getRoleId());
            gen.writeStringField("roleName",roleDTO.getRoleName().getValue());
            gen.writeStringField("description", roleDTO.getDescription());
            gen.writeEndObject();
        }
        gen.writeEndArray();
        if(AuthorityUtil.isAdminRole()) {
            gen.writeStringField("createdAt", user.getCreatedAt().toString());
            gen.writeStringField("updatedAt", user.getUpdatedAt().toString());
            gen.writeStringField("status", user.getStatus().getValue());
            gen.writeBooleanField("isUserVerified", user.getIsUserVerified());
            gen.writeBooleanField("isAccountExpired", user.getIsAccountExpired());
            gen.writeBooleanField("isCredentialsExpired", user.getIsCredentialsExpired());
            gen.writeBooleanField("isAccountLocked", user.getIsAccountLocked());
        }
        gen.writeEndObject();
    }
}
