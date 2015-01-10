#extension GL_OES_EGL_image_external : require
precision lowp float;
uniform samplerExternalOES u_img;
uniform float dx, dy;
varying vec2 tex_coord;

/*
	Детектор Харриса
*/

void main() {
	vec2 t = tex_coord;
	
	vec4 c = texture2D(u_img, t);
	float c_ = 0.299*c.r+0.587*c.g+0.114*c.b;
	
	vec4 cx2 = texture2D(u_img, vec2(t.x+dx,t.y));
	float cx2_ = 0.299*cx2.r+0.587*cx2.g+0.114*cx2.b;
	
	vec4 cx1 = texture2D(u_img, vec2(t.x-dx,t.y));
	float cx1_ = 0.299*cx1.r+0.587*cx1.g+0.114*cx1.b;
	
	vec4 cy2 = texture2D(u_img, vec2(t.x,t.y+dy));
	float cy2_ = 0.299*cy2.r+0.587*cy2.g+0.114*cy2.b;
	
	vec4 cy1 = texture2D(u_img, vec2(t.x,t.y-dy));
	float cy1_ = 0.299*cy1.r+0.587*cy1.g+0.114*cy1.b;
	
	vec4 cxy2 = texture2D(u_img, vec2(t.x+dx,t.y+dy));
	float cxy2_ = 0.299*cxy2.r+0.587*cxy2.g+0.114*cxy2.b;
	
	vec4 cxy1 = texture2D(u_img, vec2(t.x-dx,t.y-dy));
	float cxy1_ = 0.299*cxy1.r+0.587*cxy1.g+0.114*cxy1.b;
	
	float Ix = cx2_-cx1_;//2.0*c_-cx2_-cx1_;
	float Iy = cy2_-cy1_;//2.0*c_-cy2_-cy1_;
	float Ixy = cxy2_-cxy1_;//2.0*c_-cxy2_-cxy1_;
	
	float y1 = Ix*Ix; // lambda1
	float y2 = Iy*Iy; // lambda2
	float trace = (Ix+Iy); // trace(M)
	
	float Mc = y1*y2-Ixy*Ixy - 0.09*trace*trace;
	
	//Mc=Mc/10.0;
	//Mc-=0.2;
	Mc=abs(Mc);
	//Mc=Mc*2.0;
	
	//while(Mc < 0.0) Mc += 1.0;
	//while(Mc > 1.0) Mc -= 1.0;
	
	//Mc=1.0-Mc;
	
	gl_FragColor = vec4(Mc, Mc, Mc, 1.0);
}