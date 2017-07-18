#version 330

in vec2 texturePos0;

out vec4 fragColor;

uniform vec3 ambientIntensity;
uniform sampler2D sampler;

void main()
{
  fragColor = texture(sampler, texturePos0.xy) * vec4(ambientIntensity, 1);
}
