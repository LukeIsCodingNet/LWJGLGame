package net.lukeiscoding.lwjglgame;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    private int width = 1920;
    private int height = 1080;

    // the widow handle
    private long window;

    public void run() {
        System.out.println("LWJGL Version: " + Version.getVersion());

        init();
        loop();

        // free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // terminate glfw and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // set a error callback
        // will print the error message in System err
        GLFWErrorCallback.createPrint(System.err).set();

        // init GLFW. Most functions will not work without doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("");
        }

        // configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // create the window
        window = glfwCreateWindow(width, height, "LWJGLGame", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Unable to create the window!");
        }

        // TODO: Create a key call back

        // get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            // get the window size
            glfwGetWindowSize(window, pWidth, pHeight);

            // get the resolution of primary monitor
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // center the window
            glfwSetWindowPos(
                    window,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2
            );
        }   // the stack frame is pooped automatically

        // make the open gl context current
        glfwMakeContextCurrent(window);

        // enable v-sync
        glfwSwapInterval(1);

        // make the window visible
        glfwShowWindow(window);
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // set the clear color
        glClearColor(0.0f, 255.0f, 255.0f, 0.0f);

        // run the rendering loop until we close
        // the window or has pressed the escape key
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glfwSwapBuffers(window);    // swap the color buffers

            // poll for the window events, the key callback above will only invoked during this call
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
