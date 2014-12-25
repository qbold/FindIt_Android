attribute vec2 pos;
attribute vec2 texture;
varying vec2 tex_coord;

void main() {
	tex_coord = texture;
	gl_Position = vec4(pos.x, pos.y, 0.0, 1.0);
}