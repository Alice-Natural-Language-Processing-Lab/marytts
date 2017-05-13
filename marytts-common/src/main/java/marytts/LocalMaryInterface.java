/**
 * Copyright 2011 DFKI GmbH.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * This file is part of MARY TTS.
 *
 * MARY TTS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package marytts;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.w3c.dom.Document;

import marytts.config.LanguageConfig;
import marytts.config.MaryConfig;
import marytts.datatypes.MaryData;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.server.Request;
import marytts.util.MaryRuntimeUtils;

/**
 * This class and its subclasses are intended to grow into a simple-to-use,
 * unified interface for both the local MARY server and a MARY client.
 *
 * @author marc
 *
 */
public class LocalMaryInterface implements MaryInterface {
	private Locale locale;
	private AudioFileFormat audioFileFormat;
	private String outputTypeParams;
	private boolean isStreaming;

	public LocalMaryInterface() throws MaryConfigurationException {
		try {
			MaryRuntimeUtils.ensureMaryStarted();
		} catch (Exception e) {
			throw new MaryConfigurationException("Cannot start MARY server", e);
		}

		init();
	}

	protected void init() {
		setReasonableDefaults();
	}

	protected void setReasonableDefaults() {
		locale = Locale.US;
		outputTypeParams = null;
		isStreaming = false;

	}
	/*
	 * (non-Javadoc)
	 *
	 * @see marytts.MaryInterface#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale newLocale) throws IllegalArgumentException {
		if (MaryConfig.getLanguageConfig(newLocale) == null) {
			throw new IllegalArgumentException("Unsupported locale: " + newLocale);
		}
		locale = newLocale;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see marytts.MaryInterface#getLocale()
	 */
	@Override
	public Locale getLocale() {
		return locale;
	}

	private MaryData process(String configuration, String input_data) throws SynthesisException {
		Request r = new Request(configuration, input_data);
		try {
			r.process();
		} catch (Exception e) {
			throw new SynthesisException("cannot process", e);
		}
		return r.getOutputData();
	}

	@Override
	public Set<Locale> getAvailableLocales() {
		Set<Locale> locales = new HashSet<Locale>();
		for (LanguageConfig lc : MaryConfig.getLanguageConfigs()) {
			locales.addAll(lc.getLocales());
		}
		return locales;
	}
}
