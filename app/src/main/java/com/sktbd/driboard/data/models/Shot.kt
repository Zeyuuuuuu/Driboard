package com.sktbd.driboard.data.models


import com.google.gson.annotations.SerializedName

data class Shot(
    @SerializedName("animated")
    val animated: Boolean, // false
    @SerializedName("attachments")
    val attachments: List<Attachment>,
    @SerializedName("description")
    val description: String, // <p>Quick, messy, five minute sketch of something that might become a fictional something.</p>
    @SerializedName("height")
    val height: Int, // 300
    @SerializedName("html_url")
    val htmlUrl: String, // https://dribbble.com/shots/471756-Sasquatch
    @SerializedName("id")
    val id: Int, // 471756
    @SerializedName("images")
    val images: Images,
    @SerializedName("low_profile")
    val lowProfile: Boolean, // false
    @SerializedName("projects")
    val projects: List<Project>,
    @SerializedName("published_at")
    val publishedAt: String, // 2012-03-15T01:52:33Z
    @SerializedName("tags")
    val tags: List<String>,
    @SerializedName("team")
    val team: Team,
    @SerializedName("title")
    val title: String, // Sasquatch
    @SerializedName("updated_at")
    val updatedAt: String, // 2012-03-15T02:12:57Z
    @SerializedName("video")
    val video: Video,
    @SerializedName("width")
    val width: Int // 400
) {
    data class Attachment(
        @SerializedName("content_type")
        val contentType: String, // image/jpeg
        @SerializedName("created_at")
        val createdAt: String, // 2014-02-07T16:35:09Z
        @SerializedName("id")
        val id: Int, // 206165
        @SerializedName("size")
        val size: Int, // 116375
        @SerializedName("thumbnail_url")
        val thumbnailUrl: String, // https://d13yacurqjgara.cloudfront.net/users/1/screenshots/1412410/attachments/206165/thumbnail/weathered-ball-detail.jpg
        @SerializedName("url")
        val url: String // https://d13yacurqjgara.cloudfront.net/users/1/screenshots/1412410/attachments/206165/weathered-ball-detail.jpg
    )

    data class Images(
        @SerializedName("hidpi")
        val hidpi: Any, // null
        @SerializedName("normal")
        val normal: String, // https://d13yacurqjgara.cloudfront.net/users/1/screenshots/471756/sasquatch.png
        @SerializedName("teaser")
        val teaser: String // https://d13yacurqjgara.cloudfront.net/users/1/screenshots/471756/sasquatch_teaser.png
    )

    data class Project(
        @SerializedName("created_at")
        val createdAt: String, // 2011-04-14T03:43:47Z
        @SerializedName("description")
        val description: String, // I did visual design and art direction for this project, working with the <a href="http://webstandards.org">Web Standards Project</a> and Microsoft.
        @SerializedName("id")
        val id: Int, // 3
        @SerializedName("name")
        val name: String, // Web Standards Sherpa
        @SerializedName("shots_count")
        val shotsCount: Int, // 4
        @SerializedName("updated_at")
        val updatedAt: String // 2012-04-04T22:39:53Z
    )

    data class Team(
        @SerializedName("avatar_url")
        val avatarUrl: String, // https://d13yacurqjgara.cloudfront.net/users/39/avatars/normal/apple-flat-precomposed.png?1388527574
        @SerializedName("bio")
        val bio: String, // Show and tell for designers. This is Dribbble on Dribbble.
        @SerializedName("created_at")
        val createdAt: String, // 2009-08-18T18:34:31Z
        @SerializedName("html_url")
        val htmlUrl: String, // https://dribbble.com/dribbble
        @SerializedName("id")
        val id: Int, // 39
        @SerializedName("links")
        val links: Links,
        @SerializedName("location")
        val location: String, // Salem, MA
        @SerializedName("login")
        val login: String, // dribbble
        @SerializedName("name")
        val name: String, // Dribbble
        @SerializedName("type")
        val type: String, // Team
        @SerializedName("updated_at")
        val updatedAt: String // 2014-02-14T22:32:11Z
    ) {
        data class Links(
            @SerializedName("twitter")
            val twitter: String, // https://twitter.com/dribbble
            @SerializedName("web")
            val web: String // http://dribbble.com
        )
    }

    data class Video(
        @SerializedName("created_at")
        val createdAt: String, // 2019-01-28T15:53:23Z
        @SerializedName("duration")
        val duration: Int, // 17
        @SerializedName("height")
        val height: Int, // 1200
        @SerializedName("id")
        val id: Int, // 10542
        @SerializedName("large_preview_url")
        val largePreviewUrl: String, // https://cdn.dribbble.com/users/1/videos/10542/ratatouille_large_preview.webm
        @SerializedName("silent")
        val silent: Boolean, // true
        @SerializedName("small_preview_url")
        val smallPreviewUrl: String, // https://cdn.dribbble.com/users/1/videos/10542/ratatouille_small_preview.webm
        @SerializedName("updated_at")
        val updatedAt: String, // 2019-01-28T15:56:04Z
        @SerializedName("url")
        val url: String, // https://cdn.dribbble.com/users/1/videos/10542/ratatouille.webm
        @SerializedName("video_file_name")
        val videoFileName: String, // ratatouille.webm
        @SerializedName("video_file_size")
        val videoFileSize: Int, // 12173472
        @SerializedName("width")
        val width: Int, // 1600
        @SerializedName("xlarge_preview_url")
        val xlargePreviewUrl: String // https://cdn.dribbble.com/users/1/videos/10542/ratatouille_xlarge_preview.webm
    )
}