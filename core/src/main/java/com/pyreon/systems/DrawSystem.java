package com.pyreon.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.pyreon.components.PositionComponent;
import com.pyreon.components.SpriteComponent;
import com.pyreon.components.TextureComponent;
import com.pyreon.tournament.Tournament;

public class DrawSystem extends BaseEntitySystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<SpriteComponent> sm;
    ComponentMapper<TextureComponent> tm;
    Tournament game;

    public DrawSystem(Tournament game){
        super(Aspect.all(PositionComponent.class).one(SpriteComponent.class, TextureComponent.class));
        this.game = game;
    }

    @Override
    protected void processSystem() {
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        for (int i = 0, s = actives.size(); i < s; i++) {
            PositionComponent position = pm.get(ids[i]);
            if(sm.has(ids[i])) {
                game.batch.draw(sm.get(ids[i]).sprite, position.x, position.y);
            } else if (tm.has(ids[i])){
                game.batch.draw(tm.get(ids[i]).texture, position.x, position.y);
            }

        }
        game.batch.end();
    }
}
