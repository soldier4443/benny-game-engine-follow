#version 330

in vec2 texturePos0;

uniform sampler2D sampler;

void main()
{
  gl_FragColor = texture2D(sampler, texturePos0.xy);
}
