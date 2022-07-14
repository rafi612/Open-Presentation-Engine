#version 130

in vec2 processedTexCoords;

out vec4 fragColor;

uniform sampler2D image;
uniform sampler2D image2;

uniform float mixAmount;

void main()
{
	vec4 color1 = texture(image, processedTexCoords);
	vec4 color2 = texture(image2, processedTexCoords);
    fragColor = mix(color1,color2,mixAmount);
}