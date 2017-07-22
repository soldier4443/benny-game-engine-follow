uniform vec3 C_eyePos;
uniform float specularIntensity;
uniform float specularPower;

struct BaseLight
{
  vec3 color;
  float intensity;
};

struct Attenuation
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
  float range;
};

struct SpotLight
{
  PointLight pointLight;
  vec3 direction;
  float cutoff;
};

struct DirectionalLight
{
  BaseLight base;
  vec3 direction;
};

vec4 calculateLight(BaseLight base, vec3 direction, vec3 normal, vec3 worldPos)
{
  float diffuseFactor = dot(normal, -direction);

  vec4 diffuseColor = vec4(0, 0, 0, 0);
  vec4 specularColor = vec4(0, 0, 0, 0);

  if (diffuseFactor > 0)
  {
    diffuseColor = vec4(base.color, 1.0) * base.intensity * diffuseFactor;

    vec3 directionToEye = normalize(C_eyePos - worldPos);
    // vec3 reflectDirection = normalize(reflect(direction, normal));
    vec3 halfDirection = normalize(directionToEye - direction);

    float specularFactor = dot(halfDirection, normal);
    // float specularFactor = dot(directionToEye, reflectDirection);
    specularFactor = pow(specularFactor, specularPower);

    if (specularFactor > 0)
    {
      specularColor = vec4(base.color, 1.0) * specularIntensity *  specularFactor;
    }
  }

  return diffuseColor + specularColor;
}

vec4 calculatePointLight(PointLight pl, vec3 normal, vec3 worldPos)
{
  vec3 lightDirection = worldPos - pl.position;
  float distanceToPoint = length(lightDirection);

  if (distanceToPoint > pl.range)
    return vec4(0, 0, 0, 0);

  lightDirection = normalize(lightDirection);

  vec4 color = calculateLight(pl.base, lightDirection, normal, worldPos);

  float attenuation = pl.attenuation.constant +
                      pl.attenuation.linear * distanceToPoint +
                      pl.attenuation.exponent * distanceToPoint * distanceToPoint +
                      0.0001; // Prevent div by zero

  return color / attenuation;
}

vec4 calculateSpotLight(SpotLight sl, vec3 normal, vec3 worldPos)
{
  vec3 lightDirection = normalize(worldPos - sl.pointLight.position);
  float spotFactor = dot(lightDirection, sl.direction);

  vec4 color = vec4(0, 0, 0, 0);

  if (spotFactor > sl.cutoff)
  {
    color = calculatePointLight(sl.pointLight, normal, worldPos) *
            (1.0 - (1.0 - spotFactor) / (1.0 - sl.cutoff));
            // Darker as getting closer to the edge
  }

  return color;
}

vec4 calculateDirectionalLight(DirectionalLight dl, vec3 normal, vec3 worldPos)
{
  return calculateLight(dl.base, -dl.direction, normal, worldPos);
}
