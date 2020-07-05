package net.lukeiscoding.lwjglgame.shaders;

public abstract class Shader {

    private int vertexShaderID, fragmentShaderID, programID;

    public Shader(String vertexShaderFile, String fragmentShaderFile) {
        vertexShaderFile = vertexShaderFile;
        fragmentShaderFile = fragmentShaderFile;
    }
}
