package com.pyreon.tournament;

import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.pyreon.systems.SpriteBatchDrawSystem;
import com.pyreon.systems.InputSystem;
import com.pyreon.systems.MovementSystem;
import com.pyreon.systems.PhysicsSyncSystem;

public class Tournament extends Game {
    public SpriteBatch batch;
    public TextureAtlas atlas;
    public Texture img;


    @Override
    public void create() {
        atlas = new TextureAtlas(Gdx.files.internal("packed/assets.atlas"));
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
