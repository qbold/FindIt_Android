//#extension GL_OES_EGL_image_external : require
precision lowp float;
uniform sampler2D u_img;
uniform float dx, dy;
varying vec2 tex_coord;

/*
	Ёрози€
*/

void main() {
	vec2 tx = tex_coord;
	
	for(float i=-1.0*dx; i<1.0*dx; i+=dx) {
		for(float j=-1.0*dy; j<1.0*dy; j+=dy) {
			if(i==0.0&&j==0.0) continue;
			vec4 cl = texture2D(u_img, vec2(tx.x+i, tx.y+j));
			if(cl.r<1.0) {
				gl_FragColor = vec4(0.0,0.0,0.0,1.0);
				return;
			}
		}
	}

	gl_FragColor = vec4(1.0,1.0,1.0,1.0);
}