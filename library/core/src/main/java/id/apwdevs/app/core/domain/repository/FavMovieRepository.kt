package id.apwdevs.app.core.domain.repository

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.Movies

interface FavMovieRepository : FavoriteRepository<Movies, DetailMovie>