package com.sktbd.driboard.utils

object Constants {
    const val BASE_URL = "https://api.dribbble.com/v2/"
    const val OAUTH_URL = "https://dribbble.com/oauth/"
    const val EMPTY_STRING = ""
    const val ACCESS_TOKEN = "e615df148ee00bb0ca37daefe53dc3d68e948c4cd29ba2076a333a6eae959338"
    const val REQUEST_CAMERA_PHOTO = 0
    const val REQUEST_GALLERY_PHOTO = 1
    const val REUQEST_PUBLISH_PERMISSION = 0
    const val IMAGE_DIRECTORY = "Image Upload"
    // create a shot and publish
    const val NEW_SHOT_STATE = 0
    // edit a shot and update
    const val UPDATE_SHOT_STATE = 1
    // load the draft which create a shot and publish
    const val NEW_DRAFT_STATE = 2
    // load the draft which edit a shot and update
    const val UPDATE_DRAFT_STATE = 3

    const val WIFI = "WIFI"
    const val CELLULAR = "CELLULAR"
    const val ERROR = "ERROR"

    const val CLIENT_ID = "776e7c6224c49228e619abeaba2c967e84f3d0360865de17d61b1e653759fd1f"
    const val CLIENT_SECRET = "ec8ea1868ccebd63dd334bf5985f432ce7ce6e8ed5e0537c242ea0f28f5cc03b"
    const val REDIRECT_URI = "driboard://callback"

}