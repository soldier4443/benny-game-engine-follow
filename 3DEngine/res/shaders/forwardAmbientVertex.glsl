#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texturePosition;

out vec2 texturePos0;

uniform mat4 MVP;

void main()
{
    gl_Position = MVP * vec4(position, 1.0);
    texturePos0 = texturePosition;
}
