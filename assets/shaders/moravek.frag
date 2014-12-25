#extension GL_OES_EGL_image_external : require
precision lowp float;
uniform samplerExternalOES u_img;
uniform float dx, dy;
varying vec2 tex_coord;

float sr(vec4 s) {
	return (s.x+s.y+s.z)/3.0;
}

float delta(float a, float b) {
	float w2=a-b;
	return w2*w2;
}

void main() {
	float x = tex_coord.x;
	float y = tex_coord.y;
	float dx2 = 2.0*dx;
	float dy2 = 2.0*dy;

	float x1 = x-dx2,x2=x-dx,x3=x+dx,x4=x+dx2;
	float y1 = y-dy2,y2=y-dy,y3=y+dy,y4=y+dy2;
	float color11 = sr(texture2D(u_img, vec2(x1, y1)));
	float color12 = sr(texture2D(u_img, vec2(x1, y2)));
	float color13 = sr(texture2D(u_img, vec2(x1, y)));
	float color14 = sr(texture2D(u_img, vec2(x1, y3)));
	float color15 = sr(texture2D(u_img, vec2(x1, y4)));

	float color21 = sr(texture2D(u_img, vec2(x2, y1)));
	float color22 = sr(texture2D(u_img, vec2(x2, y2)));
	float color23 = sr(texture2D(u_img, vec2(x2, y)));
	float color24 = sr(texture2D(u_img, vec2(x2, y3)));
	float color25 = sr(texture2D(u_img, vec2(x2, y4)));

	float color31 = sr(texture2D(u_img, vec2(x, y1)));
	float color32 = sr(texture2D(u_img, vec2(x, y2)));
	float color33 = sr(texture2D(u_img, vec2(x, y)));
	float color34 = sr(texture2D(u_img, vec2(x, y3)));
	float color35 = sr(texture2D(u_img, vec2(x, y4)));

	float color41 = sr(texture2D(u_img, vec2(x3, y1)));
	float color42 = sr(texture2D(u_img, vec2(x3, y2)));
	float color43 = sr(texture2D(u_img, vec2(x3, y)));
	float color44 = sr(texture2D(u_img, vec2(x3, y3)));
	float color45 = sr(texture2D(u_img, vec2(x3, y4)));

	float color51 = sr(texture2D(u_img, vec2(x4, y1)));
	float color52 = sr(texture2D(u_img, vec2(x4, y2)));
	float color53 = sr(texture2D(u_img, vec2(x4, y)));
	float color54 = sr(texture2D(u_img, vec2(x4, y3)));
	float color55 = sr(texture2D(u_img, vec2(x4, y4)));

	float d1 = delta(color11, color22)+delta(color21, color32)+delta(color31,color42)+delta(color12,color23)+delta(color22, color33)+delta(color32, color43)+delta(color13, color24)+delta(color23, color34)+delta(color33, color44);
	float d2 = delta(color33, color22)+delta(color43, color32)+delta(color53,color42)+delta(color34,color23)+delta(color44, color55)+delta(color54, color43)+delta(color35, color24)+delta(color45, color34)+delta(color55, color44);

	float d3 = delta(color13, color22)+delta(color23, color32)+delta(color33,color42)+delta(color14,color23)+delta(color24, color33)+delta(color34, color43)+delta(color15, color24)+delta(color25, color34)+delta(color35, color44);
	float d4 = delta(color31, color22)+delta(color41, color32)+delta(color51,color42)+delta(color32,color23)+delta(color42, color33)+delta(color52, color43)+delta(color33, color24)+delta(color43, color34)+delta(color53, color44);

	float d5 = delta(color21, color22)+delta(color31, color32)+delta(color41,color42)+delta(color22,color23)+delta(color32, color33)+delta(color42, color43)+delta(color23, color24)+delta(color33, color34)+delta(color43, color44);
	float d6 = delta(color23, color22)+delta(color33, color32)+delta(color43,color42)+delta(color24,color23)+delta(color34, color33)+delta(color44, color43)+delta(color25, color24)+delta(color35, color34)+delta(color45, color44);

	float d7 = delta(color12, color22)+delta(color22, color32)+delta(color32,color42)+delta(color13,color23)+delta(color23, color33)+delta(color33, color43)+delta(color14, color24)+delta(color24, color34)+delta(color34, color44);
	float d8 = delta(color32, color22)+delta(color42, color32)+delta(color52,color42)+delta(color33,color23)+delta(color43, color33)+delta(color53, color43)+delta(color34, color24)+delta(color44, color34)+delta(color54, color44);

	float min_ = min(min(min(d1,d2),min(d5,d6)),min(min(d3,d4),min(d7,d8)));
	
	//if(min_<0.3) min_=0.0;

	gl_FragColor = vec4(min_,min_,min_,1.0);
}