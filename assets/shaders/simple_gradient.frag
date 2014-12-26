#extension GL_OES_EGL_image_external : require
precision lowp float;
uniform samplerExternalOES u_img;
uniform float dx, dy;
uniform float type;
varying vec2 tex_coord;

void main() {
	vec2 t = tex_coord;
	
	float Mc=0.0;
	
	if(type==0.0) {
		vec4 c2v = texture2D(u_img, vec2(t.x+2.0*dx,t.y+2.0*dy));
		vec4 c1v = texture2D(u_img, vec2(t.x+1.0*dx,t.y+1.0*dy));
		vec4 c_2v = texture2D(u_img, vec2(t.x-2.0*dx,t.y-2.0*dy));
		vec4 c_1v = texture2D(u_img, vec2(t.x-1.0*dx,t.y-1.0*dy));
	
		float c2 = (c2v.x+c2v.y+c2v.z)/3.0;
		float c1 = (c1v.x+c1v.y+c1v.z)/3.0;
		float c_2 = (c_2v.x+c_2v.y+c_2v.z)/3.0;
		float c_1 = (c_1v.x+c_1v.y+c_1v.z)/3.0;
		float Ixy = (2.0*c2+c1-c_1-2.0*c_2)/6.0;
	
		Mc = Ixy*20.0;
	} else if(type==1.0) {
		vec4 c2v = texture2D(u_img, vec2(t.x+2.0*dx,t.y));
		vec4 c1v = texture2D(u_img, vec2(t.x+1.0*dx,t.y));
		vec4 c_2v = texture2D(u_img, vec2(t.x-2.0*dx,t.y));
		vec4 c_1v = texture2D(u_img, vec2(t.x-1.0*dx,t.y));
	
		float c2 = (c2v.x+c2v.y+c2v.z)/3.0;
		float c1 = (c1v.x+c1v.y+c1v.z)/3.0;
		float c_2 = (c_2v.x+c_2v.y+c_2v.z)/3.0;
		float c_1 = (c_1v.x+c_1v.y+c_1v.z)/3.0;
		float Ix = (2.0*c2+c1-c_1-2.0*c_2)/6.0;
	
		Mc = Ix*20.0;
	} else if(type==1.0) {
		vec4 c2v = texture2D(u_img, vec2(t.x,t.y+2.0*dy));
		vec4 c1v = texture2D(u_img, vec2(t.x,t.y+1.0*dy));
		vec4 c_2v = texture2D(u_img, vec2(t.x,t.y-2.0*dy));
		vec4 c_1v = texture2D(u_img, vec2(t.x,t.y-1.0*dy));
	
		float c2 = (c2v.x+c2v.y+c2v.z)/3.0;
		float c1 = (c1v.x+c1v.y+c1v.z)/3.0;
		float c_2 = (c_2v.x+c_2v.y+c_2v.z)/3.0;
		float c_1 = (c_1v.x+c_1v.y+c_1v.z)/3.0;
		float Iy = (2.0*c2+c1-c_1-2.0*c_2)/6.0;
	
		Mc = Iy*20.0;
	}
	
	//while(Mc < 0.0) Mc += 1.0;
	//while(Mc > 1.0) Mc -= 1.0;
	
	gl_FragColor = vec4(Mc, Mc, Mc, 1.0);
}