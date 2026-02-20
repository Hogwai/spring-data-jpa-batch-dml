package com.hogwai.springdatajpabatchdml.repository;

import java.util.List;

/**
 * Custom repository interface that replaces the default Spring Data JPA {@code save} methods
 * with explicit JPA operations: {@code persist}, {@code merge}, and {@code update}.
 *
 * <p>The standard {@code save} method in Spring Data calls {@code merge} for both new and
 * existing entities, which can trigger unnecessary SELECT queries. This interface forces
 * callers to choose the correct operation explicitly.</p>
 *
 * @param <T> the entity type
 * @see HibernateRepositoryImpl
 */
public interface HibernateRepository<T> {

    //Save methods will trigger an UnsupportedOperationException
    /**
     *
     * @deprecated
     */
    @Deprecated(since = "0.0.1")
    <S extends T> S save(S entity);
    /**
     *
     * @deprecated
     */
    @Deprecated(since = "0.0.1")
    <S extends T> List<S> saveAll(Iterable<S> entities);

    /**
     *
     * @deprecated
     */
    @Deprecated(since = "0.0.1")
    <S extends T> S saveAndFlush(S entity);

    /**
     *
     * @deprecated
     */
    @Deprecated(since = "0.0.1")
    <S extends T> List<S> saveAllAndFlush(Iterable<S> entities);

    //Persist methods are meant to save newly created entities

    <S extends T> S persist(S entity);

    <S extends T> S persistAndFlush(S entity);

    <S extends T> List<S> persistAll(Iterable<S> entities);

    <S extends T> List<S> persistAllAndFlush(Iterable<S> entities);

    //Merge methods are meant to propagate detached entity state changes
    //if they are really needed

    <S extends T> S merge(S entity);

    <S extends T> S mergeAndFlush(S entity);

    <S extends T> List<S> mergeAll(Iterable<S> entities);

    <S extends T> List<S> mergeAllAndFlush(Iterable<S> entities);

    //Update methods are meant to force the detached entity state changes

    <S extends T> S update(S entity);

    <S extends T> S updateAndFlush(S entity);

    <S extends T> List<S> updateAll(Iterable<S> entities);

    <S extends T> List<S> updateAllAndFlush(Iterable<S> entities);

}