package github.com.LimaDevCod3r.mappers;

import github.com.LimaDevCod3r.dtos.user.UserCreateDTO;
import github.com.LimaDevCod3r.dtos.user.UserResponseDto;
import github.com.LimaDevCod3r.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserCreateDTO userCreateDTO);

    UserResponseDto toResponse(User user);
}
