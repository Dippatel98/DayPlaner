/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package NetIO;

import java.util.HashMap;


public class DATA {
    static String SID=null;
    public static String USERNAME=null;
    public static boolean isLogedIn()
    {
        return (SID!=null)&&(USERNAME!=null);
    }
    public static HashMap<String,String> pendingList=new HashMap<String,String>();
    public static HashMap<String,String> rejectedList=new HashMap<String,String>();
    public static HashMap<String,String> acceptedList=new HashMap<String,String>();
}
