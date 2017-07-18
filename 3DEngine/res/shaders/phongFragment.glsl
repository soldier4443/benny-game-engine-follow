#version 330

const int MAX_POINT_LIGHTS = 4;

in vec2 texturePos0;
in vec3 normal0;
in vec3 worldPos0;

out vec4 fragColor;

struct BaseLight
{
  vec3 color;
  float intensity;
};

struct DirectionalLight
{
  BaseLight base;
  vec3 direction;
};

struct Attenuation  // How quickly point lights disappear (fading out..)
{
  float constant;
  float linear;
  float exponent;
};

struct PointLight
{
  BaseLight base;
  Attenuation attenuation;
  vec3 position;
};

uniform vec3 eyePos;

uniform vec3 baseColor;
uniform vec3 ambientLight;
uniform sampler2D sampler;

uniform float specularIntensity;
uniform float specularPower;

uniform DirectionalLight directionalLight;
uniform PointLight pointLights[MAX_POINT_LIGHTS];

vec4 calculateLight(BaseLight base, vec3 direction, vec3 normal)
{
  float diffuseFactor = dot(normal, -direction);

  vec4 diffuseColor = vec4(0, 0, 0, 0);
  vec4 specularColor = vec4(0, 0, 0, 0);

  if (diffuseFactor > 0)
  {
    diffuseColor = vec4(base.color, 1.0) * base.intensity * diffuseFactor;

    vec3 directionToEye = normalize(eyePos - worldPos0);
    vec3 reflectDirection = normalize(reflect(direction, normal));

    float specularFactor = dot(directionToEye, reflectDirection);
    specularFactor = pow(specularFactor, specularPower);

    if (specularFactor > 0)
    {
      specularColor = vec4(base.color, 1.0) * specularIntensity *  specularFactor;
    }
  }

  return diffuseColor + specularColor;
}

vec4 calculateDirectionalLight(DirectionalLight dl, vec3 normal)
{
  return calculateLight(dl.base, -dl.direction, normal);
}

vec4 calculatePointLight(PointLight pl, vec3 normal)
{
  vec3 lightDirection = worldPos0 - pl.position;
  float distanceToPoint = length(lightDirection);
  lightDirection = normalize(lightDirection);

  vec4 color = calculateLight(pl.base, lightDirection, normal);

  float attenuation = pl.attenuation.constant +
                      pl.attenuation.linear * distanceToPoint +
                      pl.attenuation.exponent * distanceToPoint * distanceToPoint +
                      0.0001; // Prevent div by zero

  return color / attenuation;
}

void main()
{
  vec4 textureColor = texture2D(sampler, texturePos0.xy);
  vec4 totalLight = vec4(ambientLight, 1);
  vec4 color = vec4(baseColor, 1);

  if (textureColor != vec4(0, 0, 0, 0))
    color *= textureColor;

  vec3 normal = normalize(normal0);

  totalLight += calculateDirectionalLight(directionalLight, normal);

  for (int i = 0; i < MAX_POINT_LIGHTS; i++)
    totalLight += calculatePointLight(pointLights[i], normal);

  fragColor = color * totalLight;
}
