package com.medicos.demo.repo

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import com.medicos.demo.entity.Book
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.*

@Repository
class BookRepoImpl : BookRepo {
    @Autowired
    private val jdbcTemplate: JdbcTemplate? = null

    override fun saveBook(books: List<Book>): Boolean {
        val batchUpdate = jdbcTemplate!!.batchUpdate("INSERT INTO book (title, writer, subject, rating, link, image, edition, description, amazon_link, created, is_deleted) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", object : BatchPreparedStatementSetter {
            @Throws(SQLException::class)
            override fun setValues(ps: PreparedStatement, i: Int) {
                val book = books[i]
                ps.setString(1, book.title)
                ps.setString(2, book.writer)
                ps.setString(3, book.subject)
                ps.setDouble(4, defaultDouble(book.rating))
                ps.setString(5, book.link)
                ps.setString(6, book.image)
                ps.setString(7, book.edition)
                ps.setString(8, book.description)
                ps.setString(9, book.amazonLink)
                ps.setLong(10, 1606322799)
                ps.setBoolean(11, false)
            }

            override fun getBatchSize(): Int {
                return books.size
            }
        })
        return books.size == batchUpdate.size
    }

    fun defaultDouble(double: Double?): Double {
        return double ?: 0.0
    }

    override fun getFirestoreBooks(): List<Book> {
        val firestore: Firestore? = FirestoreClient.getFirestore()
        return firestore!!.collection("books")
                .listDocuments()
                .map { documentReference: DocumentReference ->
                    val future = documentReference.get()
                    val doc = future.get()
                    val title = doc.getString("title")
                    val writer = doc.getString("writer")
                    val subject = doc.getString("subject")
                    val rating = doc.getDouble("rating")
                    val link = doc.getString("link")
                    val image = doc.getString("image")
                    val edition = doc.getString("edition")
                    val description = doc.getString("description")
                    val amazonLink = doc.getString("amazonLink")
                    val book = Book()
                    book.title = title
                    book.writer = writer
                    book.subject = subject
                    book.rating = rating
                    book.link = link
                    book.image = image
                    book.edition = edition
                    book.description = description
                    book.amazonLink = amazonLink
                    return@map book
                }.toList()
    }
}