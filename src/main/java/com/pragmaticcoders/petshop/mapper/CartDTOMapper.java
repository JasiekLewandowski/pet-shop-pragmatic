package com.pragmaticcoders.petshop.mapper;

import com.pragmaticcoders.petshop.dto.CartDTO;
import com.pragmaticcoders.petshop.dto.CartItemDTO;
import com.pragmaticcoders.petshop.model.CartEntity;
import com.pragmaticcoders.petshop.model.CartItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", uses = CartItemDTOMapper.class)
public interface CartDTOMapper {

    @Mapping(source = "sessionId", target = "sessionId")
    @Mapping(source = "items", target = "items")
    CartDTO toCartDTO(CartEntity cartEntity);
}
