package ru.practicum.shareit;

public interface DtoMapper<T, M> {
    T mapToDto(M model);

    M mapFromDto(T model, int id, Integer ownerId);
}
