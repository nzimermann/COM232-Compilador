import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JButton;

public class ToolBarButton extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ToolBarButton(String text, Icon icon) {
		super();
		this.setText(text);
		this.setIcon(icon);
		this.setMaximumSize(new Dimension(106, 70));
		this.setFocusable(false);
		this.setHorizontalTextPosition(JButton.CENTER);
		this.setVerticalTextPosition(JButton.BOTTOM);
	}
	
}
