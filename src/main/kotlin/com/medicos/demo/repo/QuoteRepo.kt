package com.medicos.demo.repo

import com.medicos.demo.entity.Quote

interface QuoteRepo {
    fun saveQuotes(quoteList: List<Quote>): Boolean

    fun getFirebaseQuotes(): List<Quote>
}