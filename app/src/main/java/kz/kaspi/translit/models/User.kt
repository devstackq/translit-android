package kz.kaspi.translit.models

data class User (
val name: String,
val email: String,
val photo: String,
val countWord: String,
val timeInApp : String
)

//class UserFirebase{
//    fun converT(hashMap: HashMap<String, String>): User =
//    User(hashMap["name"] ?: "", hashMap["email"] ?: "", hashMap["photo"] ?: "", hashMap["countWord"] ?: "", hashMap["timeInApp"] ?: "")
//}

//1 auth -> save state Logged, (save all fragments state save) 2 -> get data from FB DB, put -> dataClass -> show UI
//2 when event, call trans func, user logout, update values