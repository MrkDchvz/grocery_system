/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdf;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.FileNotFoundException;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;

import com.itextpdf.layout.element.Table;

import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import utils.DateUtils;

/**
 *
 * @author Marku
 */
public class GeneratePDF {
    final static float THREE_COL = 190f;
    final static float FULL_WIDTH[] = {THREE_COL * 3};
    final static float TWO_COL = 285f;
    final static float TWO_COL_150 = TWO_COL + 150f;
    
    String paths;
    PdfWriter pdfWriter;
    PdfDocument pdfDocument;
    Document document;
    
    public GeneratePDF() throws FileNotFoundException {
        this.paths = "invoice.pdf";
        this.pdfWriter = new PdfWriter(this.paths);
        this.pdfDocument = new PdfDocument(this.pdfWriter);
        this.pdfDocument.setDefaultPageSize(PageSize.A4);
        this.document = new Document(this.pdfDocument);
        
        
    }
    
    public void header() throws FileNotFoundException {
        // Header Part
//        Declare the width of each column 
        float headerRightCol = TWO_COL;
        float headerLeftCol = TWO_COL_150;
        
//        Create two cells (like in a spreadsheet) with the width of the declared floats.
        float twoColArr[] = {headerLeftCol, headerRightCol};
//      Header Table (2 Columns)
        Table tbl =  new Table(twoColArr);
//        Header Column 1
        tbl.addCell(new Cell().add(new Paragraph("Invoice")).setFontSize(20f).setBold().setBorder(Border.NO_BORDER));
  
//       Header Column 2     
        Table nestedRightCol = new Table(new float[] {headerRightCol / 3, headerRightCol / 3});
            nestedRightCol.addCell(new Cell().add(new Paragraph("Invoice Date:")).setBold().setBorder(Border.NO_BORDER));
            nestedRightCol.addCell(new Cell().add(new Paragraph(DateUtils.formatDate(DateUtils.getCurrentDate(), "MMM, d yyyy"))).setBold().setBorder(Border.NO_BORDER));
            
        tbl.addCell(new Cell().add(nestedRightCol).setBorder(Border.NO_BORDER));
        this.document.add(tbl);
    }
    
    public void divider()  throws FileNotFoundException {
        //        Header Divider
        Border greyBorder = new SolidBorder(ColorConstants.GRAY, 1f /2f);
        Table divider = new Table(FULL_WIDTH);
        divider.setBorder(greyBorder);
        
        this.document.add(divider);
    }
    
    public void orderDetailsTable(JTable table, double totalPrice) throws FileNotFoundException {
        //      Order Details Header
        this.document.add(new Paragraph("Order Details").setBold());
        Table orderDetailsTableHeader = new Table(new float[] {THREE_COL, THREE_COL, THREE_COL});
        orderDetailsTableHeader.setBackgroundColor(ColorConstants.BLACK);
//       Initialize Order Details Header
        orderDetailsTableHeader.addCell(new Cell().add(new Paragraph("Product Name").setBold().setFontColor(ColorConstants.WHITE).setBorder(Border.NO_BORDER)));
        orderDetailsTableHeader.addCell(new Cell().add(new Paragraph("Quantity").setBold().setFontColor(ColorConstants.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER)));
        orderDetailsTableHeader.addCell(new Cell().add(new Paragraph("Price").setBold().setFontColor(ColorConstants.WHITE).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER)));
        
        
         //      Order Details Body
        Table orderDetailsTableBody =new Table(new float[] {THREE_COL, THREE_COL, THREE_COL});
        orderDetailsTableBody.setBackgroundColor(ColorConstants.WHITE);
        
        List<OrderDetailsCell> orderDetailsList =  generateList(table);
        
        // Populate Order Details
        for (OrderDetailsCell detail : orderDetailsList) {
           String name = detail.getProductName();
           String quantity = Integer.toString(detail.getQuantity());
           String price = Integer.toString(detail.getPrice());
  
            
            orderDetailsTableBody.addCell(new Cell().add(new Paragraph(name).setBorder(Border.NO_BORDER)));
            orderDetailsTableBody.addCell(new Cell().add(new Paragraph(quantity).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER)));
            orderDetailsTableBody.addCell(new Cell().add(new Paragraph(price).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER)));
        }
        // Add Total Price
        orderDetailsTableBody.addCell(new Cell().add(new Paragraph("").setBorder(Border.NO_BORDER)));
        orderDetailsTableBody.addCell(new Cell().add(new Paragraph("Total Price:").setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER)));
        orderDetailsTableBody.addCell(new Cell().add(new Paragraph(Double.toString(totalPrice)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER)));
        
        
        this.document.add(orderDetailsTableHeader);
        this.document.add(orderDetailsTableBody);
        
    }
    
    private List<OrderDetailsCell> generateList(JTable table) {
//        Declare Constant values for the columns in the table
        final int NAME_COL = 1;
        final int QUANTITY_COL = 2;
        final int PRICE_COL = 3;
//        Initialize variables
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        List<OrderDetailsCell> orderDetailsList = new ArrayList<>();
//      Populate list
        for (int row = 0; row < model.getRowCount(); row++) {
            String productName = String.valueOf(model.getValueAt(row, NAME_COL));
            int productQuantity = Integer.parseInt(String.valueOf(model.getValueAt(row, QUANTITY_COL)));
            int productPrice = Integer.parseInt(String.valueOf(model.getValueAt(row, PRICE_COL)));
            
            orderDetailsList.add(new OrderDetailsCell(productName, productQuantity, productPrice));
        }
        return orderDetailsList;
        
        
    }
    
    public void closeDocument() {
        this.document.close();
    }
    
    
   
    
    
}
