package com.medicos.demo.repo

import com.google.cloud.firestore.DocumentReference
import com.google.firebase.cloud.FirestoreClient
import com.medicos.demo.entity.Youtube
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement
import java.sql.SQLException

@Repository
class YoutubeRepoImpl : YoutubeRepo {
    @Autowired
    private val jdbcTemplate: JdbcTemplate? = null

    override fun saveYoutube(youtubeList: List<Youtube>): Boolean {
        val batchUpdate = jdbcTemplate!!.batchUpdate("INSERT INTO youtube (channel, avatar, url, created, deleted) VALUES(?, ?, ?, ?, ?)", object : BatchPreparedStatementSetter {
            @Throws(SQLException::class)
            override fun setValues(ps: PreparedStatement, i: Int) {
                val youtube = youtubeList[i]
                ps.setString(1, youtube.channel)
                ps.setString(2, youtube.avatar)
                ps.setString(3, youtube.url)
                ps.setLong(4, 1606322799)
                ps.setBoolean(5, false)
            }

            override fun getBatchSize(): Int {
                return youtubeList.size
            }
        })
        return youtubeList.size == batchUpdate.size
    }

    override fun getFirebaseYoutubeList(): List<Youtube> {
        val firestore = FirestoreClient.getFirestore()
        return firestore.collection("youtube_channels")
                .listDocuments()
                .map { documentReference: DocumentReference ->
                    val future = documentReference.get()
                    val doc = future.get()
                    val avatar = doc.getString("avatarLink")
                    val channel = doc.getString("channelName")
                    val url = doc.getString("channelUrl")
                    val youtube = Youtube()
                    youtube.avatar = avatar
                    youtube.channel = channel
                    youtube.url = url
                    return@map youtube
                }.toList()
    }
}