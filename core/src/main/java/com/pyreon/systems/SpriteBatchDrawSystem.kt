package com.pyreon.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.artemis.utils.IntBag
import com.badlogic.gdx.graphics.g2d.Sprite
import com.pyreon.components.PositionComponent
import com.pyreon.components.SpriteComponent
import com.pyreon.components.TextureComponent
import com.pyreon.tournament.Tournament
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class SpriteBatchDrawSystem(internal var game: Tournament) : IteratingSystem(Aspect.all(PositionComponent::class.java).one(SpriteComponent::class.java, TextureComponent::class.java)) {

    internal var pm: ComponentMapper<PositionComponent>? = null
    internal var sm: ComponentMapper<SpriteComponent>? = null
    internal var tm: ComponentMapper<TextureComponent>? = null

    override fun begin(){
        game.batch.begin()
    }
    override fun process(entityId: Int) {
        val position = pm!!.get(entityId)
        if (sm!!.has(entityId)) {
            val sprite = sm!!.get(entityId).sprite
            sprite!!.setPosition(position.x - sprite.width / 2, position.y - sprite.height / 2)
            sprite.draw(game.batch)
        } else if (tm!!.has(entityId)) {
            game.batch.draw(tm!!.get(entityId).texture, position.x, position.y)
        }
    }

    override fun end(){
        game.batch.end()
    }

    companion object {
        private val log = LogManager.getLogger(SpriteBatchDrawSystem::class.java.simpleName)
    }
}
