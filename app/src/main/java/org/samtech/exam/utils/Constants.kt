package org.samtech.exam.utils

object Constants {
    const val BASE_PATH = "https://api.themoviedb.org/3"
    const val PROFILE_PATH = "/account/20169309"
    const val RATED_PATH = "$PROFILE_PATH/rated/movies"
    const val CATEGORIES_PATH = "/genre/movie/list?language=es"
    const val POPULAR_PATH = "/movie/popular"
    const val BEST_RATED_PATH = "/movie/top_rated"
    const val BEST_RECOMMENDED_PATH = "/movie/now_playing"
    
    val AUTHORIZATION = "Authorization"
    val BEARER = "Bearer "
    val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4NjI3ODUxMzgzOTBiMTc4MTUzNmQ4NzQ1ZmJkMjEzNSIsInN1YiI6IjY0YjcxOTg2YTU3NDNkMDBhY2I1ZTY1ZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.X4uDfBK4W9rwbGKS5oxS6k3YGOCuZVJy9g8Wthzxne8"

    val COLLECTION_USER = "users"
    val KEY_AVATAR = "avatar"
    //TODO BASE FOR IMAGES

    //http://image.tmdb.org/t/p/w500/zcZBlpCeuaFCHHsgGAd6yB5RxZV.jpg
}