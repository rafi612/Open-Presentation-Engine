#version 130

in vec2 processedTexCoords;

out vec4 fragColor;

uniform sampler2D image;

void main()
{    
    fragColor = texture(image, processedTexCoords);
}