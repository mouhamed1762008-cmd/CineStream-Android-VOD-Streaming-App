package com.cinestream.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import com.cinestream.app.data.repository.ContentRepository
import com.cinestream.app.ui.screens.formatMillis

class ExampleRobolectricTest {

  @Test
  fun `verify content repository has movies and series`() {
    val movies = ContentRepository.getAllMovies()
    val series = ContentRepository.getAllSeries()
    assertTrue(movies.isNotEmpty())
    assertTrue(series.isNotEmpty())
  }

  @Test
  fun `verify search finds relevant content`() {
    val results = ContentRepository.search("cyber")
    assertTrue(results.isNotEmpty())
  }

  @Test
  fun `verify formatMillis backward compatibility`() {
    assertEquals("00:00", formatMillis(0L))
    assertEquals("01:30", formatMillis(90_000L))
    assertEquals("1:05:20", formatMillis(3_920_000L))
  }

  @Test
  fun `verify all streaming video URLs are valid https on Android 7+`() {
    val allContent = ContentRepository.getAllContent()
    for (item in allContent) {
      assertTrue("URL must be HTTPS for ${item.title}", item.videoUrl.startsWith("https://"))
      item.seasons?.flatMap { it.episodes }?.forEach { ep ->
        assertTrue("Episode URL must be HTTPS for ${ep.title}", ep.videoUrl.startsWith("https://"))
      }
    }
  }

  @Test
  fun `verify similar items recommendations logic`() {
    val movie = ContentRepository.getAllMovies().first()
    val similar = ContentRepository.getSimilar(movie)
    assertTrue(similar.isNotEmpty())
    assertTrue(similar.none { it.id == movie.id })
  }
}
