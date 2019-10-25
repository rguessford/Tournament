package com.pyreon.tournament

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin

class Tournament : Game() {
    lateinit var batch: SpriteBatch
    lateinit var atlas: TextureAtlas
    lateinit var uiAtlas: TextureAtlas
    lateinit var uiSkin:Skin


    override fun create() {
        batch = SpriteBatch()
        atlas = TextureAtlas(Gdx.files.internal("packed/assets.atlas"))
        uiAtlas = TextureAtlas(Gdx.files.internal("packed/uiskin.atlas"))
        uiSkin = Skin(Gdx.files.internal("packed/uiskin.json"))
        uiSkin.addRegions(uiAtlas)

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
