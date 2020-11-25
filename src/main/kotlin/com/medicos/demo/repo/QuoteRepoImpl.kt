package com.medicos.demo.repo

import com.google.cloud.firestore.DocumentReference
import com.google.firebase.cloud.FirestoreClient
import com.medicos.demo.entity.Quote
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement
import java.sql.SQLException

@Repository
class QuoteRepoImpl : QuoteRepo {
    @Autowired
    private val jdbcTemplate: JdbcTemplate? = null

    override fun saveQuotes(quoteList: List<Quote>): Boolean {
        val batchUpdate = jdbcTemplate!!.batchUpdate("INSERT INTO quote (quote, author, created, deleted) VALUES(?, ?, ?, ?)", object : BatchPreparedStatementSetter {
            @Throws(SQLException::class)
            override fun setValues(ps: PreparedStatement, i: Int) {
                val quote = quoteList[i]
                ps.setString(1, quote.quote)
                ps.setString(2, quote.author)
                ps.setLong(3, 1606322799)
                ps.setBoolean(4, false)
            }

            override fun getBatchSize(): Int {
                return quoteList.size
            }
        })
        return quoteList.size == batchUpdate.size
    }

    override fun getFirebaseQuotes(): List<Quote> {
        val firestore = FirestoreClient.getFirestore()
        return firestore.collection("quotes")
                .listDocuments()
                .map { documentReference: DocumentReference ->
                    val future = documentReference.get()
                    val doc = future.get()
                    val author = doc.getString("author")
                    val quote = doc.getString("quote")
                    val quoteObj = Quote()
                    quoteObj.quote = quote
                    quoteObj.author = author
                    return@map quoteObj
                }.toList()
    }
}