package com.cinestream.app.data.repository

import com.cinestream.app.data.model.CastMember
import com.cinestream.app.data.model.ContentItem
import com.cinestream.app.data.model.ContentType
import com.cinestream.app.data.model.Episode
import com.cinestream.app.data.model.Season

object ContentRepository {

    // Verified open-source streaming video URLs (MP4 HD)
    const val VIDEO_TEARS_OF_STEEL = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
    const val VIDEO_SINTEL = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"
    const val VIDEO_BIG_BUCK_BUNNY = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    const val VIDEO_WE_ARE_GOING_ON_BULLRUN = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4"
    const val VIDEO_ELEPHANTS_DREAM = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
    const val VIDEO_FOR_BIGGER_BLAZES = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
    const val VIDEO_FOR_BIGGER_ESCAPES = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
    const val VIDEO_SUBARU_OUTBACK = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4"

    val GENRES = listOf(
        "All",
        "Action",
        "Adventure",
        "Sci-Fi",
        "Drama",
        "Comedy",
        "Thriller",
        "Horror",
        "Romance",
        "Animation"
    )

    private val sampleCast = listOf(
        CastMember("Marcus Vance", "Commander Jack Miller", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300&q=80"),
        CastMember("Elena Rostova", "Dr. Lyra Cross", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&q=80"),
        CastMember("Kenji Sato", "Chief Engineer Ryu", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=300&q=80"),
        CastMember("Chloe Bennett", "Special Agent Rivera", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=300&q=80"),
        CastMember("David Hayes", "Director Sterling", "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=300&q=80")
    )

    private val catalog: List<ContentItem> = listOf(
        // Featured Movie 1
        ContentItem(
            id = "movie_1",
            title = "Apex Cyber: Beyond Earth",
            type = ContentType.MOVIE,
            overview = "In a dystopian 2098, a solitary cybernetic pilot must recover an ancient quantum drive from the orbital ruins of Old Europa before rival mega-corporations plunge human colonies into perpetual eclipse.",
            releaseYear = 2024,
            rating = 8.9,
            ageRating = "16+",
            duration = "2h 18m",
            genres = listOf("Sci-Fi", "Action", "Thriller"),
            posterUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&q=80",
            videoUrl = VIDEO_TEARS_OF_STEEL,
            director = "Denis Villeneuve",
            cast = sampleCast,
            isFeatured = true,
            isTrending = true,
            isTopRated = true
        ),

        // Movie 2 - Sintel Fantasy
        ContentItem(
            id = "movie_2",
            title = "Chronicles of Sintel",
            type = ContentType.MOVIE,
            overview = "A determined desert wanderer forms an unbreakable bond with a wounded dragon scaleling, setting out on a perilous pilgrimage across frozen crags and forgotten fortresses to reclaim what was stolen.",
            releaseYear = 2023,
            rating = 8.7,
            ageRating = "13+",
            duration = "1h 52m",
            genres = listOf("Adventure", "Animation", "Drama"),
            posterUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&q=80",
            videoUrl = VIDEO_SINTEL,
            director = "Colin Levy",
            cast = sampleCast,
            isTrending = true,
            isTopRated = true
        ),

        // Movie 3 - Action Thriller
        ContentItem(
            id = "movie_3",
            title = "Neon Velocity",
            type = ContentType.MOVIE,
            overview = "An elite pursuit team racing through neon-soaked Tokyo expressway tunnels uncovers an illicit syndicate controlling real-time grid telemetry.",
            releaseYear = 2024,
            rating = 8.4,
            ageRating = "18+",
            duration = "2h 05m",
            genres = listOf("Action", "Thriller"),
            posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1514565131-fce0801e5785?w=1200&q=80",
            videoUrl = VIDEO_FOR_BIGGER_BLAZES,
            director = "Chad Stahelski",
            cast = sampleCast,
            isTrending = true,
            isRecentlyAdded = true
        ),

        // Movie 4 - Animation Comedy
        ContentItem(
            id = "movie_4",
            title = "The Forest Paradox",
            type = ContentType.MOVIE,
            overview = "A jovial woodland champion and his mischievous rodent comrades orchestrate the ultimate comedic heist against encroaching mechanized bulldozers.",
            releaseYear = 2023,
            rating = 8.1,
            ageRating = "All",
            duration = "1h 35m",
            genres = listOf("Animation", "Comedy", "Adventure"),
            posterUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1511447333015-45b65e60f6d5?w=1200&q=80",
            videoUrl = VIDEO_BIG_BUCK_BUNNY,
            director = "Sacha Goedegebure",
            cast = sampleCast,
            isRecentlyAdded = true
        ),

        // Movie 5 - Psychological Thriller / Horror
        ContentItem(
            id = "movie_5",
            title = "Shadows in the Deep",
            type = ContentType.MOVIE,
            overview = "A submersible research crew stationed seven miles below the Mariana Trench detects rhythmic electromagnetic pulses matching no biological creature known to science.",
            releaseYear = 2023,
            rating = 8.3,
            ageRating = "16+",
            duration = "1h 58m",
            genres = listOf("Horror", "Thriller", "Sci-Fi"),
            posterUrl = "https://images.unsplash.com/photo-1509281373149-e957c6296406?w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=1200&q=80",
            videoUrl = VIDEO_FOR_BIGGER_ESCAPES,
            director = "Guillermo del Toro",
            cast = sampleCast,
            isTopRated = true
        ),

        // Movie 6 - Romantic Drama
        ContentItem(
            id = "movie_6",
            title = "Midnight in Lisbon",
            type = ContentType.MOVIE,
            overview = "Two classical musicians from warring continents cross paths on the historic cobblestone hills of Alfama, sparking a transformative seven-day symphony.",
            releaseYear = 2024,
            rating = 7.9,
            ageRating = "13+",
            duration = "1h 48m",
            genres = listOf("Romance", "Drama"),
            posterUrl = "https://images.unsplash.com/photo-1518173946687-a4c8a383392e?w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1499856871958-5b9627545d1a?w=1200&q=80",
            videoUrl = VIDEO_SUBARU_OUTBACK,
            director = "Richard Linklater",
            cast = sampleCast,
            isRecentlyAdded = true
        ),

        // Movie 7 - Sci-Fi Mystery
        ContentItem(
            id = "movie_7",
            title = "The Dream Machinery",
            type = ContentType.MOVIE,
            overview = "Two eccentric mechanists enter the organic clockwork cavern of the Grand Architect to reboot the universe's cosmic memory before irreversible decay.",
            releaseYear = 2023,
            rating = 8.5,
            ageRating = "16+",
            duration = "2h 10m",
            genres = listOf("Sci-Fi", "Drama"),
            posterUrl = "https://images.unsplash.com/photo-1533134242443-d4fd215305ad?w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=1200&q=80",
            videoUrl = VIDEO_ELEPHANTS_DREAM,
            director = "Bassam Kurdali",
            cast = sampleCast,
            isTopRated = true
        ),

        // Movie 8 - Comedy
        ContentItem(
            id = "movie_8",
            title = "Silicon Valley Rogue",
            type = ContentType.MOVIE,
            overview = "When an algorithm accidentally acquires legal ownership of an autonomous satellite constellation, three clueless college dropouts must keep the Pentagon from noticing.",
            releaseYear = 2024,
            rating = 8.0,
            ageRating = "13+",
            duration = "1h 42m",
            genres = listOf("Comedy"),
            posterUrl = "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1519389950473-47ba0277781c?w=1200&q=80",
            videoUrl = VIDEO_WE_ARE_GOING_ON_BULLRUN,
            director = "Adam McKay",
            cast = sampleCast,
            isRecentlyAdded = true
        ),

        // ================= SERIES =================

        // Series 1 - Cyberpunk Mystery
        ContentItem(
            id = "series_1",
            title = "Orbital Protocol",
            type = ContentType.SERIES,
            overview = "Inside the orbital megastructure Kepler-9, an investigator unearths a genetic conspiracy that reaches the highest echelons of planetary government.",
            releaseYear = 2024,
            rating = 9.1,
            ageRating = "18+",
            duration = "3 Seasons",
            genres = listOf("Sci-Fi", "Thriller", "Drama"),
            posterUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=1200&q=80",
            videoUrl = VIDEO_TEARS_OF_STEEL,
            director = "Alex Garland",
            cast = sampleCast,
            isFeatured = true,
            isTrending = true,
            isTopRated = true,
            seasons = listOf(
                Season(
                    seasonNumber = 1,
                    title = "Season 1: Deconstruction",
                    episodes = listOf(
                        Episode("s1_e1", 1, 1, "Signal in the Static", "54m", "Detective Miller is called to the zero-g laboratory after an encrypted broadcast from an empty pod.", "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&q=80", VIDEO_TEARS_OF_STEEL),
                        Episode("s1_e2", 2, 1, "Null Sector Echo", "49m", "A raid in Sector 7 reveals that the quantum fragments possess predictive synthetic consciousness.", "https://images.unsplash.com/photo-1514565131-fce0801e5785?w=600&q=80", VIDEO_FOR_BIGGER_BLAZES),
                        Episode("s1_e3", 3, 1, "The Europa Sequence", "58m", "Under siege, the station's atmospheric filters are compromised as the countdown accelerates.", "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600&q=80", VIDEO_ELEPHANTS_DREAM),
                        Episode("s1_e4", 4, 1, "Singularity Fallen", "62m", "Season finale: Miller breaches the inner core only to discover the architect was waiting for him.", "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&q=80", VIDEO_SINTEL)
                    )
                ),
                Season(
                    seasonNumber = 2,
                    title = "Season 2: Retribution",
                    episodes = listOf(
                        Episode("s2_e1", 1, 2, "Dark Tether", "52m", "Exiled to the outer rings, Miller intercepts an encrypted frequency speaking in his deceased partner's voice.", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&q=80", VIDEO_TEARS_OF_STEEL),
                        Episode("s2_e2", 2, 2, "Solar Maximum", "48m", "A solar flare shuts down the defensive grid, allowing rogue syndicate dreadnoughts to dock.", "https://images.unsplash.com/photo-1511447333015-45b65e60f6d5?w=600&q=80", VIDEO_FOR_BIGGER_ESCAPES)
                    )
                )
            )
        ),

        // Series 2 - Fantasy Adventure
        ContentItem(
            id = "series_2",
            title = "Echoes of Valeria",
            type = ContentType.SERIES,
            overview = "Across the fractured archipelago of Valeria, elemental keepers battle tyrannical guildmasters for dominion over mythical rune springs.",
            releaseYear = 2023,
            rating = 8.8,
            ageRating = "16+",
            duration = "2 Seasons",
            genres = listOf("Adventure", "Drama", "Action"),
            posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&q=80",
            videoUrl = VIDEO_SINTEL,
            director = "Jane Campion",
            cast = sampleCast,
            isTrending = true,
            isTopRated = true,
            seasons = listOf(
                Season(
                    seasonNumber = 1,
                    title = "Season 1: Rune Awakening",
                    episodes = listOf(
                        Episode("s2_e1", 1, 1, "The Ash Peak", "47m", "A hunter discovers an ancient dragon egg glowing beneath basalt glaciers.", "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=600&q=80", VIDEO_SINTEL),
                        Episode("s2_e2", 2, 1, "Whispering Tides", "53m", "The coastal fleet faces phantom warships sailing upon moonlit mist.", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=600&q=80", VIDEO_FOR_BIGGER_BLAZES),
                        Episode("s2_e3", 3, 1, "The Obsidian Throne", "59m", "Climactic battle at the ancestral gates as the guardian dragon awakens.", "https://images.unsplash.com/photo-1533134242443-d4fd215305ad?w=600&q=80", VIDEO_TEARS_OF_STEEL)
                    )
                )
            )
        ),

        // Series 3 - Crime Drama
        ContentItem(
            id = "series_3",
            title = "Subterranean Syndicate",
            type = ContentType.SERIES,
            overview = "A forensic accountant is thrust into the gritty criminal underworld beneath Chicago's abandoned subterranean train network.",
            releaseYear = 2024,
            rating = 8.6,
            ageRating = "18+",
            duration = "1 Season",
            genres = listOf("Drama", "Thriller", "Action"),
            posterUrl = "https://images.unsplash.com/photo-1514565131-fce0801e5785?w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200&q=80",
            videoUrl = VIDEO_FOR_BIGGER_BLAZES,
            director = "David Fincher",
            cast = sampleCast,
            isRecentlyAdded = true,
            seasons = listOf(
                Season(
                    seasonNumber = 1,
                    title = "Season 1",
                    episodes = listOf(
                        Episode("s3_e1", 1, 1, "Blood Balance", "51m", "An audited ledger leaves three prominent bankers dead within 24 hours.", "https://images.unsplash.com/photo-1514565131-fce0801e5785?w=600&q=80", VIDEO_FOR_BIGGER_BLAZES),
                        Episode("s3_e2", 2, 1, "Tunnel Rats", "55m", "Pursued into subterranean freight tunnels, secrets about city hall emerge.", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&q=80", VIDEO_TEARS_OF_STEEL)
                    )
                )
            )
        ),

        // Series 4 - Animated Comedy
        ContentItem(
            id = "series_4",
            title = "Bunny & The Beasties",
            type = ContentType.SERIES,
            overview = "Join the hilarious forest crew as they protect their sunny glade through cartoon traps, giant flying apples, and slapstick antics.",
            releaseYear = 2023,
            rating = 8.2,
            ageRating = "All",
            duration = "2 Seasons",
            genres = listOf("Animation", "Comedy"),
            posterUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1511447333015-45b65e60f6d5?w=1200&q=80",
            videoUrl = VIDEO_BIG_BUCK_BUNNY,
            director = "Ton Roosendaal",
            cast = sampleCast,
            isTrending = true,
            seasons = listOf(
                Season(
                    seasonNumber = 1,
                    title = "Season 1",
                    episodes = listOf(
                        Episode("s4_e1", 1, 1, "The Great Acorn Caper", "24m", "Flying squirrels hijack the seasonal acorn cache using leaf parachutes.", "https://images.unsplash.com/photo-1563089145-599997674d42?w=600&q=80", VIDEO_BIG_BUCK_BUNNY),
                        Episode("s4_e2", 2, 1, "Big Bunny Strikes Back", "25m", "An ingenious slingshot mechanism restores tranquility to the forest.", "https://images.unsplash.com/photo-1511447333015-45b65e60f6d5?w=600&q=80", VIDEO_WE_Are_Going_On_BullrunSafe())
                    )
                )
            )
        )
    )

    private fun VIDEO_WE_Are_Going_On_BullrunSafe() = VIDEO_WE_ARE_GOING_ON_BULLRUN

    // Methods
    fun getAllContent(): List<ContentItem> = catalog

    fun getAllMovies(): List<ContentItem> = catalog.filter { it.type == ContentType.MOVIE }

    fun getAllSeries(): List<ContentItem> = catalog.filter { it.type == ContentType.SERIES }

    fun getFeatured(): ContentItem = catalog.firstOrNull { it.isFeatured } ?: catalog.first()

    fun getTrending(): List<ContentItem> = catalog.filter { it.isTrending }

    fun getPopularMovies(): List<ContentItem> = catalog.filter { it.type == ContentType.MOVIE && it.rating >= 8.2 }

    fun getPopularSeries(): List<ContentItem> = catalog.filter { it.type == ContentType.SERIES }

    fun getRecommended(): List<ContentItem> = catalog.filter { it.isTopRated || it.isTrending }.shuffled()

    fun getTopRated(): List<ContentItem> = catalog.sortedByDescending { it.rating }

    fun getRecentlyAdded(): List<ContentItem> = catalog.filter { it.isRecentlyAdded || it.releaseYear >= 2024 }

    fun getByGenre(genre: String, type: ContentType? = null): List<ContentItem> {
        if (genre.equals("All", ignoreCase = true)) {
            return if (type != null) catalog.filter { it.type == type } else catalog
        }
        return catalog.filter { item ->
            (type == null || item.type == type) && item.genres.any { it.equals(genre, ignoreCase = true) }
        }
    }

    fun getContentById(id: String): ContentItem? {
        return catalog.find { it.id == id }
    }

    fun getSimilar(item: ContentItem): List<ContentItem> {
        return catalog.filter { other ->
            other.id != item.id && (other.type == item.type || other.genres.any { item.genres.contains(it) })
        }.take(6)
    }

    fun search(query: String): List<ContentItem> {
        val clean = query.trim().lowercase()
        if (clean.isEmpty()) return emptyList()

        return catalog.filter { item ->
            item.title.lowercase().contains(clean) ||
            item.overview.lowercase().contains(clean) ||
            item.genres.any { it.lowercase().contains(clean) } ||
            item.director.lowercase().contains(clean) ||
            item.cast.any { it.name.lowercase().contains(clean) || it.role.lowercase().contains(clean) }
        }
    }

    val TRENDING_SEARCH_KEYWORDS = listOf(
        "Sci-Fi", "Cyberpunk", "Denis Villeneuve", "Action 2024", "Animation", "Christopher Nolan", "Dragon", "Tokyo Expressway"
    )
}
