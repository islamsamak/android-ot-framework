/*
 * Copyright (C) 2013 OneTeam (IslamSamak : islamsamak01@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oneteam.framework.android.net;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.client.methods.HttpGet;

/**
 * @author islam
 * 
 */
public class HttpGetConnection extends AbstractHttpConnection {

	protected Map<String, String> mParamsMap;

	/**
	 * Establish a HTTP connection to the given url with the specified params
	 * 
	 * @param url
	 *            the connection url
	 */
	public HttpGetConnection(String url) {
		super(url);

		mParamsMap = new HashMap<String, String>();
	}

	public boolean addParam(String key, String value) {

		if (mParamsMap == null) {
			return false;
		}

		mParamsMap.put(key, value);

		return true;
	}

	public boolean removeParam(String key) {

		if (mParamsMap != null && mParamsMap.containsKey(key)) {

			mParamsMap.remove(key);

			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.oneteam.framework.android.net.Connection#connect(java.lang.String,
	 * java.lang.String[])
	 */
	@Override
	public void connect() {

		StringBuilder params = new StringBuilder(100);

		if (mParamsMap != null && mParamsMap.size() > 0) {

			params.append("?");

			Set<Map.Entry<String, String>> entries = mParamsMap.entrySet();

			int index = 0;

			int size = entries.size();

			for (Entry<String, String> entry : entries) {

				String key = entry.getKey();

				String value = entry.getValue();

				params.append(key).append("=").append(value);

				if (index < size - 1) {
					params.append("&");
				}
			}

		}

		HttpGet httpGet = new HttpGet(mUrl + params.toString());

		super.connect(httpGet);
	}

}
