package com.pyreon.tournament;

import com.artemis.ComponentMapper;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pyreon.components.*;
import com.pyreon.systems.InputSystem;
import com.pyreon.systems.MovementSystem;
import com.pyreon.systems.PhysicsSyncSystem;
import com.pyreon.systems.SpriteBatchDrawSystem;


public class TestScreen implements Screen {
    final Tournament game;
    OrthographicCamera camera;

    public com.artemis.World entityWorld;
    public com.badlogic.gdx.physics.box2d.World physicsWorld;

    int testEntity;
    private ComponentMapper<PositionComponent> positionComponentMapper;
    private ComponentMapper<TextureComponent> textureComponentMapper;
    private ComponentMapper<PlayerControlComponent> playerControlComponentMapper;
    private ComponentMapper<SpriteComponent> spriteComponentMapper;
    private ComponentMapper<PhysicsBodyComponent> physicsBodyComponentMapper;

    private final float CAMERA_WIDTH = 800f/32; //25
    private final float CAMERA_HEIGHT = 600f/32; //15
    Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

    Array<TextureAtlas.AtlasRegion> lightEffect;
    Sprite testSprite;
    ScreenViewport screenViewport;

    Stage stage;

    public TestScreen(final Tournament tournament) {
        this.game = tournament;
        Box2D.init();

        // setting up camera, viewport, stage
        camera = new OrthographicCamera(CAMERA_WIDTH,CAMERA_HEIGHT);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);

        screenViewport = new ScreenViewport(camera);
        screenViewport.setUnitsPerPixel(1/32f);

        stage = new Stage(screenViewport, game.batch);

        // setting up input handling
        InputSystem inputSystem = new InputSystem(); // key press commands
        InputMultiplexer inputMultiplexer = new InputMultiplexer();

        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(inputSystem);

        Gdx.input.setInputProcessor(inputMultiplexer);

        // init artemis and box2d worlds
        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(new PhysicsSyncSystem(),
                        inputSystem,
                        new MovementSystem(),
                        new SpriteBatchDrawSystem(game))
                .build();
        entityWorld = new com.artemis.World(config);
        physicsWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -10f), true);

        testSprite = game.atlas.createSprite("Effects/magic/LightEffect1", 1);
        positionComponentMapper = entityWorld.getMapper(PositionComponent.class);
        textureComponentMapper = entityWorld.getMapper(TextureComponent.class);
        playerControlComponentMapper = entityWorld.getMapper(PlayerControlComponent.class);
        spriteComponentMapper = entityWorld.getMapper(SpriteComponent.class);
        physicsBodyComponentMapper = entityWorld.getMapper(PhysicsBodyComponent.class);
        lightEffect = game.atlas.findRegions("Effects/magic/LightEffect1");
    }

    @Override
    public void show() {
        testEntity = entityWorld.create();
        PositionComponent pc = positionComponentMapper.create(testEntity);
        pc.x = 12;
        pc.y = 15;
        SpriteComponent sc = spriteComponentMapper.create(testEntity);
        sc.sprite = game.atlas.createSprite("Effects/magic/LightEffect1", 3);
        sc.sprite.setSize(4,4);

        sc.sprite.setOriginCenter();
        sc.sprite.setOriginBasedPosition(-1.5f,-1.5f);
        playerControlComponentMapper.create(testEntity);

        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
// Set our body's starting position in the world
        bodyDef.position.set(12, 15);

        Body body = physicsWorld.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(1f);

// Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit

// Create our fixture and attach it to the body
        body.createFixture(fixtureDef);
        PhysicsBodyComponent pbc = physicsBodyComponentMapper.create(testEntity);
        pbc.body = body;
// Remember to dispose of any shapes after you're done with them!
// BodyDef and FixtureDef don't need disposing, but shapes do.
        circle.dispose();

        // Create our body definition
        BodyDef groundBodyDef = new BodyDef();
// Set its world position
        groundBodyDef.position.set(new Vector2(12.5f, 0));

// Create a body from the defintion and add it to the world
        Body groundBody = physicsWorld.createBody(groundBodyDef);

// Create a polygon shape
        PolygonShape groundBox = new PolygonShape();
// Set the polygon shape as a box which is twice the size of our view port and 20 high
// (setAsBox takes half-width and half-height as arguments)
        groundBox.setAsBox(5f, 5f);
// Create a fixture from our polygon shape and add it to our ground body
        groundBody.createFixture(groundBox, 0.0f);
// Clean up after ourselves
        groundBox.dispose();

    }

    @Override
    public void render(float delta){
        entityWorld.setDelta(delta);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        entityWorld.process();

        stage.act(delta);
        stage.draw();

        debugRenderer.render(physicsWorld, camera.combined);
        physicsWorld.step(1/60f, 6, 2);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
