#extension GL_OES_EGL_image_external : require
precision lowp float;
uniform samplerExternalOES u_img;
uniform float dx, dy;
varying vec2 tex_coord;

void main() {
	gl_FragColor = texture2D(u_img, tex_coord);
}