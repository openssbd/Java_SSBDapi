
import jp.riken.qbic.*;
import java.lang.String;

public class TestSSBD {

    public static void main(String[] args) {
        SSBD q = new SSBD();
        String test1=null, test2 = null, test3=null, test4=null;
        try {
            // searches SSBD on meta_data API for address which contains the word osaka
            test1 = q.meta_data("address", "osaka");

            // searches SSBD on data API for bdmlID which contains the phrase 2475d
            test2 = q.data("bdmlUUID", "2475d");

            // searches SSBD on scale API for bdmlID which contains the phrase d15115
            test3 = q.scale("bdml__bdml_ID", "d15115");

            // searches SSBD on coordsXYZ API for bdmlID which contains the phrase 247 and timepoint 10, offset 0 and limit to 20 data set
            test4 = q.coordsXYZ("247", 10, 0, 20);
        } catch (BadSSBDException e) {
            e.printStackTrace();
        }
        System.out.println("SSBD searching using meta_data for address which contains osaka : "+test1);
        System.out.println("SSBD searching using data for bdmlID which contains the phrase 2475d : "+test2);
        System.out.println("SSBD searching using scale for bdmlID which contains the phrase d15115 : "+test3);
        System.out.println("SSBD searching using coordsXYZ for bdmlID which contains the phrase 247 and at timepoint 10, offset 0 and limit to 20 dtaset: "+test4);

    }
} // end Main
