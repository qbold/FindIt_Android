package com.example.findit.ml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.example.findit.Core;
import com.example.findit.R;

/*
 * ��������� ������� ������
 */
public class DataSet {

	private HashMap<String, ArrayList<Object>> data; // ������

	private boolean is_cache;

	public DataSet() {
		data = new HashMap<>();
	}

	/*
	 * ���������� ����, ������ �� ���� float[], ������� �� ��� String � �������
	 */
	public void setIsCache(boolean s) {
		is_cache = s;
	}

	/*
	 * �������� �� �� ������ ������ ���� float[]
	 */
	public boolean isCache() {
		return is_cache;
	}

	/*
	 * �������� ���������� ����� � �������
	 */
	public int size(String key) {
		return data.get(key).size();
	}

	/*
	 * �������� ������
	 */
	public void add(String key, Object value) {
		if (!data.containsKey(key)) {
			data.put(key, new ArrayList<Object>());
		}
		data.get(key).add(value);
	}

	/*
	 * ���������� ����� �������� ������ � ������ key � �������� ind
	 */
	public void set(String key, int ind, Object value) {
		data.get(key).set(ind, value);
	}

	/*
	 * �������� ������ �� ����� key � ������ num � �������
	 */
	public Object getObject(String key, int num) {
		return data.get(key).get(num);
	}

	/*
	 * �������� ������ �� ����� key � ������ num � �������
	 */
	public String getString(String key, int num) {
		return (String) data.get(key).get(num);
	}

	/*
	 * ����� ������ ������� �� ����� key, ������ num. delim ��� �������,
	 * ����������� ������ � ������
	 */
	public float[] getFloatVector(String key, int num, String delimiters) {
		Object obj = getObject(key, num);
		if (obj instanceof float[])
			return (float[]) obj;
		String str = (String) obj;
		str = str.replaceAll(",", ".");

		StringTokenizer tk = new StringTokenizer(str, delimiters);
		float[] fl = new float[tk.countTokens()];
		int i = 0;
		while (tk.hasMoreTokens()) {
			fl[i++] = Float.parseFloat(tk.nextToken());
		}

		if (is_cache) {
			// ���� ������� ������ ����, �� ������!
			set(key, num, fl);
		}

		return fl;
	}

	// �������� ������� �� xml �����
	public static DataSet loadXML(int resource) {
		XmlPullParser p = Core.core.getResources().getXml(R.xml.set);
		DataSet set = new DataSet();
		try {
			String tag = null;
			while (p.getEventType() != XmlPullParser.END_DOCUMENT) {
				switch (p.getEventType()) {
				case XmlPullParser.START_TAG:
					tag = p.getName();
					break;
				case XmlPullParser.TEXT:
					set.add(tag, p.getText());
					break;
				default:
					break;
				}
				p.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return set;
	}
}
