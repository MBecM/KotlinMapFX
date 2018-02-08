package kotlinmapfx.layer.tile

import kotlinmapfx.OSM_CACHE
import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.Image
import mu.KotlinLogging
import java.awt.image.RenderedImage
import java.io.File
import java.nio.file.Files
import javax.imageio.ImageIO

private val log = KotlinLogging.logger { }

/**
 * @author Mateusz Becker
 */
class TileLoader(private val provider: TilesProvider, onTileChange: () -> Unit) {

    var tiles: Array<MutableMap<Long, MutableMap<Long, Tile>>> = Array(20) { _ -> mutableMapOf<Long, MutableMap<Long, Tile>>() }

    private val cacheDir = System.getProperty("user.home") + OSM_CACHE

    init {
        Files.createDirectories(File(cacheDir).toPath())
        provider.selectedTileTypeProperty.addListener { _, _, selectedTileType ->
            tiles = Array(20) { _ -> mutableMapOf<Long, MutableMap<Long, Tile>>() }
            log.debug { "Selected TileType = $selectedTileType" }
            onTileChange.invoke()
        }
    }

    fun generateTile(zoom: Int, x: Long, y: Long): Tile {
        val imageName = y.toString() + ".png"
        val imageDir = zoom.toString() + "/" + x + "/"

        val url = provider.selectedTileType.url + imageDir + imageName
        val cache = cacheDir + provider.selectedTileType.name + "/"

        val imageFromCache = checkCache(cache + imageDir + imageName)
        val image = imageFromCache ?: Image(url, true)

        val tile = Tile(zoom, x, y, image)
        if (imageFromCache == null) {
            image.progressProperty().addListener { _, _, progress ->
                if (progress.toDouble() >= 1.0) {
                    saveToCache(image, cache, imageDir, imageName)
                }
            }
        }
        return tile
    }

    private fun checkCache(cacheFile: String): Image? {
        val file = File(cacheFile)
        if (file.exists()) {
            return Image(file.toURI().toString(), true)
        }
        return null;
    }

    private fun saveToCache(img: Image, cacheDir: String, dir: String, fileName: String) {
        val dirs = File(cacheDir, dir)
        val file = File(dirs, fileName)

        Files.createDirectories(dirs.toPath())
        val buffImage: RenderedImage? = SwingFXUtils.fromFXImage(img, null)
        if (buffImage != null) {
            ImageIO.write(buffImage, "png", file)
        }
    }
}