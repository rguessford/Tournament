package com.pyreon.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.pyreon.components.PhysicsBodyComponent;
import com.pyreon.components.PositionComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This system syncs the position component's x/y values with the physics body's x/y position in the physics world.
 */
public class PhysicsSyncSystem extends BaseEntitySystem {
    private static Logger log = LogManager.getLogger(PhysicsSyncSystem.class.getSimpleName());

    private ComponentMapper<PositionComponent> positionComponentMapper;
    private ComponentMapper<PhysicsBodyComponent> physicsBodyComponentMapper;

    public PhysicsSyncSystem(){
        super(Aspect.all(PhysicsBodyComponent.class, PositionComponent.class));
    }

    @Override
    protected void processSystem() {
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();
        for (int i = 0, s = actives.size(); i < s; i++) {
            PositionComponent positionComponent = positionComponentMapper.create(ids[i]);
            PhysicsBodyComponent physicsBodyComponent = physicsBodyComponentMapper.create(ids[i]);
            positionComponent.x = physicsBodyComponent.body.getPosition().x;
            positionComponent.y = physicsBodyComponent.body.getPosition().y;
        }
    }
}
