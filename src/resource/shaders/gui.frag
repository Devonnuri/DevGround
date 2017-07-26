#version 120

uniform sampler2D sampler;

uniform vec4 color;

varying vec2 textureCoords;

void main() {
    gl_FragColor = color;
}