package com.pyreon.tournament;

import com.artemis.ComponentMapper;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.pyreon.components.PlayerControlComponent;
import com.pyreon.components.PositionComponent;
import com.pyreon.components.TextureComponent;


public class TestScreen implements Screen {
    final Tournament game;
    OrthographicCamera camera;

    int testEntity;
    private ComponentMapper<PositionComponent> positionComponentMapper;
    private ComponentMapper<TextureComponent> textureComponentMapper;
    private ComponentMapper<PlayerControlComponent> playerControlComponentMapper;


    public TestScreen(final Tournament tournament) {
        this.game = tournament;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        positionComponentMapper = game.entityWorld.getMapper(PositionComponent.class);
        textureComponentMapper = game.entityWorld.getMapper(TextureComponent.class);
        playerControlComponentMapper = game.entityWorld.getMapper(PlayerControlComponent.class);
    }

    @Override
    public void show() {
        testEntity = game.entityWorld.create();
        PositionComponent pc = positionComponentMapper.create(testEntity);
        pc.x = 100;
        pc.y = 100;
        TextureComponent tc = textureComponentMapper.create(testEntity);
        tc.texture = new Texture("badlogic.jpg");
        playerControlComponentMapper.create(testEntity);

    }

    @Override
    public void render(float delta){
        game.entityWorld.setDelta(delta);
        game.entityWorld.process();
        game.physicsWorld.step(1/60f, 6, 2);
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
