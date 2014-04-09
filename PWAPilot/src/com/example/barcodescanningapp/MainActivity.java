package com.example.barcodescanningapp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.util.FileManager;



public class MainActivity extends ActionBarActivity{
	private static String readLineInfo=null,readLineInfoL=null, readLineInfoR=null;
	private static HashMap<String, String> placeBureauMap=new HashMap<String, String>();
	//HelloSemanticWeb tracker=new HelloSemanticWeb(getApplicationContext());
	//HelloSemanticWeb trial=(HelloSemanticWeb)getApplication();

	
	

	static String defaultNameSpace = "http://org.semwebprogramming/chapter2/people#";
	Model _friends = null;
	Model schema = null;
	InfModel inferredFriends = null;
	CoordinatesPlaces coordinatesPlaces=new CoordinatesPlaces();
	HashMap<String, String> coordinatePlaceMap=new HashMap<String, String>();
	
	protected void loadFunction(Context context)
	{
		
		try {
			Log.e("MainActivity", "entered the loadFunction method");
			//InputStream inputStream = openFileInput("test.txt");
			//AssetManager assetManager = getAssets();
			//AssetManager am = getApplicationContext().getAssets();
			InputStream inputStream = context.getAssets().open("test");
			//InputStream is = getResources().openRawResource(R.raw.test);  
			//InputStream is = am.open("test.txt"); 
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader br = new BufferedReader(inputStreamReader);
			//br=new BufferedReader(new FileReader(("C:/Users/mavefreak/workspace/BarcodeScanningApp/src/files/CoordinatesPlaces.txt")));
			//br = new BufferedReader(new FileReader("C:/Users/mavefreak/workspace/BarcodeScanningApp/src/files/CoordinatesPlaces"));
			while(((readLineInfo=br.readLine())!=null)){
				readLineInfoL=readLineInfo.substring(0,readLineInfo.indexOf('='));
				Log.e("MainActivity", "readLineInfoL="+readLineInfoL);
				readLineInfoR=readLineInfo.substring(readLineInfo.indexOf('=')+1,readLineInfo.length());
				Log.e("MainActivity", "readLineInfoR="+readLineInfoR);
				placeBureauMap.put(readLineInfoL, readLineInfoR);
				
		}
			Log.e("MainActivity", "exited the loadFunction method");}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(IOException e){}
		
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
		
		Context context=getApplicationContext();
		MainActivity mainAct=new MainActivity();
		mainAct.loadFunction(context);
		
		//code to call the class begins
		/*
		System.out.println("Load my FOAF Friends");
		populateFOAFFriends();
		
		// Say Hello to myself
		System.out.println("\nSay Hello to Myself");
		mySelf(_friends);  
		
		// Say Hello to my FOAF Friends
		System.out.println("\nSay Hello to my FOAF Friends");
		myFriends(_friends);
		
		//Add my new friends
		System.out.println("\nadd my new friends");
		try {
			populateNewFriends();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Say hello to my friends - hey my new ones are missing?
		System.out.println("\nSay hello to all my friends - hey the new ones are missing!");
		myFriends(_friends);
		
		// Add the ontologies
		System.out.println("\nAdd the Ontologies");
		try {
			populateFOAFSchema();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			populateNewFriendsSchema();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//See if the ontologies help identify my new friends? Nope!
		System.out.println("\nSee if the ontologies help to say hello to all my friends - Nope!");
		myFriends(_friends);
		
		//Align the ontologies to bind my friends together
		System.out.println("\nOk, lets add alignment statements for the two ontologies.");
		addAlignment();
		
		//Now say hello to my friends - nope still no new friends!
		System.out.println("\nTry again - Hello to all my friends - nope still not all!");
		myFriends(_friends);
		
		//Run reasoner to  align the instances
		System.out.println("\nRun a Reasoner");
		bindReasoner();
		//System.out.println("Running Pellet");
		//runPellet();
		
		//Say hello to all my friends
		System.out.println("\fFinally- Hello to all my friends!");
		myFriends(inferredFriends);
	
		
		// Say hello to my self again - oh no there are two of us!
		System.out.println("\nSay hello to myself - oh no there are two names for me!");
		mySelf(inferredFriends);
		
		

		// Add a rule to make us the same
		System.out.println("\nAdd a rule to make just one name");
		//applySelfRule(inferredFriends);
		

		// Now say hello to us - hey one of us now
		System.out.println("\nJust checking there is now one name for me!");
		mySelf(inferredFriends);
		
		// Just to make sure i didn't mess up anything - say hello to my all my friends again
		System.out.println("\nJust checking that I didn't mess anthing up - Say hello to all my friends again.");
		myFriends(inferredFriends);
		
		//One more thing - now we can set a restriction
		System.out.println("\nEstablishing a restriction to just get email friends");
		try {
			setRestriction(inferredFriends);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myEmailFriends(inferredFriends);
		
		//then a rule
		System.out.println("\nSay hello to my gmail friends only");
		runJenaRule(inferredFriends);
		myGmailFriends(inferredFriends);
		
		myGmailFriends(_friends);

       
		System.out.println("\nSay hello to my gmail friends only wo entailments");
		System.out.println("\nSuccess!");
		
		*/
		
		
	}
	
	//code to include ontology begins
	
	private void populateFOAFFriends(){
		_friends = ModelFactory.createOntologyModel();
		InputStream inFoafInstance = FileManager.get().open("Ontologies/FOAFFriends.rdf");
		_friends.read(inFoafInstance,defaultNameSpace);
		//inFoafInstance.close();

	}
	
	private void mySelf(Model model){
		//Hello to Me - focused search
		runQuery(" select DISTINCT ?name where{ people:me foaf:name ?name  }", model);  //add the query string

	}
	
	private void myFriends(Model model){
		//Hello to just my friends - navigation
		runQuery(" select DISTINCT ?myname ?name where{  people:me foaf:knows ?friend. ?friend foaf:name ?name } ", model);  //add the query string

	}
	

	
	
	
	private void populateNewFriends() throws IOException {		
		InputStream inFoafInstance = FileManager.get().open("Ontologies/additionalFriends.owl");
		_friends.read(inFoafInstance,defaultNameSpace);
		inFoafInstance.close();


	} 
	
	private void addAlignment(){
		
		// State that :individual is equivalentClass of foaf:Person
		Resource resource = schema.createResource(defaultNameSpace + "Individual");
		Property prop = schema.createProperty("http://www.w3.org/2002/07/owl#equivalentClass");
		Resource obj = schema.createResource("http://xmlns.com/foaf/0.1/Person");
		schema.add(resource,prop,obj);
		
		//State that :hasName is an equivalentProperty of foaf:name
		resource = schema.createResource(defaultNameSpace + "hasName");
		//prop = schema.createProperty("http://www.w3.org/2000/01/rdf-schema#subPropertyOf");
		prop = schema.createProperty("http://www.w3.org/2002/07/owl#equivalentProperty");
		obj = schema.createResource("http://xmlns.com/foaf/0.1/name");
		schema.add(resource,prop,obj);
		
		//State that :hasFriend is a subproperty of foaf:knows
		resource = schema.createResource(defaultNameSpace + "hasFriend");
		prop = schema.createProperty("http://www.w3.org/2000/01/rdf-schema#subPropertyOf");
		obj = schema.createResource("http://xmlns.com/foaf/0.1/knows");
		schema.add(resource,prop,obj);
		
		
		//State that sem web is the same person as Semantic Web
		resource = schema.createResource("http://org.semwebprogramming/chapter2/people#me");
		prop = schema.createProperty("http://www.w3.org/2002/07/owl#sameAs");
		obj = schema.createResource("http://org.semwebprogramming/chapter2/people#Individual_5");
		schema.add(resource,prop,obj);
	}
	
	private void populateFOAFSchema() throws IOException{
		InputStream inFoaf = FileManager.get().open("Ontologies/foaf.rdf");
		InputStream inFoaf2 = FileManager.get().open("Ontologies/foaf.rdf");
		schema = ModelFactory.createOntologyModel();
		//schema.read("http://xmlns.com/foaf/spec/index.rdf");
		//_friends.read("http://xmlns.com/foaf/spec/index.rdf");
		
		// Use local copy for demos without network connection
		schema.read(inFoaf, defaultNameSpace);
		_friends.read(inFoaf2, defaultNameSpace);	
		inFoaf.close();
		inFoaf2.close();
		}
	
	private void populateNewFriendsSchema() throws IOException {
		InputStream inFoafInstance = FileManager.get().open("Ontologies/additionalFriendsSchema.owl");
		_friends.read(inFoafInstance,defaultNameSpace);
		inFoafInstance.close();
	}
	
	private void bindReasoner(){
	    Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
	    reasoner = reasoner.bindSchema(schema);
	    inferredFriends = ModelFactory.createInfModel(reasoner, _friends);

	}

	private void runQuery(String queryRequest, Model model){
		
		StringBuffer queryStr = new StringBuffer();
		// Establish Prefixes
		//Set default Name space first
		queryStr.append("PREFIX people" + ": <" + defaultNameSpace + "> ");
		queryStr.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdf-schema#" + "> ");
		queryStr.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdf-syntax-ns#" + "> ");
		queryStr.append("PREFIX foaf" + ": <" + "http://xmlns.com/foaf/0.1/" + "> ");
		
		//Now add query
		queryStr.append(queryRequest);
		Query query = QueryFactory.create(queryStr.toString());
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
		ResultSet response = qexec.execSelect();
		
		while( response.hasNext()){
			QuerySolution soln = response.nextSolution();
			RDFNode name = soln.get("?name");
			if( name != null ){
				System.out.println( "Hello to " + name.toString() );
			}
			else
				System.out.println("No Friends found!");
			}
		} finally { qexec.close();}				
		}
		
	
	private void runJenaRule(Model model){
		String rules = "[emailChange: (?person <http://xmlns.com/foaf/0.1/mbox> ?email), strConcat(?email, ?lit), regex( ?lit, '(.*@gmail.com)') -> (?person <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://org.semwebprogramming/chapter2/people#GmailPerson>)]";

		Reasoner ruleReasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		ruleReasoner = ruleReasoner.bindSchema(schema);
	    inferredFriends = ModelFactory.createInfModel(ruleReasoner, model);		
	}
	
	private void runPellet( ){
		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
	    reasoner = reasoner.bindSchema(schema);
	    inferredFriends = ModelFactory.createInfModel(reasoner, _friends);
	    
	    ValidityReport report = inferredFriends.validate();
	    //printIterator(report.getReports(), "Validation Results");
		
	}
    public static void printIterator(Iterator i, String header) {

        System.out.println(header);

        for(int c = 0; c < header.length(); c++)

            System.out.print("=");

        System.out.println();
       

        if(i.hasNext()) {

	        while (i.hasNext()) 

	            System.out.println( i.next() );

        }       

        else

            System.out.println("<EMPTY>");

        

        System.out.println();

    }

    public void setRestriction(Model model) throws IOException{
    	// Load restriction - if entered in model with reasoner, reasoner sets entailments
		InputStream inResInstance = FileManager.get().open("Ontologies/restriction.owl");
		model.read(inResInstance,defaultNameSpace);
		inResInstance.close();
		
		/*
		FileOutputStream outFoafInstance;
		try {
			outFoafInstance = new FileOutputStream("Ontologies/friendsWithRestriction.turtle");
			model.write(outFoafInstance, "TURTLE");
			outFoafInstance.close();
		} catch (Exception e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/	
    }
    
    public void myEmailFriends(Model model){
     	//just get all my email friends only - ones with email
		runQuery(" select DISTINCT ?name where{  ?sub rdf:type <http://org.semwebprogramming/chapter2/people#EmailPerson> . ?sub foaf:name ?name } ", model);  //add the query string

    }
    
    public void myGmailFriends(Model model){
		runQuery(" select DISTINCT ?name where{  ?sub rdf:type people:GmailPerson. ?sub foaf:name ?name } ", model);  //add the query string
   	
    }

	
	
    
    
    //code to include ontology ends

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	
	
	//scanning activity result
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent){
		//retrieve scan result
		String readLineInfo;
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		String value=null;
		BufferedReader br;
//		try {
//			Log.e("MainActivity", "entered the static method");
//			//FileInputStream fis = new FileInputStream("C:/Users/mavefreak/workspace/BarcodeScanningApp/src/files/CoordinatesPlaces.txt");
//			//br = new BufferedReader(new InputStreamReader(getAssets().open("C:/Users/mavefreak/workspace/BarcodeScanningApp/src/files/CoordinatesPlaces.txt")));
////			File f=new File("C:/Users/mavefreak/workspace/BarcodeScanningApp/src/files/CoordinatesPlaces");
////			FileReader fr=new FileReader(f);
////			br=new BufferedReader(fr);
//			
//			FileInputStream inFile= new FileInputStream("C:/Users/mavefreak/workspace/BarcodeScanningApp/src/files/CoordinatesPlaces.txt"); 
//            InputStreamReader isr= new InputStreamReader(inFile); 
//            br= new BufferedReader(isr); 
//            
//            
//			//br=new BufferedReader(new FileReader(("C:/Users/mavefreak/workspace/BarcodeScanningApp/src/files/CoordinatesPlaces.txt")));
//			//br = new BufferedReader(new FileReader("C:/Users/mavefreak/workspace/BarcodeScanningApp/src/files/CoordinatesPlaces"));
//			while(((readLineInfo=br.readLine())!=null)){
//				readLineInfoL=readLineInfo.substring(0,readLineInfo.indexOf('=')-1);
//				Log.e("MainActivity", "readLineInfoL="+readLineInfoL);
//				readLineInfoR=readLineInfo.substring(readLineInfo.indexOf('=')+1,readLineInfo.length());
//				Log.e("MainActivity", "readLineInfoR="+readLineInfoR);
//				placeBureauMap.put(readLineInfoL, readLineInfoR);
//				Log.e("MainActivity", "exited the static method");
//		} }
//		catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch(IOException e){}
		if (scanningResult != null) {
			String scanContent = scanningResult.getContents();
//			String scanFormat = scanningResult.getFormatName();
			value=placeBureauMap.get(scanContent);
			Toast toast=Toast.makeText(getApplicationContext(), "the code scanned is"+value, Toast.LENGTH_LONG);
			Log.e("MainActivity", "The scanned value is"+value);
			}
		else{
		    Toast toast = Toast.makeText(getApplicationContext(), 
		        "No scan data received!", Toast.LENGTH_SHORT);
		    toast.show();
		}
		
		
		}

}
