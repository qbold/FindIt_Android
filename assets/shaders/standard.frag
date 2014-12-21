#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES u_img;
varying vec2 tex_coord;

void main() {
	gl_FragColor = texture2D(u_img, tex_coord);
}