import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import java.awt.image.BufferedImage;

public class OverlaidField extends JPanel{

	public OverlaidField(){
		this.setLayout(new OverlayLayout(this));

	}
}