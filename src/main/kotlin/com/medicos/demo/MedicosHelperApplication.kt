package com.medicos.demo

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.medicos.demo.entity.Youtube
import com.medicos.demo.repo.YoutubeRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.FileInputStream
import java.io.IOException

@SpringBootApplication
class SpringBootApplication : ApplicationRunner {
    @Autowired
    private val youtubeRepo: YoutubeRepo? = null

    override fun run(args: ApplicationArguments?) {
        initFirebase()
        val youtubeList: List<Youtube>? = youtubeRepo?.getFirebaseYoutubeList()
        youtubeRepo?.saveYoutube(youtubeList!!)
    }
}

fun main(args: Array<String>) {
    runApplication<com.medicos.demo.SpringBootApplication>(*args)
}


@Throws(IOException::class)
fun initFirebase() {
    val serviceAccount = FileInputStream("/home/kshitijd/Documents/Code/Medicos/secret/vast-fuze-89905-firebase-adminsdk-q4wxx-a921daebd5.json")
    val options: FirebaseOptions = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://vast-fuze-89905.firebaseio.com")
            .build()
    FirebaseApp.initializeApp(options)
}