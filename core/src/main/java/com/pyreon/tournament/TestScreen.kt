package com.pyreon.tournament

import com.artemis.ComponentMapper
import com.artemis.WorldConfiguration
import com.artemis.WorldConfigurationBuilder
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.pyreon.components.*
import com.pyreon.systems.InputSystem
import com.pyreon.systems.MovementSystem
import com.pyreon.systems.PhysicsSyncSystem
import com.pyreon.systems.SpriteBatchDrawSystem


class TestScreen(internal val game: Tournament) : Screen {
    internal var camera: OrthographicCamera

    var entityWorld: com.artemis.World
    var physicsWorld: com.badlogic.gdx.physics.box2d.World

    internal var testEntity: Int = 0
    private val positionComponentMapper: ComponentMapper<PositionComponent>
    private val textureComponentMapper: ComponentMapper<TextureComponent>
    private val playerControlComponentMapper: ComponentMapper<PlayerControlComponent>
    private val spriteComponentMapper: ComponentMapper<SpriteComponent>
    private val physicsBodyComponentMapper: ComponentMapper<PhysicsBodyComponent>

    private val CAMERA_WIDTH = 800f / 32 //25
    private val CAMERA_HEIGHT = 600f / 32 //15
    internal var debugRenderer = Box2DDebugRenderer()

    internal var lightEffect: Array<TextureAtlas.AtlasRegion>
    internal var testSprite: Sprite
    internal var screenViewport: ScreenViewport

    internal var stage: Stage
    internal var table: Table

    init {
        Box2D.init()

        // setting up camera, viewport, stage
        camera = OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT)
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f)

        screenViewport = ScreenViewport(camera)
        screenViewport.unitsPerPixel = 1 / 32f

        stage = Stage(ScreenViewport(), game.batch)

        // setting up input handling
        val inputSystem = InputSystem() // key press commands
        val inputMultiplexer = InputMultiplexer()

        inputMultiplexer.addProcessor(stage)
        inputMultiplexer.addProcessor(inputSystem)

        Gdx.input.inputProcessor = inputMultiplexer

        // init artemis and box2d worlds
        physicsWorld = com.badlogic.gdx.physics.box2d.World(Vector2(0f, -10f), true)
        val config = WorldConfigurationBuilder()
                .with(PhysicsSyncSystem(),
                        inputSystem,
                        MovementSystem(),
                        SpriteBatchDrawSystem(game, stage))
                .build()
        entityWorld = com.artemis.World(config)


        testSprite = game.atlas.createSprite("Effects/magic/LightEffect1", 1)
        positionComponentMapper = entityWorld.getMapper(PositionComponent::class.java)
        textureComponentMapper = entityWorld.getMapper(TextureComponent::class.java)
        playerControlComponentMapper = entityWorld.getMapper(PlayerControlComponent::class.java)
        spriteComponentMapper = entityWorld.getMapper(SpriteComponent::class.java)
        physicsBodyComponentMapper = entityWorld.getMapper(PhysicsBodyComponent::class.java)
        lightEffect = game.atlas.findRegions("Effects/magic/LightEffect1")

        //UI
        val nameLabel = Label("Name:", game.uiSkin)
        val nameText = TextField("", game.uiSkin)
        val addressLabel = Label("Address:", game.uiSkin)
        val addressText = TextField("", game.uiSkin)

        table = Table()
        table.setFillParent(true)

        table.setDebug(true)

        stage.addActor(table)
        table.add(nameLabel)
        table.add(nameText).width(300f)
        table.row()
        table.add(addressLabel)
        table.add(addressText).width(300f)
    }

    override fun show() {
        testEntity = entityWorld.create()
        val pc = positionComponentMapper.create(testEntity)
        pc.x = 12f
        pc.y = 15f
        val sc = spriteComponentMapper.create(testEntity)
        sc.sprite = game.atlas.createSprite("Effects/magic/LightEffect1", 3)
        sc.sprite!!.setSize(4f, 4f)

        sc.sprite!!.setOriginCenter()
        sc.sprite!!.setOriginBasedPosition(-1.5f, -1.5f)
        playerControlComponentMapper.create(testEntity)

        // First we create a body definition
        val bodyDef = BodyDef()
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody
        // Set our body's starting position in the world
        bodyDef.position.set(12f, 15f)

        val body = physicsWorld.createBody(bodyDef)

        val circle = CircleShape()
        circle.radius = 3f

        // Create a fixture definition to apply our shape to
        val fixtureDef = FixtureDef()
        fixtureDef.shape = circle
        fixtureDef.density = 0.5f
        fixtureDef.friction = 0.4f
        fixtureDef.restitution = 0.6f // Make it bounce a little bit

        // Create our fixture and attach it to the body
        body.createFixture(fixtureDef)
        val pbc = physicsBodyComponentMapper.create(testEntity)
        pbc.body = body
        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        circle.dispose()

        // Create our body definition
        val groundBodyDef = BodyDef()
        // Set its world position
        groundBodyDef.position.set(Vector2(12.5f, 0f))

        // Create a body from the defintion and add it to the world
        val groundBody = physicsWorld.createBody(groundBodyDef)

        // Create a polygon shape
        val groundBox = PolygonShape()
        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        groundBox.setAsBox(5f, 5f)
        // Create a fixture from our polygon shape and add it to our ground body
        groundBody.createFixture(groundBox, 0.0f)
        // Clean up after ourselves
        groundBox.dispose()

    }

    override fun render(delta: Float) {
        entityWorld.setDelta(delta)
        camera.update()
        game.batch.projectionMatrix = camera.combined
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        entityWorld.process()

        stage.act(delta)
        stage.draw()

        debugRenderer.render(physicsWorld, camera.combined)
        physicsWorld.step(1 / 60f, 6, 2)
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {
        stage.dispose()
    }
}
