#extension GL_OES_EGL_image_external : require
precision lowp float;
uniform samplerExternalOES u_img;//sampler2D a, b;
uniform float dx, dy;
varying vec2 tex_coord;

#define M_PI 3.1415

void main() {
	vec2 t = tex_coord;
	
	//float Ix = texture2D(a, t).r;
	//float Iy = texture2D(b, t).r;
	
	vec4 c1v = texture2D(u_img, vec2(t.x+1.0*dx,t.y));
	vec4 c_1v = texture2D(u_img, vec2(t.x-1.0*dx,t.y));
	float c1 = (c1v.x*0.299+c1v.y*0.587+c1v.z*0.114);
	float c_1 = (c_1v.x*0.299+c_1v.y*0.587+c_1v.z*0.114);
	float Ix = abs(c1-c_1);
		
	c1v = texture2D(u_img, vec2(t.x,t.y+1.0*dy));
	c_1v = texture2D(u_img, vec2(t.x,t.y-1.0*dy));
	c1 = (c1v.x*0.299+c1v.y*0.587+c1v.z*0.114);
	c_1 = (c_1v.x*0.299+c_1v.y*0.587+c_1v.z*0.114);
	float Iy = abs(c1-c_1);
	
	float angle = 0.0;
	if(Ix!=0.0) {
		angle = atan(Iy/Ix); // (-pi/2; pi/2) + pi/2 -> (0; pi)
		if(angle<0.0) angle=angle+M_PI;
	} else {
		angle = M_PI/2.0;
	}
	
	float Mc = angle/M_PI;
	
	gl_FragColor = vec4(Mc, Mc, Mc, 1.0);
}