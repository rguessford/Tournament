package com.pyreon.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.pyreon.components.PositionComponent;
import com.pyreon.components.SpriteComponent;
import com.pyreon.components.TextureComponent;
import com.pyreon.tournament.Tournament;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpriteBatchDrawSystem extends BaseEntitySystem {
    private static Logger log = LogManager.getLogger(SpriteBatchDrawSystem.class.getSimpleName());

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<SpriteComponent> sm;
    ComponentMapper<TextureComponent> tm;
    Tournament game;

    public SpriteBatchDrawSystem(Tournament game) {
        super(Aspect.all(PositionComponent.class).one(SpriteComponent.class, TextureComponent.class));
        this.game = game;
    }

    @Override
    protected void processSystem() {
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();

        game.batch.begin();
        for (int i = 0, s = actives.size(); i < s; i++) {
            PositionComponent position = pm.get(ids[i]);
            if (sm.has(ids[i])) {
                Sprite sprite = sm.get(ids[i]).sprite;
                sprite.setPosition(position.x - sprite.getWidth()/2, position.y - sprite.getHeight()/2);
                sprite.draw(game.batch);
            } else if (tm.has(ids[i])) {
                game.batch.draw(tm.get(ids[i]).texture, position.x, position.y);
            }
        }
        game.batch.end();
    }
}
