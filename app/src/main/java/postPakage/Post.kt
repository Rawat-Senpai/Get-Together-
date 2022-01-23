package postPakage

import details_person.Person

data class Post(
    val text:String="",
    val imageUrl:String="",
    val createdBy: Person =Person(),
    val createdAt:Long=0L,
    val likedBy:ArrayList<String> = ArrayList()
)
