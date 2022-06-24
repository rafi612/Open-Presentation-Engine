#version 330 core

layout (location = 0) in vec2 verticies;
layout (location = 1) in vec2 texCoords;

out vec2 processedTexCoords;

uniform mat4 transformMatrix;
uniform mat4 projectionMatrix;

void main()
{
    processedTexCoords = texCoords;
    gl_Position = projectionMatrix * transformMatrix * vec4(verticies, 0.0, 1.0);
}