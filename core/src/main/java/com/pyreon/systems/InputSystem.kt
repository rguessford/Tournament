package com.pyreon.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.pyreon.components.PlayerControlComponent
import com.pyreon.components.TransformComponent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.Arrays

class InputSystem : BaseEntitySystem(Aspect.one(PlayerControlComponent::class.java)), InputProcessor {

    private val transformComponentMapper: ComponentMapper<TransformComponent>? = null
    private val playerControlComponentMapper: ComponentMapper<PlayerControlComponent>? = null
    internal var currentlyPressed: BooleanArray

    init {
        currentlyPressed = BooleanArray(256)
        Arrays.fill(currentlyPressed, false)
    }

    public override fun processSystem() {
        val actives = subscription.entities
        val ids = actives.data
        var i = 0
        val s = actives.size()
        while (i < s) {
            if (playerControlComponentMapper!!.has(ids[i])) {
                val pcc = playerControlComponentMapper.get(ids[i])
                pcc.up = currentlyPressed[Input.Keys.W]
                pcc.down = currentlyPressed[Input.Keys.S]
                pcc.left = currentlyPressed[Input.Keys.A]
                pcc.right = currentlyPressed[Input.Keys.D]
            }
            i++
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        currentlyPressed[keycode] = true
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        currentlyPressed[keycode] = false
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    companion object {

        private val log = LogManager.getLogger(InputSystem::class.java.simpleName)
    }
}
