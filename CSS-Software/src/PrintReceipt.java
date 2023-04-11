import java.awt.*;
import java.awt.print.*;
import java.util.HashMap;

public class PrintReceipt implements Printable {

    public static double cmToPPI(double cm) {
        return cm * 0.393600787 * 72d;
    }

    public static PageFormat getPageFormat(PrinterJob pj) {

        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();
        double middleHeight = 8.0;
        double headerHeight = 2.0;
        double footerHeight = 2.0;
        double width = cmToPPI(8);      //printer know only point per inch.default value is 72ppi
        double height = cmToPPI(headerHeight + middleHeight + footerHeight);
        paper.setSize(width, height);
        paper.setImageableArea(0, 10, width, height - cmToPPI(1));
        pf.setOrientation(PageFormat.PORTRAIT);           //select orientation portrait or landscape but for this time portrait
        pf.setPaper(paper);

        return pf;
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

        Clock clock = new Clock();
        int result = NO_SUCH_PAGE;
        if (pageIndex == 0) {

            Graphics2D g2d = (Graphics2D) graphics;

            try {
                // Draw Header
                int y = 20;
                int yShift = 10;
                int headerRectHeight = 15;

                g2d.setFont(new Font("Courier New", Font.BOLD, 10));
                g2d.drawString("      " + clock.getCurrentDate() + "     " + clock.getCurrentTime() + "  ", 12, y);
                y += yShift;
                g2d.drawString("-------------------------------------", 12, y);
                y += yShift;
                g2d.drawString("            Chum-Shum-Shop           ", 12, y);
                y += yShift;
                g2d.drawString("-------------------------------------", 12, y);
                y += headerRectHeight;
                g2d.drawString("Order ID: " + String.format("%04d", Order.getOrderNum()), 10, y);
                y += yShift;
                g2d.drawString("Ordered                  Total Price ", 10, y);
                y += yShift;
                g2d.drawString("-------------------------------------", 10, y);
                y += headerRectHeight;
//                g2d.drawString(" " + pn1a + "                  " + pp1a + "  ", 10, y);
//                y += yShift;
//                Order.getOrder().size();
                HashMap boughtlist = GUI.getBoughtList();
                for (Object i : boughtlist.keySet()) {
                    String firstpart = i + " * [" + boughtlist.get(i) + "]";
                    String secondpart = Total.getPrice(i + "", Integer.parseInt(boughtlist.get(i) + ""));
//                    System.out.println(firstpart + repeat(36 - firstpart.length() - secondpart.length()) + secondpart);
                    g2d.drawString(firstpart + repeat(36 - firstpart.length() - secondpart.length()) + secondpart, 10, y);
                    y += yShift;
                }

                g2d.drawString("-------------------------------------", 10, y);
                y += yShift;
                String grand = "Grand Total: ";
                String total = String.format("%.02f ", Total.getTotal());
                g2d.drawString(grand + repeat(36 - grand.length() - total.length()) + total, 10, y);
//                g2d.drawString("Grand Total: " + String.format("%.02f ฿ ", Total.getTotal()), 10, y);
                y += yShift;
                g2d.drawString("-------------------------------------", 10, y);
                y += yShift;
                g2d.drawString("                                     ", 10, y);
                y += yShift;
                g2d.drawString("             02XXXXXXX               ", 10, y);
                y += yShift;
                g2d.drawString("*************************************", 10, y);
                y += yShift;
                g2d.drawString("          THANKS FOR VISITING        ", 10, y);
                y += yShift;
                g2d.drawString("*************************************", 10, y);
                y += yShift;

            } catch (Exception r) {
                r.printStackTrace();
            }

            result = PAGE_EXISTS;
        }
        return result;
    }

    public String repeat(int count) {
        return new String(new char[count]).replace("\0", " ");
    }
}