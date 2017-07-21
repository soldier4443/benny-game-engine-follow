#version 330
#include "lighting.header.glsl"

in vec2 texturePos0;
in vec3 normal0;
in vec3 worldPos0;

out vec4 fragColor;

uniform sampler2D diffuse;
uniform PointLight pointLight;

void main()
{
  fragColor = texture(diffuse, texturePos0.xy) * calculatePointLight(pointLight, normalize(normal0), worldPos0);
}
