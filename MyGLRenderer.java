package com.example.hw3_opengl_nj;



import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Triangle mTriangle;
    private Square   mSquare;
    private Circle mCircle;


    public volatile float mAngle;
    public volatile float mTran;

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }

    public float getTran()
    {
        return mTran;
    }

    public void setTran(float tran)
    {
        mTran = tran;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        float color[] = { 0.0f, 0.0f, 0.0f, 1.0f };
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        // initialize a triangle
        mTriangle = new Triangle();
        // initialize a square
        mSquare = new Square();
        mCircle = new Circle();
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    private float[] rotationMatrix = new float[16];
    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        float[] scratch = new float[16];
        float[] square = new float[16];
        float[] tranMatrix = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //mTriangle.draw(vPMatrix);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        // Draw shape
        //mTriangle.draw(vPMatrix);
        // Create a rotation transformation for the triangle
        //long time = SystemClock.uptimeMillis() % 4000L;
        //float angle = 0.090f * ((int) time);
        Matrix.setRotateM(rotationMatrix, 0, mAngle, 0, 0, -1.0f);
        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);

        Matrix.setIdentityM(tranMatrix, 0);
        Matrix.translateM(tranMatrix, 0, mTran, mTran, 0);
        Matrix.multiplyMM(square, 0, tranMatrix, 0, vPMatrix, 0);

        // Draw triangle
        mSquare.draw(square);
        mCircle.draw(vPMatrix);
        mTriangle.draw(scratch);
        //mSquare.draw(vPMatrix);
        //mCircle.draw();
    }

    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }
}
