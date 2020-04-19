package com.pyreon.components

import com.artemis.Component
import com.badlogic.gdx.physics.box2d.Body

data class PhysicsBodyComponent(var body: Body?) : Component(){
    constructor() : this(null)
}