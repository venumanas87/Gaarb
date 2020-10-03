package xyz.v.gaarb.settings

import com.google.firebase.database.FirebaseDatabase

class FirebaseSetting: android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}