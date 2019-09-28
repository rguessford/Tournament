package com.pyreon.tournament

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas

class Tournament : Game() {
    lateinit var batch: SpriteBatch
    lateinit var atlas: TextureAtlas


    override fun create() {
        batch = SpriteBatch()
        atlas = TextureAtlas(Gdx.files.internal("packed/assets.atlas"))
        this.setScreen(TestScreen(this))
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {
        batch.dispose()
        atlas.dispose()
    }
}
