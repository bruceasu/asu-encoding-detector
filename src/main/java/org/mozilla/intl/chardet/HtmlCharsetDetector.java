/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is mozilla.org code.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by the Initial Developer are Copyright (C) 1998
 * the Initial Developer. All Rights Reserved.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

/*
 * DO NOT EDIT THIS DOCUMENT MANUALLY !!!
 * THIS FILE IS AUTOMATICALLY GENERATED BY THE TOOLS UNDER
 *    AutoDetect/tools/
 */

package org.mozilla.intl.chardet;

import java.io.BufferedInputStream;
import java.net.URL;

public class HtmlCharsetDetector {

	public static boolean found = false;

	public static void main(String argv[]) throws Exception {

		if (argv.length != 1 && argv.length != 2) {

			System.out.println("Usage: HtmlCharsetDetector <url> [<languageHint>]");

			System.out.println("");
			System.out.println("Where <url> is http://...");
			System.out.println("For optional <languageHint>. Use following...");
			System.out.println("		1 => Japanese");
			System.out.println("		2 => Chinese");
			System.out.println("		3 => Simplified Chinese");
			System.out.println("		4 => Traditional Chinese");
			System.out.println("		5 => Korean");
			System.out.println("		6 => Dont know (default)");

			return;
		}

		// Initalize the nsDetector() ;
		int lang = (argv.length == 2) ? Integer.parseInt(argv[1]) : nsPSMDetector.ALL;
		nsDetector det = new nsDetector(lang);

		// Set an observer...
		// The Notify() will be called when a matching charset is found.

		det.Init(new nsICharsetDetectionObserver() {
			@Override
			public void Notify(String charset) {
				HtmlCharsetDetector.found = true;
				System.out.println("CHARSET = " + charset);
			}
		});

		URL url = new URL(argv[0]);
		BufferedInputStream imp = new BufferedInputStream(url.openStream());

		byte[] buf = new byte[1024];
		int len;
		boolean done = false;
		boolean isAscii = true;

		while ((len = imp.read(buf, 0, buf.length)) != -1) {

			// Check if the stream is only ascii.
			if (isAscii) {
				isAscii = det.isAscii(buf, len);
			}

			// DoIt if non-ascii and not done yet.
			if (!isAscii && !done) {
				done = det.DoIt(buf, len, false);
			}
		}
		det.DataEnd();

		if (isAscii) {
			System.out.println("CHARSET = ASCII");
			found = true;
		}

		if (!found) {
			String prob[] = det.getProbableCharsets();
			for (int i = 0; i < prob.length; i++) {
				System.out.println("Probable Charset = " + prob[i]);
			}
		}
	}
}
