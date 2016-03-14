package org.apache.uima.components.languagedetector;

import java.io.IOException;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.types.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

/**
 * <ul>
 * <li><code>SomeParameter</code> - description
 * </ul>
 */
public class LanguageDetector extends JCasAnnotator_ImplBase {

	private com.optimaize.langdetect.LanguageDetector languageDetector;
	private TextObjectFactory textObjectFactory;
	private Logger logger = LoggerFactory.getLogger(LanguageDetector.class);
	
	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
    	List<LanguageProfile> languageProfiles;
		try {
			languageProfiles = new LanguageProfileReader().readAllBuiltIn();
		} catch (IOException e) {
			throw new ResourceInitializationException(e);
		}

    	languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard()).withProfiles(languageProfiles).build();
    	textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		String text = aJCas.getDocumentText();
    	TextObject textObject = textObjectFactory.forText(text);
    	Optional<LdLocale> lang = languageDetector.detect(textObject);
    	if (lang.isPresent()) {
    		Metadata metadata = new Metadata(aJCas);
    		metadata.setLanguage(lang.get().getLanguage());
    		metadata.addToIndexes();
    	}
	}

	/**
	 * Name of configuration parameter that must be set to the path of a directory containing input
	 */
	// public static final String PARAM_INPUTDIR = "InputDirectory";

	/**
	 * @see org.apache.uima.analysis_component.JCasAnnotator_ImplBase#initialize()
	 */

	/**
	 * @see org.apache.uima.collection.CollectionReader#getNext(org.apache.uima.cas.CAS)
	 */
//	public void getNext(CAS aCAS) throws IOException, CollectionException {
//		JCas jcas;
//		try {
//			jcas = aCAS.getJCas();
//		} catch (CASException e) {
//			throw new CollectionException(e);
//		}
//
//		ArchiveRecord document = documentIterator.next();
//        byte[] rawData = new byte[document.available()];
//        document.read(rawData);
//		String text = new String(rawData);
//		jcas.setDocumentText(text);
//		
//		if (!documentIterator.hasNext()) {
//			// We have arrived at the end of the current file pointed to by the current path
//
//			if (pathIterator.hasNext()) {
//				// And there is still a file left
//				currentPath = pathIterator.next();
//				fileCounter++;
//				forwardDocumentIterator();
//			}
//		}
//	}

}