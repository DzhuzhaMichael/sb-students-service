package com.example.sbstudentsservice.service.mapper;

public interface ResponseDtoMapper<D, T> {

    D mapToDto(T t);

}
