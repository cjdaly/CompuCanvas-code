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

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
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

	private AllPoetryService _service;

	private String _poemUri = "http://allpoetry.com/poems";
	private ArrayList<Poem> _poems = new ArrayList<Poem>();

	private Poem _poem = null;
	private int _poemLineNum = -1;

	public PoemReader(AllPoetryService service) {
		_service = service;
	}

	protected long getPreSleepMillis() {
		return 1000 * 20;
	}

	protected long getPostSleepMillis() {
		return 1000 * 10;
	}

	private static final Pattern _PoemPattern = Pattern.compile(
			"<a class=[\"']u[\"'] data-name=[\"'](.*?)[\"'] href=[\"']/(.*?)[\"']><img(.*?)<div class=[\"']preview poem_body[\"']>(.*?)<div class=[\"']copyright[\"']>",
			Pattern.DOTALL);

	private static final Pattern _MetadataPattern = Pattern.compile(
			"<h1 class=[\"']title vcard item[\"'][^>]*><a class=[^>]+href=\"([^\"]*)\">([^<]+)</a>", Pattern.DOTALL);

	public boolean cycle() throws Exception {
		if (_poem != null) {
			String line = _poem.getLine(_poemLineNum);
			if (_service.serviceIsLoggingEnabled("read")) {
				C3Util.log("AllPoetry - poemLine: " + line);
			}
			_poemLineNum++;
			if (_poemLineNum >= _poem.getTotalLineCount()) {
				_poem = null;
				_poemLineNum = -1;
				int sleepMinutes = ThreadLocalRandom.current().nextInt(5, 10);
				Thread.sleep(1000 * 60 * sleepMinutes);
			}
		} else if (_poems.size() > 0) {
			_poem = _poems.remove(0);
			_poemLineNum = 0;
			if (_service.serviceIsLoggingEnabled("read")) {
				C3Util.log("AllPoetry - poem: " + _poem.getTitle() + ", author: " + _poem.getAuthorName());
			}
		} else {
			String bodyText = null;
			try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
				CloseableHttpResponse response = httpClient.execute(new HttpGet(_poemUri));
				bodyText = EntityUtils.toString(response.getEntity());
			}

			if (bodyText == null) {
				if (_service.serviceIsLoggingEnabled("stats")) {
					C3Util.log("AllPoetry - unable to get poems!");
					int sleepMinutes = ThreadLocalRandom.current().nextInt(15, 30);
					Thread.sleep(1000 * 60 * sleepMinutes);
				}
			} else {
				Matcher matcher = _PoemPattern.matcher(bodyText);
				while (matcher.find()) {
					String authorName = matcher.group(1);
					String authorUrl = matcher.group(2);
					String poemMetadata = matcher.group(3);
					String poemBodyRaw = matcher.group(4);
					Poem poem = constructPoem(authorName, authorUrl, poemMetadata, poemBodyRaw);
					if ((poem != null) && (sanityCheckPoem(poem))) {
						_poems.add(poem);
					}
				}

				if (_service.serviceIsLoggingEnabled("stats")) {
					C3Util.log("AllPoetry - retrieved " + _poems.size() + " poems.");
				}
			}
		}

		return true;
	}

	private Poem constructPoem(String authorName, String authorUrl, String poemMetadata, String poemBodyRaw) {
		String poemBodyFix = poemBodyRaw.replaceAll("<[^<>]+>", "");
		poemBodyFix = poemBodyFix.replace("&nbsp;", "");
		String[] poemBodyLines = poemBodyFix.split("\\r?\\n");

		Matcher matcher = _MetadataPattern.matcher(poemMetadata);
		if (matcher.find()) {
			String poemUrl = matcher.group(1);
			String poemTitle = matcher.group(2);
			if (_service.serviceIsLoggingEnabled("dump")) {
				C3Util.log("AllPoetry - constructing poem: " + poemTitle + " (" + poemUrl + ") by " + authorName + " ("
						+ authorUrl + ")");
				for (String line : poemBodyLines) {
					C3Util.log("> " + line);
				}
				C3Util.log("\n---\n\n");
			}
			return new Poem(poemTitle, poemUrl, authorName, authorUrl, poemBodyLines);
		}

		return null;
	}

	private boolean sanityCheckPoem(Poem poem) {
		boolean sanityCheck = true;

		int lineCount = poem.getLineCount();
		int lineCountMin = _service.serviceGetConfigInt("poem.lineCountMin", 2);
		int lineCountMax = _service.serviceGetConfigInt("poem.lineCountMax", 99);
		if ((lineCount < lineCountMin) || (lineCount > lineCountMax)) {
			sanityCheck = false;
		}

		int maxLineLength = poem.getMaxLineLength();
		int maxLineLengthMin = _service.serviceGetConfigInt("poem.maxLineLengthMin", 16);
		int maxLineLengthMax = _service.serviceGetConfigInt("poem.maxLineLengthMax", 79);
		if ((maxLineLength < maxLineLengthMin) || (maxLineLength > maxLineLengthMax)) {
			sanityCheck = false;
		}

		char[] nonAsciiChars = poem.getAllNonAsciiChars();
		if (nonAsciiChars.length > 0) {
			sanityCheck = false;
		}

		if (_service.serviceIsLoggingEnabled("stats")) {
			C3Util.log("AllPoetry - poem: " + poem.getTitle() + " by " + poem.getAuthorName());
			C3Util.log("- sanityCheck: " + sanityCheck + ", lineCount: " + lineCount + ", maxLineLength: "
					+ maxLineLength);
			if (nonAsciiChars.length > 0) {
				StringBuilder sb = new StringBuilder();
				for (char c : poem.getUniqueNonAsciiChars()) {
					int cVal = c;
					sb.append("#" + cVal + ", ");
				}
				C3Util.log("- nonAsciiChars (total: " + nonAsciiChars.length + "): " + sb);
			}
		}

		return sanityCheck;
	}

}
