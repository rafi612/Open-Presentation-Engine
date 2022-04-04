#version 330 core
layout (location = 0) in vec4 aPos;

uniform mat4 model;
uniform mat4 projection;

uniform vec4 rectColor;
out vec4 col;

void main()
{
	col = rectColor;
    gl_Position = projection * model * aPos;
}