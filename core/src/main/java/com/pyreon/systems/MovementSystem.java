package com.pyreon.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.pyreon.components.PlayerControlComponent;
import com.pyreon.components.PositionComponent;
import com.pyreon.components.TransformComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.crypto.dsig.Transform;

public class MovementSystem extends BaseEntitySystem {
    private static Logger log = LogManager.getLogger(MovementSystem.class.getSimpleName());

    private ComponentMapper<TransformComponent> transformComponentMapper;
    private ComponentMapper<PositionComponent> positionComponentMapper;
    private ComponentMapper<PlayerControlComponent> playerControlComponentMapper;

    public MovementSystem(){
        super(Aspect.all(PositionComponent.class).one(TransformComponent.class, PlayerControlComponent.class));
    }

    @Override
    protected void processSystem() {
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();
        for (int i = 0, s = actives.size(); i < s; i++) {
            PositionComponent positionComponent = positionComponentMapper.create(ids[i]);
            if (transformComponentMapper.has(ids[i])) {
                TransformComponent transformComponent = transformComponentMapper.create(ids[i]);
                positionComponent.x += transformComponent.x;
                positionComponent.y += transformComponent.y;
            } else if (playerControlComponentMapper.has(ids[i])){
                PlayerControlComponent pcc = playerControlComponentMapper.get(ids[i]);
                log.debug("up:" + pcc.up + " down:" + pcc.down + " left:" + pcc.left + " right:" + pcc.right);
                log.debug(positionComponent.x +","+ positionComponent.y);
                if (pcc.up){
                    if(pcc.left || pcc.right){
                        positionComponent.y += 7.07f;
                    } else {
                        positionComponent.y += 10;
                    }
                }
                if (pcc.left){
                    if(pcc.up || pcc.down){
                        positionComponent.x -= 7.07f;
                    } else {
                        positionComponent.x -= 10;
                    }
                }
                if (pcc.down){
                    if(pcc.left || pcc.right){
                        positionComponent.y -= 7.07f;
                    } else {
                        positionComponent.y -= 10;
                    }
                }
                if (pcc.right){
                    if(pcc.up || pcc.down){
                        positionComponent.x += 7.07f;
                    } else {
                        positionComponent.x += 10;
                    }
                }
            }
        }
    }
}
