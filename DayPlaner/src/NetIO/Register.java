/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package NetIO;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class Register {

    
    public static int regReq(String username,String password,String email) throws Exception
    {
        NetIO.DATA.USERNAME=null;
        NetIO.DATA.SID=null;
        
        URL url=new URL("http://dayplanner.pe.hu/register.php" );
        String s="USERNAME="+URLEncoder.encode(username, StandardCharsets.UTF_8.toString())+"&PASSWORD="+URLEncoder.encode(password, StandardCharsets.UTF_8.toString())+"&EMAIL="+URLEncoder.encode(email, StandardCharsets.UTF_8.toString());
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
        
        String response=in.next();
        conn.disconnect();
        if(response.equals("USERNAME"))
        {
            System.out.println(s);
            return 1;
        }
        else if(response.equals("USERNAME_EMAIL"))
        {
            System.out.println(s);
            return 2;
        }
        else if(response.equals("ERROR"))
        {
            
            return -1;
        }
        return 0;
    }
    
}
