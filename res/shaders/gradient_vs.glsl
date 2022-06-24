#version 330 core

layout (location = 0) in vec4 verticies;

uniform mat4 transformMatrix;
uniform mat4 projectionMatrix;

void main()
{
    gl_Position = projectionMatrix * transformMatrix * verticies;
}
