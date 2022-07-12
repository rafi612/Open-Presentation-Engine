#version 130

in vec2 verticies;
in vec2 texCoords;

out vec2 processedTexCoords;

uniform mat4 transformMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main()
{
    processedTexCoords = texCoords;
    gl_Position = projectionMatrix * viewMatrix * transformMatrix * vec4(verticies, 0.0, 1.0);
}