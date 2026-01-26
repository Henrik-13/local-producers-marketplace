package edu.bbte.localproducersmarketplace.mapper;

import edu.bbte.localproducersmarketplace.dto.out.UserOutDto;
import edu.bbte.localproducersmarketplace.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserOutDto userToUserOutDto(User user);
}
