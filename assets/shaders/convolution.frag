#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES u_img;
varying vec2 tex_coord;
uniform mat3 convolution;

void main() {
	vec2 tx = tex_coord;
	const int N = 3;
	float div=float(0),r=float(0);
	vec4 color;
	vec2 ps;
	float dv = float(1); // N/2
	int x, y;
	for(x=0;x<N;x++) {
		for(y=0;y<N;y++) {
			ps = vec2(tx.x-dv+float(x), tx.y-dv+float(y));
			color = texture2D(u_img, ps);

			r += convolution[x][y]*(color.r+color.g+color.b);
			div += abs(convolution[x][y]);
		}
	}
	
	r /= float(3);
	r /= div;

	if(r < 0.0) r=0.0;
	if(r > 1.0) r=1.0;
	

	gl_FragColor = vec4(r, convolution[0][1], convolution[0][2], 1);
}