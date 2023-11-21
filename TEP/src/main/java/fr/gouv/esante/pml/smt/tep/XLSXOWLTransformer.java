package fr.gouv.esante.pml.smt.tep;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import fr.gouv.esante.pml.smt.utils.ADMSVocabulary;
import fr.gouv.esante.pml.smt.utils.ChargerMapping;
import fr.gouv.esante.pml.smt.utils.DCTVocabulary;
import fr.gouv.esante.pml.smt.utils.DCVocabulary;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.SKOSVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWL2DatatypeImpl;

import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;



public class XLSXOWLTransformer {
	
	//*********
	private static String xlsxTEPFileName = PropertiesUtil.getProperties("xlsxTepFile");
	  private static String owlTEPFileName = PropertiesUtil.getProperties("owlTepFileName");

	  private static OWLOntologyManager man = null;
	  private static OWLOntology onto = null;
	  private static OWLDataFactory fact = null;
	  
	  private static OWLAnnotationProperty skosNotation  = null;
	  private static OWLAnnotationProperty rdfsLabel  = null;
	  private static OWLAnnotationProperty skosAltLabel  = null;
	  private static OWLAnnotationProperty dcType  = null;
	  //private static OWLAnnotationProperty dctDescription  = null;
	  //private static OWLAnnotationProperty admsStatus  = null;
	 // private static OWLAnnotationProperty rdfsComment  = null;
	
	public static void main(String[] args) throws Exception {
		
		ChargerMapping.chargeExcelConceptToList(xlsxTEPFileName);
		  
		final OutputStream fileoutputstream = new FileOutputStream(owlTEPFileName);
		 man = OWLManager.createOWLOntologyManager();
		 onto = man.createOntology(IRI.create(PropertiesUtil.getProperties("terminologie_IRI")));
		 fact = onto.getOWLOntologyManager().getOWLDataFactory();
		
		 skosNotation =  fact.getOWLAnnotationProperty(SKOSVocabulary.NOTATION.getIRI());
		 rdfsLabel =  fact.getOWLAnnotationProperty(fr.gouv.esante.pml.smt.utils.OWLRDFVocabulary.RDFS_LABEL.getIRI());
		 skosAltLabel =  fact.getOWLAnnotationProperty(SKOSVocabulary.ALTLABEL.getIRI());
		 dcType = fact.getOWLAnnotationProperty(DCVocabulary.type.getIRI());
		 //dctDescription = fact.getOWLAnnotationProperty(DCTVocabulary.description.getIRI());
		// admsStatus = fact.getOWLAnnotationProperty(ADMSVocabulary.status.getIRI());
		 //rdfsComment =  fact.getOWLAnnotationProperty(fr.gouv.esante.pml.smt.utils.OWLRDFVocabulary.RDFS_COMMENT.getIRI());

		 
		 
		    OWLClass owlClass = null;
		    
		    
		    createPrincipalNoeud();
		    

		    
		    for(String id: ChargerMapping.listConceptsTpe.keySet()) {
		    	final String about = PropertiesUtil.getProperties("terminologie_URI") + id;
		        owlClass = fact.getOWLClass(IRI.create(about));
		        OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
		        man.applyChange(new AddAxiom(onto, declare));
		        
		        
		        
		        String aboutSubClass = null;
		        aboutSubClass = PropertiesUtil.getProperties("terminologie_URI")+ ChargerMapping.listConceptsTpe.get(id).get(5)  ;
		        OWLClass subClass = fact.getOWLClass(IRI.create(aboutSubClass));
		        OWLAxiom axiom = fact.getOWLSubClassOfAxiom(owlClass, subClass);
		        man.applyChange(new AddAxiom(onto, axiom));
		        
		        
		       

		        
		        
		        addLateralAxioms(skosNotation, id, owlClass);
		        addLateralAxioms(dcType, ChargerMapping.listConceptsTpe.get(id).get(6), owlClass);
		        addLateralAxioms(rdfsLabel, ChargerMapping.listConceptsTpe.get(id).get(0), owlClass);
		        //if(!"N/A".equals(ChargerMapping.listConceptsTpe.get(id).get(1)))
		          //addLateralAxioms(dctDescription, ChargerMapping.listConceptsTpe.get(id).get(1), owlClass);
		        if(!"N/A".equals(ChargerMapping.listConceptsTpe.get(id).get(2)))
		          addLateralAxioms(skosAltLabel, ChargerMapping.listConceptsTpe.get(id).get(2), owlClass);
		       // addLateralAxioms(admsStatus, ChargerMapping.listConceptsTpe.get(id).get(3), owlClass);
		        //if(!"N/A".equals(ChargerMapping.listConceptsTpe.get(id).get(4)))
		          //addLateralAxioms(rdfsComment, ChargerMapping.listConceptsTpe.get(id).get(4), owlClass);
		      
		       
		      
		       
		     
		      
		       
		       
		      
		        
		    }
		    
		    
		    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
		    ontologyFormat.setPrefix("dct", "http://purl.org/dc/terms/");
		    
		    
		    IRI iri = IRI.create(PropertiesUtil.getProperties("terminologie_IRI"));
		    man.applyChange(new SetOntologyID(onto,  new OWLOntologyID(iri)));
		   
		  //  addPropertiesOntology();
		    
		    man.saveOntology(onto, ontologyFormat, fileoutputstream);
		    fileoutputstream.close();
		    System.out.println("Done.");
		
		

	}
	
	public static void addLateralAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass) {
	    final OWLAnnotation annotation =
	        fact.getOWLAnnotation(prop, fact.getOWLLiteral(val));
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(onto, axiom));
	  }
  
  public static void addLateralAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass, String lang) {
	    final OWLAnnotation annotation =
	        fact.getOWLAnnotation(prop, fact.getOWLLiteral(val, lang));
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(onto, axiom));
	  }
  
  public static void addDatelAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass) {
	    final OWLAnnotation annotation =
	    		fact.getOWLAnnotation(prop, fact.getOWLLiteral(val,OWL2Datatype.XSD_DATE_TIME));
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(onto, axiom));
	  }
  
  
  
  
  
  
  public static void createPrincipalNoeud() {
	  
	// String noeud_parent = PropertiesUtil.getProperties("noeud_parent");
	   String noeud_parent_label=PropertiesUtil.getProperties("label_noeud_parent");
	   String noeud_parent_notation=PropertiesUtil.getProperties("notation_noeud_parent");
	    
	   final String aboutSubClass1 = PropertiesUtil.getProperties("URI_parent") ;
	   OWLClass subClass1 = fact.getOWLClass(IRI.create(aboutSubClass1));
      addLateralAxioms(skosNotation, noeud_parent_notation, subClass1);
      addLateralAxioms(rdfsLabel, noeud_parent_label, subClass1, "fr");
      addLateralAxioms(rdfsLabel, noeud_parent_label, subClass1, "en");
	  
  }
  
  
  
  

}
