package com.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh 
{
	public int VAO,EBO;
	
	public int indices_size;
	
	public Mesh(float[] vertices,int[] indices)
	{
	    VAO = glGenVertexArrays();
	    EBO = glGenBuffers();
	    
	    int VBO = glGenBuffers();
	    
	    indices_size = indices.length;
	    
	    glBindBuffer(GL_ARRAY_BUFFER, VBO);
	    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
	    
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
	    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW); 
	    
	    glBindVertexArray(VAO);
	    
	    storeInAttributes(0, 3, vertices);
	    
	    glBindVertexArray(0);
	}
	
	public void storeInAttributes(int attrib,int length, float[] data)
	{
		int VBO = glGenBuffers();
		glBindVertexArray(VAO);
		
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
	    glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		
	    glVertexAttribPointer(attrib, length, GL_FLOAT, false, length * 4, 0);
	    glEnableVertexAttribArray(attrib);
	}
	
	public void draw()
	{
		glBindVertexArray(VAO);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
	    glDrawElements(GL_TRIANGLES,indices_size,GL_UNSIGNED_INT,0);
	    
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	    glBindVertexArray(0);
	}

}
