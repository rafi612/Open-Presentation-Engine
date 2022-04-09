#version 330 core

layout (location = 0) in vec4 aPos;

uniform mat4 model;
uniform mat4 projection;

uniform vec4 col1;
uniform vec4 col2;

uniform vec2 dimension_;

out vec4 color1;
out vec4 color2;
out vec2 dimension;

void main()
{
	color1 = col1;
	color2 = col2;
	dimension = dimension_;
    gl_Position = projection * model * aPos;
}
