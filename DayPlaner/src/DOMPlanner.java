

import java.io.*;
import java.awt.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;

public class DOMPlanner {
  
   private JTextArea display;    // for displaying output
   private InputSource input;    // for reading the XML document
   private Document document;    // document node object

   // Variables to store the query parameters and the result

   private int year, month, day, timePeriod; 
   private String resultYear, resultDay;

   public DOMPlanner( JTextArea output ) {

      year    = month = day = timePeriod = -1;
      display = output; 

      try {

         // Obtain the default parser

         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setValidating( true );
         DocumentBuilder builder = factory.newDocumentBuilder();

         // Set error handler for validation errors

         builder.setErrorHandler( new MyErrorHandler() );

         // Obtain Document Object from XML Document

         document = builder.parse( new File( "./xml/planner.xml" ) );
      } 

      // Catch errors .....

      catch ( SAXParseException spe ) {
         System.err.println( "Parse error: " + 
            spe.getMessage() );
         System.exit( 1 );
      }

      catch ( SAXException se ) {
         se.printStackTrace();         
      }

      catch ( FileNotFoundException fne ) {
         System.err.println( "File \"planner.xml\" not found." );
         System.exit( 1 );
      }

      catch ( Exception e ) {
         e.printStackTrace();
      }
   }

   // Method to get the available years from the XML file

   public String[] getYears() {

      String availableYears[];   
      StringTokenizer tokens;
      String str = " ";
      int i = 0;

      Element root = document.getDocumentElement();
      NodeList yearNodes = root.getElementsByTagName( "year" );

      // Get value of attribute 'value' for each 'year' node

      for ( i = 0; i < yearNodes.getLength(); i++ ) {
         NamedNodeMap yearAttributes = yearNodes.item(i).getAttributes();

         str += " " + yearAttributes.item( 0 ).getNodeValue();
      }  

      tokens = new StringTokenizer( str );
      availableYears = new String[ tokens.countTokens() + 1 ];
      availableYears[ 0 ] = "ANY";
      i = 1;

      // Form an array of strings containing available years

      while ( tokens.hasMoreTokens() )
         availableYears[ i++ ] = tokens.nextToken();

      return availableYears;
   }

   // method to initialize the query

   public void getQueryResult( int y, int m, int d, int t ) {

       try {
           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
           factory.setValidating( true );
           DocumentBuilder builder = factory.newDocumentBuilder();
           document = builder.parse( new File( "./xml/planner.xml" ) );
           year       = y;
           month      = m;
           day        = d;
           resultYear = "";
           resultDay  = "";
           timePeriod = t;
           display.setText( "*** The Day Planner ***\n"+"*** Welcome "+NetIO.DATA.USERNAME+" ***" );
           getResult( document );
       } catch (ParserConfigurationException ex) {
           Logger.getLogger(DOMPlanner.class.getName()).log(Level.SEVERE, null, ex);
       } catch (SAXException ex) {
           Logger.getLogger(DOMPlanner.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(DOMPlanner.class.getName()).log(Level.SEVERE, null, ex);
       }

   }   

   // Method to output the result of query

   public void getResult( Node node ) {

      // Recursively process each type of node...

      switch ( node.getNodeType() ) {

         // If it is a Document node process its children

         case Node.DOCUMENT_NODE:
              Document doc = ( Document ) node;
              getResult( doc.getDocumentElement() );
              break;

         // Process element node according to its tag name

         case Node.ELEMENT_NODE:

              if ( node.getNodeName().equals( "planner" ) )
                 processChildNodes( node.getChildNodes() );
              else if ( node.getNodeName().equals( "year" ) ) {
  
                 // find the attribute value for year and 
                 // check if it matches the query

                 NamedNodeMap yearAttributes = node.getAttributes();
                 Node value = yearAttributes.item( 0 );

                 if ( Integer.parseInt( value.getNodeValue() ) == year || year == -1 ) {
                      resultYear = " Y " + Integer.parseInt( value.getNodeValue() );
                      processChildNodes( node.getChildNodes() );
                 } else
                      return;  

              } else if ( node.getNodeName().equals( "date" ) ) {
                 Element dateElement = ( Element ) node;
                 int m = Integer.parseInt( dateElement.getAttribute( "month" ) );
                 int d = Integer.parseInt( dateElement.getAttribute( "day" ) );

                 // Check if the current 'date' node satisfies query

                 if ( ( m == month && d == day )  || ( month == -1 && d == day ) ||
                      ( m == month && day == -1 ) || ( month == -1 && day == -1 ) ) {
                      resultDay = "DATE: D " + d + " M " + m ;
                      processChildNodes( dateElement.getChildNodes() );
                 } else
                      return;

              } else if ( node.getNodeName().equals( "note" ) ) {

                 // Fetch attributes for the note node and
                 // verify its attribute values with the query

                 NamedNodeMap noteAttributes = node.getAttributes();

                 int scheduleTime;

                 if ( noteAttributes.getLength() != 0 ) {
                      Node nodeTime = noteAttributes.item( 0 );

                      scheduleTime = Integer.parseInt( nodeTime.getNodeValue() );
                 } else
                      scheduleTime = -1;

                 // If the time lies between the periods of the
                 // day display the value of node 'note'

                 if ( isBetween( scheduleTime ) )  {
                      Node child = ( node.getChildNodes() ).item( 0 );
                      String s = child.getNodeValue().trim();     

                      display.append( "\n" + resultDay + resultYear );

                      if ( scheduleTime != -1 )
                          display.append( "\nTIME: " + scheduleTime +" > " + s );
                      else
                          display.append( "\nALL DAY > " + s ); 

                     display.append( "\n* * * * * * * * * *" );
                 } else
                     return;
              }
              break;
      }
   }

   // Method to process child nodes

   public void processChildNodes( NodeList children ) {

      if ( children.getLength() != 0 )
         for ( int i = 0; i < children.getLength(); i++ )
               getResult( children.item( i ) );

      return;
   }

   // Method to compare the time with various periods of the day

   public boolean isBetween( int time ) {

      switch ( timePeriod ) {
         case -1:         // add day
            return true;
         case  0:         // morning
               if ( time >= 500 && time < 1200 )
                  return true;
               break;
          case 1:         // afternoon
               if ( time >= 1200 && time < 1800 )
                  return true;
               break;
          case 2:         // evening
               if ( time >= 1800 && time < 2100 ) 
                  return true;
               break;
          case 3:         // night
               if ( time >= 2100 || time < 500 )
                  return true;
               break;
          default:
               System.out.println( "Illegal time in XML file" );
      }

      return false;
   }
}
