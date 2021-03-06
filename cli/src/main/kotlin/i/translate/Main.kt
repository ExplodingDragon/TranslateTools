package i.translate

import com.github.openEdgn.logger4k.getLogger
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class Main : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.initStyle(StageStyle.TRANSPARENT)
        val resource = javaClass.getResource("/main.fxml")!!
        val load = FXMLLoader.load<VBox>(resource)
        val scene = Scene(load)
        scene.fill = Color.TRANSPARENT
        scene.stylesheets.add(Main::class.java.getResource("/main.css").toExternalForm())
        primaryStage.scene = scene
        primaryStage.isAlwaysOnTop = true
        primaryStage.show()
        Thread {
            while (true) {
                TimeUnit.SECONDS.sleep(3)
                javafx.application.Platform.runLater {
                    if (!primaryStage.scene.window.isFocused){
                        primaryStage.scene.window.requestFocus()
                    }
                }
            }
        }.start()

        primaryStage.setOnCloseRequest {
            exitProcess(0)
        }
    }
}

private val logger = Main::class.getLogger()

fun main(args: Array<String>) {
    logger.info("boot!")
    Application.launch(Main::class.java, *args)
}
