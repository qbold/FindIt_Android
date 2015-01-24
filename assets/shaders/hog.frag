#extension GL_OES_EGL_image_external : require
precision lowp float;
uniform samplerExternalOES u_img;//sampler2D a, b;
uniform float dx, dy;
varying vec2 tex_coord;

#define M_PI 3.1415

/*
	√истограмма ориентированных градиентов (возвращает значение 0..255 (в шейдере 0..1), соответствующее углу направлени€ вектора градиента (»з (0; 359) вз€ли да сделали (0; 255)))
*/

float grayscale(vec4 v) {
	return v.x*0.299+v.y*0.587+v.z*0.114;
}

void main() {
	vec2 t = tex_coord;
	
	//float Ix = texture2D(a, t).r;
	//float Iy = texture2D(b, t).r;
	
	vec4 c1v = texture2D(u_img, vec2(t.x+1.0*dx,t.y));
	vec4 c_1v = texture2D(u_img, vec2(t.x-1.0*dx,t.y));
	vec4 cv = texture2D(u_img, t);
	float c1 = (c1v.x*0.299+c1v.y*0.587+c1v.z*0.114);
	float c_1 = (c_1v.x*0.299+c_1v.y*0.587+c_1v.z*0.114);
	float cv1 = (cv.x*0.299+cv.y*0.587+cv.z*0.114);
	
	float Ix = c1-c_1;
	
	c1v = texture2D(u_img, vec2(t.x,t.y+1.0*dy));
	c_1v = texture2D(u_img, vec2(t.x,t.y-1.0*dy));
	cv = texture2D(u_img, t);
	c1 = (c1v.x*0.299+c1v.y*0.587+c1v.z*0.114);
	c_1 = (c_1v.x*0.299+c_1v.y*0.587+c_1v.z*0.114);
	cv1 = (cv.x*0.299+cv.y*0.587+cv.z*0.114);
	float Iy = c1-c_1;
	
	if(Ix*Ix+Iy*Iy<0.225) { Ix=0.0; Iy=0.0; }
	
	/*vec4 v_1_1 = texture2D(u_img, vec2(t.x-dx,t.y-dy));
	vec4 v11 = texture2D(u_img, vec2(t.x+dx,t.y+dy));
	vec4 v0_1 = texture2D(u_img, vec2(t.x,t.y-dy));
	vec4 v01 = texture2D(u_img, vec2(t.x,t.y+dy));
	vec4 v1_1 = texture2D(u_img, vec2(t.x+dx,t.y-dy));
	vec4 v_11 = texture2D(u_img, vec2(t.x-dx,t.y+dy));
	vec4 v_10 = texture2D(u_img, vec2(t.x-dx,t.y));
	vec4 v10 = texture2D(u_img, vec2(t.x+dx,t.y));
	
	float c_1_1 = grayscale(v_1_1);
	float c11 = grayscale(v11);
	float c0_1 = 2.0*grayscale(v0_1);
	float c01 = 2.0*grayscale(v01);
	float c1_1 = grayscale(v1_1);
	float c_11 = grayscale(v_11);
	float c_10 = 2.0*grayscale(v_10);
	float c10 = 2.0*grayscale(v10);
	
	float Ix = c10+c11+c1_1-c_10-c_11-c_1_1;
	float Iy = c01+c_11+c11-c0_1-c_1_1-c1_1;*/
	
	float angle = 0.0;
	//if(Ix != 0.0) {
	
	if(Ix==0.0) angle = M_PI/2.0; else
	angle = atan(Iy, Ix);
	angle = angle + M_PI; // (-pi; pi) -> (0; 2*pi)
	//angle = 1.0/angle;
	
	//} else {
	//	angle = M_PI/2.0;
	//}
	
	float Mc = abs(angle/(2.0*M_PI));
	
	gl_FragColor = vec4(Mc, Mc, Mc, 1.0);
}