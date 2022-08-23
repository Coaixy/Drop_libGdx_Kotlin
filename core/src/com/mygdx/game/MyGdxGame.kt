package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.TimeUtils

class MyGdxGame : ApplicationAdapter() {
    private lateinit var dropImg:Texture;
    private lateinit var bucketImg:Texture;
    private lateinit var camera:OrthographicCamera
    private lateinit var batch:SpriteBatch
    private lateinit var bucket: Rectangle
    private var rainDrops:MutableList<Rectangle> = mutableListOf()
    private var lastDropTime:Long = 0;
    override fun create() {
        //载入图片
        dropImg = Texture(Gdx.files.internal("dropImg.png"))
        bucketImg = Texture(Gdx.files.internal("bucketImg.png"))

        camera=OrthographicCamera()
        camera.setToOrtho(false, 800F, 480F)
        batch = SpriteBatch()

        bucket = Rectangle()
        bucket.x = (800/2-64/2).toFloat();bucket.y= 20F;bucket.width= 64F;bucket.height= 64F

        spawnRainDrop()


    }
    override fun render() {
        ScreenUtils.clear(0F,0F,0.2F,1F)
        camera.update()
        batch.projectionMatrix = camera.combined
        batch.begin()
        batch.draw(bucketImg, bucket.x, bucket.y)
        for (i in rainDrops){
            batch.draw(dropImg, i.x, i.y)
        }
        batch.end()

        if (Gdx.input.isTouched){
            var touchPos = Vector3()
            touchPos.set(Gdx.input.x.toFloat(),Gdx.input.y.toFloat(),0F)
            camera.unproject(touchPos)
            bucket.x = touchPos.x - 64/2
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) bucket.x = bucket.x - 200 * Gdx.graphics.deltaTime
        if(Gdx.input.isKeyPressed(Input.Keys.D)) bucket.x = bucket.x + 200 * Gdx.graphics.deltaTime
        if (bucket.x < 0) bucket.x = 0F
        if (bucket.x > 800 - 64) bucket.x = (800 - 64).toFloat()

        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRainDrop()

        for (i in rainDrops.size-1 downTo  0 step 1){
            val value = rainDrops[i]
            value.y = value.y - 200*Gdx.graphics.deltaTime
            if (value.y+64<0) rainDrops.remove(value)
            if (value.overlaps(bucket)){
                rainDrops.remove(value)
            }
        }
    }
    override fun dispose() {
        dropImg.dispose()
        bucketImg.dispose()
        batch.dispose()
    }
    private fun spawnRainDrop(){
        var rainDrop = Rectangle()
        rainDrop.x = MathUtils.random(0,800-64).toFloat()
        rainDrop.y = 480F
        rainDrop.width = 64F
        rainDrop.height = 64F
        rainDrops.add(rainDrop)
        lastDropTime = TimeUtils.nanoTime()
    }
}

