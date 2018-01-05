package com.sjjkk.datastructurework

import android.Manifest
import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import android.Manifest.permission
import android.Manifest.permission.WRITE_CALENDAR
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.bumptech.glide.Glide
import com.sjjkk.datastructurework.company.AvlTree
import com.sjjkk.datastructurework.company.SearchResult
import java.io.FileNotFoundException
import java.io.FileReader
import java.util.*


class MainActivity : AppCompatActivity() {
    var alertDialog: AlertDialog? = null
    internal var imageData: MutableList<Image> = ArrayList()
    private var avlTree: AvlTree<Image> = AvlTree<Image>();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        buildTreeStore()
    }

    private fun buildTreeStore() {
        var fileName: String = "database.txt";
//        var fileReader: FileReader? = null;
//        try {
//            fileReader = FileReader(fileName);
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace();
//        }
        var scanner: Scanner = Scanner(assets.open(fileName));


        while (scanner.hasNext()) {
            var name: String = scanner.next();
            var count: Int = scanner.nextInt();
            var rate: Float = scanner.nextFloat();
            imageData.add(Image(name, count, rate));
        }
        for (imageDatum: Image in imageData) {
            avlTree.insert(imageDatum);
        }
        toast("Building Completed")
    }


    private val PERMISSIONS_REQUEST: Int = 980

    private fun initViews() {

        // Assume thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        Array(2, { Manifest.permission.READ_EXTERNAL_STORAGE;Manifest.permission.WRITE_EXTERNAL_STORAGE }),
                        PERMISSIONS_REQUEST);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        alertDialog = alert {
            customView {
                include<RelativeLayout>(R.layout.recyclerview_loading_layout) {
                    padding = dip(10)
                }
            }
        }.build() as AlertDialog

        search_button.onClick {
            doSearch(search_text.text.toString())
        }

        to_two.onClick {
            startActivity<Main2Activity>()
        }

        asl_button.onClick{
            var sum = 0;
            for (i in imageData){
                var result:SearchResult = avlTree.getNodeByName(i.name)
                sum += result.height
            }
            var asl:Float = (sum.toFloat())/imageData.size
            toast("ASL is " + asl)
        }
    }

    private fun doSearch(searchName: String) {
        var node: SearchResult? = avlTree.getNodeByName(searchName)

        node?.let {
            if (node.avlNode.element is Image) {
                toast("found" + (node.avlNode.element as Image).name + "use height" + node.height)
                log.append("found:airplanes" + "  use height " + node.height + "\n")

                var prefix1: String = "/storage/emulated/0/tencent/database - 副本/database - 副本/" + (node.avlNode.element as Image).name + "/image_0001.jpg"
                var prefix2: String = "/storage/emulated/0/tencent/database - 副本/database - 副本/" + (node.avlNode.element as Image).name + "/image_0002.jpg"
                var prefix3: String = "/storage/emulated/0/tencent/database - 副本/database - 副本/" + (node.avlNode.element as Image).name + "/image_0003.jpg"

                Glide.with(this).load(prefix1).into(pic_1)
                Glide.with(this).load(prefix2).into(pic_2)
                Glide.with(this).load(prefix3).into(pic_3)
            }
        }
        if (node == null) {
            toast("class not found")
            log.append("class not found\n")
        }
    }


    fun showProgress(show: Boolean) {
        if (show) {
            alertDialog!!.show()
        } else {
            alertDialog!!.hide()
        }
    }

}
