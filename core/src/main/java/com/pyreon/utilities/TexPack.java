package com.pyreon.utilities;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
public class TexPack {
    public static void main (String[] args) throws Exception {
        TexturePacker.process("core/assets/", "core/assets/packed", "assets");
    }
}