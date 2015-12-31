package cn.px.qr;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class QrFrame {
  private static final int WIDTH = 320;
  private static final int HEIGHT = 600;

  private Image backImg = null;

  private Shell shell;
  private Shell window;
  private Display display;


  private Button btnCreate;
  private Button btnClear;

  private Label labImg;
  private Label diyLab;

  private Text textDiy;

  public QrFrame(Shell shell) {
    shell.setText("二维码生成工具");
    this.shell = shell;
    this.window = shell.getShell();
    this.display = shell.getDisplay();
    
    backImg = new Image(display, QrFrame.class.getResourceAsStream("/bg.jpg"));

    Rectangle rect = window.getBounds();
    this.shell.setBounds(rect.width / 5 - 30, rect.height / 5 - 50, WIDTH, HEIGHT);

    Font font = new Font(display, "Arial", 14, SWT.BOLD | SWT.ITALIC);

    GridLayout gridLayout = new GridLayout();

    gridLayout.numColumns = 2;
    gridLayout.verticalSpacing = 8;

    shell.setLayout(gridLayout);
    
    labImg = new Label(shell, SWT.CENTER);
    labImg.setImage(backImg);
    labImg.setLayoutData(new GridData(SWT.FILL, SWT.MIN, true, false, 2, 1));

    diyLab = new Label(shell, SWT.LEFT);
    diyLab.setText("内容");
    diyLab.setFont(font);

    textDiy = new Text(shell, SWT.MULTI);
    textDiy.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    btnCreate = new Button(shell, SWT.LEFT);
    btnCreate.setText("生成");
    btnCreate.setFont(font);

    btnCreate.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent event) {
        String content = textDiy.getText();
        if(null == content || "".equals(content = content.trim())){
          MessageDialog.openInformation(shell, "失败", "请先输入内容");
          return;
        }
        try{
          File file = File.createTempFile("tmp_qr_" + System.currentTimeMillis(), "jpg");
          
          Creator.create(content,file,300,300);
          InputStream is = new FileInputStream(file);
          Image image = new Image(display, is);
          labImg.setImage(image);
          
          close(is);
        }catch(Exception e){
          MessageDialog.openInformation(shell, "失败", "失败：" + e.getMessage());
        }
      }
    });
    
    btnClear = new Button(shell, SWT.LEFT);
    btnClear.setText("清空");
    btnClear.setFont(font);
    btnClear.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent event) {
        textDiy.setText("");
        labImg.setImage(backImg);
      }
    });
    
    shell.open();

//    while (!shell.isDisposed()) {
//      if (!display.readAndDispatch())
//        display.sleep();
//    }
//    display.dispose();
  }
  
  
  private static void close(Closeable c){
    if(null != c){
      try {
        c.close();
      } catch (Exception e) {
      }
    }
  }

  public static void main(String[] args) {
    new QrFrame(new Shell(new Display()));
  }
}
