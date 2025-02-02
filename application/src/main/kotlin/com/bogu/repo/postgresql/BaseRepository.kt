package com.bogu.repo.postgresql

import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.query.FluentQuery
import java.io.Serializable
import java.util.*
import java.util.function.Function

@NoRepositoryBean
interface BaseRepository<T, ID : Serializable> : JpaRepository<T, ID> {
    @Deprecated("Do not use")
    override fun findAll(): List<T> {
        throw UnsupportedOperationException("findAll() is not supported")
    }

    @Deprecated("Do not use")
    override fun <S : T> findAll(example: Example<S>, pageable: Pageable): Page<S> {
        throw UnsupportedOperationException("findAll() is not supported")
    }

    @Deprecated("Do not use")
    override fun <S : T> findAll(example: Example<S>, sort: Sort): MutableList<S> {
        throw UnsupportedOperationException("findAll() is not supported")
    }

    @Deprecated("Do not use")
    override fun <S : T> findAll(example: Example<S>): MutableList<S> {
        throw UnsupportedOperationException("findAll() is not supported")
    }

    @Deprecated("Do not use")
    override fun findAll(pageable: Pageable): Page<T> {
        throw UnsupportedOperationException("findAll() is not supported")
    }

    @Deprecated("Do not use")
    override fun findAll(sort: Sort): MutableList<T> {
        throw UnsupportedOperationException("findAll() is not supported")
    }

    @Deprecated("Do not use")
    override fun <S : T, R : Any?> findBy(
        example: Example<S>,
        queryFunction: Function<FluentQuery.FetchableFluentQuery<S>, R>
    ): R & Any {
        throw UnsupportedOperationException("findAll() is not supported")
    }

    @Deprecated("Do not use")
    override fun findById(id: ID): Optional<T> {
        throw UnsupportedOperationException("findById() is not supported")
    }

    @Deprecated("Do not use")
    override fun findAllById(ids: MutableIterable<ID>): MutableList<T> {
        throw UnsupportedOperationException("findAllById() is not supported")
    }

    @Deprecated("Do not use")
    override fun <S : T> findOne(example: Example<S>): Optional<S> {
        throw UnsupportedOperationException("findOne() is not supported")
    }

    @Deprecated("Deprecated in Java")
    override fun getById(id: ID): T & Any {
        throw UnsupportedOperationException("getById() is not supported")
    }

    @Deprecated("Deprecated in Java")
    override fun getOne(id: ID): T & Any {
        throw UnsupportedOperationException("getOne() is not supported")
    }
}