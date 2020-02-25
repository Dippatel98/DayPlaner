/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package NetIO;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class XMLManager {

    
    public static void upload() throws Exception
    {
        FileInputStream fin=new FileInputStream("./xml/planner.xml");
        String s="SID="+URLEncoder.encode(DATA.SID, StandardCharsets.UTF_8.toString())+"&UID="+URLEncoder.encode(DATA.USERNAME, StandardCharsets.UTF_8.toString())+"&q=";
        URL url=new URL("http://dayplanner.pe.hu/uploadXML.php" );
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setInstanceFollowRedirects( false );
        conn.setRequestMethod( "POST" );
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
        conn.setRequestProperty( "charset", "utf-8");
        conn.setRequestProperty( "Content-Length", Integer.toString( fin.available() )+Integer.toString( s.length() ));
        conn.setUseCaches( false );
        StringBuilder sb=new StringBuilder();
        try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
            wr.write(s.getBytes());
            while(fin.available()>0)
            {
                sb.append((char)fin.read());
            }
            s=s+URLEncoder.encode(sb.toString(),StandardCharsets.UTF_8.toString());
            wr.write(s.getBytes());
            wr.flush();
            wr.close();
        }
        
        Scanner in=new Scanner(conn.getInputStream());
        while(in.hasNext())
        {
            System.out.print(in.next()+" ");
        }
        conn.disconnect();
    }
    
    public static void download() throws Exception
    {
        String s="SID="+URLEncoder.encode(DATA.SID, StandardCharsets.UTF_8.toString())+"&UID="+URLEncoder.encode(DATA.USERNAME, StandardCharsets.UTF_8.toString())+"&q=";
        URL url=new URL("http://dayplanner.pe.hu/downloadXML.php" );
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setInstanceFollowRedirects( false );
        conn.setRequestMethod( "POST" );
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
        conn.setRequestProperty( "charset", "utf-8");
        conn.setRequestProperty( "Content-Length", Integer.toString( s.length() ));
        conn.setUseCaches( false );
        
        try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
            wr.write(s.getBytes());
        }
        InputStream in=conn.getInputStream();
        FileOutputStream fos=new FileOutputStream("./xml/planner.xml",false);
        while(in.available()>0)
        {
            fos.write(in.read());
        }
        conn.disconnect();
    }
    public static void downloadLists() throws Exception
    {
        String s="SID="+URLEncoder.encode(DATA.SID, StandardCharsets.UTF_8.toString())+"&UID="+URLEncoder.encode(DATA.USERNAME, StandardCharsets.UTF_8.toString())+"&q=";
        URL url=new URL("http://dayplanner.pe.hu/downloadList.php" );
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setInstanceFollowRedirects( false );
        conn.setRequestMethod( "POST" );
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
        conn.setRequestProperty( "charset", "utf-8");
        conn.setRequestProperty( "Content-Length", Integer.toString( s.length() ));
        conn.setUseCaches( false );
        
        try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
            wr.write(s.getBytes());
        }
        Scanner in=new Scanner(conn.getInputStream());
        String name,id;
        DATA.acceptedList.clear();
        DATA.pendingList.clear();
        DATA.rejectedList.clear();
        int t=Integer.parseInt(in.nextLine());
        for(int i=0;i<t;i++)
        {
           name=in.nextLine();
           id=in.nextLine();
           DATA.acceptedList.put(id, name);
        }
        t=Integer.parseInt(in.nextLine());
        for(int i=0;i<t;i++)
        {
           name=in.nextLine();
           id=in.nextLine();
           DATA.pendingList.put(id, name);
        }
        t=Integer.parseInt(in.nextLine());
        for(int i=0;i<t;i++)
        {
           name=in.nextLine();
           id=in.nextLine();
           DATA.rejectedList.put(id, name);
        }
        
    }
}
