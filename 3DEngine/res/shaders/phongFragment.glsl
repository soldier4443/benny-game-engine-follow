#version 330

in vec2 texturePos0;

uniform vec3 baseColor;
uniform vec3 ambientLight;
uniform sampler2D sampler;

void main()
{
  vec4 textureColor = texture2D(sampler, texturePos0.xy);
  vec4 totalLight = vec4(ambientLight, 1);
  vec4 color = vec4(baseColor, 1);

  if (textureColor != vec4(0, 0, 0, 0))
    color *= textureColor;

  gl_FragColor = color * totalLight;
}
