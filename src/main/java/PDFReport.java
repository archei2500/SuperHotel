import java.io.FileNotFoundException;

import javax.swing.table.DefaultTableModel;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

public class PDFReport {
	public void main(DefaultTableModel model, String str) throws FileNotFoundException {
		// Creating a PdfWriter 
		String dest = "D:/документы/ООП 4 сем/report.pdf";
		PdfWriter writer = new PdfWriter(dest);
		// Creating a PdfDocument  
		PdfDocument pdfDoc = new PdfDocument(writer);
		// Creating a Document 
		Document doc = new Document(pdfDoc);
		
		// Creating an Area Break    
		Paragraph paragr = new Paragraph (str);
		
		// Creating a table       
	    float [] pointColumnWidths = {150F, 150F, 150F};   
	    Table table = new Table(pointColumnWidths);    
	      
	    // Adding cells to the table       
	    table.addCell(new Cell().add("Room"));       
	    table.addCell(new Cell().add("Days reserved"));       
	    table.addCell(new Cell().add("Days available"));
	    
	    for (int i = 0; i < model.getRowCount(); i++) {
	    	table.addCell(new Cell().add(model.getValueAt(i, 0).toString()));
	    	table.addCell(new Cell().add(model.getValueAt(i, 1).toString()));
	    	table.addCell(new Cell().add(model.getValueAt(i, 2).toString()));
	    }                 
	      
	    // Adding area break to the PDF 
	    doc.add(paragr); 
	    
	    // Adding Table to document        
	    doc.add(table);                  
	         
	    // Closing the document       
	    doc.close();
	}
}