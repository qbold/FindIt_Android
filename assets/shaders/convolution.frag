#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES u_img;
varying vec2 tex_coord;
uniform mat3 convolution;

/*
	—вЄртка
*/

void main() {
	mat3 mt = mat3(0.0, 1.0, 0.0,
		 1.0, -5.0, 1.0,
		 0.0, 1.0, 0.0);
	vec2 tx = tex_coord;
	const int N = 3;
	float div1 = 0.0;
	float r = 0.0;
	float dv = 1.0; // N/2

	for(int i = 0; i < 3; i++) {
		for(int j = 0; j < 3; j++) {
			vec2 ps = vec2(tx.x - (dv - float(i))/50.0, tx.y - (dv - float(j))/50.0);
			vec4 color = texture2D(u_img, ps);

			float fr = 0.0;
			if(i==0) {
				if(j==0) {
					fr = mt[0][0];
				} else if(j==1) {
					fr = mt[0][1];
				} else if(j==2) {
					fr = mt[0][2];
				}
			} else if(i==1) {
				if(j==0) {
					fr = mt[1][0];
				} else if(j==1) {
					fr = mt[1][1];
				} else if(j==2) {
					fr = mt[1][2];
				}
			} else if(i==2) {
				if(j==0) {
					fr = mt[2][0];
				} else if(j==1) {
					fr = mt[2][1];
				} else if(j==2) {
					fr = mt[2][2];
				}
			}

			r += fr * (color.x + color.y + color.z);
			div1 += abs(fr);
		}
	}
	div1 *= 3.0;
	r /= div1;
	
	while(r < 0.0) r += 1.0;
	while(r > 1.0) r -= 1.0;
	gl_FragColor = vec4(r, r, r, 1.0);
}