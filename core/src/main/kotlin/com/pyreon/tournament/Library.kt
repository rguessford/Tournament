package com.pyreon.tournament

import com.artemis.World
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.pyreon.components.*

fun generateTestPlayer(game : Tournament, entityWorld : World, physicsWorld : com.badlogic.gdx.physics.box2d.World){

    val testEntity : Int = entityWorld.create()
    val pc = entityWorld.getMapper(PositionComponent::class.java).create(testEntity)
    pc.x = 12f
    pc.y = 15f
    val sc = entityWorld.getMapper(SpriteComponent::class.java).create(testEntity)
    sc.sprite = game.atlas.createSprite("Effects/magic/LightEffect1", 3)
    sc.sprite!!.setSize(4f, 4f)

    sc.sprite!!.setOriginCenter()
    sc.sprite!!.setOriginBasedPosition(-1.5f, -1.5f)
    entityWorld.getMapper(PlayerControlComponent::class.java).create(testEntity)

    // First we create a body definition
    val bodyDef = BodyDef()
    // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
    bodyDef.type = BodyDef.BodyType.DynamicBody

    bodyDef.linearDamping = 10f
    // Set our body's starting position in the world
    bodyDef.position.set(12f, 15f)

    val body = physicsWorld.createBody(bodyDef)

    val circle = CircleShape()
    circle.radius = 2f

    // Create a fixture definition to apply our shape to
    val fixtureDef = FixtureDef()
    fixtureDef.shape = circle
    fixtureDef.density = 0.5f
    fixtureDef.friction = 0.4f
    fixtureDef.restitution = 0.6f // Make it bounce a little bit

    // Create our fixture and attach it to the body
    body.createFixture(fixtureDef)
    val pbc = entityWorld.getMapper(PhysicsBodyComponent::class.java).create(testEntity)
    pbc.body = body
    // Remember to dispose of any shapes after you're done with them!
    // BodyDef and FixtureDef don't need disposing, but shapes do.
    circle.dispose()
}