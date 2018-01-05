package com.sjjkk.datastructurework

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import kotlinx.android.synthetic.main.activity_main2.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList
import android.graphics.BitmapFactory
import android.util.Log
import android.util.SparseIntArray
import com.sjjkk.datastructurework.company.HashImage
import com.sjjkk.datastructurework.company.HashTable
import com.sjjkk.datastructurework.company.HashTableWithLinkedList
import com.sjjkk.datastructurework.company.Item
import java.io.*


class Main2Activity : AppCompatActivity() {
    var alertDialog: AlertDialog? = null
    var sourceImage: Bitmap? = null
    var grayImage: Bitmap? = null
    var smallImage: Bitmap? = null
    var finalBitMap: Bitmap? = null
    var images: List<Uri> = ArrayList<Uri>()

    var hashTable: HashTable = HashTable(600)
    var linkedHashTable: HashTableWithLinkedList = HashTableWithLinkedList(600)
    var similarArray: MutableList<SimilarContent> = ArrayList()
    var sortedList: List<SimilarContent>? = null

    var imageData: MutableCollection<HashImage> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        initViews()
    }

    private fun initViews() {
        alertDialog = alert {
            customView {
                include<RelativeLayout>(R.layout.recyclerview_loading_layout) {
                    padding = dip(10)
                }
            }
        }.build() as AlertDialog
        select_button.onClick {
            Matisse.from(this@Main2Activity)
                    .choose(MimeType.allOf())
                    .maxSelectable(1)
                    .theme(R.style.Matisse_Dracula)
                    .imageEngine(GlideEngine())
                    .forResult(MATISSE_REQUEST_CODE)

        }

        gray_button.onClick {
            gray()
        }

        small_button.onClick {
            grayImage?.let {
                small()
            }
        }

        calculate_button.onClick {
            smallImage?.let {
                calculate()
            }
        }

        generate_button.onClick {
            buildCustomFingerStore()
            buildFingerPrintStoreTable()
        }

        asl_linear.onClick {
            calculateLinear()
        }

        asl_linked.onClick {
            calculateLinked()
        }

        search_button.onClick {
            searchSimilar(currentFingerPrint)
        }

    }

    private fun searchSimilar(currentFingerPrint: String) {
        buildSimilarArray(currentFingerPrint)


    }

    private fun buildSimilarArray(currentFingerPrint: String) {
        similarArray.clear()

        for (i in imageData) {
            var similarDis: Int = compare(currentFingerPrint, i.fingerPrint)
            similarArray.add(SimilarContent(similarDis, i.name))
        }
        val similarArray = similarArray.sortedWith(compareBy({it.similarDistance}))

        var pic_1: String? = null
        var pic_2: String? = null
        var pic_3: String? = null
        pic_1 = BASE_NAME + similarArray[0].fileName.replace('\\', '/')
        pic_2 = BASE_NAME + similarArray[1].fileName.replace('\\', '/')
        pic_3 = BASE_NAME + similarArray[2].fileName.replace('\\', '/')
        Glide.with(this).load(pic_1).into(out_image_1)
        Glide.with(this).load(pic_2).into(out_image_2)
        Glide.with(this).load(pic_3).into(out_image_3)
    }

    private fun calculateLinked() {
        var sum: Float = 0F
        for (i in imageData) {
            var searchResult: HashSearchResult = linkedHashTable.find(i.name)
            sum += searchResult.height
        }
        var average: Float = sum / imageData.size
        toast("Linear Hash Average\n is " + average)
    }

    private fun calculateLinear() {
        var sum: Float = 0F
        for (i in imageData) {
            var searchResult: HashSearchResult = hashTable.find(i.name)
            sum += searchResult.height
        }
        var average: Float = sum / imageData.size
        toast("Linear Hash Average\n is " + average)
    }

    private fun buildFingerPrintStoreTable() {
        var fingetPrintFile: File = File(BASE_NAME + newFingerPrintStoreName)
        var fileInputStream = FileInputStream(fingetPrintFile)
        var fingerPrintScanner: Scanner = Scanner(fileInputStream)

        var fileNameScanner: Scanner = Scanner(assets.open("filename.txt"))
        while (fingerPrintScanner.hasNext()) {
            var fingerPrint = fingerPrintScanner.next()
            var fileName = fileNameScanner.next()
            fileName = fileName.subSequence(12, fileName.length).toString()
            var hashImage: HashImage = HashImage(fileName, fingerPrint!!);
            hashTable.insert(hashImage)
            linkedHashTable.insert(hashImage)
            imageData.add(hashImage)
        }

    }

    private var currentFingerPrint: String = ""

    private fun calculate() {
        var range = 4
        var pixels = IntArray(64)
        var newPixels = IntArray(64)
        var newFlags = IntArray(size = 64)
        smallImage!!.getPixels(pixels, 0, 8, 0, 0, 8, 8)
        var sum: Int = 0
        for (i in 0..7) {
            for (j in 0..7) {
                var newValue = (pixels[8 * i + j]) / 4
                var newPixel = (newValue)
                newPixels[8 * i + j] = newPixel
                sum += newValue
            }
        }
        var average: Int = sum / 64

//        0 1 化
        for (i in 0..7) {
            for (j in 0..7) {
                var newValue: Int = (newPixels[8 * i + j])
                if (newValue > average) {
                    newPixels[8 * i + j] = 0xFFFFFF
                    newFlags[8 * i + j] = 1
                } else {
                    newPixels[8 * i + j] = 0x000000
                    newFlags[8 * i + j] = 0
                }
            }
        }
        var bigSum: BigInteger = BigInteger("0")

        for (i in 0..63) {
            bigSum = bigSum.add(BigInteger(Math.pow(newFlags[i] * 2.toDouble(), i.toDouble()).toInt().toString()))
        }
        var stringBuilder = StringBuilder()
        for (i in newFlags) {
            stringBuilder.append(i)
        }
//        var intResult: BigInteger = binaryToInteger(stringBuilder.toString())


        toast("fingerPrint" + stringBuilder.toString())
        currentFingerPrint = stringBuilder.toString()
        finalBitMap = Bitmap.createBitmap(8, 8, Bitmap.Config.RGB_565)
        finalBitMap!!.setPixels(newPixels, 0, 8, 0, 0, 8, 8)
        Glide.with(this).load(toBytes(finalBitMap!!)).into(image_4)

    }

    private fun calculate(bitmap: Bitmap): String {
        var range = 4
        var pixels = IntArray(64)
        var newPixels = IntArray(64)
        var newFlags = IntArray(size = 64)
        bitmap!!.getPixels(pixels, 0, 8, 0, 0, 8, 8)
        var sum: Int = 0
        for (i in 0..7) {
            for (j in 0..7) {
                var newValue = (pixels[8 * i + j]) / 4
                var newPixel = (newValue)
                newPixels[8 * i + j] = newPixel
                sum += newValue
            }
        }
        var average: Int = sum / 64

//        0 1 化
        for (i in 0..7) {
            for (j in 0..7) {
                var newValue: Int = (newPixels[8 * i + j])
                if (newValue > average) {
                    newPixels[8 * i + j] = 0xFFFFFF
                    newFlags[8 * i + j] = 1
                } else {
                    newPixels[8 * i + j] = 0x000000
                    newFlags[8 * i + j] = 0
                }
            }
        }
        var stringBuilder = StringBuilder()
        for (i in newFlags) {
            stringBuilder.append(i)
        }

        return stringBuilder.toString()
    }


    fun binaryToInteger(binary: String): BigInteger {
        var numbers: CharArray = binary.toCharArray()
        var result: BigInteger = BigInteger("0")
        var i = numbers.size - 1
        while (i >= 0) {
            if (numbers[i] == '1') {
                result = result + BigInteger((Math.pow(2.0, (numbers.size - i - 1).toDouble()).toInt()).toString())
            }
            i--
        }

        return result
    }

    private fun small() {
        var widthNeed = 8;
        var heightNeed = 8;
        var width = grayImage!!.width
        var height = grayImage!!.height
        var pixels = IntArray(width * height)
        var newPixels = IntArray(widthNeed * heightNeed)
        grayImage!!.getPixels(pixels, 0, width, 0, 0, width, height)
        var widthStepLength = width / widthNeed
        var heightStepLength = height / heightNeed

        var alpha = 0xFF shl 24

        for (i in 0..heightNeed - 1) {
            for (j in 0..widthNeed - 1) {
                var blockSum = 0
                for (k in 0..heightStepLength - 1) {
                    for (l in 0..widthStepLength - 1) {
//                      绝对x
                        var x = j * widthStepLength + l
//                      绝对y
                        var y = i * heightStepLength + k
//                      获取 最小单位的pixel
                        var pixel = pixels[y * width + x]
//                      获取红色
                        var red = ((pixel and 0x00FF0000) shr 16)
                        blockSum += red
                    }
                }
//              块 结束
                var red = blockSum / (widthStepLength * heightStepLength)
                var newPixel = alpha or (red shl 16) or (red shl 8) or red;
                newPixels[i * widthNeed + j] = newPixel
            }
        }
        smallImage = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888)
        smallImage!!.setPixels(newPixels, 0, widthNeed, 0, 0, widthNeed, heightNeed)

        Glide.with(this).load(toBytes(smallImage!!)).into(image_3)
    }

    private fun small(bitmap: Bitmap): Bitmap {
        var widthNeed = 8;
        var heightNeed = 8;
        var width = bitmap!!.width
        var height = bitmap!!.height
        var pixels = IntArray(width * height)
        var newPixels = IntArray(widthNeed * heightNeed)
        bitmap!!.getPixels(pixels, 0, width, 0, 0, width, height)
        var widthStepLength = width / widthNeed
        var heightStepLength = height / heightNeed

        var alpha = 0xFF shl 24

        for (i in 0..heightNeed - 1) {
            for (j in 0..widthNeed - 1) {
                var blockSum = 0
                for (k in 0..heightStepLength - 1) {
                    for (l in 0..widthStepLength - 1) {
//                      绝对x
                        var x = j * widthStepLength + l
//                      绝对y
                        var y = i * heightStepLength + k
//                      获取 最小单位的pixel
                        var pixel = pixels[y * width + x]
//                      获取红色
                        var red = ((pixel and 0x00FF0000) shr 16)
                        blockSum += red
                    }
                }
//              块 结束
                var red = blockSum / (widthStepLength * heightStepLength)
                var newPixel = alpha or (red shl 16) or (red shl 8) or red;
                newPixels[i * widthNeed + j] = newPixel
            }
        }
        var newImage = Bitmap.createBitmap(8, 8, Bitmap.Config.ARGB_8888)
        newImage!!.setPixels(newPixels, 0, widthNeed, 0, 0, widthNeed, heightNeed)
        return newImage

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            MATISSE_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    images = Matisse.obtainResult(data!!)

                    sourceImage = MediaStore.Images.Media.getBitmap(contentResolver, images[0])
                    var path: String = getRealPathFromUri(this, images[0])
                    Glide.with(this)
                            .load(path)
                            .into(image_1)

                }
            }
        }
    }

    fun getRealPathFromUri(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor!!.moveToFirst()
            return cursor!!.getString(column_index)
        } finally {
            if (cursor != null) {
                cursor!!.close()
            }
        }
    }

    fun gray() {
        sourceImage?.let {
            grayImage = convertGreyImg(sourceImage!!)
            Glide.with(this).load(toBytes(grayImage!!)).into(image_2)
        }
    }

    fun toBytes(bitmap: Bitmap): ByteArray {
        var grayImage = convertGreyImg(bitmap)
        var baos = ByteArrayOutputStream()
        grayImage.compress(Bitmap.CompressFormat.PNG, 100, baos)
        var bytes: ByteArray = baos.toByteArray();
        return bytes
    }

    companion object {
        val MATISSE_REQUEST_CODE = 123
        val BASE_NAME = "/storage/emulated/0/tencent/database - 副本/database - 副本/"
        var newFingerPrintStoreName: String = "fingerPrint.txt"


    }

    fun showProgress(show: Boolean) {
        if (show) {
            alertDialog!!.show()
        } else {
            alertDialog!!.hide()
        }
    }

    fun convertGreyImg(img: Bitmap): Bitmap {
        var width = img.width //获取位图的宽
        var height = img.height //获取位图的高
        var pixels = IntArray(width * height, { 0 }) //通过位图的大小创建像素点数组
        img.getPixels(pixels, 0, width, 0, 0, width, height)
        var alpha = 0xFF shl 24
        for (i in 0..height - 1) {
            for (j in 0..width - 1) {
                var grey = pixels[width * i + j]
                var red = ((grey and 0x00FF0000) shr 16)
                var green = ((grey and 0x0000FF00) shr 8)
                var blue = (grey and 0x000000FF)
                grey = ((red * 0.3 + green * 0.59 + blue * 0.11).toInt())
                grey = alpha or (grey shl 16) or (grey shl 8) or grey;
                pixels[width * i + j] = grey
            }
        }

        var result: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        result.setPixels(pixels, 0, width, 0, 0, width, height)
        return result
    }

    fun buildCustomFingerStore() {
        showProgress(true)
        var newFingerPrintStorePath: String = BASE_NAME
        var fileNameText: String = "filename.txt";
        var fileNameArray: MutableCollection<String> = ArrayList()

        var fingerPrintFile = File(newFingerPrintStorePath, newFingerPrintStoreName)
        var writer: OutputStreamWriter? = null
        var fileOutStream: FileOutputStream? = null


        try {
            if (!fingerPrintFile.exists()) {
                fingerPrintFile.createNewFile()
            }
            fileOutStream = FileOutputStream(fingerPrintFile)
            writer = OutputStreamWriter(fileOutStream)
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

//        var fileReader: FileReader? = null;
//        try {
//            fileReader = FileReader(fileName);
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace();
//        }
        var scanner: Scanner = Scanner(assets.open(fileNameText))

//        val filename = "fingerprint.txt"
//        val string = "Hello world!\n"
//        val outputStream: FileOutputStream

//        try {
//            outputStream = openFileOutput(filename, Context.MODE_PRIVATE)
//            outputStream.write(string.toByteArray())
//            outputStream.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

        var count: Int = 1

        while (scanner.hasNext()) {
            var name: String = scanner.next()
            fileNameArray.add(name)
            var newName: String = name.subSequence(12, name.length).toString()
            var fingerPrint: String = processPicture(newName)
            Log.d("Content", " write process " + fingerPrint + " " + count);
            count++
            writer!!.append(fingerPrint + "\n")
        }
        writer!!.close()
        fileOutStream!!.flush()
        fileOutStream!!.close()
        toast("Building Completed")
    }

    private fun processPicture(newName: String): String {
        var nnewName = newName.replace('\\', '/')
        var targetFileName: String = BASE_NAME + nnewName
        var imageFile = File(targetFileName)
        var rawImage = BitmapFactory.decodeFile(imageFile.absolutePath)
//        var rawImage = MediaStore.Images.Media.getBitmap(contentResolver, images[0])
        var gray = convertGreyImg(rawImage)
        var small = small(gray)
        var fingerPrint: String = calculate(small)
        return fingerPrint
    }

    private fun compare(source: String, target: String): Int {
        var count: Int = 0
        for (i in 0..63) {
            if (source[i] != target[i]) {
                count++
            }
        }
        return count
    }
}
