package com.pyreon.tournament;

import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.pyreon.systems.SpriteBatchDrawSystem;
import com.pyreon.systems.InputSystem;
import com.pyreon.systems.MovementSystem;
import com.pyreon.systems.PhysicsSyncSystem;

public class Tournament extends Game {
    public SpriteBatch batch;
    public TextureAtlas atlas;
    public Texture img;
    public com.artemis.World entityWorld;
    public com.badlogic.gdx.physics.box2d.World physicsWorld;

    @Override
    public void create() {
        atlas = new TextureAtlas(Gdx.files.internal("packed/assets.atlas"));
        Box2D.init();
        InputSystem inputSystem = new InputSystem();
        Gdx.input.setInputProcessor(inputSystem);
        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(new PhysicsSyncSystem(),
                        inputSystem,
                        new MovementSystem(),
                        new SpriteBatchDrawSystem(this))
                .build();
        entityWorld = new com.artemis.World(config);
        physicsWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, -10f), true);
        batch = new SpriteBatch();
        this.setScreen(new TestScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
