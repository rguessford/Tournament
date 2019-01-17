package com.pyreon.tournament;

import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.pyreon.systems.DrawSystem;
import com.pyreon.systems.InputSystem;
import com.pyreon.systems.MovementSystem;

public class Tournament extends Game {
	public SpriteBatch batch;
	public Texture img;
	public com.artemis.World entityWorld;
	public com.badlogic.gdx.physics.box2d.World physicsWorld;

	@Override
	public void create () {
		Box2D.init();
		InputSystem inputSystem = new InputSystem();
		Gdx.input.setInputProcessor(inputSystem);
		WorldConfiguration config = new WorldConfigurationBuilder()
				.with(inputSystem,
						new MovementSystem(),
						new DrawSystem(this))
				.build();
		entityWorld = new com.artemis.World(config);
		physicsWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0,0), true);
		batch = new SpriteBatch();
		this.setScreen(new TestScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
