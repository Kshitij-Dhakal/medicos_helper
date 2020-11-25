package com.medicos.demo.repo

import com.medicos.demo.entity.Youtube

interface YoutubeRepo {
    fun saveYoutube(youtubeList: List<Youtube>): Boolean

    fun getFirebaseYoutubeList(): List<Youtube>
}