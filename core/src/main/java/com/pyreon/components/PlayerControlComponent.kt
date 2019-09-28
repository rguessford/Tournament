package com.pyreon.components

import com.artemis.Component

data class PlayerControlComponent(var up: Boolean = false,
                                  var down: Boolean = false,
                                  var left: Boolean = false,
                                  var right: Boolean = false,
                                  var a1: Boolean = false,
                                  var a2: Boolean = false) : Component()
