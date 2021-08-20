package com.android.aroundme.data.model
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GoogleMapDirection(
    @SerializedName("routes")
    var arrayListRouts: MutableList<Routs>
)  


data class Routs(
    @SerializedName("legs")
    var arrayListLegs: MutableList<Legs>

) 

data class Legs(
    @SerializedName("steps")
    var arrayListSteps: MutableList<Steps>,

    @SerializedName("distance")
    var distance: Distance,

    @SerializedName("duration")
    var duration: Duration

)

data class Duration(
    @SerializedName("text")
    var text: String = "",

    @SerializedName("value")
    var value: String = ""

)

data class Distance(
    @SerializedName("text")
    var text: String = "",

    @SerializedName("value")
    var value: String = ""

) 

data class Steps(
    @SerializedName("polyline")
    var polyline: PolyLine = PolyLine()

) 

data class PolyLine(
    @SerializedName("points")
    var points: String? = ""

) 