package org.tolking.dto.converter;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class DTOConverter<E, D> {
    protected final ModelMapper modelMapper;

    public E toEntity(D dto) {
        return modelMapper.map(dto, getTypeEntity());
    }

    public D toDto(E entity) {
        return modelMapper.map(entity, getTypeDTO());
    }

    public List<E> toEntityList(List<D> dtoList) {
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<D> toDtoList(List<E> entityList) {
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    protected abstract Class<E> getTypeEntity();

    protected abstract Class<D> getTypeDTO();
}