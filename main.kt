package seamcarving

import java.awt.Color
import java.awt.Image
import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage
import kotlin.math.pow
import kotlin.math.sqrt
import conditionalChecks

class ImageMethods {

    private val directory = System.getProperty("user.home") + "/Desktop"
    private lateinit var image: BufferedImage
    private val pixelIntensity = mutableListOf<Int>()


    fun readImage(f: String){
        val fileName = File(f)
        image = ImageIO.read(fileName)
    }

    fun calculateEnergy(): MutableList<MutableList<Double>>{
        var energyMatrix = MutableList(image.height) { MutableList(image.width) { 0.0 } }
        for (height in 0 until image.height) {
            for (width in 0 until image.width) {
                // Corner Conditions
                val checks = conditionalChecks(width, height, image.height, image.width)
                val (left, right) = checks.Xconditions(width)
                val (up, down) = checks.Yconditions(height)
                // Pixel RGB Values from given Co-ordinates
                val rgbX = listOf(Color(image.getRGB(left, height)), Color(image.getRGB(right, height)))
                val rgbY = listOf(Color(image.getRGB(width, up)), Color(image.getRGB(width, down)))
                // Energy Calculation
                val Energy = sqrt(
                    (rgbX[0].red - rgbX[1].red.toDouble()).pow(2) +     // The X Differences
                            (rgbX[0].green - rgbX[1].green.toDouble()).pow(2) +
                            (rgbX[0].blue - rgbX[1].blue.toDouble()).pow(2) +
                            // ---------------------
                            (rgbY[0].red - rgbY[1].red.toDouble()).pow(2) +        // The Y Differences
                            (rgbY[0].green - rgbY[1].green.toDouble()).pow(2) +
                            (rgbY[0].blue - rgbY[1].blue.toDouble()).pow(2))
                energyMatrix[height][width] = Energy
            }
        }
        return energyMatrix
    }
    fun calculateIntensity(eMatrix: MutableList<MutableList<Double>>): MutableList<MutableList<Int>>{
        val tempList = mutableListOf<Double>()
        val maxList = mutableListOf<Double>()
        val intensityMatrix = MutableList(image.height){ MutableList(image.width){0} }
        for (i in 0 until image.height){
            for (k in 0 until image.width){
                tempList.add(eMatrix[i][k])
            }
            tempList.maxOrNull()?.let { maxList.add(it) }
        }
        val maximumEnergy = maxList.maxOrNull()
        for (i in 0 until image.height){
            for (j in 0 until image.width) {
                val intensity = (255.0 * eMatrix[i][j] / maximumEnergy!!).toInt()
                intensityMatrix[i][j] = intensity
            }
        }
        return intensityMatrix
    }
    fun createImage(fileName: String, iMatrix: MutableList<MutableList<Int>>) {
        val newImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
        for (i in 0 until  image.height){
            for (j in 0 until image.width) {
                val imageColours = Color(iMatrix[i][j], iMatrix[i][j], iMatrix[i][j])
                newImage.setRGB(j, i, imageColours.rgb)
            }
        }
        val output = File(fileName)
        ImageIO.write(newImage, "png", output)
    }
}


fun main(args: Array<String>){
    lateinit var e: MutableList<MutableList<Double>>
    var fileName: String
    val methods = ImageMethods()
    for (i in args.indices) {
        when (args[i]) {
            "-in" -> {
                fileName = args[i + 1]
                methods.readImage(fileName)
                e = methods.calculateEnergy()
            }
            "-out" -> {
                fileName = args[i + 1]
                val intensity: MutableList<MutableList<Int>> = methods.calculateIntensity(e)
                methods.createImage(fileName, intensity)
            }
        }
    }
}
