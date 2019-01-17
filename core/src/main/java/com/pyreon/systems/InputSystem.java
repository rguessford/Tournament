package com.pyreon.systems;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.pyreon.components.PlayerControlComponent;
import com.pyreon.components.TransformComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class InputSystem extends BaseEntitySystem implements InputProcessor {

    private static Logger log = LogManager.getLogger(InputSystem.class.getSimpleName());

    private ComponentMapper<TransformComponent> transformComponentMapper;
    private ComponentMapper<PlayerControlComponent> playerControlComponentMapper;
    boolean[] currentlyPressed;

    public InputSystem(){
        super(Aspect.one(PlayerControlComponent.class));
        currentlyPressed = new boolean[256];
        Arrays.fill(currentlyPressed, false);
    }

    @Override
    public final void processSystem() {
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();
        for (int i = 0, s = actives.size(); i < s; i++) {
            if (playerControlComponentMapper.has(ids[i])){
                PlayerControlComponent pcc = playerControlComponentMapper.get(ids[i]);
                pcc.up = currentlyPressed[Input.Keys.W];
                pcc.down = currentlyPressed[Input.Keys.S];
                pcc.left = currentlyPressed[Input.Keys.A];
                pcc.right = currentlyPressed[Input.Keys.D];
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        currentlyPressed[keycode] = true;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        currentlyPressed[keycode] = false;
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
