package com.sktbd.driboard.data.model

//TODO
interface Repository {
    fun add(item: Shot?)
    fun update(item: Shot?)
    fun remove(item: Shot?)
//    fun query(specification: Specification?): List<*>?
}