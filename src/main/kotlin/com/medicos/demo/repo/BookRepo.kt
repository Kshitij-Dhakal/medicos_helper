package com.medicos.demo.repo

import com.medicos.demo.entity.Book

interface BookRepo {
    fun saveBook(books: List<Book>): Boolean

    fun getFirestoreBooks(): List<Book>
}