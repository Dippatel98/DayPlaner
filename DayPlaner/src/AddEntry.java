import NetIO.XMLManager;
import java.awt.*;                  
import java.awt.event.*;            
import javax.swing.*;               
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

class AddEntry
{
	private JFrame fr;
	private AddPanel pan;
	private JButton  add;
        private JTextField year;
	private JComboBox month,day,hour,minute;
	private JTextArea note;
	private String file="";
        private Document xmlDOM;
        private DayPlanner parent=null;
	
	private void buildGUI(DayPlanner d)
	{
                parent=d;
                readFile();
		fr=new JFrame("Add Note");
                pan=new AddPanel();
                year=pan.year;
                month=pan.month;
                day=pan.day;
                hour=pan.hours;
                minute=pan.minutes;
                add=pan.add;
                note=pan.note;
                add.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            addData();
                            XMLManager.upload();
                            parent.setVisible(false);
                            parent=new DayPlanner();
                            fr.dispose();
                        } catch (Exception ex) {
                            Logger.getLogger(AddEntry.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                fr.add(pan);
                fr.pack();
                fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                fr.setVisible(true);
	}
	private void addData()
	{
            try {
                Element root=xmlDOM.getDocumentElement();
                root.appendChild(newYearNode(root));
                
                
               
                
                FileOutputStream fout=new FileOutputStream("./xml/planner.xml",false);
                
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                
                transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "planner.dtd");
                transformer.setOutputProperty(OutputKeys.INDENT, "true");
                DOMSource source = new DOMSource(xmlDOM);
                StreamResult console = new StreamResult(fout);
                transformer.transform(source, console);
                fr.dispose();
            } catch (TransformerConfigurationException ex) {
                Logger.getLogger(AddEntry.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TransformerException ex) {
                Logger.getLogger(AddEntry.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AddEntry.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                
	}
        private Element newYearNode(Element e)
        {
            NodeList yearNodes = e.getElementsByTagName( "year" );
            Element newNode=null;
            for (int  i = 0; i < yearNodes.getLength(); i++ )
            {
                NamedNodeMap yearAttributes = yearNodes.item(i).getAttributes();
                if(yearAttributes.item(0).getNodeValue().equals(this.year.getText()))
                {
                   newNode= (Element)yearNodes.item(i);
                }
            }
            if(newNode==null)
            {
                newNode=xmlDOM.createElement("year");
            }
            
            newNode.setAttribute("value",this.year.getText());
            newNode.appendChild(newDateNode());
            return newNode;   
        }
        private Element newDateNode()
        {
            Element newNode_date=xmlDOM.createElement("date");
            newNode_date.setAttribute("month", this.month.getSelectedItem().toString());
            newNode_date.setAttribute("day", this.day.getSelectedItem().toString());
                
            Element newNode_note=newNoteNode();
            newNode_date.appendChild(newNode_note);
            return newNode_date;        
        }
        private Element newNoteNode()
        {
            Element newNode_note=xmlDOM.createElement("note");
            newNode_note.setAttribute("time", this.hour.getSelectedItem().toString()+this.minute.getSelectedItem().toString());
            newNode_note.setTextContent(this.note.getText());
            return newNode_note;
        }
	private void readFile()
	{
		try
		{
			DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
                        DocumentBuilder db=dbf.newDocumentBuilder();
                        xmlDOM=db.parse(new File("./xml/planner.xml"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public static void open(DayPlanner d)
	{
            new AddEntry().buildGUI(d);
	}
}
