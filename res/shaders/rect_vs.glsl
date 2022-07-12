#version 130

in vec4 verticies;

uniform mat4 transformMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * transformMatrix * verticies;
}