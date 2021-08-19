package com.android.aroundme.data.model

import com.android.aroundme.BuildConfig
import com.google.gson.annotations.SerializedName


data class PlaceData(
    @SerializedName("results")
    val placeList: ArrayList<Places>,
    @SerializedName("status")
    val status: String
)

data class Places(
    @SerializedName("business_status")
    val business_status: String,

    @SerializedName("geometry")
    val geometry: Geometry ,

    @SerializedName("icon")
    val icon: String ,

    @SerializedName("photos")
    val photos: ArrayList<Photos>,

    @SerializedName("icon_background_color")
    val icon_background_color: String,

    @SerializedName("icon_mask_base_uri")
    val icon_mask_base_uri: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("place_id")
    val place_id: String,

    @SerializedName("opening_hours")
    val openingHours: OpeningHours,

    @SerializedName("rating")
    val rating: String,

    @SerializedName("vicinity")
    val vicinity: String

){
    val ratings: Float
        get() = rating.toFloat()
}

data class Geometry(

    @SerializedName("location")
    val location: Locations

  /*  @SerializedName("viewport")
    val viewport : Viewport,*/

)

data class Locations(

    @SerializedName("lat")
    val lat: Double,

    @SerializedName("lng")
    val lng: Double,
)

data class OpeningHours(
    @SerializedName("open_now")
    val open_now: Boolean
)

data class Photos(
    @SerializedName("photo_reference")
    val photo_reference: String
){
    val placeImage : String
       get() = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$photo_reference&sensor=false&key=${BuildConfig.PLACE_API_KEY}"


}


