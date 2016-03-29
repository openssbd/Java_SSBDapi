package jp.riken.qbic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
// Using org.json library
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SSBD {
    private static String urlbase = "http://ssbd.qbic.riken.jp/SSBD/api/v1/";
    private URL url;
    private static ArrayList<String> apifunc_list;
    private static ArrayList<String> data_searchfields;
    private static ArrayList<String> meta_data_searchfields;
    private static ArrayList<String> scale_searchfields;
    private static ArrayList<String> coordsXYZ_searchfields;
    // Constructor
    public SSBD(){
        apifunc_list = new ArrayList<String>();
        apifunc_list.add("data");
        apifunc_list.add("meta_data");
        apifunc_list.add("scale");
        apifunc_list.add("coordXYZ");

        data_searchfields = new ArrayList<String>();
        data_searchfields.add("bdmlUUID");
        data_searchfields.add("localid");

        meta_data_searchfields = new ArrayList<String>();
        meta_data_searchfields.add("address");
        meta_data_searchfields.add("basedon");
        meta_data_searchfields.add("contributors");
        meta_data_searchfields.add("datatype");
        meta_data_searchfields.add("department");
        meta_data_searchfields.add("description");
        meta_data_searchfields.add("laboratory");
        meta_data_searchfields.add("name");
        meta_data_searchfields.add("localid");
        meta_data_searchfields.add("organism");
        meta_data_searchfields.add("organization");

        scale_searchfields = new ArrayList<String>();
        scale_searchfields.add("bdml__bdml_ID");
        scale_searchfields.add("tUnit");
        scale_searchfields.add("xyzUnit");

        coordsXYZ_searchfields = new ArrayList<String>();
        coordsXYZ_searchfields.add("bdml__bdml_ID");
    }
    public ArrayList<String> get_apifunc_list() {
        return apifunc_list;
    }

    public Boolean check_fields(String apifunc, String field)
        throws BadSSBDException
    {
        if (apifunc.compareTo("apifunc")==0) {
            System.out.println("checked : apifunc");
            return apifunc_list.contains(field);
        }
        if (apifunc.compareTo("data") == 0) {
            System.out.println("checked : data and "+field);
            return data_searchfields.contains(field);
        }
        if (apifunc.compareTo("meta_data") == 0) {
                System.out.println("checked : meta_data and "+field);
                return meta_data_searchfields.contains(field);
        }
        if (apifunc.compareTo("scale") == 0) {
            System.out.println("checked : scale and "+field);
            return scale_searchfields.contains(field);
        }
        if (apifunc.compareTo("coordsXYZ") == 0) {
            System.out.println("checked : coordsXYZ and "+field);
            return coordsXYZ_searchfields.contains(field);
        }

        System.out.println("checked : error - "+field) ;
        return Boolean.FALSE;
    }

    public String ssbd_get(String apifunc, String field, String search)
        throws BadSSBDException
    {
       Boolean check_status=Boolean.FALSE;
       if (check_fields("meta_data", field)){
          check_status=Boolean.TRUE;
       }
       if (check_fields("data", field)){
           check_status=Boolean.TRUE;
       }
       if (check_fields("scale", field)){
            check_status=Boolean.TRUE;
       }
       if (check_fields("coordsXYZ", field)){
            check_status=Boolean.TRUE;
       }
       if (check_status){
        try {
            String wholeurl = urlbase+apifunc+"/?"+field+"__icontains="+search;
            System.out.println("ssbd_get : "+wholeurl) ;
            url = new URL(wholeurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            String return_mesg="";
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
//                System.out.println(output);
                return_mesg = return_mesg+output;
            }
            conn.disconnect();
            return return_mesg;

        } catch (MalformedURLException e) {

            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } // end catch
    }else {
           return null;
       }
    } // end of ssbd_geto


    public String ssbd_get_t(String field, String search, int t, int offset, int limit)
            throws BadSSBDException
    {
            try {
                String wholeurl = urlbase+"coordsXYZ"+"/?"+field+"__icontains="+search+";t="+t;
                System.out.println("ssbd_get : "+wholeurl) ;
                url = new URL(wholeurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                String return_mesg="";
                System.out.println("Output from Server .... \n");
                while ((output = br.readLine()) != null) {
//                System.out.println(output);
                    return_mesg = return_mesg+output;
                }
                conn.disconnect();
                return return_mesg;

            } catch (MalformedURLException e) {

                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } // end catch
    } // end of ssbd_get_t
/*
 Processing JSON data stream
 Using org.json library, default on Android
*/

    /*
    http://stackoverflow.com/questions/9151619/java-iterate-over-jsonobject
     */
    private static void printJsonObject(JSONObject jsonObj) {
        for (Object key : jsonObj.keySet()) {
            //based on you key types
            String keyStr = (String)key;
            Object keyvalue = jsonObj.get(keyStr);

            //Print key and value
            System.out.println("key: "+ keyStr + " value: " + keyvalue);

            //for nested objects iteration if required
            if (keyvalue instanceof JSONObject)
                printJsonObject((JSONObject)keyvalue);
        }
    }

    public Boolean display_items(String apifunc, String bdmldata_mesg) {
        ArrayList<String> fields=null;
            try {
                JSONObject bdmldata = new JSONObject(bdmldata_mesg);
//                System.out.println("JSON:" + bdmldata.toString());

                JSONObject jobj;
                JSONObject bdmlmeta;
                bdmlmeta = bdmldata.getJSONObject("meta");
//                System.out.println("Meta JSON:" + bdmlmeta.toString());
                BigInteger bdmllimit, count;
                bdmllimit = bdmlmeta.getBigInteger("limit");
                count = bdmlmeta.getBigInteger("total_count");
                System.out.println("Meta Limit :" + bdmllimit);
                System.out.println("Meta total_count :" + count);
                JSONArray bdmlobjects;
                int icount = 1;
                bdmlobjects = bdmldata.getJSONArray("objects");
                // retrieve meta_data fields
                for (int i = 0; i < bdmlobjects.length(); i++) {
                    icount = i + 1;
                    JSONObject eachbdmlobject = bdmlobjects.getJSONObject(i);
                    System.out.println("No. " + icount);
//                   System.out.println("eachbdmlobject: " + eachbdmlobject.toString());
                    printJsonObject(eachbdmlobject);
               } // end for i
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } // end catch
            return Boolean.TRUE;
//        }// end of if
//        return Boolean.FALSE;
    } // end display_items


    public String meta_data(String field, String search)
            throws BadSSBDException
    {
        if (check_fields("meta_data", field)){
            String return_msg = ssbd_get("meta_data", field, search);
            display_items("meta_data", return_msg);
            return return_msg;
        }
        else{
            return null;
        }
    }

    public String data(String field, String search)
            throws BadSSBDException
    {
        if (check_fields("data", field)){
            String return_msg = ssbd_get("data", field, search);
            display_items("data", return_msg);
            return return_msg;
        }
        else{
            return null;
        }
    }

    public String scale(String field, String search)
            throws BadSSBDException
    {
        if (check_fields("scale", field)){
            String return_msg = ssbd_get("scale", field, search);
            display_items("scale", return_msg);
            return return_msg;
        }
        else{
            return null;
        }
    }

     public String coordsXYZ(String bdmlID, int timept, int offset, int limit)
            throws BadSSBDException
    {
            String return_msg = ssbd_get_t("bdml__bdml_ID", bdmlID, timept, 0, 20);
            display_items("coordsXYZ", return_msg);
            return return_msg;
    }

} // end SSBD class
