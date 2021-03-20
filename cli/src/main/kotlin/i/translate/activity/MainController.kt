package i.translate.activity

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ScrollBar
import javafx.scene.control.TextArea
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder
import java.net.http.HttpClient
import java.nio.charset.Charset
import java.util.*
import kotlin.system.exitProcess

class MainController:Initializable {


    @FXML
    lateinit var oldText: TextArea

    @FXML
    lateinit var newText: TextArea

    @FXML
    lateinit var mainUi: VBox

    private val stage by lazy {
        mainUi.scene.window as Stage
    }


    enum class LANG {
        CN, EN
    }

    fun translate(event: MouseEvent) {
        if (event.button == MouseButton.PRIMARY) {
            //to Eng
            translate(LANG.EN)
        } else {
            // Chs
            translate(LANG.CN)
        }
    }

    var useGoogle = true
    private fun translate(type: LANG) {
        if (useGoogle) {
            googleTranslate(type)
        } else {
            youdaoTranslate(type)
        }
    }

    private fun youdaoTranslate(type: LANG) {
        val url = "https://fanyi.youdao.com/translate?&doctype=json&type=${
            when (type) {
                LANG.CN -> "EN2ZH_CN"
                LANG.EN -> "ZH_CN2EN"
            }
        }&i=" + URLEncoder.encode(oldText.text, Charsets.UTF_8)
        val data =
            (URL(url).openConnection() as HttpURLConnection).inputStream.readAllBytes().toString(Charsets.UTF_8)
        newText.text =
            JSONObject(data).getJSONArray("translateResult").getJSONArray(0).getJSONObject(0).getString("tgt")
    }

    private fun googleTranslate(type: LANG) {
        val urlConn = URL(
            "https://translate.google.cn/translate_a/single?client=gtx&dt=t&dj=1&ie=UTF-8&sl=auto&tl=" +
                    when (type) {
                        LANG.CN -> "zh_CN"
                        LANG.EN -> "en_US"
                    }
                    + "&q=" +
                    URLEncoder.encode(oldText.text, Charsets.UTF_8)
        ).openConnection() as HttpURLConnection
        val data = urlConn.inputStream.readAllBytes().toString(Charsets.UTF_8)
        println(data)
        val jsonObject = JSONObject(data)
        val string = jsonObject.getJSONArray("sentences").getJSONObject(0).getString("trans")
        newText.text = string
    }


    var requestFocus = false
    fun replace(event: MouseEvent) {
        if (event.button == MouseButton.PRIMARY) {
            requestFocus = !requestFocus
            if (requestFocus) {
                oldText.requestFocus()
            } else {
                newText.requestFocus()
            }
        } else {
            useGoogle = !useGoogle
        }
    }


    private var isHide = false
    fun close(event: MouseEvent) {
        if (event.button == MouseButton.MIDDLE) {
            exitProcess(0)
        } else if (event.button == MouseButton.SECONDARY) {
            isHide = !isHide
            mainUi.isVisible = isHide
            mainUi.isManaged = isHide
        }
    }


    private var xOffset = 0.0
    private var yOffset = 0.0
    fun mouseDragged(event: MouseEvent) {
        stage.x = event.screenX + xOffset;
        stage.y = event.screenY + yOffset;
    }

    fun mousePressed(event: MouseEvent) {
        xOffset = stage.x - event.screenX
        yOffset = stage.y - event.screenY
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
    }
}
