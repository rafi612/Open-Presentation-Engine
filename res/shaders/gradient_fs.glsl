#version 330 core

uniform vec4 color1;
uniform vec4 color2;
uniform vec2 size;

out vec4 FragColor;

void main(void)
{	
 	//float a = gl_FragCoord.y / dimension.y;
	//gl_FragColor = mix(color1, color2,a);
	FragColor = color1 * (gl_FragCoord.y / size.y) + color2 * (1.4 - (gl_FragCoord.y / size.y));
}