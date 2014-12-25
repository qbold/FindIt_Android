#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES u_img;
uniform float dx, dy;
varying vec2 tex_coord;

void main() {
	vec2 tx = tex_coord;

	vec3 a33 = texture2D(u_img, tx).rgb;
	
	tx.x-=dx;
	vec3 a23 = texture2D(u_img, tx).rgb;
	
	tx.x-=dx;
	vec3 a13 = texture2D(u_img, tx).rgb;
	
	tx.y-=dy;
	vec3 a12 = texture2D(u_img, tx).rgb;
	
	tx.y-=dy;
	vec3 a11 = texture2D(u_img, tx).rgb;
	
	tx.x+=dx;
	vec3 a21 = texture2D(u_img, tx).rgb;
	
	tx.x+=dx;
	vec3 a31 = texture2D(u_img, tx).rgb;
	
	tx.x+=dx;
	vec3 a41 = texture2D(u_img, tx).rgb;
	
	tx.x+=dx;
	vec3 a51 = texture2D(u_img, tx).rgb;
	
	tx.y+=dy;
	vec3 a52 = texture2D(u_img, tx).rgb;
	
	tx.y+=dy;
	vec3 a53 = texture2D(u_img, tx).rgb;
	
	tx.y+=dy;
	vec3 a54 = texture2D(u_img, tx).rgb;
	
	tx.y+=dy;
	vec3 a55 = texture2D(u_img, tx).rgb;
	
	tx.x-=dx;
	vec3 a45 = texture2D(u_img, tx).rgb;
	
	tx.x-=dx;
	vec3 a35 = texture2D(u_img, tx).rgb;
	
	tx.x-=dx;
	vec3 a25 = texture2D(u_img, tx).rgb;
	
	tx.x-=dx;
	vec3 a15 = texture2D(u_img, tx).rgb;
	
	tx.y-=dy;
	vec3 a14 = texture2D(u_img, tx).rgb;
	
	tx.x+=dx;
	vec3 a24 = texture2D(u_img, tx).rgb;
	
	tx.x+=dx;
	vec3 a34 = texture2D(u_img, tx).rgb;
	
	tx.x+=dx;
	vec3 a44 = texture2D(u_img, tx).rgb;
	
	tx.y-=dy;
	vec3 a43 = texture2D(u_img, tx).rgb;
	
	tx.y-=dy;
	vec3 a42 = texture2D(u_img, tx).rgb;
	
	tx.x-=dx;
	vec3 a32 = texture2D(u_img, tx).rgb;
	
	tx.x-=dx;
	vec3 a22 = texture2D(u_img, tx).rgb;
	
	gl_FragColor = vec4(a33*0.225821+0.111345*(a23+a32+a43+a34)+0.054901*(a22+a42+a44+a24)+0.006581*(a21+a12+a41+a52+a54+a14+a25+a45)+0.000789*(a11+a55+a51+a15), 1.0);
}