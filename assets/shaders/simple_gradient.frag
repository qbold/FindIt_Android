#extension GL_OES_EGL_image_external : require
precision lowp float;
uniform samplerExternalOES u_img;
uniform float dx, dy;
uniform float type;
varying vec2 tex_coord;

/*
	Просто(й) градиент, а точнее обобщённый. При type = 0 возвращает Ixy, 1 - Ix, 2 - Iy
*/

void main() {
	vec2 t = tex_coord;
	
	float Mc=0.0;
	
	if(type==0.0) {
		//vec4 c2v = texture2D(u_img, vec2(t.x+2.0*dx,t.y+2.0*dy));
		vec4 c1v = texture2D(u_img, vec2(t.x+1.0*dx,t.y+1.0*dy));
		//vec4 c_2v = texture2D(u_img, vec2(t.x-2.0*dx,t.y-2.0*dy));
		vec4 c_1v = texture2D(u_img, vec2(t.x-1.0*dx,t.y-1.0*dy));
	
		//float c2 = (c2v.x+c2v.y+c2v.z)/3.0;
		float c1 = (c1v.x*0.299+c1v.y*0.587+c1v.z*0.114);
		//float c_2 = (c_2v.x+c_2v.y+c_2v.z)/3.0;
		float c_1 = (c_1v.x*0.299+c_1v.y*0.587+c_1v.z*0.114);
		float Ix = c1-c_1;//(2.0*c2+c1-c_1-2.0*c_2)/6.0;
	
		Mc = Ix;
	} else if(type==1.0) {
		//vec4 c2v = texture2D(u_img, vec2(t.x+2.0*dx,t.y));
		vec4 c1v = texture2D(u_img, vec2(t.x+1.0*dx,t.y));
		//vec4 c_2v = texture2D(u_img, vec2(t.x-2.0*dx,t.y));
		vec4 c_1v = texture2D(u_img, vec2(t.x-1.0*dx,t.y));
	
		//float c2 = (c2v.x+c2v.y+c2v.z)/3.0;
		float c1 = (c1v.x*0.299+c1v.y*0.587+c1v.z*0.114);
		//float c_2 = (c_2v.x+c_2v.y+c_2v.z)/3.0;
		float c_1 = (c_1v.x*0.299+c_1v.y*0.587+c_1v.z*0.114);
		float Ix = c1-c_1;//(2.0*c2+c1-c_1-2.0*c_2)/6.0;
	
		Mc = Ix;
	} else if(type==2.0) {
		//vec4 c2v = texture2D(u_img, vec2(t.x,t.y+2.0*dy));
		vec4 c1v = texture2D(u_img, vec2(t.x,t.y+1.0*dy));
		//vec4 c_2v = texture2D(u_img, vec2(t.x,t.y-2.0*dy));
		vec4 c_1v = texture2D(u_img, vec2(t.x,t.y-1.0*dy));
	
		//float c2 = (c2v.x+c2v.y+c2v.z)/3.0;
		float c1 = (c1v.x*0.299+c1v.y*0.587+c1v.z*0.114);
		//float c_2 = (c_2v.x+c_2v.y+c_2v.z)/3.0;
		float c_1 = (c_1v.x*0.299+c_1v.y*0.587+c_1v.z*0.114);
		float Iy = c1-c_1;//(2.0*c2+c1-c_1-2.0*c_2)/6.0;
	
		Mc = Iy;
	}
	
	//while(Mc < 0.0) Mc += 1.0;
	//while(Mc > 1.0) Mc -= 1.0;
	
	gl_FragColor = vec4(Mc, Mc, Mc, 1.0);
}