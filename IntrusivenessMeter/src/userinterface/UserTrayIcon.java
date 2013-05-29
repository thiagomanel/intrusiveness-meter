package userinterface;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class UserTrayIcon {

	private Image iconImage;
	private TrayIcon trayIcon;
	
	private static final String INTRUSIVENESS_METER_HOME = System.getenv("INTRUSIVENESS_METER_HOME");
	private static final String RED_ICON_FILE = INTRUSIVENESS_METER_HOME + "/images/red_icon.png";
	private static final String RUNNER_ICON_FILE = INTRUSIVENESS_METER_HOME + "/images/runner_icon.jpg";
	
	private static final String STOP_HADOOP_TASKS_SCRIPT = INTRUSIVENESS_METER_HOME + "/source/stop_hadoop_tasks.py";
	private static final String PYTHON_INTERPRETER = "/usr/bin/python";
	
	public UserTrayIcon(String imagePathName) {
		iconImage = Toolkit.getDefaultToolkit().getImage(imagePathName);
		trayIcon = new TrayIcon(iconImage, "Sua máquina está lenta? Clique aqui.");
	}
	
	public void start() {
		try {
			if (SystemTray.isSupported()) {
				SystemTray tray = SystemTray.getSystemTray();
				trayIcon.addMouseListener(new DiscomfortMouseListener(trayIcon));
				tray.add(trayIcon);
			} else {
				System.err.println("SystemTray is not supported.");
			} 
		} catch (AWTException e) {
			System.err.println(e);
		} catch (Throwable e) {
			System.err.println(e);
		}
	}
	
	public void stop() {
		try {
			if (SystemTray.isSupported()) {
				SystemTray tray = SystemTray.getSystemTray();
				tray.remove(trayIcon);
			}
		} catch (Throwable e) {
			System.err.println(e);
		}
	}
	
	private static class DiscomfortMouseListener implements MouseListener {
		
		public DiscomfortMouseListener(TrayIcon trayIcon) {
			
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			reportDiscomfort();
		}
		
		private void reportDiscomfort() {
			try {
				ProcessBuilder pb = new ProcessBuilder(PYTHON_INTERPRETER, STOP_HADOOP_TASKS_SCRIPT);
				pb.directory(new File("."));
				pb.start();
			} catch (IOException e) {
				// FIXME ADD LOGS
				e.printStackTrace();
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
		}
	}

	public static void main(String[] args) throws InterruptedException {
		UserTrayIcon icon = new UserTrayIcon(RED_ICON_FILE);

		icon.start();
	}
}
