package org.apache.uima.components.languagedetector;

import java.io.IOException;
import java.util.List;

import org.apache.uima.UIMAException;
import org.junit.Ignore;
import org.junit.Test;
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

public class LanguageDetectorTest {

	private Logger logger = LoggerFactory.getLogger(LanguageDetectorTest.class);

	@Test
	@Ignore
	public void test() throws IOException, UIMAException {
		// TODO Instantiate CR with a non-deprecated method
//        CollectionReader cccr = CollectionReaderFactory.createCollectionReader(CommonCrawlCollectionReader.class);
//        
//        for (int i=0; i<1000 && cccr.hasNext(); i++) {
//        	JCas jCas = JCasFactory.createJCas();
//        	cccr.getNext(jCas.getCas());
//        	logger.info(jCas.getDocumentText());
//        }
	}
    
    @Test
    @Ignore
    /**
     * Just a little method to try out the Language Detector library
     */
    public void tryOut() throws IOException {
    	List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();

    	//build language detector:
    	com.optimaize.langdetect.LanguageDetector languageDetector = 
    			LanguageDetectorBuilder.create(NgramExtractors.standard())
    			.withProfiles(languageProfiles)
    			.build();

    	//create a text object factory
    	TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();

    	//query:
    	TextObject textObject = textObjectFactory.forText("This is my text. It is written in English. English is one of the world's most dominant languages.");
    	Optional<LdLocale> lang = languageDetector.detect(textObject);
    	if (lang.isPresent()) {
    		logger.info("Detected language == " + lang.get());
    	}
    }
    
}