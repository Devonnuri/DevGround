#version 120

attribute vec3 vertices;
attribute vec2 textures;

varying vec2 textureCoords;

uniform mat4 projection;
uniform mat4 texModifier;

void main() {
    textureCoords = (texModifier * vec4(textures, 0, 1)).xy;
    gl_Position = projection * vec4(vertices, 1);
}