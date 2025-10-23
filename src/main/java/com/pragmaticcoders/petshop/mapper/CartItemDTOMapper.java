package com.pragmaticcoders.petshop.mapper;

import com.pragmaticcoders.petshop.dto.CartItemDTO;
import com.pragmaticcoders.petshop.model.CartItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemDTOMapper {

    @Mapping(source = "product.barcode", target = "barcode")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "priceStamp", target = "unitPrice")
    CartItemDTO toCartItemDTO(CartItemEntity cartItemEntity);

}
