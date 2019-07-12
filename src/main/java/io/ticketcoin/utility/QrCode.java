package io.ticketcoin.utility;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrCode {

     public void generate(String content, int width, int height, BufferedImage logo,  ByteArrayOutputStream os) {
         // Create new configuration that specifies the error correction
         Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
         hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

         QRCodeWriter writer = new QRCodeWriter();
         BitMatrix bitMatrix = null;

         try {
             // Create a qr code with the url as content and a size of WxH px
             bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

             // Load QR image
             BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, getMatrixConfig());


	
	             // Initialize combined image
	             BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
	             Graphics2D g = (Graphics2D) combined.getGraphics();
	
	             // Write QR code to new image at position 0/0
	             g.drawImage(qrImage, 0, 0, null);
	             g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	
	             
	             // Logo image
	 			if (logo!=null)
	 			{
	 	             // Calculate the delta height and width between QR code and logo
	 	             int deltaHeight = qrImage.getHeight() - logo.getHeight();
	 	             int deltaWidth = qrImage.getWidth() - logo.getWidth();
	 	             
		             // Write logo into combine image at position (deltaWidth / 2) and
		             // (deltaHeight / 2). Background: Left/Right and Top/Bottom must be
		             // the same space for the logo to be centered
		             g.drawImage(logo, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);
		
		         }
	             
             // Write combined image as PNG to OutputStream
             ImageIO.write(combined, "png", os);

         } catch (WriterException e) {
             e.printStackTrace();
             //LOG.error("WriterException occured", e);
         } catch (IOException e) {
             e.printStackTrace();
             //LOG.error("IOException occured", e);
         }
     }




     private MatrixToImageConfig getMatrixConfig() {
         // ARGB Colors
         // Check Colors ENUM
         return new MatrixToImageConfig(QrCode.Colors.BLACK.getArgb(), QrCode.Colors.WHITE.getArgb());
     }


     public enum Colors {

         BLUE(0xFF40BAD0),
         RED(0xFFE91C43),
         PURPLE(0xFF8A4F9E),
         ORANGE(0xFFF4B13D),
         WHITE(0xFFFFFFFF),
         BLACK(0xFF000000);

         private final int argb;

         Colors(final int argb){
             this.argb = argb;
         }

         public int getArgb(){
             return argb;
         }
     }
 }