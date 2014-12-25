//#extension GL_OES_EGL_image_external : require
precision highp float;
uniform sampler2D u_img;
uniform float dx, dy;
varying vec2 tex_coord;

void main() {
	vec2 tx = tex_coord;
	
	vec4 c2, c1, c = texture2D(u_img, vec2(tx.x, tx.y)), c_1, c_2;
	
		c2 = texture2D(u_img, vec2(tx.x+2.0*dx, tx.y));
		c1 = texture2D(u_img, vec2(tx.x+1.0*dx, tx.y));
		c_1 = texture2D(u_img, vec2(tx.x-1.0*dx, tx.y));
		c_2 = texture2D(u_img, vec2(tx.x-2.0*dx, tx.y));
	
	gl_FragColor = ((c2+c_2)*0.028087+(c1+c_1)*0.23431+c*0.475207);
}