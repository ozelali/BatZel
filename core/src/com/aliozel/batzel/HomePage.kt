package com.aliozel.batzel

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import java.util.*

class HomePage : ApplicationAdapter() {
    //Variables
    lateinit var batch: SpriteBatch
    lateinit var taptoPlay : Texture
    lateinit var ball: Texture
    lateinit var blockLeft_1: Texture
    lateinit var blockRight_1: Texture
    lateinit var blockLeft_2: Texture
    lateinit var blockRight_2: Texture
    lateinit var background: Texture
    lateinit var highScoreTx: Texture
    lateinit var tryagainTx: Texture
    lateinit var yourHighScore: Texture
    lateinit var shape : ShapeRenderer
    lateinit var font : BitmapFont
    lateinit var scoreFont: BitmapFont
    lateinit var rnd : Random
    lateinit var userHighScore: Preferences

    //Score Variables
    var uHighScore: Int = 0

    //Ball Variables
    var ballX: Float = 0.0f
    var ballY: Float = 0.0f
    var ballLeft: Boolean = true

    //Block Variables
    internal var block1Y: Float = 0f
    internal var block2Y: Float = -800f
    internal var blockHeight: Float = 100f

    //Score Variables
    var score: Int = 0
    lateinit var ballCircle : Circle
    lateinit var blockRect_left : Rectangle
    lateinit var blockRect_right : Rectangle
    lateinit var blockRect_left1 : Rectangle
    lateinit var blockRect_right1 : Rectangle

    internal var blockRight_width = FloatArray(2)
    internal var blockOffSetFin = FloatArray(2)
    internal var blockOffSetBeg = FloatArray(2)
    //Physical Variables
    var velocity: Float = 0.0f
    var blockVelocity : Float = 3f
    var blockVelocityG : Float = 0.003f
    var gravity: Float = 0.5f
    var horizontal_move_ball : Float = 0f
    var block_horizontal_distance: Float = 300f

    var gameState = 2

    override fun create() {
        userHighScore = Gdx.app.getPreferences("HighScore")
        uHighScore = userHighScore.getInteger("highscore")

        batch =  SpriteBatch()
        ball = Texture("basketball.png")
        background = Texture("background1.png")
        blockLeft_1 = Texture("block.png")
        blockRight_1 = Texture("block.png")
        blockLeft_2 = Texture("block.png")
        blockRight_2 = Texture("block.png")
        taptoPlay = Texture("taptoplay.png")
        highScoreTx = Texture("highScore.png")
        tryagainTx = Texture("tryAgain.png")
        yourHighScore = Texture("yourhighscore.png")

        font = BitmapFont()
        scoreFont = BitmapFont()
        scoreFont.setColor(255f, 24f, 57f, 1f)
        scoreFont.data.setScale(8f,8f)
        font.setColor(Color.BLACK)
        font.data.setScale(4f)
        shape = ShapeRenderer()
        rnd = Random()

        ballCircle = Circle()
        for(i in 0..1)
        {
            blockOffSetFin?.set(i, (rnd.nextInt(Gdx.graphics.width - block_horizontal_distance.toInt())).toFloat())
            blockOffSetBeg?.set(i, blockOffSetFin?.get(i)!!.plus(block_horizontal_distance))
            blockRight_width?.set(i, (Gdx.graphics.width.toFloat() - (blockOffSetFin?.get(i)!!.plus(block_horizontal_distance))))

            blockRect_left = Rectangle()
            blockRect_right = Rectangle()
            blockRect_left1 = Rectangle()
            blockRect_right1 = Rectangle()
        }
        ballX = Gdx.graphics.width.toFloat() / 2f
        ballY = Gdx.graphics.height.toFloat() / 1.2f
    }

    override fun render() {
        uHighScore = userHighScore.getInteger("highscore")
        batch.begin()
        if(gameState == 1)
        {
            batch.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
            batch.draw(ball, ballX - 50, ballY)
            if(Gdx.input.justTouched())
            {
                if(ballLeft == true)
                {
                    horizontal_move_ball = -5f
                    ballLeft = false
                }
                else
                {
                    horizontal_move_ball = 5f
                    ballLeft = true
                }
                velocity = -15f
            }

            if(ballX - 50f == 0f)
                kotlin.run {
                    horizontal_move_ball = 5f
                }
            else if(ballX + 50f == Gdx.graphics.width.toFloat())
                kotlin.run {
                    horizontal_move_ball = -5f
                }

            if(ballY > Gdx.graphics.height.toFloat() + 400f)
                kotlin.run {
                    gameState = 0
                }
            else if (ballY < 50)
                kotlin.run {
                    gameState = 0
                }

            if(block1Y > Gdx.graphics.height.toFloat())
                kotlin.run {
                    score++
                    block1Y = 0f
                    blockOffSetFin?.set(0, (rnd.nextInt(Gdx.graphics.width - 300)).toFloat())
                    blockOffSetBeg?.set(0, blockOffSetFin?.get(0)!!.plus(300f))
                    blockRight_width?.set(0, (Gdx.graphics.width.toFloat() - (blockOffSetFin?.get(0)!!.plus(300f))))

                    blockRect_left = Rectangle()
                    blockRect_right = Rectangle()
                }
            else if (block2Y > Gdx.graphics.height.toFloat())
                kotlin.run {
                    score++
                    block2Y = 0f
                    blockOffSetFin?.set(1, (rnd.nextInt(Gdx.graphics.width - 300)).toFloat())
                    blockOffSetBeg?.set(1, blockOffSetFin?.get(1)!!.plus(300f))
                    blockRight_width?.set(1, (Gdx.graphics.width.toFloat() - (blockOffSetFin?.get(1)!!.plus(300f))))

                    blockRect_left1 = Rectangle()
                    blockRect_right1 = Rectangle()
                }
            else
                kotlin.run {
                    block1Y += blockVelocity
                    block2Y += blockVelocity
                }

            batch.draw(blockLeft_1, 0f, block1Y, blockOffSetFin?.get(0)!!.toFloat(), blockHeight)

            batch.draw(blockRight_1, blockOffSetBeg?.get(0)!!.toFloat(), block1Y, blockRight_width!!.get(0), blockHeight)

            batch.draw(blockLeft_1, 0f, block2Y, blockOffSetFin?.get(1)!!.toFloat(), blockHeight)
            batch.draw(blockRight_1, blockOffSetBeg?.get(1)!!.toFloat(), block2Y, blockRight_width!!.get(1), blockHeight)
            font.draw(batch, score.toString(), Gdx.graphics.width / 1.2f,Gdx.graphics.height / 1.02f)

            blockRect_left.set(0f, block1Y, blockOffSetFin?.get(0)!!.toFloat(), blockHeight)
            blockRect_right.set(blockOffSetBeg?.get(0)!!.toFloat(), block1Y, blockRight_width!!.get(0), blockHeight)

            blockRect_left1.set(0f, block2Y, blockOffSetFin?.get(1)!!.toFloat(), blockHeight)
            blockRect_right1.set(blockOffSetBeg?.get(1)!!.toFloat(), block2Y, blockRight_width!!.get(1), blockHeight)

            if(ballY > 100)
             kotlin.run{
                velocity = velocity + gravity
                ballY = ballY - velocity
                ballX = ballX + horizontal_move_ball
                if(blockVelocity < 7f)
                    kotlin.run {
                    blockVelocity += blockVelocityG
                    }
            }
            else
                kotlin.run {
                    gameState = 0
                }

        }
        else if (gameState == 0)
            kotlin.run{
                batch.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
            if(Gdx.input.justTouched())
                kotlin.run{
                    userHighScore.flush()
                    if(ballLeft == true)
                        kotlin.run{
                        horizontal_move_ball = -5f
                        ballLeft = false
                        }
                    else
                        kotlin.run{
                        horizontal_move_ball = 5f
                        ballLeft = true
                        }
                    velocity = -20f
                    score = 0
                    gameState = 1
                }
                else
                kotlin.run {
                    if (score > uHighScore)
                        kotlin.run{
                            userHighScore.putInteger("highscore", score)
                            batch.draw(highScoreTx, Gdx.graphics.width/4f, Gdx.graphics.height / 4f,Gdx.graphics.width / 2f , Gdx.graphics.height/6f)
                            scoreFont.draw(batch, score.toString(), Gdx.graphics.width/2f,Gdx.graphics.height/2f)
                            batch.draw(tryagainTx, Gdx.graphics.width/3.3f, Gdx.graphics.height/ 1.7f, Gdx.graphics.width / 3f , Gdx.graphics.height/4f)

                        }
                    else
                        kotlin.run {
                            batch.draw(yourHighScore, Gdx.graphics.width/3.3f, Gdx.graphics.height / 1.2f,Gdx.graphics.width / 2f , Gdx.graphics.height/7f)
                            scoreFont.draw(batch, uHighScore.toString(), Gdx.graphics.width / 2f - 10f, Gdx.graphics.height / 1.3f)

                            scoreFont.draw(batch, score.toString(), Gdx.graphics.width/2f,Gdx.graphics.height / 4.5f)
                            batch.draw(tryagainTx, Gdx.graphics.width/3.3f, Gdx.graphics.height/ 4f, Gdx.graphics.width / 3f , Gdx.graphics.height/3f)

                        }

                }

                velocity = 0f
                blockVelocity = 3f
                ballY = Gdx.graphics.height.toFloat() / 1.2f
                block1Y = 0f
                block2Y = -800f


                for(i in 0..1)
                {
                    blockOffSetFin?.set(i, (rnd.nextInt(Gdx.graphics.width - block_horizontal_distance.toInt())).toFloat())
                    blockOffSetBeg?.set(i, blockOffSetFin?.get(i)!!.plus(block_horizontal_distance))
                    blockRight_width?.set(i, (Gdx.graphics.width.toFloat() - (blockOffSetFin?.get(i)!!.plus(block_horizontal_distance))))

                    blockRect_left = Rectangle()
                    blockRect_right = Rectangle()
                    blockRect_left1 = Rectangle()
                    blockRect_right1 = Rectangle()
                }
            }
        else if (gameState == 2)
            kotlin.run {

                batch.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
                batch.draw(yourHighScore, Gdx.graphics.width/3.3f, Gdx.graphics.height / 1.2f,Gdx.graphics.width / 2f , Gdx.graphics.height/7f)
                scoreFont.draw(batch, uHighScore.toString(), Gdx.graphics.width / 2f - 10f, Gdx.graphics.height / 1.4f)
                batch.draw(taptoPlay, Gdx.graphics.width/3.3f, Gdx.graphics.height/ 4f, Gdx.graphics.width / 3f , Gdx.graphics.height/3f)

                if(Gdx.input.justTouched())
                    kotlin.run{
                        if(ballLeft == true)
                            kotlin.run{
                                horizontal_move_ball = -5f
                                ballLeft = false
                            }
                        else
                            kotlin.run{
                                horizontal_move_ball = 5f
                                ballLeft = true
                            }
                        velocity = -20f
                        gameState = 1
                    }
                velocity = 0f
                blockVelocity = 3f
                ballY = Gdx.graphics.height.toFloat() / 1.2f
                block1Y = 0f
                block2Y = -800f


                for(i in 0..1)
                {
                    blockOffSetFin?.set(i, (rnd.nextInt(Gdx.graphics.width - block_horizontal_distance.toInt())).toFloat())
                    blockOffSetBeg?.set(i, blockOffSetFin?.get(i)!!.plus(block_horizontal_distance))
                    blockRight_width?.set(i, (Gdx.graphics.width.toFloat() - (blockOffSetFin?.get(i)!!.plus(block_horizontal_distance))))

                    blockRect_left = Rectangle()
                    blockRect_right = Rectangle()
                    blockRect_left1 = Rectangle()
                    blockRect_right1 = Rectangle()
                }
            }



        ballCircle.set(ballX +50 , ballY+50 , 50f)

        batch.end()

        if (Intersector.overlaps(ballCircle, blockRect_left) || Intersector.overlaps(ballCircle, blockRect_left1) || Intersector.overlaps(ballCircle, blockRect_right) || Intersector.overlaps(ballCircle, blockRect_right1)) {
            gameState = 0
        }
    }

    override fun dispose() {
    }
}



