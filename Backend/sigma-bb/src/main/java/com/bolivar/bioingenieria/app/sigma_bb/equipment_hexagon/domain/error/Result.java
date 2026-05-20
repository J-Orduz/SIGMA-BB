package com.bolivar.bioingenieria.app.sigma_bb.equipment_hexagon.domain.error;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Aqui si ayudo la IA UnU
 * Representa el resultado de una operación que puede ser exitosa (Ok) o fallida (Error).
 * @param <T> Tipo del valor en caso de éxito
 * @param <E> Tipo del error en caso de fallo (suele ser un objeto de dominio como DomainError)
 */
public sealed interface Result<T, E> permits Result.Ok, Result.Err {

    // ---- Métodos básicos ----
    boolean isSuccess();
    boolean isFailure();
    T getValue() throws IllegalStateException;
    E getError() throws IllegalStateException;

    // ---- Constructores estáticos ----
    static <T, E> Result<T, E> ok(T value) {
        return new Ok<>(value);
    }

    static <T, E> Result<T, E> err(E error) {
        return new Err<>(error);
    }

    // ---- Métodos de transformación ----
    <U> Result<U, E> map(Function<? super T, ? extends U> mapper);
    <U> Result<U, E> flatMap(Function<? super T, Result<U, E>> mapper);
    Result<T, E> onSuccess(Consumer<? super T> action);
    Result<T, E> onFailure(Consumer<? super E> action);
    T orElse(T other);
    T orElseGet(Function<? super E, ? extends T> fallback);
    <X extends Throwable> T orElseThrow(Function<? super E, ? extends X> exceptionMapper) throws X;

    // ---- Conversión a Optional ----
    Optional<T> toOptional();

    // ---- Implementaciones concretas ----
    record Ok<T, E>(T value) implements Result<T, E> {
        @Override public boolean isSuccess() { return true; }
        @Override public boolean isFailure() { return false; }
        @Override public T getValue() { return value; }
        @Override public E getError() { throw new IllegalStateException("Cannot get error from Ok result"); }
        @Override public <U> Result<U, E> map(Function<? super T, ? extends U> mapper) {
            return Result.ok(mapper.apply(value));
        }
        @Override public <U> Result<U, E> flatMap(Function<? super T, Result<U, E>> mapper) {
            return mapper.apply(value);
        }
        @Override public Result<T, E> onSuccess(Consumer<? super T> action) {
            action.accept(value);
            return this;
        }
        @Override public Result<T, E> onFailure(Consumer<? super E> action) {
            return this;
        }
        @Override public T orElse(T other) {
            return value;
        }
        @Override public T orElseGet(Function<? super E, ? extends T> fallback) {
            return value;
        }
        @Override public <X extends Throwable> T orElseThrow(Function<? super E, ? extends X> exceptionMapper) throws X {
            return value;
        }
        @Override public Optional<T> toOptional() {
            return Optional.of(value);
        }
    }

    record Err<T, E>(E error) implements Result<T, E> {
        @Override public boolean isSuccess() { return false; }
        @Override public boolean isFailure() { return true; }
        @Override public T getValue() { throw new IllegalStateException("Cannot get value from Err result"); }
        @Override public E getError() { return error; }
        @Override public <U> Result<U, E> map(Function<? super T, ? extends U> mapper) {
            return Result.err(error);
        }
        @Override public <U> Result<U, E> flatMap(Function<? super T, Result<U, E>> mapper) {
            return Result.err(error);
        }
        @Override public Result<T, E> onSuccess(Consumer<? super T> action) {
            return this;
        }
        @Override public Result<T, E> onFailure(Consumer<? super E> action) {
            action.accept(error);
            return this;
        }
        @Override public T orElse(T other) {
            return other;
        }
        @Override public T orElseGet(Function<? super E, ? extends T> fallback) {
            return fallback.apply(error);
        }
        @Override public <X extends Throwable> T orElseThrow(Function<? super E, ? extends X> exceptionMapper) throws X {
            throw exceptionMapper.apply(error);
        }
        @Override public Optional<T> toOptional() {
            return Optional.empty();
        }
    }
}