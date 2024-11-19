package org.tolking.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class DTOConverter<E, D> {
    protected final ModelMapper modelMapper;

    public DTOConverter() {
        this.modelMapper = new ModelMapper();
    }

    public E convertToEntity(D dto) {
        return modelMapper.map(dto, getTypeEntity());
    }

    public D convertToDto(E entity) {
        return modelMapper.map(entity, getTypeDTO());
    }

    public List<E> convertToEntityList(List<D> dtoList) {
        return dtoList.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
    }

    public Set<E> convertToEntitySet(Set<D> dtoSet) {
        return dtoSet.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toSet());
    }

    public List<D> convertToDtoList(List<E> entityList) {
        return entityList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    protected abstract Class<E> getTypeEntity();

    protected abstract Class<D> getTypeDTO();
}
