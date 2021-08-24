package com.android.aroundme.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.android.aroundme.BuildConfig
import com.android.aroundme.CoreApp
import com.android.aroundme.utils.ktx.getLat
import com.android.aroundme.utils.ktx.getLong
import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

@Keep
data class PlaceData(
    @SerializedName("results")
    var placeList: ArrayList<Places>,
    @SerializedName("status")
    var status: String
)

data class Places(
    @SerializedName("business_status")
    var business_status: String,

    @SerializedName("geometry")
    var geometry: Geometry,

    @SerializedName("icon")
    var icon: String,

    @SerializedName("photos")
    var photos: ArrayList<Photos>?,

    @SerializedName("icon_background_color")
    var icon_background_color: String,

    @SerializedName("icon_mask_base_uri")
    var icon_mask_base_uri: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("place_id")
    var place_id: String,

    @SerializedName("opening_hours")
    var openingHours: OpeningHours?,

    @SerializedName("rating")
    var rating: String?,

    @SerializedName("vicinity")
    var vicinity: String

): Parcelable {
    val ratings: Float
        get() = if(rating == null) 0.0f else rating!!.toFloat()

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        TODO("geometry"),
        parcel.readString()!!,
        TODO("photos"),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        TODO("openingHours"),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(business_status)
        parcel.writeString(icon)
        parcel.writeString(icon_background_color)
        parcel.writeString(icon_mask_base_uri)
        parcel.writeString(name)
        parcel.writeString(place_id)
        parcel.writeString(rating)
        parcel.writeString(vicinity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Places> {
        override fun createFromParcel(parcel: Parcel): Places {
            return Places(parcel)
        }

        override fun newArray(size: Int): Array<Places?> {
            return arrayOfNulls(size)
        }
    }
}

data class Geometry(

    @SerializedName("location")
    var location: Locations

    /*  @SerializedName("viewport")
      var viewport : Viewport,*/

)

data class Locations(

    @SerializedName("lat")
    var lat: Double,

    @SerializedName("lng")
    var lng: Double,
) {
    val getDistance: String
        get() = distance(
            CoreApp.mInstance?.getLat()!!.toDouble(),
            CoreApp.mInstance?.getLong()!!.toDouble(),
            lat,
            lng
        ).toString() + " Miles"
}

data class OpeningHours(
    @SerializedName("open_now")
    var is_open: Boolean?
){
    val isOpenNow : Boolean
    get() = if(is_open != null) is_open!! else false
}

data class Photos(
    @SerializedName("photo_reference")
    var photo_reference: String
) {
    val placeImage: String
        get() = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$photo_reference&sensor=false&key=${BuildConfig.PLACE_API_KEY}"


}


private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val theta = lon1 - lon2
    var dist = (Math.sin(deg2rad(lat1))
            * Math.sin(deg2rad(lat2))
            + (Math.cos(deg2rad(lat1))
            * Math.cos(deg2rad(lat2))
            * Math.cos(deg2rad(theta))))
    dist = Math.acos(dist)
    dist = rad2deg(dist)
    dist = dist * 60 * 1.1515
    return (DecimalFormat("##.#").format(dist)).toDouble()
}

private fun deg2rad(deg: Double): Double {
    return deg * Math.PI / 180.0
}

private fun rad2deg(rad: Double): Double {
    return rad * 180.0 / Math.PI
}


