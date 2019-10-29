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
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min

class MovementSystem : IteratingSystem(Aspect.all(PositionComponent::class.java).one(TransformComponent::class.java, PlayerControlComponent::class.java, PhysicsBodyComponent::class.java)) {
    val log = LogManager.getLogger(MovementSystem::class.java)

    val SPEED : Float = 20f
    override fun process(entityId: Int) {
        val positionComponent = positionComponentMapper!!.create(entityId)
        val physicsBodyComponent = physicsBodyComponentMapper!!.create(entityId)
        if (playerControlComponentMapper!!.has(entityId)) {
            val pcc = playerControlComponentMapper.get(entityId)
            if (pcc.up) {
                if (pcc.left || pcc.right) {
                    physicsBodyComponent.body!!.applyLinearImpulse(0f * SPEED,.707f* SPEED,positionComponent.x,positionComponent.y,true)
                } else {
                    physicsBodyComponent.body!!.applyLinearImpulse(0f * SPEED,1f* SPEED,positionComponent.x,positionComponent.y,true)
                }
            }
            if (pcc.left) {
                if (pcc.up || pcc.down) {
                    physicsBodyComponent.body!!.applyLinearImpulse(-.707f* SPEED,0f* SPEED,positionComponent.x,positionComponent.y,true)
                } else {
                    physicsBodyComponent.body!!.applyLinearImpulse(-1f* SPEED,0f* SPEED,positionComponent.x,positionComponent.y,true)
                }
            }
            if (pcc.down) {
                if (pcc.left || pcc.right) {
                    physicsBodyComponent.body!!.applyLinearImpulse(0f* SPEED,-.707f* SPEED,positionComponent.x,positionComponent.y,true)
                } else {
                    physicsBodyComponent.body!!.applyLinearImpulse(0f* SPEED,-1f* SPEED,positionComponent.x,positionComponent.y,true)
                }
            }
            if (pcc.right) {
                if (pcc.up || pcc.down) {
                    physicsBodyComponent.body!!.applyLinearImpulse(.707f* SPEED,0f* SPEED,positionComponent.x,positionComponent.y,true)
                } else {
                    physicsBodyComponent.body!!.applyLinearImpulse(1f* SPEED,0f* SPEED,positionComponent.x,positionComponent.y,true)
                }
            }

            if(physicsBodyComponent.body!!.linearVelocity.len() >= 0.1f){
                val facing = physicsBodyComponent.body!!.angle
                val facingLookahead = facing + physicsBodyComponent.body!!.angularVelocity / 30
                val headingVector = physicsBodyComponent.body!!.linearVelocity
                val heading = atan2(headingVector.y,headingVector.x)

                var rotation = heading - facingLookahead

                while(rotation < -PI ) rotation += 2 * PI.toFloat()
                while(rotation > PI ) rotation -= 2 * PI.toFloat()

                val maxAngularVelocity = 30f
                var impulseMagnitude = rotation * 30
                impulseMagnitude = min(maxAngularVelocity, max(-maxAngularVelocity, impulseMagnitude))
                val impulse = physicsBodyComponent.body!!.inertia * impulseMagnitude

                physicsBodyComponent.body!!.applyAngularImpulse(impulse,true)

            } else if(physicsBodyComponent.body!!.linearVelocity.len() < 0.1f){

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
