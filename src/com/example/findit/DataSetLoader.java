package com.example.findit;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.example.findit.ml.DataSet;

/*
 * ����� ������������� ����������� ������ ��� �������� ������� ������ �� ��������� ����������
 */
public class DataSetLoader {

	// �������� ������� �� xml �����
	public static DataSet loadXML(int resource) {
		XmlPullParser p = Core.core.getResources().getXml(R.xml.set);
		try {
			while (p.getEventType() != XmlPullParser.END_DOCUMENT) {
				switch (p.getEventType()) {
				case XmlPullParser.START_TAG:
					// Log.d(LOG_TAG, "START_TAG: ��� ���� = " + p.getName()
					// + ", ������� = " + p.getDepth()
					// + ", ����� ��������� = " + p.getAttributeCount());
					break;

				case XmlPullParser.TEXT:
					// Log.d(LOG_TAG, "����� = " + p.getText());
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
		return null;
	}
}
