package xyz.v.gaarb.objects

import androidx.annotation.Keep


@Keep
data class Orders(var address:String ="",var dateTime:String="",var id:String="",var landmark:String="",var name:String="",var phone:String="", var weight:String="",var status:String = "",var amount:String="",var type:String="")

