package com.example.findit;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.example.findit.ml.DataSet;

/*
 * Класс инкапсулирует статические методы для загрузки выборок данных из различных источников
 */
public class DataSetLoader {

	// Загрузка выборки из xml файла
	public static DataSet loadXML(int resource) {
		XmlPullParser p = Core.core.getResources().getXml(R.xml.set);
		try {
			while (p.getEventType() != XmlPullParser.END_DOCUMENT) {
				switch (p.getEventType()) {
				case XmlPullParser.START_TAG:
					// Log.d(LOG_TAG, "START_TAG: имя тэга = " + p.getName()
					// + ", глубина = " + p.getDepth()
					// + ", число атрибутов = " + p.getAttributeCount());
					break;

				case XmlPullParser.TEXT:
					// Log.d(LOG_TAG, "текст = " + p.getText());
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
