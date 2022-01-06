import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/6 15:07
 */
public class ItextDemo {
    @Test
    public void test01(){
       try{
           Document document = new Document();
           PdfWriter.getInstance(document, new FileOutputStream("D:\\test.pdf"));
           document.open();
           document.add(new Paragraph("hello itext"));
           document.close();
       }catch (FileNotFoundException e){
           e.printStackTrace();
       }catch (DocumentException e){
           e.printStackTrace();
       }
    }
}
