package xyz.v.gaarb.objects

import kotlinx.serialization.Serializable

@Serializable
data class Orders(var Address:String ="",var dateTime:String="",var id:String="",var landmark:String="",var name:String="",var phone:String="", var weight:String="",var status:String = "",var amount:String="")

