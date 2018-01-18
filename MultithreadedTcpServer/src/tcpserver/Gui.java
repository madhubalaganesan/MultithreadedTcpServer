package tcpserver;
import org.eclipse.swt.widgets.Display;   
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;


import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.custom.StyledText;


public class Gui{

	protected Shell shell;
	private Text inputtext;
	private Text urltext;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Gui window = new Gui();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(961, 498);
		shell.setText("Java GUI");

		Label Javaguilbl = new Label(shell, SWT.NONE);
		Javaguilbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		Javaguilbl.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
		Javaguilbl.setBackground(SWTResourceManager.getColor(240, 240, 240));
		Javaguilbl.setToolTipText("");
		Javaguilbl.setBounds(380, 21, 178, 33);
		Javaguilbl.setText("Java GUI");

		Label inputlabel = new Label(shell, SWT.NONE);
		inputlabel.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		inputlabel.setBounds(38, 143, 189, 41);
		inputlabel.setText("Input Command");

		Label outputlabel = new Label(shell, SWT.NONE);
		outputlabel.setText("Output");
		outputlabel.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		outputlabel.setBounds(38, 300, 189, 41);

		inputtext = new Text(shell, SWT.BORDER);
		inputtext.setBounds(233, 129, 631, 41);

		StyledText outputtext = new StyledText(shell, SWT.BORDER);
		outputtext.setEditable(false);
		outputtext.setBounds(233, 291, 631, 157);


		urltext = new Text(shell, SWT.BORDER);
		urltext.setBounds(233, 80, 380, 41);

		Label urllabel = new Label(shell, SWT.NONE);
		urllabel.setText("URL");
		urllabel.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		urllabel.setBounds(38, 90, 189, 41);

		Button executebtn = new Button(shell, SWT.NONE);
		executebtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if(inputtext.getText() == null || inputtext.getText().equals("") || urltext.getText()== null || urltext.getText().equals("")){
						JOptionPane.showMessageDialog(null, "Please enter the URL and command!");
					}
					else {
						System.out.println("Linux commands to be executed.....");
						String op = linuxExecute(inputtext.getText(), urltext.getText());
						outputtext.setText(op);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		executebtn.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD | SWT.ITALIC));
		executebtn.setBounds(437, 212, 210, 46);
		executebtn.setText("Execute");

	}
	public String linuxExecute(String command, String host) throws IOException
	{

		Socket clientSocket = new Socket(host, 8090);
		if (clientSocket != null)
		{
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			outToServer.writeBytes(command + "\n");						   
			String line = null;
			String reply = "";
			while ((line = inFromServer.readLine()) != null )
			{
				 if (line.isEmpty()) {
				        break;
				    }
				reply += line;
				reply +="\n";
			}
			inFromServer.close();
			return reply;
		}

		else{	
			return "Not Connected";
		}
	}
}
