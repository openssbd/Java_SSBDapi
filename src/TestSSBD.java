
import jp.riken.qbic.*;
import java.lang.String;

public class TestSSBD {

    public static void main(String[] args) {
        SSBD q = new SSBD();
        String test1=null, test2 = null, test3=null, test4=null;
        try {
            test1 = q.meta_data("address", "osaka");
            test2 = q.scale("bdml__bdml_ID", "d15115");
            test3 = q.data("bdmlUUID", "2475d");
            test4 = q.coordsXYZ("247", 10, 0, 20);
        } catch (BadSSBDException e) {
            e.printStackTrace();
        }
        System.out.println("Main test1: "+test1);
        System.out.println("Main test2: "+test2);
        System.out.println("Main test3: "+test3);
        System.out.println("Main test4: "+test4);

    }
} // end Main
