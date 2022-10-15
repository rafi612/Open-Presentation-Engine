package com.ope.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.stream.Collectors;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public class Shader 
{
	int ID;

	public Shader(String vertexpath,String fragmentpath)
	{
		String vShaderCode = read(Shader.class.getResourceAsStream(vertexpath));
		String fShaderCode = read(Shader.class.getResourceAsStream(fragmentpath));
		
		int vertex, fragment;
		
		// vertex Shader
		vertex = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertex, vShaderCode);
		glCompileShader(vertex);
		
		if (glGetShaderi(vertex, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.out.println("===========" + vertexpath + "===========");
			String infoLog = glGetShaderInfoLog(vertex, glGetShaderi(vertex, GL_INFO_LOG_LENGTH));
			System.out.println("Vertex Error:" + infoLog);
		}
		
		
		//fragment shader
		fragment = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragment, fShaderCode);
		glCompileShader(fragment);
		
		if (glGetShaderi(fragment, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.out.println("===========" + fragmentpath + "===========");
			String infoLog = glGetShaderInfoLog(fragment, glGetShaderi(fragment, GL_INFO_LOG_LENGTH));
			System.out.println("Fragment Error:" + infoLog);
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
		try (MemoryStack stack = MemoryStack.stackPush())
		{
			FloatBuffer matrixBuffer = stack.mallocFloat(16);
			mat.get(matrixBuffer);
			
			glUniformMatrix4fv(glGetUniformLocation(ID,name),false,matrixBuffer);
		}
	}
	
	private static String read(InputStream i)
	{	
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(i)))
		{
			return buffer.lines().collect(Collectors.joining("\n"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return "";
		}
	}
}
