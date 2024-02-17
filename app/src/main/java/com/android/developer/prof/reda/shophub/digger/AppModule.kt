package com.android.developer.prof.reda.shophub.digger

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.android.developer.prof.reda.shophub.util.Constants.INTRODUCTION_SP
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestoreDB() = Firebase.firestore

    @Provides
    fun provideIntroductionSP(application: Application) =
        application.getSharedPreferences(INTRODUCTION_SP, MODE_PRIVATE)
}