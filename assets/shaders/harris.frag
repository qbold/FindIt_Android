#extension GL_OES_EGL_image_external : require
precision lowp float;
uniform samplerExternalOES u_img;
uniform float dx, dy;
varying vec2 tex_coord;

void main() {
	vec2 t = tex_coord;
	
	vec4 c2v = texture2D(u_img, vec2(t.x+2.0*dx,t.y));
	vec4 c1v = texture2D(u_img, vec2(t.x+1.0*dx,t.y));
	vec4 c_2v = texture2D(u_img, vec2(t.x-2.0*dx,t.y));
	vec4 c_1v = texture2D(u_img, vec2(t.x-1.0*dx,t.y));
	
	float c2 = (c2v.x+c2v.y+c2v.z)/3.0;
	float c1 = (c1v.x+c1v.y+c1v.z)/3.0;
	float c_2 = (c_2v.x+c_2v.y+c_2v.z)/3.0;
	float c_1 = (c_1v.x+c_1v.y+c_1v.z)/3.0;
	float Ix = (2.0*c2+c1-c_1-2.0*c_2)/6.0;
	
	c2v = texture2D(u_img, vec2(t.x,t.y+2.0*dy));
	c1v = texture2D(u_img, vec2(t.x,t.y+1.0*dy));
	c_2v = texture2D(u_img, vec2(t.x,t.y-2.0*dy));
	c_1v = texture2D(u_img, vec2(t.x,t.y-1.0*dy));
	
	c2 = (c2v.x+c2v.y+c2v.z)/3.0;
	c1 = (c1v.x+c1v.y+c1v.z)/3.0;
	c_2 = (c_2v.x+c_2v.y+c_2v.z)/3.0;
	c_1 = (c_1v.x+c_1v.y+c_1v.z)/3.0;
	float Iy = (2.0*c2+c1-c_1-2.0*c_2)/6.0;
	
	c2v = texture2D(u_img, vec2(t.x+2.0*dx,t.y+2.0*dy));
	c1v = texture2D(u_img, vec2(t.x+1.0*dx,t.y+1.0*dy));
	c_2v = texture2D(u_img, vec2(t.x-2.0*dx,t.y-2.0*dy));
	c_1v = texture2D(u_img, vec2(t.x-1.0*dx,t.y-1.0*dy));
	
	c2 = (c2v.x+c2v.y+c2v.z)/3.0;
	c1 = (c1v.x+c1v.y+c1v.z)/3.0;
	c_2 = (c_2v.x+c_2v.y+c_2v.z)/3.0;
	c_1 = (c_1v.x+c_1v.y+c_1v.z)/3.0;
	float Ixy = (2.0*c2+c1-c_1-2.0*c_2)/6.0;
	
	float y1 = Ix*Ix; // lambda1
	float y2 = Iy*Iy; // lambda2
	float trace = (Ix+Iy); // trace(M)
	
	float Mc = y1*y2-Ixy*Ixy - 0.04*trace*trace;
	
	//Mc=Mc/10.0;
	Mc-=0.2;
	
	while(Mc < 0.0) Mc += 1.0;
	while(Mc > 1.0) Mc -= 1.0;
	
	Mc=1.0-Mc;
	
	gl_FragColor = vec4(Mc, Mc, Mc, 1.0);
}