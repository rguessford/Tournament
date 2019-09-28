package com.pyreon.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.artemis.utils.IntBag
import com.pyreon.components.PhysicsBodyComponent
import com.pyreon.components.PlayerControlComponent
import com.pyreon.components.PositionComponent
import com.pyreon.components.TransformComponent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import javax.xml.crypto.dsig.Transform

class MovementSystem : IteratingSystem(Aspect.all(PositionComponent::class.java).one(TransformComponent::class.java, PlayerControlComponent::class.java)) {
    override fun process(entityId: Int) {
        val positionComponent = positionComponentMapper!!.create(entityId)
        if (transformComponentMapper!!.has(entityId)) {
            val transformComponent = transformComponentMapper.create(entityId)
            positionComponent.x += transformComponent.x
            positionComponent.y += transformComponent.y
        } else if (playerControlComponentMapper!!.has(entityId)) {
            val pcc = playerControlComponentMapper.get(entityId)
            if (pcc.up) {
                if (pcc.left || pcc.right) {
                    positionComponent.y += 1f
                } else {
                    positionComponent.y += 1f
                }
            }
            if (pcc.left) {
                if (pcc.up || pcc.down) {
                    positionComponent.x -= 1f
                } else {
                    positionComponent.x -= 1f
                }
            }
            if (pcc.down) {
                if (pcc.left || pcc.right) {
                    positionComponent.y -= 1f
                } else {
                    positionComponent.y -= 1f
                }
            }
            if (pcc.right) {
                if (pcc.up || pcc.down) {
                    positionComponent.x += 1f
                } else {
                    positionComponent.x += 1f
                }
            }
        }
    }

    private val transformComponentMapper: ComponentMapper<TransformComponent>? = null
    private val positionComponentMapper: ComponentMapper<PositionComponent>? = null
    private val playerControlComponentMapper: ComponentMapper<PlayerControlComponent>? = null
    private val physicsBodyComponentMapper: ComponentMapper<PhysicsBodyComponent>? = null

    companion object {
        private val log = LogManager.getLogger(MovementSystem::class.java.simpleName)
    }
}
