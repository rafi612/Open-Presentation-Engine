#version 330 core

in vec4 color1;
in vec4 color2;
in vec2 dimension;

out vec4 FragColor;

void main(void)
{	
 	//float a = gl_FragCoord.y / dimension.y;
	//gl_FragColor = mix(color1, color2,a);
	gl_FragColor = color1 * (gl_FragCoord.y / dimension.y) + color2 * (1.4 - (gl_FragCoord.y / dimension.y));
}