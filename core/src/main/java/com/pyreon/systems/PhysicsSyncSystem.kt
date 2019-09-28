package com.pyreon.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.artemis.utils.IntBag
import com.pyreon.components.PhysicsBodyComponent
import com.pyreon.components.PositionComponent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * This system syncs the position component's x/y values with the physics body's x/y position in the physics world.
 */
class PhysicsSyncSystem : IteratingSystem(Aspect.all(PhysicsBodyComponent::class.java, PositionComponent::class.java)) {

    override fun process(entityId: Int) {
        val positionComponent = positionComponentMapper!!.create(entityId)
        val physicsBodyComponent = physicsBodyComponentMapper!!.create(entityId)
        positionComponent.x = physicsBodyComponent.body!!.position.x
        positionComponent.y = physicsBodyComponent.body!!.position.y
    }

    private val positionComponentMapper: ComponentMapper<PositionComponent>? = null
    private val physicsBodyComponentMapper: ComponentMapper<PhysicsBodyComponent>? = null

    companion object {
        private val log = LogManager.getLogger(PhysicsSyncSystem::class.java.simpleName)
    }
}
