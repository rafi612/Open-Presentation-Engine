package com.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class Shader 
{
	int ID;

	public Shader(String vertexpath,String fragmentpath)
	{
		String vShaderCode = load(Shader.class.getResourceAsStream(vertexpath));
		String fShaderCode = load(Shader.class.getResourceAsStream(fragmentpath));
		
		int vertex, fragment;
		
		// vertex Shader
		vertex = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertex, vShaderCode);
		glCompileShader(vertex);
		
		IntBuffer error = BufferUtils.createIntBuffer(1);
		glGetShaderiv(vertex, GL_COMPILE_STATUS, error);
		
		if (error.get() == 0)
		{
			System.out.println("Vertex Error");
		}
		
		
		//fragment shader
		fragment = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragment, fShaderCode);
		glCompileShader(fragment);
		
		IntBuffer error2 = BufferUtils.createIntBuffer(1);
		glGetShaderiv(fragment, GL_COMPILE_STATUS, error2);
		
		if (error2.get() == 0)
		{
			System.out.println("Fragment Error");
		}
		
		// shader Program
		ID = glCreateProgram();
		glAttachShader(ID, vertex);
		glAttachShader(ID, fragment);
		glLinkProgram(ID);
		
		// delete the shaders as they're linked into our program now and no longer necessary
		glDeleteShader(vertex);
		glDeleteShader(fragment);
	}

    public void use()
    {
    	glUseProgram(ID);
    }

    public void setBool(String name, boolean value)
    {
    	glUniform1i(glGetUniformLocation(ID,name), value ? 1 : 0);
    }
    public void setInt(String name, int value)
    {
    	glUniform1i(glGetUniformLocation(ID,name),value);
    }   
    public void setFloat(String name, float value)
    {
    	glUniform1f(glGetUniformLocation(ID,name),value);
    }
    
    public void setVector2f(String name, Vector2f vec)
    {
    	glUniform2f(glGetUniformLocation(ID,name),vec.x,vec.y);
    }
    
    public void setVector3f(String name, Vector3f vec)
    {
    	glUniform3f(glGetUniformLocation(ID,name),vec.x,vec.y,vec.z);
    }
    
    public void setVector4f(String name, Vector4f vec)
    {
    	glUniform4f(glGetUniformLocation(ID,name),vec.x,vec.y,vec.z,vec.w);
    }
    
    public void setMatrix4(String name, Matrix4f mat)
    {
    	FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
    	mat.get(matrixBuffer);
    	
    	glUniformMatrix4fv(glGetUniformLocation(ID,name),false,matrixBuffer);
    }
    
    private static String load(InputStream i)
    {	
    	String s = "";
    	String line;
    	try {
			BufferedReader br = new BufferedReader(new InputStreamReader(i));
			while ((line = br.readLine()) != null)
			{
				s += line + "\n";
			}
			
			br.close();
			i.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return s; 
    }
}
