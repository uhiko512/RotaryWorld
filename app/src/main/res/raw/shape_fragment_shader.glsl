precision mediump float;

uniform vec3 u_LightPos;

varying vec3 v_Position;
varying vec4 v_Color;
varying vec3 v_Normal;

void main() {
    float distance = length(u_LightPos - v_Position);

    vec3 lightVector = normalize(u_LightPos - v_Position);
    float diffuse = max(dot(v_Normal, lightVector), 0.1);

    //diffuse = diffuse * (1.0 / distance);

    diffuse = diffuse + 0.35;

    gl_FragColor = vec4(v_Color.rg * diffuse, v_Color.b * diffuse * 0.97, v_Color.a * diffuse);
}