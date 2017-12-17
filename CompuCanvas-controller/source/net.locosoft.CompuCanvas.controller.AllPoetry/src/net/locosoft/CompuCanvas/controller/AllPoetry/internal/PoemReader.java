/*****************************************************************************
 * Copyright (c) 2017 Chris J Daly (github user cjdaly)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   cjdaly - initial API and implementation
 ****************************************************************************/

package net.locosoft.CompuCanvas.controller.AllPoetry.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import net.locosoft.CompuCanvas.controller.util.C3Util;
import net.locosoft.CompuCanvas.controller.util.MonitorThread;

@SuppressWarnings("restriction")
public class PoemReader extends MonitorThread {

	protected long getPreSleepMillis() {
		return 1000 * 30;
	}

	protected long getPostSleepMillis() {
		return 1000 * 60 * 5;
	}

	private static final Pattern _PoemPattern = Pattern.compile(
			"<a class=[\"']u[\"'] data-name=[\"'](.*?)[\"'] href=[\"']/(.*?)[\"']><img(.*?)<div class=[\"']preview poem_body[\"']>(.*?)<div class=[\"']copyright[\"']>",
			Pattern.DOTALL);

	private static final Pattern _MetadataPattern = Pattern.compile(
			"<h1 class=[\"']title vcard item[\"'][^>]*><a class=[^>]+href=\"([^\"]*)\">([^<]+)</a>", Pattern.DOTALL);

	public boolean cycle() throws Exception {
		String uri = "http://allpoetry.com/poems";

		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			CloseableHttpResponse response = httpClient.execute(new HttpGet(uri));
			String bodyText = EntityUtils.toString(response.getEntity());
			Matcher matcher = _PoemPattern.matcher(bodyText);
			while (matcher.find()) {
				String authorName = matcher.group(1);
				String authorLink = matcher.group(2);
				String poemMetadata = matcher.group(3);
				String poemBody = matcher.group(4);

				Matcher matcher2 = _MetadataPattern.matcher(poemMetadata);
				if (matcher2.find()) {
					String poemUrl = matcher2.group(1);
					String poemTitle = matcher2.group(2);
					C3Util.log("\nPOEM DUMP");
					C3Util.log("aLink: " + authorLink + ", aName: " + authorName);
					C3Util.log("pTitle: " + poemTitle + ", pLink: " + poemUrl);
					C3Util.log(poemBody + "\n");
				} else {
					C3Util.log("\nPOEM DUMP (partial)");
					C3Util.log("aLink: " + authorLink + ", aName: " + authorName);
					C3Util.log("[[[metadata: " + poemMetadata + "]]]");
					C3Util.log(poemBody + "\n");
				}
			}
		}

		return true;
	}

}
