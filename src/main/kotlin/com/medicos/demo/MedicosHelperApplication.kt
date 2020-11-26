package com.medicos.demo

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.FileInputStream
import java.io.IOException

@SpringBootApplication
class SpringBootApplication : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        initFirebase()

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